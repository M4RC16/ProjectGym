import { inject, Injectable } from '@angular/core';
import { Trainer, User } from '../models/models.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RequestsService {
  private baseUrl = environment.apiUrl;
  private httpClient = inject(HttpClient);

/*   getTrainers(): Observable<Trainer[]> {
    return this.httpClient.get<Trainer[]>(`${this.baseUrl}/api/requests/getAllTrainer`, { withCredentials: true });
  }

  getTrainerTimeSlots(trainerId: string, date: string): Observable<TimeSlot[]> {
    return this.httpClient.get<TimeSlot[]>(
      `${this.baseUrl}/trainers/${trainerId}/timeslots?date=${date}`,
      { withCredentials: true },
    );
  }

  createBooking(booking: BookingRequest): Observable<any> {
    return this.httpClient.post(`${this.baseUrl}/bookings`, booking, { withCredentials: true });
  } */

  // Admin endpoints

}
