import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, tap, catchError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Cart, Quote, QuoteCreateRequest, QuoteStatusUpdate } from './models';
import { ReaExpressService } from './rea-express.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly apiUrl = environment.apiUrl;
  private readonly countSubject = new BehaviorSubject<number>(0);
  readonly itemCount$ = this.countSubject.asObservable();

  constructor(
    private readonly http: HttpClient,
    private readonly auth: ReaExpressService
  ) {
    this.auth.currentUser$.subscribe((user) => {
      if (user) {
        this.refreshCount();
      } else {
        this.countSubject.next(0);
      }
    });
  }

  get itemCount(): number {
    return this.countSubject.value;
  }

  getCart(): Observable<Cart> {
    return this.http.get<Cart>(`${this.apiUrl}/cart`).pipe(
      tap((cart) => this.countSubject.next(cart.totalQuantity || 0))
    );
  }

  addItem(productId: number, quantity = 1): Observable<Cart> {
    return this.http.post<Cart>(`${this.apiUrl}/cart/items`, { productId, quantity }).pipe(
      tap((cart) => this.countSubject.next(cart.totalQuantity || 0))
    );
  }

  updateItem(productId: number, quantity: number): Observable<Cart> {
    return this.http.put<Cart>(`${this.apiUrl}/cart/items/${productId}`, { productId, quantity }).pipe(
      tap((cart) => this.countSubject.next(cart.totalQuantity || 0))
    );
  }

  removeItem(productId: number): Observable<Cart> {
    return this.http.delete<Cart>(`${this.apiUrl}/cart/items/${productId}`).pipe(
      tap((cart) => this.countSubject.next(cart.totalQuantity || 0))
    );
  }

  clear(): Observable<Cart> {
    return this.http.delete<Cart>(`${this.apiUrl}/cart`).pipe(
      tap(() => this.countSubject.next(0))
    );
  }

  requestQuote(payload: QuoteCreateRequest = {}): Observable<Quote> {
    return this.http.post<Quote>(`${this.apiUrl}/quotes`, payload).pipe(
      tap(() => this.countSubject.next(0))
    );
  }

  myQuotes(): Observable<Quote[]> {
    return this.http.get<Quote[]>(`${this.apiUrl}/quotes/mine`);
  }

  allQuotes(): Observable<Quote[]> {
    return this.http.get<Quote[]>(`${this.apiUrl}/quotes`);
  }

  updateQuoteStatus(id: number, payload: QuoteStatusUpdate): Observable<Quote> {
    return this.http.put<Quote>(`${this.apiUrl}/quotes/${id}/status`, payload);
  }

  refreshCount(): void {
    if (!this.auth.isLoggedIn()) {
      this.countSubject.next(0);
      return;
    }
    this.getCart().pipe(
      catchError(() => {
        this.countSubject.next(0);
        return of({ items: [], itemCount: 0, totalQuantity: 0 } as Cart);
      })
    ).subscribe();
  }
}
