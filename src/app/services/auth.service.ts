import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { loginData, loginResponse, registerData, User } from '../models/models.model';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);
  private router = inject(Router);

  private readonly baseUrl = 'http://localhost:8080';
  private JWTtoken: string | null = null;
  private refreshTimeout: any;

  // User state kezelése
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  // Session visszaállítása ha van érvényes cookie
  constructor() {
    this.initializeAuth();
  }

  private initializeAuth(): void {
    this.refreshTokens().subscribe({
      next: () => console.log('✅ Session restored from cookie'),
      error: () => console.log('❌ No valid session'),
    });
  }

  // Login
  login(data: loginData): Observable<loginResponse> {
    return this.httpClient
      .post<loginResponse>(this.baseUrl + '/login', data, { withCredentials: true })
      .pipe(
        tap((response) => this.handleLoginResponse(response)),
        catchError((error) => {
          console.error('Login error:', error);
          return throwError(() => error);
        }),
      );
  }

  // Login response kezelése
  private handleLoginResponse(response: loginResponse): void {
    this.JWTtoken = response.JWTtoken;

/*     const decodedToken = jwtDecode(this.JWTtoken); */

    const user: User = {
      id: 1,
      name: 'asdasd',
      role: {
        id: 1,
        name: 'USER',
      },
    };

    this.currentUserSubject.next(user);

    this.scheduleTokenRefresh(1000);

    console.log('✅ Auth success:', user.name);
  }

  // Token frissítés ütemezése
  private scheduleTokenRefresh(expiresIn: number): void {
    if (this.refreshTimeout) {
      clearTimeout(this.refreshTimeout);
    }

    const refreshTime = (expiresIn - 60) * 1000; // 1 perccel a lejárat előtt

    this.refreshTimeout = setTimeout(() => {
      console.log('🔄 Auto-refreshing token...');
      this.refreshTokens().subscribe({
        next: () => console.log('✅ Token auto-refreshed'),
        error: (err) => console.error('❌ Auto-refresh failed', err),
      });
    }, refreshTime);

    console.log(`⏰ Token refresh scheduled in ${refreshTime / 1000 / 60} minutes`);
  }

  // Token frissítése
  refreshTokens(): Observable<loginResponse> {
    return this.httpClient
      .post<loginResponse>(
        this.baseUrl + '/refresh',
        {},
        {
          withCredentials: true,
        },
      )
      .pipe(
        tap((response) => this.handleLoginResponse(response)),
        catchError((error) => {
          console.error('Token refresh failed', error);
          this.clearAuthState();
          return throwError(() => error);
        }),
      );
  }

  // Auth state törlése
  private clearAuthState(): void {
    this.JWTtoken = null;
    this.currentUserSubject.next(null);

    if (this.refreshTimeout) {
      clearTimeout(this.refreshTimeout);
      this.refreshTimeout = null;
    }

    this.router.navigate(['/login']);
  }

  // Logout
  logout(): void {
    this.httpClient
      .post(
        this.baseUrl + '/logout',
        {},
        {
          withCredentials: true,
        },
      )
      .subscribe({
        complete: () => this.clearAuthState(),
      });

    this.clearAuthState();
  }

  // register
    register(data: registerData) {
    return this.httpClient.post(this.baseUrl + '/register', data);
  }

  // egyéb

  getAccessToken(): string | null {
    return this.JWTtoken;
  }

  isAuthenticated(): boolean {
    return this.JWTtoken !== null;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }



}
