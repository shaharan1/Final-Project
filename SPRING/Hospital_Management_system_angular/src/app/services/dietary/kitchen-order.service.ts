import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { KitchenOrder } from '../../models/dietary/kitchen-order.model';

@Injectable({ providedIn: 'root' })
export class KitchenOrderService {
  private api = environment.apiUrl + 'kitchen/orders';

  constructor(private http: HttpClient) {}

  getAll(): Observable<KitchenOrder[]> {
    return this.http.get<KitchenOrder[]>(this.api);
  }
  getById(id: number): Observable<KitchenOrder> {
    return this.http.get<KitchenOrder>(`${this.api}/${id}`);
  }
  getByStatus(status: string): Observable<KitchenOrder[]> {
    return this.http.get<KitchenOrder[]>(`${this.api}/status/${status}`);
  }
  getByMealTime(mealTime: string): Observable<KitchenOrder[]> {
    return this.http.get<KitchenOrder[]>(`${this.api}/meal-time/${mealTime}`);
  }
  getTodayOrders(): Observable<KitchenOrder[]> {
    return this.http.get<KitchenOrder[]>(`${this.api}/today`);
  }
  updateStatus(id: number, status: string, notes?: string): Observable<KitchenOrder> {
    return this.http.put<KitchenOrder>(
      `${this.api}/${id}/status?status=${status}&notes=${notes || ''}`, {}
    );
  }
  update(id: number, data: KitchenOrder): Observable<KitchenOrder> {
    return this.http.put<KitchenOrder>(`${this.api}/${id}`, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
  getCountByStatus(status: string): Observable<number> {
    return this.http.get<number>(`${this.api}/count/${status}`);
  }
  getTodayOrderCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/today-count`);
  }
}
