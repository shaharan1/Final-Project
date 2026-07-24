import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DoctorDepartmentModel } from '../models/doctorDepartmentModel';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DoctorsDepartmentService {

  private apiUrl = environment.apiUrl + "doctor-departments";

  constructor(private http: HttpClient) { }

  getAllDepartments(): Observable<DoctorDepartmentModel[]> {
    return this.http.get<DoctorDepartmentModel[]>(this.apiUrl);
  }

  getDepartmentById(id: number): Observable<DoctorDepartmentModel> {
    return this.http.get<DoctorDepartmentModel>(`${this.apiUrl}/${id}`);
  }

  createDepartment(department: DoctorDepartmentModel): Observable<DoctorDepartmentModel> {
    return this.http.post<DoctorDepartmentModel>(this.apiUrl, department);
  }

  updateDepartment(id: number, department: DoctorDepartmentModel): Observable<DoctorDepartmentModel> {
    return this.http.put<DoctorDepartmentModel>(`${this.apiUrl}/${id}`, department);
  }

  deleteDepartment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
