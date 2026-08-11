import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DoctorNotification } from '../models/doctor-notification.model';

@Injectable({ providedIn: 'root' })
export class LabNotificationService {

  private api = environment.apiUrl + 'lab/notifications';

  constructor(private http: HttpClient) {}

  getByDoctor(doctorId: number): Observable<DoctorNotification[]> {
    return this.http.get<DoctorNotification[]>(`${this.api}/doctor/${doctorId}`);
  }

  getUnreadByDoctor(doctorId: number): Observable<DoctorNotification[]> {
    return this.http.get<DoctorNotification[]>(`${this.api}/doctor/${doctorId}/unread`);
  }

  getUnreadCount(doctorId: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.api}/doctor/${doctorId}/unread-count`);
  }

  markRead(id: number): Observable<any> {
    return this.http.put(`${this.api}/${id}/read`, {});
  }

  markAllRead(doctorId: number): Observable<any> {
    return this.http.put(`${this.api}/read-all/doctor/${doctorId}`, {});
  }
}
