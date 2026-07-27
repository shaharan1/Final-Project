import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DietHistory } from '../../models/dietary/diet-history.model';

@Injectable({ providedIn: 'root' })
export class DietHistoryService {
  private api = environment.apiUrl + 'diet-history';

  constructor(private http: HttpClient) {}

  getAll(): Observable<DietHistory[]> {
    return this.http.get<DietHistory[]>(this.api);
  }
  getById(id: number): Observable<DietHistory> {
    return this.http.get<DietHistory>(`${this.api}/${id}`);
  }
  getByPatientId(patientId: number): Observable<DietHistory[]> {
    return this.http.get<DietHistory[]>(`${this.api}/patient/${patientId}`);
  }
  getByDietAssignmentId(assignmentId: number): Observable<DietHistory[]> {
    return this.http.get<DietHistory[]>(`${this.api}/assignment/${assignmentId}`);
  }
  create(data: DietHistory): Observable<DietHistory> {
    return this.http.post<DietHistory>(this.api, data);
  }
}
