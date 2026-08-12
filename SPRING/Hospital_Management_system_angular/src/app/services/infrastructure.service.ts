import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { WardModel } from '../models/ward.model';
import { BedModel } from '../models/bed.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class InfrastructureService {

  api = environment.apiUrl + "infrastructure";

  constructor(private http: HttpClient) { }

  getAllWards(): Observable<WardModel[]> {
    return this.http.get<WardModel[]>(this.api + "/wards");
  }

  getAllBeds(): Observable<BedModel[]> {
    return this.http.get<BedModel[]>(this.api + "/beds");
  }

  getBedsByWard(id: number): Observable<BedModel[]> {
    return this.http.get<BedModel[]>(`${this.api}/wards/${id}/beds`);
  }

  createWard(data: { name: string; departmentId: number; roomType: string; totalBeds: number; basePricePerDay: number }): Observable<WardModel> {
    return this.http.post<WardModel>(this.api + "/wards", data);
  }

  updateWard(id: number, data: { name: string; departmentId: number; roomType: string; totalBeds: number; basePricePerDay: number }): Observable<WardModel> {
    return this.http.put<WardModel>(`${this.api}/wards/${id}`, data);
  }

  deleteWard(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/wards/${id}`);
  }

  createBed(data: { bedNumber: string; wardId: number; status?: string }): Observable<BedModel> {
    return this.http.post<BedModel>(this.api + "/beds", data);
  }

  deleteBed(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/beds/${id}`);
  }

}
