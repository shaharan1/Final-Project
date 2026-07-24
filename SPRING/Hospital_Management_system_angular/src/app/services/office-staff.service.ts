import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OfficeStaffModel } from '../models/office-staff.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class OfficeStaffService {


private apiUrl = environment.apiUrl + "office-staff";

  constructor(
    private http: HttpClient,
    

  ) { }

  create(data: OfficeStaffModel): Observable<OfficeStaffModel> {
    return this.http.post<OfficeStaffModel>(
      `${this.apiUrl}/create`,
      data
    );
  }

  getAll(): Observable<OfficeStaffModel[]> {
    return this.http.get<OfficeStaffModel[]>(this.apiUrl);
  }

  getById(id: number): Observable<OfficeStaffModel> {
    return this.http.get<OfficeStaffModel>(
      `${this.apiUrl}/${id}`
    );
  }

  getByDepartment(department: string): Observable<OfficeStaffModel[]> {
    return this.http.get<OfficeStaffModel[]>(
      `${this.apiUrl}/department/${department}`
    );
  }

  getByPosition(position: string): Observable<OfficeStaffModel[]> {
    return this.http.get<OfficeStaffModel[]>(
      `${this.apiUrl}/position/${position}`
    );
  }

  update(id: number, data: OfficeStaffModel): Observable<OfficeStaffModel> {
    return this.http.put<OfficeStaffModel>(
      `${this.apiUrl}/${id}`,
      data
    );
  }

  delete(id: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );
  }


}
