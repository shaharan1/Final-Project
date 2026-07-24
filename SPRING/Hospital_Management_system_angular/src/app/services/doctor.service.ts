import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DoctorModel, DoctorResponseModel } from '../models/doctorModel';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class DoctorModelService {


  private api = environment.apiUrl + "doctors";

  constructor(private http: HttpClient) { }

  getAll(): Observable<DoctorModel[]> {
    return this.http.get<DoctorModel[]>(this.api);
  }

  getById(id: number): Observable<DoctorModel> {
    return this.http.get<DoctorModel>(`${this.api}/${id}`);
  }

  create(data: DoctorModel): Observable<DoctorModel> {
    return this.http.post<DoctorModel>(this.api, data);
  }

  update(id: number, data: DoctorModel): Observable<DoctorModel> {
    return this.http.put<DoctorModel>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }

  getBySpecialization(specialization: string): Observable<DoctorModel[]> {
    return this.http.get<DoctorModel[]>(`${this.api}/specialization/${specialization}`);
  }
  
  getByDoctorDepartmentId(depId: number): Observable<DoctorModel[]> {
    return this.http.get<DoctorModel[]>(`${this.api}/doctordepartment/${depId}`);
  }



  findByUserId(id: number): Observable<DoctorResponseModel> {
    return this.http.get<DoctorResponseModel>(`${this.api}/user/${id}`);
  }



}
