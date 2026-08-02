import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BillingInvoiceService {
  private api = environment.apiUrl + 'billing-invoices';

  constructor(private http: HttpClient) {}

  createInvoice(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.api}/${id}`);
  }

  getByInvoiceNumber(invoiceNumber: string): Observable<any> {
    return this.http.get<any>(`${this.api}/number/${invoiceNumber}`);
  }

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getByPatientId(patientId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/patient/${patientId}`);
  }

  getByAdmittedPatientId(admittedPatientId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/admission/${admittedPatientId}`);
  }

  searchInvoices(search: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/search`, { params: { search } });
  }

  updateInvoice(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}`, data);
  }

  addItem(invoiceId: number, item: any): Observable<any> {
    return this.http.post<any>(`${this.api}/${invoiceId}/items`, item);
  }

  removeItem(invoiceId: number, itemId: number): Observable<any> {
    return this.http.delete<any>(`${this.api}/${invoiceId}/items/${itemId}`);
  }

  finalizeInvoice(id: number, finalizedBy?: string): Observable<any> {
    let params = new HttpParams();
    if (finalizedBy) params = params.set('finalizedBy', finalizedBy);
    return this.http.put<any>(`${this.api}/${id}/finalize`, {}, { params });
  }

  cancelInvoice(id: number): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}/cancel`, {});
  }

  syncFromModules(admittedPatientId: number): Observable<any> {
    return this.http.post<any>(`${this.api}/sync/${admittedPatientId}`, {});
  }

  processPayment(data: any): Observable<any> {
    return this.http.post<any>(`${this.api}/payments`, data);
  }

  getPaymentsByInvoice(invoiceId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/${invoiceId}/payments`);
  }

  getChargeCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/categories`);
  }

  getDashboardSummary(): Observable<any> {
    return this.http.get<any>(`${this.api}/dashboard-summary`);
  }
}
