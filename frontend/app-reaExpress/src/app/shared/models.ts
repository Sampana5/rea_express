export interface UserModel {
  id: number;
  name: string;
  contactNumber: string;
  email: string;
  status: string;
  roles: string[];
}

export interface LoginResponse {
  token: string;
  type: string;
  id: number;
  name: string;
  email: string;
  contactNumber: string;
  status: string;
  roles: string[];
}

export interface SignupRequest {
  name: string;
  contactNumber: string;
  email: string;
  password: string;
  role?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  description?: string;
  subCategoryCount?: number;
}

export interface SubCategory {
  id: number;
  name: string;
  slug: string;
  description?: string;
  categoryId?: number;
  categoryName?: string;
  productCount?: number;
}

export interface ProductImage {
  id: number;
  url: string;
  type?: string;
}

export interface ProductDocument {
  id: number;
  name: string;
  fileUrl: string;
  type?: string;
}

export interface Product {
  id: number;
  name: string;
  slug: string;
  description?: string;
  reference?: string;
  imageUrl?: string;
  technicalInfo?: string;
  brand?: string;
  referenceManufacturer?: string;
  unitOfSale?: string;
  availability?: string;
  subCategoryId?: number;
  subCategoryName?: string;
  categoryId?: number;
  categoryName?: string;
  images?: ProductImage[];
  documents?: ProductDocument[];
}

export interface CartItem {
  productId: number;
  productName: string;
  productReference?: string;
  productImageUrl?: string;
  availability?: string;
  quantity: number;
}

export interface Cart {
  id?: number;
  items: CartItem[];
  itemCount: number;
  totalQuantity: number;
}

export type QuoteStatus =
  | 'PENDING'
  | 'IN_REVIEW'
  | 'QUOTED'
  | 'ACCEPTED'
  | 'CANCELLED'
  | 'AWAITING_PAYMENT'
  | 'PAID'
  | 'FULFILLED';

export type PaymentStatus = 'NONE' | 'PENDING' | 'PAID' | 'FAILED' | 'REFUNDED';

export interface QuoteItem {
  productId?: number;
  productName: string;
  productReference?: string;
  productImageUrl?: string;
  quantity: number;
}

export interface Quote {
  id: number;
  reference: string;
  status: QuoteStatus;
  paymentStatus: PaymentStatus;
  clientMessage?: string;
  adminNotes?: string;
  quotedAmount?: number;
  currency: string;
  createdAt: string;
  updatedAt: string;
  userId?: number;
  userName?: string;
  userEmail?: string;
  userContact?: string;
  items: QuoteItem[];
  totalQuantity: number;
}

export interface QuoteCreateRequest {
  message?: string;
}

export interface QuoteStatusUpdate {
  status: QuoteStatus;
  adminNotes?: string;
  quotedAmount?: number | null;
}

export interface AdminStats {
  users: number;
  activeUsers: number;
  admins: number;
  products: number;
  categories: number;
  quotes: number;
  pendingQuotes: number;
  quotedQuotes: number;
}
