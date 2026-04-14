import { inject, Injectable } from '@angular/core';
import { Trainer } from '../models/user.model';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class TrainerService {

  private readonly baseUrl = environment.apiUrl;

  private httpClient = inject(HttpClient);

    getAllTrainer() 
    {
        return this.httpClient.get<Trainer[]>(`${this.baseUrl}/api/user/requests/getAllTrainer`, {
            withCredentials: true,
        });
    }
}
