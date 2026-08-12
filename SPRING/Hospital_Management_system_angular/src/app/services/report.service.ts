import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReportModel } from '../models/report.model';

@Injectable({ providedIn: 'root' })
export class ReportService {

  private api = environment.apiUrl + 'v1/reports';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ReportModel[]> {
    return this.http.get<ReportModel[]>(this.api);
  }

  getById(id: number): Observable<ReportModel> {
    return this.http.get<ReportModel>(`${this.api}/${id}`);
  }

  create(data: ReportModel): Observable<ReportModel> {
    return this.http.post<ReportModel>(this.api, data);
  }

  update(id: number, data: ReportModel): Observable<ReportModel> {
    return this.http.put<ReportModel>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
