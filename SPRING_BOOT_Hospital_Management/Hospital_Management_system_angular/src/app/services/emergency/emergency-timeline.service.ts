import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyTimeline } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyTimelineService {
  private api = environment.apiUrl + 'emergency/timeline';

  constructor(private http: HttpClient) {}

  getByEmergencyPatientId(emergencyPatientId: number): Observable<EmergencyTimeline[]> {
    return this.http.get<EmergencyTimeline[]>(`${this.api}/patient/${emergencyPatientId}`);
  }

  getAll(): Observable<EmergencyTimeline[]> {
    return this.http.get<EmergencyTimeline[]>(this.api);
  }

  addEvent(emergencyPatientId: number, event: EmergencyTimeline): Observable<EmergencyTimeline> {
    return this.http.post<EmergencyTimeline>(`${this.api}/patient/${emergencyPatientId}`, event);
  }
}
