import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AmbulanceTrip } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class AmbulanceTripService {
  private api = environment.apiUrl + 'emergency/ambulance-trips';

  constructor(private http: HttpClient) {}

  create(trip: AmbulanceTrip): Observable<AmbulanceTrip> {
    return this.http.post<AmbulanceTrip>(this.api, trip);
  }

  getById(id: number): Observable<AmbulanceTrip> {
    return this.http.get<AmbulanceTrip>(`${this.api}/${id}`);
  }

  getAll(): Observable<AmbulanceTrip[]> {
    return this.http.get<AmbulanceTrip[]>(this.api);
  }

  dispatchTrip(id: number): Observable<AmbulanceTrip> {
    return this.http.put<AmbulanceTrip>(`${this.api}/${id}/dispatch`, {});
  }

  completeTrip(id: number): Observable<AmbulanceTrip> {
    return this.http.put<AmbulanceTrip>(`${this.api}/${id}/complete`, {});
  }

  cancelTrip(id: number): Observable<AmbulanceTrip> {
    return this.http.put<AmbulanceTrip>(`${this.api}/${id}/cancel`, {});
  }

  getActiveTrips(): Observable<AmbulanceTrip[]> {
    return this.http.get<AmbulanceTrip[]>(`${this.api}/active`);
  }
}
