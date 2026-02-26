import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import {
  allFreeTrainingsTodayRequest,
  allFreeTrainingsTodayResponse,
  reservationCreateRequest,
} from '../models/models.model';

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
    return this.httpClient.post<allFreeTrainingsTodayResponse[]>(
      `${this.baseUrl}/api/reservation/getFreeTrainings`,
      data,
      {
        withCredentials: true,
      },
    );
  }
}
