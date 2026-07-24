import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { PrescriptionModel } from '../models/prescriptionModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PrescriptionService {


  private apiUrl = environment.apiUrl + 'prescriptions';

  constructor(private http: HttpClient) { }

  save(data: PrescriptionModel): Observable<PrescriptionModel> {
    return this.http.post<PrescriptionModel>(this.apiUrl, data);
  }

  getAll(): Observable<PrescriptionModel[]> {
    return this.http.get<PrescriptionModel[]>(this.apiUrl);
  }

  getById(id: number): Observable<PrescriptionModel> {
    return this.http.get<PrescriptionModel>(`${this.apiUrl}/${id}`);
  }

  update(id: number, data: PrescriptionModel) {
    return this.http.put(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  downloadPdf(id: number): Observable<Blob> {

    return this.http.get(

      `${environment.apiUrl}prescriptions/${id}/pdf`,

      {

        responseType: 'blob'

      }

    );

  }

}
