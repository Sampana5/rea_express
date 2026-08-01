import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';
import { CartService } from '../shared/cart.service';
import { LoginResponse, Quote, UserModel } from '../shared/models';

@Component({
  selector: 'app-espace-client',
  templateUrl: './espace-client.component.html',
  styleUrls: ['./espace-client.component.css']
})
export class EspaceClientComponent implements OnInit {
  user: LoginResponse | null = null;
  profile: UserModel | null = null;
  quotes: Quote[] = [];
  errorMessage = '';

  get isAdmin(): boolean {
    return this.reaService.isAdmin();
  }

  constructor(
    private reaService: ReaExpressService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.reaService.getCurrentUser();
    this.reaService.me().subscribe({
      next: (profile) => {
        this.profile = profile;
      },
      error: () => {
        this.errorMessage = 'Session expirée ou backend indisponible.';
      }
    });
    this.cartService.myQuotes().subscribe({
      next: (quotes) => { this.quotes = quotes; },
      error: () => { /* silencieux si backend panier pas encore up */ }
    });
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      PENDING: 'En attente',
      IN_REVIEW: 'En revue',
      QUOTED: 'Devisé',
      ACCEPTED: 'Accepté',
      CANCELLED: 'Annulé',
      AWAITING_PAYMENT: 'En attente de paiement',
      PAID: 'Payé',
      FULFILLED: 'Livré'
    };
    return map[status] || status;
  }

  logout(): void {
    this.reaService.logout();
    this.router.navigate(['/login']);
  }
}
