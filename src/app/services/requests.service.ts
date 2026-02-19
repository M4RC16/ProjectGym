import { inject, Injectable } from '@angular/core';
import { type BookingRequest, TimeSlot, Trainer, User } from '../models/models.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RequestsService {
  private baseUrl = environment.apiUrl;
  private httpClient = inject(HttpClient);

  getTrainers(): Observable<Trainer[]> {
    return this.httpClient.get<Trainer[]>(`${this.baseUrl}/trainers`, { withCredentials: true });
  }

  getTrainerTimeSlots(trainerId: string, date: string): Observable<TimeSlot[]> {
    return this.httpClient.get<TimeSlot[]>(
      `${this.baseUrl}/trainers/${trainerId}/timeslots?date=${date}`,
      { withCredentials: true },
    );
  }

  createBooking(booking: BookingRequest): Observable<any> {
    return this.httpClient.post(`${this.baseUrl}/bookings`, booking, { withCredentials: true });
  }

  // Admin endpoints
  getAllUsers() {
    return this.httpClient.get<User[]>(`${this.baseUrl}/api/user/requests/getAllUser`, {
      withCredentials: true,
    });
  }

  deleteUser(email: string) {
    return this.httpClient.delete(`${this.baseUrl}/api/user/delete/${email}`, {
      withCredentials: true,
    });
  }

  updateUserRole(id: number, roleId: number) {
    return this.httpClient.post(
      this.baseUrl + `/api/user/change/role`,
      { id, roleId },
      { withCredentials: true },
    );
  }

  getUserById(id: number) {
    return this.httpClient.get<User>(`${this.baseUrl}/api/user/requests/userById/${id}`, {
      withCredentials: true,
    });
  }
}
