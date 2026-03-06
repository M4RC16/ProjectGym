export interface Img {
  imgId: number;
  imgUrl: string;
  imgAlt: string;
}

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
}

export interface UserFormData {
  firstName: string;
  lastName: string;
  emailAddress: string;
  phoneNumber: string;
  message: string;
}

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

export interface CalendarEvent {
  reservationXUserId: number;
  reservationId: number;
  scheduledAt: string;
  trainerName: string;
  trainerId: number;
}

export interface EventDay {
  day: number;
  month: number;
  year: number;
  events: CalendarEvent[];
}

/* export interface TimeSlot {
  time: string;
  available: boolean;
} */

/* export interface BookingRequest {
  trainerId: string;
  trainerName: string;
  timeSlotId: number;
  date: string;
} */

export interface AdminUser {}

/* reservation */

export interface reservationCreateRequest {
  trainerId: string;
  date: string;
}

export interface allFreeTrainingsTodayRequest {
  trainerId: string;
  date: string;
}

export interface allFreeTrainingsTodayResponse {
  scheduledAt: string;
  free: boolean;
}

export interface deleteRequest {
  reservationXUserId: number;
  reservationId: number;
  trainerId: number;
}
