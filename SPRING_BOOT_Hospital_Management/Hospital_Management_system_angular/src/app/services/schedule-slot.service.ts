import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ScheduleSlotModel } from '../models/ScheduleSlotModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ScheduleSlotService {


  private api = environment.apiUrl + "schedule-slots";

  constructor(private http: HttpClient) { }

  create(data: ScheduleSlotModel): Observable<ScheduleSlotModel> {
    return this.http.post<ScheduleSlotModel>(this.api, data);
  }

  getAll(): Observable<ScheduleSlotModel[]> {
    return this.http.get<ScheduleSlotModel[]>(this.api);
  }

  getAvailableSlots(doctorId: number, date: string): Observable<ScheduleSlotModel[]> {

    return this.http.get<ScheduleSlotModel[]>(
      `${this.api}/doctor/${doctorId}/available?date=${date}`
    );

  }

  changeStatus(id: number, isBooked: boolean): Observable<ScheduleSlotModel> {

    return this.http.put<ScheduleSlotModel>(
      `${this.api}/${id}/status?isBooked=${isBooked}`,
      {}
    );

  }


  // Get available (free) slots
  findAvailableSlots(
    doctorId: number,
    date: string
  ): Observable<ScheduleSlotModel[]> {

    return this.http.get<ScheduleSlotModel[]>(
      `${this.api}/doctor/${doctorId}/free?date=${date}`
    );
  }



}
