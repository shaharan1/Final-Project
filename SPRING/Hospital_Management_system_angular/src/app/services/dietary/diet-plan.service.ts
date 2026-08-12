import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DietPlan } from '../../models/dietary/diet-plan.model';

@Injectable({ providedIn: 'root' })
export class DietPlanService {
  private api = environment.apiUrl + 'diet-plans';

  constructor(private http: HttpClient) {}

  getAll(): Observable<DietPlan[]> {
    return this.http.get<DietPlan[]>(this.api);
  }
  getById(id: number): Observable<DietPlan> {
    return this.http.get<DietPlan>(`${this.api}/${id}`);
  }
  getActive(): Observable<DietPlan[]> {
    return this.http.get<DietPlan[]>(`${this.api}/active`);
  }
  getByDietType(dietType: string): Observable<DietPlan[]> {
    return this.http.get<DietPlan[]>(`${this.api}/type/${dietType}`);
  }
  search(keyword: string): Observable<DietPlan[]> {
    return this.http.get<DietPlan[]>(`${this.api}/search`, { params: { keyword } });
  }
  getActiveCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/count/active`);
  }
  create(data: DietPlan): Observable<DietPlan> {
    return this.http.post<DietPlan>(this.api, data);
  }
  update(id: number, data: DietPlan): Observable<DietPlan> {
    return this.http.put<DietPlan>(`${this.api}/${id}`, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
