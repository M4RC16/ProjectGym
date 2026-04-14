export interface loginData {
  email: string;
  password: string;
  browserOSType: string;
}

export interface loginResponse {
  accessToken: string;
}

export interface AppJwtPayload {
  userId: number;
}

export interface registerData {
  email: string;
  password: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  password1: string;
  password2: string;
}

export interface ForgottenPasswordRequest {
  token: string;
  password1: string;
  password2: string;
}