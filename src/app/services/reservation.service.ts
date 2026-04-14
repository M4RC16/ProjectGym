import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import {
  allFreeTrainingsTodayRequest,
  allFreeTrainingsTodayResponse,
  deleteRequest,
  reservationCreateRequest,
} from '../models/reservation.model';
import { CalendarEvent } from '../models/calendar.model';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private readonly baseUrl = environment.apiUrl;

  private httpClient = inject(HttpClient);

  createreservation(data: reservationCreateRequest) {
    return this.httpClient.post(`${this.baseUrl}/api/reservation/new/training`, data, {
      withCredentials: true,
    });
  }

  getAllReservationsToday(data: allFreeTrainingsTodayRequest) {
    return this.httpClient.get<allFreeTrainingsTodayResponse[]>(
      `${this.baseUrl}/api/reservation/getFreeTrainings`,
      {
        params: { ...data},
        withCredentials: true,
      },
    );
  }

  getMyTrainings() {
    return this.httpClient.get<CalendarEvent[]>(`${this.baseUrl}/api/reservation/getMyTrainings`, {
      withCredentials: true,
    });
  }

  deleteReservation(data: deleteRequest) {
    return this.httpClient.delete(`${this.baseUrl}/api/reservation/delete`, {
      params: { ...data },
      withCredentials: true,
    });
  }

  
}
