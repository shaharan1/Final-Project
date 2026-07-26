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
    return this.http.get<PatientAnalytics>(`${this.api}/patient/analytics`);
  }

  getAppointmentAnalytics(): Observable<AppointmentAnalytics> {
    return this.http.get<AppointmentAnalytics>(`${this.api}/appointment/analytics`);
  }

  getDoctorAnalytics(): Observable<DoctorAnalytics> {
    return this.http.get<DoctorAnalytics>(`${this.api}/doctor/analytics`);
  }

  getLabAnalytics(): Observable<LabAnalytics> {
    return this.http.get<LabAnalytics>(`${this.api}/laboratory/analytics`);
  }

  getPharmacyAnalytics(): Observable<PharmacyAnalytics> {
    return this.http.get<PharmacyAnalytics>(`${this.api}/pharmacy/analytics`);
  }

  getRevenueAnalytics(): Observable<RevenueAnalytics> {
    return this.http.get<RevenueAnalytics>(`${this.api}/revenue/analytics`);
  }

  getBedOccupancy(): Observable<BedOccupancy> {
    return this.http.get<BedOccupancy>(`${this.api}/bed/occupancy`);
  }

  getEmergencyAnalytics(): Observable<EmergencyAnalytics> {
    return this.http.get<EmergencyAnalytics>(`${this.api}/emergency/analytics`);
  }

  getFinancialAnalytics(): Observable<FinancialAnalytics> {
    return this.http.get<FinancialAnalytics>(`${this.api}/financial/analytics`);
  }

  getDailyRevenue(date: string): Observable<any> {
    return this.http.get(`${this.api}/daily-revenue`, { params: { date } });
  }

  getMonthlyRevenue(year: number, month: number): Observable<any> {
    return this.http.get(`${this.api}/monthly-revenue`, { params: { year: year.toString(), month: month.toString() } });
  }

  getDepartmentRevenue(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/department-revenue`);
  }

  getTopDoctors(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/top-doctors`);
  }

  getTopMedicines(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/top-medicines`);
  }

  getRecentActivity(): Observable<ActivityItem[]> {
    return this.http.get<ActivityItem[]>(`${this.api}/recent-activity`);
  }
}
