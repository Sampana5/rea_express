import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { ReaExpressService } from '../shared/rea-express.service';
import { Category, Product, SubCategory } from '../shared/models';

interface SubCategoryGroup {
  subCategory: SubCategory;
  products: Product[];
}

@Component({
  selector: 'app-catalogue',
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css']
})
export class CatalogueComponent implements OnInit {
  categories: Category[] = [];
  selectedCategory?: Category;
  groups: SubCategoryGroup[] = [];
  loading = false;
  error = '';
  readonly fallbackImage = 'assets/images/Logo.jpg';

  searchMode = false;
  searchTerm = '';
  searchResults: Product[] = [];

  constructor(
    private readonly api: ReaExpressService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.api.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.route.queryParamMap.subscribe((params) => {
          const query = (params.get('q') || '').trim();
          if (query) {
            this.runSearch(query);
          } else if (categories.length) {
            this.searchMode = false;
            this.showCategory(this.selectedCategory ?? categories[0]);
          } else {
            this.loading = false;
          }
        });
      },
      error: () => {
        this.error = 'Impossible de charger le catalogue. Vérifiez que le serveur est démarré.';
        this.loading = false;
      }
    });
  }

  showCategory(category: Category): void {
    if (this.searchMode) {
      this.clearSearch(category);
      return;
    }
    if (this.selectedCategory?.id === category.id && this.groups.length) {
      return;
    }
    this.selectedCategory = category;
    this.loading = true;
    this.error = '';
    this.groups = [];

    this.api.getSubCategoriesByCategory(category.id).pipe(
      switchMap((subCategories) => {
        if (!subCategories.length) {
          return of([] as SubCategoryGroup[]);
        }
        return forkJoin(
          subCategories.map((subCategory) =>
            this.api.getProductsBySubCategory(subCategory.id).pipe(
              map((products) => ({ subCategory, products }))
            )
          )
        );
      })
    ).subscribe({
      next: (groups) => {
        this.groups = groups;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger les produits de cette catégorie.';
        this.loading = false;
      }
    });
  }

  clearSearch(category?: Category): void {
    this.searchMode = false;
    this.searchTerm = '';
    this.searchResults = [];
    const target = category ?? this.selectedCategory ?? this.categories[0];
    this.router.navigate(['/catalogue']).then(() => {
      if (target) {
        this.selectedCategory = undefined;
        this.showCategory(target);
      }
    });
  }

  onImageError(event: Event): void {
    (event.target as HTMLImageElement).src = this.fallbackImage;
  }

  trackById(_index: number, item: { id: number }): number {
    return item.id;
  }

  trackByGroup(_index: number, group: SubCategoryGroup): number {
    return group.subCategory.id;
  }

  private runSearch(query: string): void {
    this.searchMode = true;
    this.searchTerm = query;
    this.loading = true;
    this.error = '';
    this.api.getProducts(undefined, query).subscribe({
      next: (products) => {
        this.searchResults = products;
        this.loading = false;
      },
      error: () => {
        this.error = 'La recherche a échoué. Vérifiez que le serveur est démarré.';
        this.loading = false;
      }
    });
  }
}
