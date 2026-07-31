import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';
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
  readonly fallbackImage = 'assets/images/Logo.jpg';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly api: ReaExpressService
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

  selectImage(url: string): void {
    this.activeImage = url;
  }

  onImageError(event: Event): void {
    (event.target as HTMLImageElement).src = this.fallbackImage;
  }

  get quoteParams(): Record<string, string> {
    if (!this.product) {
      return {};
    }
    return {
      productId: String(this.product.id),
      product: this.product.name,
      reference: this.product.reference || '',
      image: this.product.imageUrl || ''
    };
  }
}
