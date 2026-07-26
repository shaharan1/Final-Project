import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BillingDashboardService {
  private api = environment.apiUrl + 'billing-dashboard';
  constructor(private http: HttpClient) {}
  getSummary(): Observable<any> { return this.http.get<any>(`${this.api}/summary`); }
  getDailyRevenueChart(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/daily-revenue-chart`); }
  getMonthlyRevenueChart(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/monthly-revenue-chart`); }
  getDepartmentRevenue(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/department-revenue`); }
  getPaymentMethodDistribution(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/payment-methods`); }
  getRecentActivity(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/recent-activity`); }
}
