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