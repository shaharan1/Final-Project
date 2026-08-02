import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DoctorChargeModel {
  id: number;
  description: string;
  amount: number;
  visitDate: string;
  doctorId: number;
  doctorName: string;
  bedBookingId: number;
  admittedPatientId: number;
  patientName: string;
  admissionStatus: string;
  billingStatus: string;
}

@Injectable({ providedIn: 'root' })
export class DoctorChargeService {
  private api = environment.apiUrl + 'doctor-charges';
  constructor(private http: HttpClient) {}

  create(data: any): Observable<DoctorChargeModel> {
    return this.http.post<DoctorChargeModel>(this.api, data);
  }

  getAll(): Observable<DoctorChargeModel[]> {
    return this.http.get<DoctorChargeModel[]>(this.api);
  }

  getById(id: number): Observable<DoctorChargeModel> {
    return this.http.get<DoctorChargeModel>(`${this.api}/${id}`);
  }

  update(id: number, data: any): Observable<DoctorChargeModel> {
    return this.http.put<DoctorChargeModel>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }
}
