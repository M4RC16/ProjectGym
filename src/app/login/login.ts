import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { filter, take } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private auth = inject(AuthService);
  private router = inject(Router);

  password = signal('');
  email = signal('');
  emailError = signal('');
  passwordError = signal('');
  loginError = signal('');
  showPassword = signal(false);

  togglePassword() {
    this.showPassword.set(!this.showPassword());
  }

  detectBrowser() {
    const ua = navigator.userAgent;

    if (ua.includes('Opera')) return 'Opera';
    if (ua.includes('Edge')) return 'Edge';
    if (ua.includes('Chrome') && !ua.includes('OPR')) return 'Chrome';
    if (ua.includes('Firefox')) return 'Firefox';
    if (ua.includes('Safari')) return 'Safari';
    return 'Unknown';
  }

  detectOS() {
    const ua = navigator.userAgent;
    if (/Win/i.test(ua)) return 'Windows';
    if (/Mac/i.test(ua)) return 'macOS';
    if (/Linux/i.test(ua)) return 'Linux';
    if (/Android/i.test(ua)) return 'Android';
    if (/iPhone|iPad|iPod/i.test(ua)) return 'iOS';
    return '';
  }

  onLogin() {
    this.emailError.set('');
    this.passwordError.set('');
    this.loginError.set('');

    let hasError = false;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!this.email()) {
      this.emailError.set('Add meg az email címed');
      hasError = true;
    } else if (!emailRegex.test(this.email())) {
      this.emailError.set('Érvénytelen email formátum');
      hasError = true;
    }

    if (!this.password()) {
      this.passwordError.set('Add meg a jelszavad');
      hasError = true;
    }

    if (hasError) return;

    this.auth
      .login({
        email: this.email(),
        password: this.password(),
        browserOSType: this.detectBrowser() + ' - ' + this.detectOS(),
      })
      .subscribe({
        next: () => {

          this.auth.currentUser$
            .pipe(
              filter((user) => user !== null),
              take(1)
            )
            .subscribe((user) => {
              let path = '';

              switch (user.role?.[0]?.id) {
                case 1:
                  path = 'admin';
                  break;
                case 2:
                  path = 'trainer';
                  break;
                case 3:
                  path = 'user';
                  break;
              }

              this.router.navigate(['/profil/' + path]);
            });
        },
        error: (error) => {
          console.error('Login failed:', error);
          this.loginError.set('Hibás email cím vagy jelszó');
        },
      });
  }
}
