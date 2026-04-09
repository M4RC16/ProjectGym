import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { addTicket, Ticket } from '../models/models.model';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  
  private baseUrl = environment.apiUrl;
  private httpClient = inject(HttpClient);


  getAllTickets() {
    return this.httpClient.get<Ticket[]>(`${this.baseUrl}/api/tickets/getAll`);
  }

  addTicket(ticket: addTicket) {
    return this.httpClient.post(`${this.baseUrl}/api/tickets/add`, ticket, { withCredentials: true });

  }

  deleteTicket(id: number) {
    return this.httpClient.delete(`${this.baseUrl}/api/tickets/delete/${id}`, { withCredentials: true });
  }

  purchaseTicket(ticketId: number) {
    return this.httpClient.put(`${this.baseUrl}/api/user/add/ticket`, ticketId, { withCredentials: true });
  }





}
