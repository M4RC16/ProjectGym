import { Component, inject, OnInit } from '@angular/core';
import { TicketService } from '../services/ticket.service';
import { Ticket } from '../models/models.model';
import { AlertService } from '../services/alert.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-tickets',
  imports: [],
  templateUrl: './tickets.html',
  styleUrl: './tickets.css',
})
export class Tickets implements OnInit {
  tickets: Ticket[] = [];
  private ticketService = inject(TicketService);
  private alertService = inject(AlertService);
  private authService = inject(AuthService);

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

  async purchaseTicket(ticketId: number) {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.alertService.error('A vásárláshoz be kell jelentkezned!');
      return;
    }

    if (currentUser.validUntil) {
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      const validUntil = new Date(currentUser.validUntil);
      validUntil.setHours(0, 0, 0, 0);

      if (!Number.isNaN(validUntil.getTime()) && validUntil >= today) {
        const confirmed = await this.alertService.confirm(
          'Már van érvényes bérleted a mai napon. Biztosan meg szeretnéd vásárolni ezt a bérletet?',
        );
        if (!confirmed) return;
      }
    } else {
      const confirmed = await this.alertService.confirm(
        'Biztosan meg szeretnéd vásárolni ezt a bérletet?',
      );
      if (!confirmed) return;
    }

    this.ticketService.purchaseTicket(ticketId).subscribe({
      next: () => {
        this.alertService.success('Sikeres vásárlás!');
      },
      error: (error) => {
        console.error('Error purchasing ticket:', error);
        this.alertService.error('Hiba történt a vásárlás során. Kérlek próbáld újra.');
      },
    });
  }

  ngOnInit() {}
}
