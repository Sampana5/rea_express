import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';
import { CartService } from '../shared/cart.service';
import { Product, ProductImage } from '../shared/models';

@Component({
  selector: 'app-produit-detail',
  templateUrl: './produit-detail.component.html',
  styleUrls: ['./produit-detail.component.css']
})
export class ProduitDetailComponent implements OnInit {
  product?: Product;
  activeImage = '';
  loading = false;
  error = '';
  quantity = 1;
  adding = false;
  feedback = '';
  feedbackError = false;
  readonly fallbackImage = 'assets/images/Logo.jpg';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly api: ReaExpressService,
    private readonly cart: CartService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      if (!id) {
        this.error = 'Produit introuvable.';
        return;
      }
      this.loadProduct(id);
    });
  }

  private loadProduct(id: number): void {
    this.loading = true;
    this.error = '';
    this.product = undefined;
    this.feedback = '';
    this.api.getProduct(id).subscribe({
      next: (product) => {
        this.product = product;
        this.activeImage = product.imageUrl || this.fallbackImage;
        this.loading = false;
      },
      error: () => {
        this.error = 'Ce produit est introuvable ou le serveur est indisponible.';
        this.loading = false;
      }
    });
  }

  get gallery(): ProductImage[] {
    return this.product?.images || [];
  }

  get isLoggedIn(): boolean {
    return this.api.isLoggedIn();
  }

  selectImage(url: string): void {
    this.activeImage = url;
  }

  onImageError(event: Event): void {
    (event.target as HTMLImageElement).src = this.fallbackImage;
  }

  changeQty(delta: number): void {
    this.quantity = Math.min(9999, Math.max(1, this.quantity + delta));
  }

  addToCart(): void {
    if (!this.product) {
      return;
    }
    this.feedback = '';
    this.feedbackError = false;

    if (!this.isLoggedIn) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: `/produit/${this.product.id}` }
      });
      return;
    }

    this.adding = true;
    this.cart.addItem(this.product.id, this.quantity).subscribe({
      next: () => {
        this.adding = false;
        this.feedback = 'Produit ajouté au panier.';
        this.feedbackError = false;
      },
      error: (err) => {
        this.adding = false;
        this.feedbackError = true;
        this.feedback = err?.error?.message || 'Impossible d’ajouter au panier.';
      }
    });
  }
}
