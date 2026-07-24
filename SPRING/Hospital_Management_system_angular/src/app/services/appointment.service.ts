import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AppointmentModel } from '../models/appointmentModel';
import { Observable } from 'rxjs';
import { AppointmentResponseModel } from '../models/AppointmentResponseModel';

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {


  private apiUrl = environment.apiUrl + "appointments";

  constructor(private http: HttpClient) { }

  bookAppointment(data: AppointmentModel): Observable<AppointmentModel> {
    return this.http.post<AppointmentModel>(this.apiUrl, data);
  }


  getAllAppointments(): Observable<AppointmentModel[]> {
    return this.http.get<AppointmentModel[]>(this.apiUrl);
  }

  getDoctorAppointments(doctorId: number) {
  return this.http.get<AppointmentResponseModel[]>(
    `${this.apiUrl}/doctor/${doctorId}`
  );
}

  getSchedule(date: string): Observable<AppointmentModel[]> {
    return this.http.get<AppointmentModel[]>(
      `${this.apiUrl}/schedule?date=${date}`
    );
  }

  cancelAppointment(id: number): Observable<AppointmentModel> {
    return this.http.put<AppointmentModel>(
      `${this.apiUrl}/${id}/cancel`,
      {}
    );
  }

  checkReturning(phone: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.apiUrl}/check-returning?phone=${phone}`
    );
  }

  calculateFee(phone: string, doctorId: number): Observable<number> {
    return this.http.get<number>(
      `${this.apiUrl}/calculate-fee?phone=${phone}&doctorId=${doctorId}`
    );
  }


  //  ---------Filter Appointments-----------
  filterAppointments(
    doctorId?: number,
    date?: string
  ): Observable<AppointmentModel[]> {

    let params: any = {};

    if (doctorId) {
      params.doctorId = doctorId;
    }

    if (date) {
      params.date = date;
    }

    return this.http.get<AppointmentModel[]>(
      `${this.apiUrl}/filter`,
      { params }
    );

  }

  getByAppointmentNumber(appointmentNumber: string) {
    return this.http.get<AppointmentModel>(
      `${this.apiUrl}/number/${appointmentNumber}`
    );
  }


   getById(id: number) {

    return this.http.get<AppointmentModel>(
      `${this.apiUrl}/${id}`
    );

  }

  //   getDoctorAppointments(doctorId: number): Observable<AppointmentModel[]> {
  //   return this.http.get<AppointmentModel[]>(
  //     `${this.apiUrl}/doctor/${doctorId}`
  //   );
  // }
  


}
