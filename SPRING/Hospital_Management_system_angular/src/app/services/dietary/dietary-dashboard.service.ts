import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DietaryDashboardStats } from '../../models/dietary/dietary-dashboard.model';
import { KitchenOrder } from '../../models/dietary/kitchen-order.model';
import { PatientDietAlert } from '../../models/dietary/patient-diet-alert.model';
import { MealSchedule } from '../../models/dietary/meal-schedule.model';

@Injectable({ providedIn: 'root' })
export class DietaryDashboardService {
  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<any> {
    return forkJoin({
      todayMealsServed: this.http.get<number>(`${this.api}kitchen/orders/today-count`),
      activePatients: this.http.get<any[]>(`${this.api}diet-assignments/active`),
      dietPlansAssigned: this.http.get<number>(`${this.api}diet-assignments/count/active`),
      kitchenOrders: this.http.get<number>(`${this.api}kitchen/orders/count/PENDING`),
      pendingMealDelivery: this.http.get<number>(`${this.api}kitchen/orders/count/READY`),
      dieticiansOnDuty: this.http.get<number>(`${this.api}dieticians/count/active`),
      orders: this.http.get<KitchenOrder[]>(`${this.api}kitchen/orders/today`),
      alerts: this.http.get<PatientDietAlert[]>(`${this.api}diet-alerts/active`),
      schedules: this.http.get<MealSchedule[]>(`${this.api}meal-schedules/active`),
    });
  }

  getTodayOrders(): Observable<KitchenOrder[]> {
    return this.http.get<KitchenOrder[]>(`${this.api}kitchen/orders/today`);
  }

  getActiveAlerts(): Observable<PatientDietAlert[]> {
    return this.http.get<PatientDietAlert[]>(`${this.api}diet-alerts/active`);
  }

  getMealSchedule(): Observable<MealSchedule[]> {
    return this.http.get<MealSchedule[]>(`${this.api}meal-schedules/active`);
  }

  getRecentActivity(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}diet-history`);
  }
}
