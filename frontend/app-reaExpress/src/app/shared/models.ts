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
