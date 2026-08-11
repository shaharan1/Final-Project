import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ForgotPasswordRequest, LoginRequest, LoginResponse, ResetPasswordRequest } from '../models/login.model';
import { Observable, tap } from 'rxjs';
import { StorageService } from './storage.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {


  private api = `${environment.apiUrl}auth`;

  constructor(private http: HttpClient, private storage: StorageService,
    private router: Router) { }

  login(dto: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.api}/login`, dto)
      .pipe(
        tap(res => this.storage.saveSession(res))
      );
  }

  verifyEmail(token: string): Observable<string> {
    return this.http.get(
      `${this.api}/verify-email?token=${token}`,
      {
        responseType: 'text'
      }
    );
  }

  forgotPassword(data: ForgotPasswordRequest): Observable<string> {
    return this.http.post(
      `${this.api}/forgot-password`,
      data,
      {
        responseType: 'text'
      }
    );
  }

  resetPassword(data: ResetPasswordRequest): Observable<string> {
    return this.http.post(
      `${this.api}/reset-password`,
      data,
      {
        responseType: 'text'
      }
    );
  }



      // ── Logout ───────────────────────────────────────────

  logout(): void {
    this.storage.clearSession();
    this.router.navigate(['/login']);
  }



}
