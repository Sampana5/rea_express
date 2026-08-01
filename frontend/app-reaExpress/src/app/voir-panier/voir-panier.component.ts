import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from '../shared/cart.service';
import { ReaExpressService } from '../shared/rea-express.service';
import { Cart, CartItem, Quote } from '../shared/models';

@Component({
  selector: 'app-voir-panier',
  templateUrl: './voir-panier.component.html',
  styleUrls: ['./voir-panier.component.css']
})
export class VoirPanierComponent implements OnInit {
  cart: Cart = { items: [], itemCount: 0, totalQuantity: 0 };
  loading = false;
  submitting = false;
  error = '';
  success = '';
  message = '';
  lastQuote?: Quote;
  readonly fallbackImage = 'assets/images/Logo.jpg';

  constructor(
    private readonly cartService: CartService,
    private readonly auth: ReaExpressService,
    private readonly router: Router
  ) {}

  get isLoggedIn(): boolean {
    return this.auth.isLoggedIn();
  }

  ngOnInit(): void {
    if (!this.isLoggedIn) {
      return;
    }
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.error = '';
    this.cartService.getCart().subscribe({
      next: (cart) => {
        this.cart = cart;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.error = 'Impossible de charger le panier.';
      }
    });
  }

  goLogin(): void {
    this.router.navigate(['/login'], { queryParams: { returnUrl: '/voir-panier' } });
  }

  updateQty(item: CartItem, quantity: number): void {
    const qty = Math.min(9999, Math.max(1, Number(quantity) || 1));
    this.cartService.updateItem(item.productId, qty).subscribe({
      next: (cart) => { this.cart = cart; },
      error: () => { this.error = 'Mise à jour de la quantité impossible.'; }
    });
  }

  remove(item: CartItem): void {
    this.cartService.removeItem(item.productId).subscribe({
      next: (cart) => { this.cart = cart; },
      error: () => { this.error = 'Suppression impossible.'; }
    });
  }

  clear(): void {
    if (!confirm('Vider le panier ?')) {
      return;
    }
    this.cartService.clear().subscribe({
      next: (cart) => { this.cart = cart; },
      error: () => { this.error = 'Impossible de vider le panier.'; }
    });
  }

  requestQuote(): void {
    if (!this.cart.items.length) {
      return;
    }
    this.submitting = true;
    this.error = '';
    this.success = '';
    this.cartService.requestQuote({ message: this.message?.trim() || undefined }).subscribe({
      next: (quote) => {
        this.submitting = false;
        this.lastQuote = quote;
        this.cart = { items: [], itemCount: 0, totalQuantity: 0 };
        this.message = '';
        this.success = `Demande de devis ${quote.reference} envoyée. Notre équipe vous répondra rapidement.`;
      },
      error: (err) => {
        this.submitting = false;
        this.error = err?.error?.message || 'Envoi du devis impossible.';
      }
    });
  }

  onImageError(event: Event): void {
    (event.target as HTMLImageElement).src = this.fallbackImage;
  }
}
