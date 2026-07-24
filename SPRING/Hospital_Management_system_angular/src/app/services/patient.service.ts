import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { PatientModel } from '../models/patientModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PatientService {

  private apiUrl = environment.apiUrl + 'patients';

  constructor(private http: HttpClient) { }



  save(patient: PatientModel): Observable<PatientModel> {
    return this.http.post<PatientModel>(this.apiUrl, patient);
  }

  getAll(): Observable<PatientModel[]> {
    return this.http.get<PatientModel[]>(this.apiUrl);
  }

  getById(id: number): Observable<PatientModel> {
    return this.http.get<PatientModel>(`${this.apiUrl}/${id}`);
  }

  getByCode(code: string): Observable<PatientModel> {
    return this.http.get<PatientModel>(`${this.apiUrl}/code/${code}`);
  }

  update(id: number, patient: PatientModel): Observable<PatientModel> {
    return this.http.put<PatientModel>(`${this.apiUrl}/${id}`, patient);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }


}
