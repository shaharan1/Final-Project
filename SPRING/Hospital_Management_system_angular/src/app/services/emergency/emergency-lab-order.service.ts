import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmergencyLabOrder } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class EmergencyLabOrderService {
  private api = environment.apiUrl + 'emergency/lab-orders';

  constructor(private http: HttpClient) {}

  create(order: EmergencyLabOrder): Observable<EmergencyLabOrder> {
    return this.http.post<EmergencyLabOrder>(this.api, order);
  }

  getByEmergencyPatientId(emergencyPatientId: number): Observable<EmergencyLabOrder[]> {
    return this.http.get<EmergencyLabOrder[]>(`${this.api}/patient/${emergencyPatientId}`);
  }

  getAll(): Observable<EmergencyLabOrder[]> {
    return this.http.get<EmergencyLabOrder[]>(this.api);
  }

  updateStatus(id: number, status: string): Observable<EmergencyLabOrder> {
    return this.http.put<EmergencyLabOrder>(`${this.api}/${id}/status`, { status });
  }

  updateResult(id: number, result: string): Observable<EmergencyLabOrder> {
    return this.http.put<EmergencyLabOrder>(`${this.api}/${id}/result`, { result });
  }

  getCriticalOrders(): Observable<EmergencyLabOrder[]> {
    return this.http.get<EmergencyLabOrder[]>(`${this.api}/critical`);
  }
}
