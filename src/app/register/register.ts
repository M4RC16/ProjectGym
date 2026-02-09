import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from "@angular/router";
import { RequestsService } from '../services/requests.service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  private requestsService = inject(RequestsService);
  email = signal('');
  password = signal('');
  confirmPassword = signal('');
  emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
  passwordRegex = "^(?=.[0-9])(?=.[!@#$%^&*]).{8,}$";

  onRegister() {
/* 
    if (this.emailRegex.match(this.email())) {
      console.log('Registering with', this.email(), this.password(), this.confirmPassword());
    }else {
      console.error('Invalid email format');
    } */

  this.requestsService.register({email: this.email(), password: this.password(), /* passwordConfirmation: this.confirmPassword() */}).subscribe({
      next: (response) => {
        console.log('Registration successful:', response);
      },
      error: (error) => {
        console.error('Registration failed:', error);
      }
    });

    
  }

}
