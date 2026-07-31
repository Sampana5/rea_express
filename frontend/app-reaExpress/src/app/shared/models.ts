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
