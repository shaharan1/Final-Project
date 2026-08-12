import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Dietician } from '../../models/dietary/dietician.model';

@Injectable({ providedIn: 'root' })
export class DieticianService {
  private api = environment.apiUrl + 'dieticians';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Dietician[]> {
    return this.http.get<Dietician[]>(this.api);
  }
  getById(id: number): Observable<Dietician> {
    return this.http.get<Dietician>(`${this.api}/${id}`);
  }
  getActive(): Observable<Dietician[]> {
    return this.http.get<Dietician[]>(`${this.api}/active`);
  }
  search(keyword: string): Observable<Dietician[]> {
    return this.http.get<Dietician[]>(`${this.api}/search`, { params: { keyword } });
  }
  getActiveCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/count/active`);
  }
  create(data: Dietician): Observable<Dietician> {
    return this.http.post<Dietician>(this.api, data);
  }
  update(id: number, data: Dietician): Observable<Dietician> {
    return this.http.put<Dietician>(`${this.api}/${id}`, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
