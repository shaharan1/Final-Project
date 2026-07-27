import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Ambulance } from '../../models/emergency';

@Injectable({ providedIn: 'root' })
export class AmbulanceService {
  private api = environment.apiUrl + 'emergency/ambulances';

  constructor(private http: HttpClient) {}

  create(ambulance: Ambulance): Observable<Ambulance> {
    return this.http.post<Ambulance>(this.api, ambulance);
  }

  getById(id: number): Observable<Ambulance> {
    return this.http.get<Ambulance>(`${this.api}/${id}`);
  }

  getAll(): Observable<Ambulance[]> {
    return this.http.get<Ambulance[]>(this.api);
  }

  update(id: number, ambulance: Ambulance): Observable<Ambulance> {
    return this.http.put<Ambulance>(`${this.api}/${id}`, ambulance);
  }

  updateStatus(id: number, status: string): Observable<Ambulance> {
    return this.http.put<Ambulance>(`${this.api}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  getByStatus(status: string): Observable<Ambulance[]> {
    return this.http.get<Ambulance[]>(`${this.api}/status/${status}`);
  }

  getAvailableCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/available-count`);
  }
}
