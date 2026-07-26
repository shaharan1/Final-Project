import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private api = environment.apiUrl + 'payments';
  constructor(private http: HttpClient) {}
  getAll(): Observable<any[]> { return this.http.get<any[]>(this.api); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.api}/${id}`); }
  getByInvoice(invoiceNumber: string): Observable<any[]> { return this.http.get<any[]>(`${this.api}/invoice/${invoiceNumber}`); }
  getByPatient(patientId: number): Observable<any[]> { return this.http.get<any[]>(`${this.api}/patient/${patientId}`); }
  processPayment(data: any): Observable<any> { return this.http.post<any>(this.api, data); }
  updatePayment(id: number, data: any): Observable<any> { return this.http.put<any>(`${this.api}/${id}`, data); }
  deletePayment(id: number): Observable<any> { return this.http.delete<any>(`${this.api}/${id}`); }
  getDashboardStats(): Observable<any> { return this.http.get<any>(`${this.api}/dashboard-stats`); }
  getDailyRevenue(date: string): Observable<any> { return this.http.get<any>(`${this.api}/daily-revenue`, { params: { date } }); }
  getMonthlyRevenue(year: number, month: number): Observable<any> { return this.http.get<any>(`${this.api}/monthly-revenue`, { params: { year, month } }); }
  getMethodBreakdown(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/method-breakdown`); }
}
