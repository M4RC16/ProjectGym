import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

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
    console.log('Logging in with', this.email(), this.password());

    this.auth
      .login({
        email: this.email(),
        password: this.password(),
        browserOSType: this.detectBrowser + ' - ' + this.detectOS,
      })
      .subscribe({
        next: (response) => {
          this.router.navigate(['/profil/' +  this.auth.getCurrentUser()?.role?.[0]?.roleName.toLowerCase() ]);
        },
        error: (error) => {
          console.error('Login failed:', error);
        },
      });
  }
}
