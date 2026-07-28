import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  private api = environment.apiUrl + 'invoices';

  constructor(private http: HttpClient) {}

  generateInvoice(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.api}/${id}`);
  }

  getDraftInvoice(admittedPatientId: number): Observable<any> {
    return this.http.get<any>(`${this.api}/admitted-patients/draft/${admittedPatientId}`);
  }

  finalizeInvoice(data: any): Observable<any> {
    return this.http.post<any>(`${this.api}/admitted-patients/finalize`, data);
  }
}
