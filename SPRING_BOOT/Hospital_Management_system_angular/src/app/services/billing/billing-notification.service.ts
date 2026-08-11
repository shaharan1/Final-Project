import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BillingNotificationService {
  private api = environment.apiUrl + 'billing-notifications';
  constructor(private http: HttpClient) {}
  getAll(): Observable<any[]> { return this.http.get<any[]>(this.api); }
  getUnread(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/unread`); }
  getUnreadCount(): Observable<number> { return this.http.get<number>(`${this.api}/unread-count`); }
  markAsRead(id: number): Observable<any> { return this.http.put<any>(`${this.api}/${id}/read`, {}); }
  markAllAsRead(): Observable<any> { return this.http.put<any>(`${this.api}/read-all`, {}); }
  delete(id: number): Observable<any> { return this.http.delete<any>(`${this.api}/${id}`); }
}
