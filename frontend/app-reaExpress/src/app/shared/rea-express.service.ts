import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import {
  Category,
  LoginRequest,
  LoginResponse,
  Product,
  SignupRequest,
  SubCategory,
  UserModel
} from './models';

const TOKEN_KEY = 'rea_token';
const USER_KEY = 'rea_user';

@Injectable({
  providedIn: 'root'
})
export class ReaExpressService {
  private readonly apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<LoginResponse | null>(this.readStoredUser());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  signup(payload: SignupRequest): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/users/signup`, payload);
  }

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, payload).pipe(
      tap((response) => this.persistSession(response))
    );
  }

  loginWithGoogle(idToken: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/google`, { idToken }).pipe(
      tap((response) => this.persistSession(response))
    );
  }

  loginWithGithub(code: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/github`, { code }).pipe(
      tap((response) => this.persistSession(response))
    );
  }

  forgotPassword(email: string, channel: 'email' | 'sms' = 'email'): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/auth/forgot-password`, { email, channel });
  }

  resetPassword(email: string, code: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/auth/reset-password`, { email, code, newPassword });
  }

  me(): Observable<UserModel> {
    return this.http.get<UserModel>(`${this.apiUrl}/auth/me`);
  }

  getAllUsers(): Observable<UserModel[]> {
    return this.http.get<UserModel[]>(`${this.apiUrl}/users`);
  }

  getUserById(id: number | string): Observable<UserModel> {
    return this.http.get<UserModel>(`${this.apiUrl}/users/${id}`);
  }

  getUserByEmail(email: string): Observable<UserModel> {
    const params = new HttpParams().set('email', email);
    return this.http.get<UserModel>(`${this.apiUrl}/users/email`, { params });
  }

  getUsersByRole(role: string): Observable<UserModel[]> {
    const params = new HttpParams().set('role', role);
    return this.http.get<UserModel[]>(`${this.apiUrl}/users/role`, { params });
  }

  updateUser(id: number, payload: Partial<SignupRequest & { status: string; role: string }>): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.apiUrl}/users/${id}`, payload);
  }

  deleteUser(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/users/${id}`);
  }

  // ----------------------------------------------------------- Catalogue
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`);
  }

  getCategory(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/categories/${id}`);
  }

  getSubCategories(categoryId?: number): Observable<SubCategory[]> {
    let params = new HttpParams();
    if (categoryId != null) {
      params = params.set('categoryId', String(categoryId));
    }
    return this.http.get<SubCategory[]>(`${this.apiUrl}/subcategories`, { params });
  }

  getSubCategoriesByCategory(categoryId: number): Observable<SubCategory[]> {
    return this.http.get<SubCategory[]>(`${this.apiUrl}/categories/${categoryId}/subcategories`);
  }

  getProductsBySubCategory(subCategoryId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/subcategories/${subCategoryId}/products`);
  }

  getProducts(subCategoryId?: number, query?: string): Observable<Product[]> {
    let params = new HttpParams();
    if (subCategoryId != null) {
      params = params.set('subCategoryId', String(subCategoryId));
    }
    if (query) {
      params = params.set('q', query);
    }
    return this.http.get<Product[]>(`${this.apiUrl}/products`, { params });
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/products/${id}`);
  }

  createCategory(payload: { name: string; description?: string }): Observable<Category> {
    return this.http.post<Category>(`${this.apiUrl}/categories`, payload);
  }

  createSubCategory(payload: { name: string; categoryId: number; description?: string }): Observable<SubCategory> {
    return this.http.post<SubCategory>(`${this.apiUrl}/subcategories`, payload);
  }

  createProduct(payload: Partial<Product> & { name: string; subCategoryId: number }): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}/products`, payload);
  }

  updateProduct(id: number, payload: Partial<Product> & { name: string; subCategoryId: number }): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/products/${id}`, payload);
  }

  deleteProduct(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/products/${id}`);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return !!user?.roles?.some((role) => role === 'ROLE_ADMIN' || role === 'admin');
  }

  private persistSession(response: LoginResponse): void {
    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(USER_KEY, JSON.stringify(response));
    this.currentUserSubject.next(response);
  }

  private readStoredUser(): LoginResponse | null {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as LoginResponse;
    } catch {
      return null;
    }
  }
}
