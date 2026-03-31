import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-forgotten-pass',
  imports: [FormsModule],
  templateUrl: './forgotten-pass.html',
  styleUrl: './forgotten-pass.css',
})
export class ForgottenPass {
  private authService = inject(AuthService);
  private alertService = inject(AlertService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  email = signal('');
  emailError = signal('');

  token = signal('');
  password1 = signal('');
  password2 = signal('');
  tokenError = signal('');
  password1Error = signal('');
  password2Error = signal('');
  loading = signal(false);
  showPassword1 = signal(false);
  showPassword2 = signal(false);

  isResetStep = signal(false);

  private emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  private passwordRegex = /^(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}$/;

  constructor() {
    this.isResetStep.set(this.route.snapshot.routeConfig?.path === 'forgotten-password/reset');
  }

  onSendResetEmail() {
    this.emailError.set('');

    if (!this.email().trim()) {
      this.emailError.set('Add meg az email címed');
      return;
    }

    if (!this.emailRegex.test(this.email().trim())) {
      this.emailError.set('Érvénytelen email formátum');
      return;
    }

    this.loading.set(true);
    this.authService.forgottenPasswordEmail(this.email().trim()).subscribe({
      next: () => {
        this.alertService.success('Email elküldve! Ellenőrizd a postafiókodat.');
        this.loading.set(false);
        this.router.navigate(['/forgotten-password/reset']);
      },
      error: (error) => {
        console.error('Forgotten password email request failed:', error);
        this.alertService.error('Az email küldése sikertelen!');
        this.loading.set(false);
      },
    });
  }

  togglePassword1() {
    this.showPassword1.set(!this.showPassword1());
  }

  togglePassword2() {
    this.showPassword2.set(!this.showPassword2());
  }

  onResetPassword() {
    this.tokenError.set('');
    this.password1Error.set('');
    this.password2Error.set('');

    let hasError = false;

    if (!this.token().trim()) {
      this.tokenError.set('Add meg a token értékét');
      hasError = true;
    }

    if (!this.password1()) {
      this.password1Error.set('Add meg az új jelszót');
      hasError = true;
    } else if (!this.passwordRegex.test(this.password1())) {
      this.password1Error.set(
        'A jelszónak legalább 8 karakter hosszúnak kell lennie, és tartalmaznia kell egy számot és egy speciális karaktert',
      );
      hasError = true;
    }

    if (!this.password2()) {
      this.password2Error.set('Erősítsd meg az új jelszót');
      hasError = true;
    } else if (this.password1() !== this.password2()) {
      this.password2Error.set('A két jelszó nem egyezik');
      hasError = true;
    }

    if (hasError) return;

    this.loading.set(true);
    this.authService
      .forgottenPassword({
        token: this.token().trim(),
        password1: this.password1(),
        password2: this.password2(),
      })
      .subscribe({
        next: () => {
          this.alertService.success('A jelszó sikeresen visszaállítva!');
          this.token.set('');
          this.password1.set('');
          this.password2.set('');
          this.loading.set(false);
        },
        error: (error) => {
          console.error('Forgotten password reset failed:', error);
          this.alertService.error('A jelszó visszaállítása sikertelen. Ellenőrizd a tokent.');
          this.loading.set(false);
        },
      });
  }
}
