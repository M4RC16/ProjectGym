import { Component, inject } from '@angular/core';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-custom-alert',
  standalone: true,
  templateUrl: './custom-alert.html',
  styleUrl: './custom-alert.css',
})
export class CustomAlert {
  alertService = inject(AlertService);
}
