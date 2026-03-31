import { Component, inject, OnInit } from '@angular/core';
import { TicketService } from '../services/ticket.service';
import { Ticket } from '../models/models.model';

@Component({
  selector: 'app-tickets',
  imports: [],
  templateUrl: './tickets.html',
  styleUrl: './tickets.css',
})
export class Tickets implements OnInit {
  tickets: Ticket[] = [];
  private ticketService = inject(TicketService);

  constructor() {
    this.ticketService.getAllTickets().subscribe({
      next: (response) => {
        this.tickets = response;
      },
      error: (error) => {
        console.error('Error fetching tickets:', error);
      },
    });
  }

  ngOnInit() {}
}
