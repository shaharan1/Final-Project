import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupplierModel } from '../models/supplier.model';

@Injectable({ providedIn: 'root' })
export class SupplierService {
  private api = environment.apiUrl + 'suppliers';
  constructor(private http: HttpClient) {}
  create(data: SupplierModel): Observable<SupplierModel> { return this.http.post<SupplierModel>(this.api, data); }
  update(id: number, data: SupplierModel): Observable<SupplierModel> { return this.http.put<SupplierModel>(`${this.api}/${id}`, data); }
  getById(id: number): Observable<SupplierModel> { return this.http.get<SupplierModel>(`${this.api}/${id}`); }
  getAll(): Observable<SupplierModel[]> { return this.http.get<SupplierModel[]>(this.api); }
  getActive(): Observable<SupplierModel[]> { return this.http.get<SupplierModel[]>(`${this.api}/active`); }
  search(keyword: string): Observable<SupplierModel[]> { return this.http.get<SupplierModel[]>(`${this.api}/search?keyword=${encodeURIComponent(keyword)}`); }
  getWithDue(): Observable<SupplierModel[]> { return this.http.get<SupplierModel[]>(`${this.api}/due`); }
  delete(id: number): Observable<any> { return this.http.delete(`${this.api}/${id}`); }
}
