import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TestOrderModel, LabStats } from '../models/test-order.model';

@Injectable({ providedIn: 'root' })
export class TestOrderService {

  private api = environment.apiUrl + 'test-orders';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TestOrderModel[]> {
    return this.http.get<TestOrderModel[]>(this.api);
  }

  getById(id: number): Observable<TestOrderModel> {
    return this.http.get<TestOrderModel>(`${this.api}/${id}`);
  }

  getByStatus(status: string): Observable<TestOrderModel[]> {
    return this.http.get<TestOrderModel[]>(`${this.api}/status/${status}`);
  }

  getByPatient(patientId: number): Observable<TestOrderModel[]> {
    return this.http.get<TestOrderModel[]>(`${this.api}/patient/${patientId}`);
  }

  getByDoctor(doctorId: number): Observable<TestOrderModel[]> {
    return this.http.get<TestOrderModel[]>(`${this.api}/doctor/${doctorId}`);
  }

  getStats(): Observable<LabStats> {
    return this.http.get<LabStats>(`${this.api}/stats`);
  }

  collectSample(id: number, collectorName: string, sampleType: string): Observable<TestOrderModel> {
    return this.http.put<TestOrderModel>(`${this.api}/${id}/collect-sample`, { collectorName, sampleType });
  }

  receiveSample(id: number, receivedBy: string): Observable<TestOrderModel> {
    return this.http.put<TestOrderModel>(`${this.api}/${id}/receive-sample`, { receivedBy });
  }

  startTesting(id: number): Observable<TestOrderModel> {
    return this.http.put<TestOrderModel>(`${this.api}/${id}/start-testing`, {});
  }

  enterResult(id: number, resultValue: string, resultNotes: string, enteredBy: string): Observable<TestOrderModel> {
    return this.http.put<TestOrderModel>(`${this.api}/${id}/enter-result`, { resultValue, resultNotes, enteredBy });
  }

  verifyResult(id: number, verifiedBy: string, verificationNotes: string): Observable<TestOrderModel> {
    return this.http.put<TestOrderModel>(`${this.api}/${id}/verify`, { verifiedBy, verificationNotes });
  }

  downloadReportPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.api}/${id}/report/pdf`, { responseType: 'blob' });
  }
}
