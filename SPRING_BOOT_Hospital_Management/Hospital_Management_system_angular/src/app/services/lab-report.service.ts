import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { InterpretPreview } from '../models/lab-config.model';
import { LabDashboard, LabReport } from '../models/lab-report.model';

export interface LabReportResultEntry {
  parameterId: number;
  resultValue: string;
}

@Injectable({ providedIn: 'root' })
export class LabReportService {

  private api = environment.apiUrl + 'lab/reports';

  constructor(private http: HttpClient) {}

  createReport(testOrderId: number, enteredBy: string, results: LabReportResultEntry[]): Observable<LabReport> {
    return this.http.post<LabReport>(this.api, { testOrderId, enteredBy, results });
  }

  getAll(): Observable<LabReport[]> {
    return this.http.get<LabReport[]>(this.api);
  }

  getById(id: number): Observable<LabReport> {
    return this.http.get<LabReport>(`${this.api}/${id}`);
  }

  getByTestOrderId(testOrderId: number): Observable<LabReport> {
    return this.http.get<LabReport>(`${this.api}/test-order/${testOrderId}`);
  }

  getByPatient(patientId: number): Observable<LabReport[]> {
    return this.http.get<LabReport[]>(`${this.api}/patient/${patientId}`);
  }

  getDashboard(): Observable<LabDashboard> {
    return this.http.get<LabDashboard>(`${this.api}/dashboard`);
  }

  preview(parameterId: number, resultValue: string, patientGender?: string, ageYears?: number): Observable<InterpretPreview> {
    return this.http.post<InterpretPreview>(`${this.api}/interpret`, { parameterId, resultValue, patientGender, ageYears });
  }

  verifyReport(id: number, specialistName: string, specialistDesignation: string, specialistSignature: string, verificationNotes: string): Observable<LabReport> {
    return this.http.put<LabReport>(`${this.api}/${id}/verify`, {
      specialistName, specialistDesignation, specialistSignature, verificationNotes
    });
  }

  downloadPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.api}/${id}/pdf`, { responseType: 'blob' });
  }
}
