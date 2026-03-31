import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AppJwtPayload, loginData, loginResponse, registerData, User } from '../models/models.model';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { jwtDecode, JwtPayload } from 'jwt-decode';
import { RequestsService } from './requests.service';
import { environment } from '../../environments/environment';
import { UserService } from './user.service';



@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);
  private router = inject(Router);
  private userService = inject(UserService);

  private readonly baseUrl = environment.apiUrl;
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
      .post<loginResponse>(this.baseUrl + '/api/user/login', data, { withCredentials: true })
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
    this.JWTtoken = response.accessToken;

    const decodedToken = jwtDecode<AppJwtPayload>(this.JWTtoken);

    this.userService.getUserById(decodedToken.userId).subscribe({
      next: (user) => {
        this.currentUserSubject.next(user);
        console.log('✅ Auth success:', user.firstName, user.role?.[0]?.roleName);
        localStorage.setItem("loggedInUser", user.role?.[0]?.id.toString() || '');
        this.scheduleTokenRefresh(1000);
      },
      error: (err) => {
        console.error('❌ Failed to fetch user data after login', err);
        this.clearAuthState();
      }
    });


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
        this.baseUrl + '/api/auth/refresh',
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
    localStorage.removeItem("loggedInUser");
    this.currentUserSubject.next(null);

    if (this.refreshTimeout) {
      clearTimeout(this.refreshTimeout);
      this.refreshTimeout = null;
    }

    /* this.router.navigate(['']); */
  }

  // Logout
  logout(): void {
    this.httpClient
      .post(
        this.baseUrl + '/api/auth/logout',
        {},
        {
          withCredentials: true,
        },
      )
      .subscribe({
        complete: () => this.clearAuthState(),
      });

    this.clearAuthState();
    this.router.navigate(['']);
  }

  // register
  register(data: registerData) {
    return this.httpClient.post(this.baseUrl + '/api/user/register', data);
  }

  // egyéb

  getAccessToken(): string | null {
    return this.JWTtoken;
  }

  isAuthenticated(): boolean {
    return localStorage.getItem("loggedInUser") !== null;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  refreshCurrentUser(): void {
    const token = this.JWTtoken;
    if (!token) return;
    const decoded = jwtDecode<AppJwtPayload>(token);
    this.userService.getUserById(decoded.userId).subscribe({
      next: (user) => this.currentUserSubject.next(user),
      error: (err) => console.error('Failed to refresh user data', err),
    });
  }

}
