import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginDto, LoginResponse } from './login-dto';
import { Environment } from '../../core/Environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http: HttpClient = inject(HttpClient);
  private router: Router = inject(Router);

  isAuthenticated = signal<boolean>(!!sessionStorage.getItem('accessToken'));

  login(body: LoginDto): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${Environment.API_URL}/auth/login`, body).pipe(
      tap(({ accessToken, refreshToken }) => {
        sessionStorage.setItem('accessToken', accessToken);
        sessionStorage.setItem('refreshToken', refreshToken);
        this.isAuthenticated.set(true);
      }),
    );
  }

  logout(body: { refreshToken?: string } = {}) {
    return this.http.post(`${Environment.API_URL}/auth/logout`, body).pipe(
      tap(() => {
        sessionStorage.removeItem('accessToken');
        sessionStorage.removeItem('refreshToken');
        this.isAuthenticated.set(false);
        this.router.navigate(['/login']);
      }),
    );
  }

  get token() {
    return sessionStorage.getItem('accessToken');
  }

  refresh() {
    const refreshToken = sessionStorage.getItem('refreshToken');
    if (!refreshToken) {
      this.logout();
      return;
    }

    return this.http
      .post<{
        newAccessToken: string;
        newRefreshToken: string;
      }>(`${Environment.API_URL}/auth/refresh`, { refreshToken })
      .pipe(
        tap(({ newAccessToken, newRefreshToken }) => {
          sessionStorage.setItem('accessToken', newAccessToken);
          sessionStorage.setItem('refreshToken', newRefreshToken);
        }),
      );
  }
}
