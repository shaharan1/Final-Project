import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PurchaseModel } from '../models/purchase.model';

@Injectable({ providedIn: 'root' })
export class PurchasePharmacyService {
  private api = environment.apiUrl + 'purchases';
  constructor(private http: HttpClient) {}
  create(data: PurchaseModel): Observable<PurchaseModel> { return this.http.post<PurchaseModel>(this.api, data); }
  getById(id: number): Observable<PurchaseModel> { return this.http.get<PurchaseModel>(`${this.api}/${id}`); }
  getAll(): Observable<PurchaseModel[]> { return this.http.get<PurchaseModel[]>(this.api); }
  delete(id: number): Observable<any> { return this.http.delete(`${this.api}/${id}`); }
}
