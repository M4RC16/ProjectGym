export interface Trainer {
  trainerId: number;
  trainerName: string;
  profilePicture: string;
  description: string;
  hourlyRate: number;
}

export interface Role {
  id: number;
  roleName: string;
}

export interface User {
  id: number;
  role: Role[] | null;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string | null;
  validUntil: string | null;
  hourlyWage: number | null;
  profilePicture: string | null;
}

export interface UserFormData {
  firstName: string;
  lastName: string;
  emailAddress: string;
  phoneNumber: string;
  message: string;
}

export interface UpdateUserNameRequest {
  firstName: string;
  lastName: string;
}

export interface message {
  id: number;
  name: string;
  phoneNumber: string;
  email: string;
  message: string;
}