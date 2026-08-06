import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { InterpretationRule, LabRule, ReferenceRange, TestMasterDetail, TestParameter } from '../models/lab-config.model';

@Injectable({ providedIn: 'root' })
export class LabConfigService {

  private api = environment.apiUrl + 'lab/config';

  constructor(private http: HttpClient) {}

  getTestMasterDetail(testMasterId: number): Observable<TestMasterDetail> {
    return this.http.get<TestMasterDetail>(`${this.api}/test-masters/${testMasterId}`);
  }

  getAllParameters(): Observable<TestParameter[]> {
    return this.http.get<TestParameter[]>(`${this.api}/parameters`);
  }

  getParameter(id: number): Observable<TestParameter> {
    return this.http.get<TestParameter>(`${this.api}/parameters/${id}`);
  }

  createParameter(data: TestParameter): Observable<TestParameter> {
    return this.http.post<TestParameter>(`${this.api}/parameters`, data);
  }

  updateParameter(id: number, data: TestParameter): Observable<TestParameter> {
    return this.http.put<TestParameter>(`${this.api}/parameters/${id}`, data);
  }

  deleteParameter(id: number): Observable<any> {
    return this.http.delete(`${this.api}/parameters/${id}`);
  }

  createRange(data: ReferenceRange): Observable<ReferenceRange> {
    return this.http.post<ReferenceRange>(`${this.api}/reference-ranges`, data);
  }

  updateRange(id: number, data: ReferenceRange): Observable<ReferenceRange> {
    return this.http.put<ReferenceRange>(`${this.api}/reference-ranges/${id}`, data);
  }

  deleteRange(id: number): Observable<any> {
    return this.http.delete(`${this.api}/reference-ranges/${id}`);
  }

  createRule(data: InterpretationRule): Observable<InterpretationRule> {
    return this.http.post<InterpretationRule>(`${this.api}/interpretation-rules`, data);
  }

  updateRule(id: number, data: InterpretationRule): Observable<InterpretationRule> {
    return this.http.put<InterpretationRule>(`${this.api}/interpretation-rules/${id}`, data);
  }

  deleteRule(id: number): Observable<any> {
    return this.http.delete(`${this.api}/interpretation-rules/${id}`);
  }

  getLabRules(): Observable<LabRule[]> {
    return this.http.get<LabRule[]>(`${this.api}/rules`);
  }

  createLabRule(data: LabRule): Observable<LabRule> {
    return this.http.post<LabRule>(`${this.api}/rules`, data);
  }

  updateLabRule(id: number, data: LabRule): Observable<LabRule> {
    return this.http.put<LabRule>(`${this.api}/rules/${id}`, data);
  }

  deleteLabRule(id: number): Observable<any> {
    return this.http.delete(`${this.api}/rules/${id}`);
  }
}
