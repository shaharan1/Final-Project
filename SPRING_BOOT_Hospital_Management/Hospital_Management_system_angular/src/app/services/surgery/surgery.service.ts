import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SurgeryRequest } from '../../models/surgery/surgery.model';
import { SurgeryResponse } from '../../models/surgery/surgery-response.model';
import { SurgerySchedule } from '../../models/surgery/surgery-schedule.model';
import { SurgeryDashboard } from '../../models/surgery/surgery-dashboard.model';

@Injectable({ providedIn: 'root' })
export class SurgeryService {
  private api = environment.apiUrl + 'surgeries';
  private dashboardApi = environment.apiUrl + 'surgery-dashboard';

  constructor(private http: HttpClient) {}

  create(data: SurgeryRequest): Observable<SurgeryResponse> {
    return this.http.post<SurgeryResponse>(this.api, data);
  }

  getById(id: number): Observable<SurgeryResponse> {
    return this.http.get<SurgeryResponse>(`${this.api}/${id}`);
  }

  getAll(): Observable<SurgeryResponse[]> {
    return this.http.get<SurgeryResponse[]>(this.api);
  }

  getByPatientId(patientId: number): Observable<SurgeryResponse[]> {
    return this.http.get<SurgeryResponse[]>(`${this.api}/patient/${patientId}`);
  }

  getByAdmittedPatientId(admittedPatientId: number): Observable<SurgeryResponse[]> {
    return this.http.get<SurgeryResponse[]>(`${this.api}/admission/${admittedPatientId}`);
  }

  getByDateRange(from: string, to: string): Observable<SurgeryResponse[]> {
    const params = new HttpParams().set('from', from).set('to', to);
    return this.http.get<SurgeryResponse[]>(`${this.api}/date-range`, { params });
  }

  getByTheatreAndDate(operationTheatreId: number, date: string): Observable<SurgeryResponse[]> {
    const params = new HttpParams()
      .set('operationTheatreId', operationTheatreId)
      .set('date', date);
    return this.http.get<SurgeryResponse[]>(`${this.api}/theatre`, { params });
  }

  search(q: string): Observable<SurgeryResponse[]> {
    return this.http.get<SurgeryResponse[]>(`${this.api}/search`, { params: { q } });
  }

  getSchedule(date?: string): Observable<SurgerySchedule[]> {
    let params = new HttpParams();
    if (date) params = params.set('date', date);
    return this.http.get<SurgerySchedule[]>(`${this.api}/schedule`, { params });
  }

  getUpcomingSchedule(): Observable<SurgerySchedule[]> {
    return this.http.get<SurgerySchedule[]>(`${this.api}/schedule/upcoming`);
  }

  update(id: number, data: SurgeryRequest): Observable<SurgeryResponse> {
    return this.http.put<SurgeryResponse>(`${this.api}/${id}`, data);
  }

  updateStatus(id: number, status: string, cancellationReason?: string): Observable<SurgeryResponse> {
    let params = new HttpParams().set('status', status);
    if (cancellationReason) params = params.set('cancellationReason', cancellationReason);
    return this.http.put<SurgeryResponse>(`${this.api}/${id}/status`, {}, { params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  getDashboard(date?: string): Observable<SurgeryDashboard> {
    let params = new HttpParams();
    if (date) params = params.set('date', date);
    return this.http.get<SurgeryDashboard>(this.dashboardApi, { params });
  }
}
