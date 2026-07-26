import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PatientDietAlert } from '../../models/dietary/patient-diet-alert.model';

@Injectable({ providedIn: 'root' })
export class PatientDietAlertService {
  private api = environment.apiUrl + 'diet-alerts';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PatientDietAlert[]> {
    return this.http.get<PatientDietAlert[]>(this.api);
  }
  getById(id: number): Observable<PatientDietAlert> {
    return this.http.get<PatientDietAlert>(`${this.api}/${id}`);
  }
  getActive(): Observable<PatientDietAlert[]> {
    return this.http.get<PatientDietAlert[]>(`${this.api}/active`);
  }
  getByPatientId(patientId: number): Observable<PatientDietAlert[]> {
    return this.http.get<PatientDietAlert[]>(`${this.api}/patient/${patientId}`);
  }
  getByAlertType(alertType: string): Observable<PatientDietAlert[]> {
    return this.http.get<PatientDietAlert[]>(`${this.api}/alert-type/${alertType}`);
  }
  updateStatus(id: number, status: string): Observable<PatientDietAlert> {
    return this.http.put<PatientDietAlert>(`${this.api}/${id}/status?status=${status}`, {});
  }
  create(data: PatientDietAlert): Observable<PatientDietAlert> {
    return this.http.post<PatientDietAlert>(this.api, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
  getActiveCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/count/active`);
  }
}
