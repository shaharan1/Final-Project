import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { MedicineModel } from '../models/medicineModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MedicineService {


  private apiUrl = environment.apiUrl + "medicines";

  constructor(private http: HttpClient) { }

  save(data: MedicineModel): Observable<MedicineModel> {
    return this.http.post<MedicineModel>(this.apiUrl, data);
  }

  getAll(): Observable<MedicineModel[]> {
    return this.http.get<MedicineModel[]>(this.apiUrl);
  }

  getById(id: number): Observable<MedicineModel> {
    return this.http.get<MedicineModel>(`${this.apiUrl}/${id}`);
  }

  update(id: number, data: MedicineModel): Observable<MedicineModel> {
    return this.http.put<MedicineModel>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // getByPrescriptionId(id:number){
  //   return this.http.get<MedicineModel[]>(`${this.apiUrl}/by-prescription/${id}`);
  // }

  getMedicineByGeneric(id: number) {
    return this.http.get<MedicineModel[]>(
      this.apiUrl + '/generic/' + id
    );
  }
  search(keyword: string) {

    const url = `${this.apiUrl}/search?keyword=${keyword}`;

    console.log("Search URL:", url);

    return this.http.get<MedicineModel[]>(url);

  }

}
