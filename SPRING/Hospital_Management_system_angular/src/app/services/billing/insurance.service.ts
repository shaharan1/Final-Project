import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class InsuranceService {
  private api = environment.apiUrl + 'insurance';
  constructor(private http: HttpClient) {}
  getAll(): Observable<any[]> { return this.http.get<any[]>(this.api); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.api}/${id}`); }
  getActive(): Observable<any[]> { return this.http.get<any[]>(`${this.api}/active`); }
  create(data: any): Observable<any> { return this.http.post<any>(this.api, data); }
  update(id: number, data: any): Observable<any> { return this.http.put<any>(`${this.api}/${id}`, data); }
  delete(id: number): Observable<any> { return this.http.delete<any>(`${this.api}/${id}`); }
  search(keyword: string): Observable<any[]> { return this.http.get<any[]>(`${this.api}/search`, { params: { keyword } }); }
}
