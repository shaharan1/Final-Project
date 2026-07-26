import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DietAssignment } from '../../models/dietary/diet-assignment.model';

@Injectable({ providedIn: 'root' })
export class DietAssignmentService {
  private api = environment.apiUrl + 'diet-assignments';

  constructor(private http: HttpClient) {}

  getAll(): Observable<DietAssignment[]> {
    return this.http.get<DietAssignment[]>(this.api);
  }
  getById(id: number): Observable<DietAssignment> {
    return this.http.get<DietAssignment>(`${this.api}/${id}`);
  }
  getByStatus(status: string): Observable<DietAssignment[]> {
    return this.http.get<DietAssignment[]>(`${this.api}/status/${status}`);
  }
  getByPatientId(patientId: number): Observable<DietAssignment[]> {
    return this.http.get<DietAssignment[]>(`${this.api}/patient/${patientId}`);
  }
  getByAdmittedPatientId(id: number): Observable<DietAssignment[]> {
    return this.http.get<DietAssignment[]>(`${this.api}/admitted-patient/${id}`);
  }
  getByDieticianId(dieticianId: number): Observable<DietAssignment[]> {
    return this.http.get<DietAssignment[]>(`${this.api}/dietician/${dieticianId}`);
  }
  getActive(): Observable<DietAssignment[]> {
    return this.http.get<DietAssignment[]>(`${this.api}/active`);
  }
  create(data: DietAssignment): Observable<DietAssignment> {
    return this.http.post<DietAssignment>(this.api, data);
  }
  update(id: number, data: DietAssignment): Observable<DietAssignment> {
    return this.http.put<DietAssignment>(`${this.api}/${id}`, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
  getActiveCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/count/active`);
  }
}
