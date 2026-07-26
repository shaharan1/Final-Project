import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  DashboardSummary,
  PatientAnalytics,
  AppointmentAnalytics,
  DoctorAnalytics,
  LabAnalytics,
  PharmacyAnalytics,
  RevenueAnalytics,
  BedOccupancy,
  EmergencyAnalytics,
  FinancialAnalytics,
  ActivityItem
} from '../../models/reports/analytics.model';

@Injectable({ providedIn: 'root' })
export class AnalyticsService {

  private api = environment.apiUrl + 'reports';

  constructor(private http: HttpClient) {}

  getDashboardSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.api}/dashboard-summary`);
  }

  getPatientAnalytics(): Observable<PatientAnalytics> {
    return this.http.get<PatientAnalytics>(`${this.api}/patients`);
  }

  getAppointmentAnalytics(): Observable<AppointmentAnalytics> {
    return this.http.get<AppointmentAnalytics>(`${this.api}/appointments`);
  }

  getDoctorAnalytics(): Observable<DoctorAnalytics> {
    return this.http.get<DoctorAnalytics>(`${this.api}/doctors`);
  }

  getLabAnalytics(): Observable<LabAnalytics> {
    return this.http.get<LabAnalytics>(`${this.api}/lab`);
  }

  getPharmacyAnalytics(): Observable<PharmacyAnalytics> {
    return this.http.get<PharmacyAnalytics>(`${this.api}/pharmacy`);
  }

  getRevenueAnalytics(): Observable<RevenueAnalytics> {
    return this.http.get<RevenueAnalytics>(`${this.api}/revenue`);
  }

  getBedOccupancy(): Observable<BedOccupancy> {
    return this.http.get<BedOccupancy>(`${this.api}/bed-occupancy`);
  }

  getEmergencyAnalytics(): Observable<EmergencyAnalytics> {
    return this.http.get<EmergencyAnalytics>(`${this.api}/emergency`);
  }

  getFinancialAnalytics(): Observable<FinancialAnalytics> {
    return this.http.get<FinancialAnalytics>(`${this.api}/financial`);
  }

  getDailyRevenue(date: string): Observable<any> {
    return this.http.get(`${this.api}/revenue/daily`, { params: { date } });
  }

  getMonthlyRevenue(year: number, month: number): Observable<any> {
    return this.http.get(`${this.api}/revenue/monthly`, { params: { year: year.toString(), month: month.toString() } });
  }

  getDepartmentRevenue(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/revenue/by-department`);
  }

  getTopDoctors(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/doctors/top`);
  }

  getTopMedicines(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/pharmacy/top-medicines`);
  }

  getRecentActivity(): Observable<ActivityItem[]> {
    return this.http.get<ActivityItem[]>(`${this.api}/activity/recent`);
  }
}
