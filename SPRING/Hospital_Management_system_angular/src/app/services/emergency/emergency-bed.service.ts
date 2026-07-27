import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyBed } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyBedService {
  private api = environment.apiUrl + 'emergency/beds';

  constructor(private http: HttpClient) {}

  create(bed: EmergencyBed): Observable<EmergencyBed> {
    return this.http.post<EmergencyBed>(this.api, bed);
  }

  getById(id: number): Observable<EmergencyBed> {
    return this.http.get<EmergencyBed>(`${this.api}/${id}`);
  }

  getAll(): Observable<EmergencyBed[]> {
    return this.http.get<EmergencyBed[]>(this.api);
  }

  updateStatus(id: number, status: string): Observable<EmergencyBed> {
    return this.http.put<EmergencyBed>(`${this.api}/${id}/status`, { status });
  }

  assignBed(bedId: number, emergencyPatientId: number): Observable<EmergencyBed> {
    return this.http.put<EmergencyBed>(`${this.api}/${bedId}/assign`, { emergencyPatientId });
  }

  releaseBed(id: number): Observable<EmergencyBed> {
    return this.http.put<EmergencyBed>(`${this.api}/${id}/release`, {});
  }

  getByStatus(status: string): Observable<EmergencyBed[]> {
    return this.http.get<EmergencyBed[]>(`${this.api}/status/${status}`);
  }

  getByWardName(wardName: string): Observable<EmergencyBed[]> {
    return this.http.get<EmergencyBed[]>(`${this.api}/ward/${wardName}`);
  }

  getAvailableCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/available-count`);
  }

  getWardSummary(): Observable<any> {
    return this.http.get<any>(`${this.api}/ward-summary`);
  }
}
