import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PharmacyReportService {
  private api = environment.apiUrl + 'pharmacy/reports';
  constructor(private http: HttpClient) {}
  dailySales(date: string): Observable<any> { return this.http.get(`${this.api}/daily-sales`, { params: { date } }); }
  monthlySales(year: number, month: number): Observable<any> { return this.http.get(`${this.api}/monthly-sales`, { params: { year: year.toString(), month: month.toString() } }); }
  purchaseReport(startDate: string, endDate: string): Observable<any> { return this.http.get(`${this.api}/purchase-report`, { params: { startDate, endDate } }); }
  stockReport(): Observable<any> { return this.http.get(`${this.api}/stock-report`); }
  supplierReport(): Observable<any> { return this.http.get(`${this.api}/supplier-report`); }
}
