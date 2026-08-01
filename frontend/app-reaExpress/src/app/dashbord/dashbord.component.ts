import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ReaExpressService } from '../shared/rea-express.service';
import { CartService } from '../shared/cart.service';
import { AdminStats, LoginResponse, Quote, QuoteStatus, UserModel } from '../shared/models';

type AdminTab = 'overview' | 'quotes' | 'users';

@Component({
  selector: 'app-dashbord',
  templateUrl: './dashbord.component.html',
  styleUrls: ['./dashbord.component.css']
})
export class DashbordComponent implements OnInit {
  currentUser: LoginResponse | null = null;
  tab: AdminTab = 'overview';
  users: UserModel[] = [];
  quotes: Quote[] = [];
  stats?: AdminStats;
  loading = false;
  errorMessage = '';
  successMessage = '';
  selectedQuote?: Quote;
  adminNotes = '';
  quotedAmount: number | null = null;
  nextStatus: QuoteStatus = 'IN_REVIEW';

  readonly statuses: QuoteStatus[] = [
    'PENDING', 'IN_REVIEW', 'QUOTED', 'ACCEPTED', 'CANCELLED',
    'AWAITING_PAYMENT', 'PAID', 'FULFILLED'
  ];

  constructor(
    private reaService: ReaExpressService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.reaService.getCurrentUser();
    this.refresh();
  }

  setTab(tab: AdminTab): void {
    this.tab = tab;
    this.errorMessage = '';
    this.successMessage = '';
  }

  refresh(): void {
    this.loading = true;
    this.errorMessage = '';
    forkJoin({
      users: this.reaService.getAllUsers(),
      quotes: this.cartService.allQuotes(),
      stats: this.reaService.getAdminStats()
    }).subscribe({
      next: ({ users, quotes, stats }) => {
        this.users = users;
        this.quotes = quotes;
        this.stats = stats;
        this.loading = false;
        if (this.selectedQuote) {
          this.selectedQuote = quotes.find((q) => q.id === this.selectedQuote?.id) || this.selectedQuote;
        }
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Impossible de charger le tableau de bord (droits admin / backend).';
      }
    });
  }

  selectQuote(quote: Quote): void {
    this.selectedQuote = quote;
    this.adminNotes = quote.adminNotes || '';
    this.quotedAmount = quote.quotedAmount ?? null;
    this.nextStatus = quote.status === 'PENDING' ? 'IN_REVIEW' : quote.status;
    this.tab = 'quotes';
  }

  saveQuote(): void {
    if (!this.selectedQuote) {
      return;
    }
    this.cartService.updateQuoteStatus(this.selectedQuote.id, {
      status: this.nextStatus,
      adminNotes: this.adminNotes?.trim() || undefined,
      quotedAmount: this.quotedAmount
    }).subscribe({
      next: (quote) => {
        this.successMessage = `Devis ${quote.reference} mis à jour.`;
        this.selectedQuote = quote;
        this.refresh();
      },
      error: () => {
        this.errorMessage = 'Mise à jour du devis impossible.';
      }
    });
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      PENDING: 'En attente',
      IN_REVIEW: 'En revue',
      QUOTED: 'Devisé',
      ACCEPTED: 'Accepté',
      CANCELLED: 'Annulé',
      AWAITING_PAYMENT: 'Paiement (bientôt)',
      PAID: 'Payé',
      FULFILLED: 'Livré'
    };
    return map[status] || status;
  }

  toggleStatus(user: UserModel): void {
    const nextStatus = user.status === 'true' ? 'false' : 'true';
    this.reaService.updateUser(user.id, { status: nextStatus }).subscribe({
      next: (res) => {
        this.successMessage = res.message;
        this.refresh();
      },
      error: () => {
        this.errorMessage = 'Mise à jour du statut impossible.';
      }
    });
  }

  deleteUser(user: UserModel): void {
    if (!confirm(`Supprimer ${user.name} ?`)) {
      return;
    }
    this.reaService.deleteUser(user.id).subscribe({
      next: (res) => {
        this.successMessage = res.message;
        this.refresh();
      },
      error: () => {
        this.errorMessage = 'Suppression impossible.';
      }
    });
  }

  logout(): void {
    this.reaService.logout();
    this.router.navigate(['/login']);
  }
}
