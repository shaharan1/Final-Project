import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MealSchedule } from '../../models/dietary/meal-schedule.model';

@Injectable({ providedIn: 'root' })
export class MealScheduleService {
  private api = environment.apiUrl + 'meal-schedules';

  constructor(private http: HttpClient) {}

  getAll(): Observable<MealSchedule[]> {
    return this.http.get<MealSchedule[]>(this.api);
  }
  getById(id: number): Observable<MealSchedule> {
    return this.http.get<MealSchedule>(`${this.api}/${id}`);
  }
  getActive(): Observable<MealSchedule[]> {
    return this.http.get<MealSchedule[]>(`${this.api}/active`);
  }
  getCurrentMeal(): Observable<MealSchedule> {
    return this.http.get<MealSchedule>(`${this.api}/current`);
  }
  create(data: MealSchedule): Observable<MealSchedule> {
    return this.http.post<MealSchedule>(this.api, data);
  }
  update(id: number, data: MealSchedule): Observable<MealSchedule> {
    return this.http.put<MealSchedule>(`${this.api}/${id}`, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
