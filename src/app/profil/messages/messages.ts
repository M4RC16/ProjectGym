import { Component, inject } from '@angular/core';
import { message } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-messages',
  imports: [],
  templateUrl: './messages.html',
  styleUrl: './messages.css',
})
export class Messages {
  messages: message[] = [];
  openedId: number | null = null;

  private userService = inject(UserService);
  private alertService = inject(AlertService);

  constructor() {
    this.userService.getAllMessages().subscribe({
      next: (data) => {
        this.messages = data;
      },
      error: (err) => {
        console.error('Hiba az üzenetek lekérésekor:', err);
        this.alertService.error(
          'Hiba történt az üzenetek lekérésekor. Kérjük, próbáld újra később.',
        );
      },
    });
  }

  toggle(id: number) {
    this.openedId = this.openedId === id ? null : id;
  }

  async delete(messageId: number) {
    const confirm = await this.alertService.confirm('Biztosan törölni szeretnéd ezt az üzenetet?');

    if (!confirm) return;

    this.userService.deleteMessage(messageId).subscribe({
      next: () => {
        this.messages = this.messages.filter(m => m.id !== messageId);
        if (this.openedId === messageId) {
          this.openedId = null;
        }
        this.alertService.success('Üzenet sikeresen törölve.');
      },
      error: (err) => {
        console.error('Hiba az üzenet törlésekor:', err);
        this.alertService.error(
          'Hiba történt az üzenet törlésekor. Kérjük, próbáld újra később.',
        );
      },
    });





  }
}
