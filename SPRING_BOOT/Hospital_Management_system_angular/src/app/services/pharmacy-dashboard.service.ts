import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PharmacyDashboardModel } from '../models/pharmacy-dashboard.model';

@Injectable({ providedIn: 'root' })
export class PharmacyDashboardService {
  private api = environment.apiUrl + 'pharmacy/dashboard';
  constructor(private http: HttpClient) {}
  getDashboard(): Observable<PharmacyDashboardModel> { return this.http.get<PharmacyDashboardModel>(this.api); }
  getExpiryAlerts(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/expiry-alerts`); }
}
