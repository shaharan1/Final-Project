import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyMedicine } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyMedicineService {
  private api = environment.apiUrl + 'emergency/medicines';

  constructor(private http: HttpClient) {}

  create(medicine: EmergencyMedicine): Observable<EmergencyMedicine> {
    return this.http.post<EmergencyMedicine>(this.api, medicine);
  }

  getByEmergencyPatientId(emergencyPatientId: number): Observable<EmergencyMedicine[]> {
    return this.http.get<EmergencyMedicine[]>(`${this.api}/patient/${emergencyPatientId}`);
  }

  getAll(): Observable<EmergencyMedicine[]> {
    return this.http.get<EmergencyMedicine[]>(this.api);
  }

  updateStatus(id: number, status: string): Observable<EmergencyMedicine> {
    return this.http.put<EmergencyMedicine>(`${this.api}/${id}/status`, { status });
  }

  requestPharmacy(id: number): Observable<EmergencyMedicine> {
    return this.http.put<EmergencyMedicine>(`${this.api}/${id}/request-pharmacy`, {});
  }

  getByStatus(status: string): Observable<EmergencyMedicine[]> {
    return this.http.get<EmergencyMedicine[]>(`${this.api}/status/${status}`);
  }
}
