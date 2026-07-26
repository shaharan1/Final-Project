import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../models/login.model';

@Injectable({ providedIn: 'root' })
export class ProfileService {

  private api = environment.apiUrl + 'profile';

  constructor(private http: HttpClient) {}

  getProfile(): Observable<LoginResponse> {
    return this.http.get<LoginResponse>(this.api);
  }

  updateProfile(data: { name: string; phone: string }): Observable<LoginResponse> {
    return this.http.put<LoginResponse>(this.api, data);
  }

  uploadImage(file: File): Observable<LoginResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<LoginResponse>(`${this.api}/image`, formData);
  }

  changePassword(currentPassword: string, newPassword: string): Observable<string> {
    return this.http.put(`${this.api}/password`, { currentPassword, newPassword }, { responseType: 'text' });
  }
}
