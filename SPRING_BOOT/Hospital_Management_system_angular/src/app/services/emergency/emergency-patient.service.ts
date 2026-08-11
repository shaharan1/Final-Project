import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyPatient, EmergencyDashboard, EmergencyStatusUpdate } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyPatientService {
  private api = environment.apiUrl + 'emergency/patients';

  constructor(private http: HttpClient) {}

  create(patient: EmergencyPatient): Observable<EmergencyPatient> {
    return this.http.post<EmergencyPatient>(this.api, patient);
  }

  getAll(): Observable<EmergencyPatient[]> {
    return this.http.get<EmergencyPatient[]>(this.api);
  }

  getById(id: number): Observable<EmergencyPatient> {
    return this.http.get<EmergencyPatient>(`${this.api}/${id}`);
  }

  search(keyword: string): Observable<EmergencyPatient[]> {
    return this.http.get<EmergencyPatient[]>(`${this.api}/search?keyword=${keyword}`);
  }

  update(id: number, patient: EmergencyPatient): Observable<EmergencyPatient> {
    return this.http.put<EmergencyPatient>(`${this.api}/${id}`, patient);
  }

  updateStatus(id: number, update: EmergencyStatusUpdate): Observable<EmergencyPatient> {
    return this.http.put<EmergencyPatient>(`${this.api}/${id}/status`, update);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  getDashboard(): Observable<EmergencyDashboard> {
    return this.http.get<EmergencyDashboard>(`${this.api}/dashboard`);
  }

  getByStatus(status: string): Observable<EmergencyPatient[]> {
    return this.http.get<EmergencyPatient[]>(`${this.api}/status/${status}`);
  }

  getByTriageLevel(level: number): Observable<EmergencyPatient[]> {
    return this.http.get<EmergencyPatient[]>(`${this.api}/triage/${level}`);
  }
}
