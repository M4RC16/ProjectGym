import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { RequestsService } from '../services/requests.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private auth = inject(AuthService);
  email = signal('');
  password = signal('');
  confirmPassword = signal('');
  emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/;

  emailError = signal('');
  passwordError = signal('');
  confirmPasswordError = signal('');
  registrationSuccess = signal(false);
  showPassword = signal(false);
  showConfirmPassword = signal(false);

  togglePassword() {
    this.showPassword.set(!this.showPassword());
  }

  toggleConfirmPassword() {
    this.showConfirmPassword.set(!this.showConfirmPassword());
  }

  onRegister() {
    this.emailError.set('');
    this.passwordError.set('');
    this.confirmPasswordError.set('');

    let hasError = false;

    if (!this.emailRegex.test(this.email())) {
      this.emailError.set('Érvénytelen email formátum');
      hasError = true;
    }

    if (!this.passwordRegex.test(this.password())) {
      this.passwordError.set('A jelszónak legalább 8 karakter hosszúnak kell lennie, és tartalmaznia kell egy számot és egy speciális karaktert');
      hasError = true;
    }

    if (this.password() !== this.confirmPassword()) {
      this.confirmPasswordError.set('A két jelszó nem egyezik');
      hasError = true;
    }

    if (hasError) return;

    this.auth
      .register({
        email: this.email(),
        password: this.password(),
      })
      .subscribe({
        next: (response) => {
          console.log('Registration successful:', response);
          this.registrationSuccess.set(true);
        },
        error: (error) => {
          console.error('Registration failed:', error);
        },
      });
  }
}
