import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BillingDashboardService {
  private api = environment.apiUrl + 'billing-dashboard';
  private reportsApi = environment.apiUrl + 'billing-reports';
  constructor(private http: HttpClient) {}
  getSummary(): Observable<any> { return this.http.get<any>(`${this.api}/summary`); }
  getDailyRevenueChart(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/daily-revenue-chart`); }
  getMonthlyRevenueChart(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/monthly-revenue-chart`); }
  getDepartmentRevenue(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/department-revenue`); }
  getPaymentMethodDistribution(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/payment-methods`); }
  getRecentActivity(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/recent-activity`); }

  getDoctorRevenue(): Observable<any[]> { return this.http.get<any[]>(`${this.reportsApi}/doctor-revenue`); }
  getPharmacyRevenue(): Observable<any[]> { return this.http.get<any[]>(`${this.reportsApi}/pharmacy-revenue`); }
  getLabRevenue(): Observable<any[]> { return this.http.get<any[]>(`${this.reportsApi}/lab-revenue`); }
  getInsuranceReport(): Observable<any[]> { return this.http.get<any[]>(`${this.reportsApi}/insurance-report`); }
  getPendingDue(): Observable<any[]> { return this.http.get<any[]>(`${this.reportsApi}/pending-due`); }
  getProfitLoss(year?: number, month?: number): Observable<any[]> {
    let params = new HttpParams();
    if (year != null) params = params.set('year', year);
    if (month != null) params = params.set('month', month);
    return this.http.get<any[]>(`${this.reportsApi}/profit-loss`, { params });
  }
  getMonthlyCollection(year: number, month: number): Observable<any> {
    return this.http.get<any>(`${this.reportsApi}/monthly-collection`, { params: { year, month } });
  }
}
