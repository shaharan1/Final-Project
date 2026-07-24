import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { WardModel } from '../models/ward.model';
import { BedModel } from '../models/bed.model';
import { AdmissionRequest } from '../models/admission.model';
import { AdmissionResponse } from '../models/admission-response.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdmissionService {


  // api = environment.apiUrl + "infrastructure";

  // constructor(private http: HttpClient) { }

  // getAllWards() {
  //   return this.http.get<WardModel[]>(this.api + "/wards");
  // }

  // getBedsByWard(id: number) {
  //   return this.http.get<BedModel[]>(`${this.api}/wards/${id}/beds`);
  // }




  private api = environment.apiUrl + "admissions";

  constructor(private http: HttpClient) { }

  admit(data: AdmissionRequest): Observable<AdmissionResponse> {

    return this.http.post<AdmissionResponse>(
      `${this.api}/admit`,
      data
    );

  }

  discharge(id: number): Observable<AdmissionResponse> {

    return this.http.put<AdmissionResponse>(
      `${this.api}/discharge/${id}`,
      {}
    );

  }


  getAll(): Observable<AdmissionResponse[]> {

    return this.http.get<AdmissionResponse[]>(this.api);

  }

  getById(id: number): Observable<AdmissionResponse> {

    return this.http.get<AdmissionResponse>(`${this.api}/${id}`);

  }

}
