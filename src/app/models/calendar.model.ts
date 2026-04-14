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