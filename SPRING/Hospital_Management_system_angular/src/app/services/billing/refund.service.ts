import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RefundService {
  private api = environment.apiUrl + 'refunds';
  constructor(private http: HttpClient) {}
  getAll(): Observable<any[]> { return this.http.get<any[]>(this.api); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.api}/${id}`); }
  getPending(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/pending`); }
  create(data: any): Observable<any> { return this.http.post<any>(this.api, data); }
  approve(id: number, approvedBy: string): Observable<any> { return this.http.put<any>(`${this.api}/${id}/approve`, { approvedBy }); }
  reject(id: number, reason: string): Observable<any> { return this.http.put<any>(`${this.api}/${id}/reject`, { reason }); }
  process(id: number): Observable<any> { return this.http.put<any>(`${this.api}/${id}/process`, {}); }
  getByStatus(status: string): Observable<any[]> { return this.http.get<any[]>(`${this.api}/status/${status}`); }
}
