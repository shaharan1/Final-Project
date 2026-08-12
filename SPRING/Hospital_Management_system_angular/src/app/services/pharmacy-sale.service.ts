import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PharmacySaleModel } from '../models/pharmacy-sale.model';

@Injectable({ providedIn: 'root' })
export class PharmacySaleService {
  private api = environment.apiUrl + 'pharmacy/sales';
  constructor(private http: HttpClient) {}
  processSale(data: PharmacySaleModel): Observable<PharmacySaleModel> { return this.http.post<PharmacySaleModel>(this.api, data); }
  getAll(): Observable<PharmacySaleModel[]> { return this.http.get<PharmacySaleModel[]>(this.api); }
  getById(id: number): Observable<PharmacySaleModel> { return this.http.get<PharmacySaleModel>(`${this.api}/${id}`); }
}
