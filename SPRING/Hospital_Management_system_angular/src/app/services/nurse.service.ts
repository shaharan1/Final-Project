import { Injectable } from '@angular/core';
import { NurseModel } from '../models/nurseModel';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NurseService {

  private apiUrl = environment.apiUrl + "nurses";
 


  constructor(private http: HttpClient) { }

  // Create Nurse
  createNurse(nurse: NurseModel): Observable<NurseModel> {
    return this.http.post<NurseModel>(
      `${this.apiUrl}/profile/create`,
      nurse
    );
  }

  // Get All
  getAllNurses(): Observable<NurseModel[]> {
    return this.http.get<NurseModel[]>(this.apiUrl);
  }

  // Get By Id
  getNurseById(id: number): Observable<NurseModel> {
    return this.http.get<NurseModel>(
      `${this.apiUrl}/${id}`
    );
  }

  // Get By Email
  getNurseByEmail(email: string): Observable<NurseModel> {
    return this.http.get<NurseModel>(
      `${this.apiUrl}/email/${email}`
    );
  }

  // Ward Wise
  getNurseByWard(ward: string): Observable<NurseModel[]> {
    return this.http.get<NurseModel[]>(
      `${this.apiUrl}/ward/${ward}`
    );
  }

  // On Duty
  getOnDutyNurses(): Observable<NurseModel[]> {
    return this.http.get<NurseModel[]>(
      `${this.apiUrl}/on-duty`
    );
  }

  // Update Duty
  updateDutyStatus(id: number, onDuty: boolean): Observable<NurseModel> {
    return this.http.put<NurseModel>(
      `${this.apiUrl}/${id}/duty-status?onDuty=${onDuty}`,
      {}
    );
  }

  // Update Active
  updateActiveStatus(id: number, active: boolean): Observable<NurseModel> {
    return this.http.put<NurseModel>(
      `${this.apiUrl}/${id}/active-status?active=${active}`,
      {}
    );
  }


}
