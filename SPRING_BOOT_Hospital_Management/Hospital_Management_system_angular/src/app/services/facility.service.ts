import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FacilityModel } from '../models/bed.model';

@Injectable({ providedIn: 'root' })
export class FacilityService {

  private api = environment.apiUrl + 'infrastructure';

  constructor(private http: HttpClient) {}

  getAll(): Observable<FacilityModel[]> {
    return this.http.get<FacilityModel[]>(`${this.api}/facilities`);
  }

  create(data: { name: string; standardCharge: number }): Observable<FacilityModel> {
    return this.http.post<FacilityModel>(`${this.api}/facilities`, data);
  }

  updateBedFacilities(bedId: number, facilityIds: number[]): Observable<any> {
    return this.http.put(`${this.api}/beds/${bedId}/facilities`, facilityIds);
  }
}
