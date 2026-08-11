import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Triage } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class TriageService {
  private api = environment.apiUrl + 'emergency/triage';

  constructor(private http: HttpClient) {}

  create(triage: Triage): Observable<Triage> {
    return this.http.post<Triage>(this.api, triage);
  }

  getByEmergencyPatientId(emergencyPatientId: number): Observable<Triage> {
    return this.http.get<Triage>(`${this.api}/patient/${emergencyPatientId}`);
  }

  getAll(): Observable<Triage[]> {
    return this.http.get<Triage[]>(this.api);
  }

  update(id: number, triage: Triage): Observable<Triage> {
    return this.http.put<Triage>(`${this.api}/${id}`, triage);
  }

  getTriageDistribution(): Observable<any> {
    return this.http.get<any>(`${this.api}/distribution`);
  }
}
