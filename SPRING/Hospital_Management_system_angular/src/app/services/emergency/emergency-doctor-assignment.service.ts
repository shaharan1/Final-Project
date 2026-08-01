import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyDoctorAssignment } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyDoctorAssignmentService {
  private api = environment.apiUrl + 'emergency/assignments';

  constructor(private http: HttpClient) {}

  create(assignment: EmergencyDoctorAssignment): Observable<EmergencyDoctorAssignment> {
    return this.http.post<EmergencyDoctorAssignment>(this.api, assignment);
  }

  getByEmergencyPatientId(emergencyPatientId: number): Observable<EmergencyDoctorAssignment[]> {
    return this.http.get<EmergencyDoctorAssignment[]>(`${this.api}/patient/${emergencyPatientId}`);
  }

  getAll(): Observable<EmergencyDoctorAssignment[]> {
    return this.http.get<EmergencyDoctorAssignment[]>(this.api);
  }

  unassign(id: number): Observable<void> {
    return this.http.put<void>(`${this.api}/${id}/unassign`, {});
  }

  getActiveAssignments(): Observable<EmergencyDoctorAssignment[]> {
    return this.http.get<EmergencyDoctorAssignment[]>(`${this.api}/active`);
  }
}
