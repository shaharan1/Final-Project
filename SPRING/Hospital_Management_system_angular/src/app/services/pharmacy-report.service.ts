import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PharmacyReportService {
  private api = environment.apiUrl + 'pharmacy/reports';
  constructor(private http: HttpClient) {}
  getDailySales(date: string): Observable<any> { return this.http.get(`${this.api}/daily-sales`, { params: { date } }); }
  getMonthlySales(year: string, month: string): Observable<any> { return this.http.get(`${this.api}/monthly-sales`, { params: { year, month } }); }
  getPurchaseReport(startDate: string, endDate: string): Observable<any> { return this.http.get(`${this.api}/purchase-report`, { params: { start: startDate, end: endDate } }); }
  getStockReport(): Observable<any> { return this.http.get(`${this.api}/stock-report`); }
  getSupplierReport(): Observable<any> { return this.http.get(`${this.api}/supplier-report`); }
}
