import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class InsuranceClaimService {
  private api = environment.apiUrl + 'insurance-claims';
  constructor(private http: HttpClient) {}
  getAll(): Observable<any[]> { return this.http.get<any[]>(this.api); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.api}/${id}`); }
  getPending(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/pending`); }
  create(data: any): Observable<any> { return this.http.post<any>(this.api, data); }
  approve(id: number, amount: number): Observable<any> { return this.http.put<any>(`${this.api}/${id}/approve`, { approvedAmount: amount }); }
  reject(id: number, reason: string): Observable<any> { return this.http.put<any>(`${this.api}/${id}/reject`, { reason }); }
  settle(id: number): Observable<any> { return this.http.put<any>(`${this.api}/${id}/settle`, {}); }
  getByInsurance(insuranceId: number): Observable<any[]> { return this.http.get<any[]>(`${this.api}/insurance/${insuranceId}`); }
}
