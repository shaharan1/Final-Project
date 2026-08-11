import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyBilling } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyBillingService {
  private api = environment.apiUrl + 'emergency/billing';

  constructor(private http: HttpClient) {}

  create(billing: EmergencyBilling): Observable<EmergencyBilling> {
    return this.http.post<EmergencyBilling>(this.api, billing);
  }

  getById(id: number): Observable<EmergencyBilling> {
    return this.http.get<EmergencyBilling>(`${this.api}/${id}`);
  }

  getByEmergencyPatientId(emergencyPatientId: number): Observable<EmergencyBilling> {
    return this.http.get<EmergencyBilling>(`${this.api}/patient/${emergencyPatientId}`);
  }

  getAll(): Observable<EmergencyBilling[]> {
    return this.http.get<EmergencyBilling[]>(this.api);
  }

  generateBill(emergencyPatientId: number): Observable<EmergencyBilling> {
    return this.http.post<EmergencyBilling>(`${this.api}/generate/${emergencyPatientId}`, {});
  }

  updatePaymentStatus(id: number, status: string): Observable<EmergencyBilling> {
    return this.http.put<EmergencyBilling>(`${this.api}/${id}/payment-status`, { status });
  }

  getTodayRevenue(): Observable<number> {
    return this.http.get<number>(`${this.api}/today-revenue`);
  }
}
