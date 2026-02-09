import { inject, Injectable } from '@angular/core';
import { loginData, loginResponse, registerData } from '../models/models.model';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class RequestsService {
  private baseUrl = 'http://localhost:8080';
  private httpClient = inject(HttpClient);

  login(data: loginData) {
    return this.httpClient.post<loginResponse>(this.baseUrl + '/login', data);
  }

  register(data: registerData) {
    return this.httpClient.post(this.baseUrl + '/register', data);
  }
}
