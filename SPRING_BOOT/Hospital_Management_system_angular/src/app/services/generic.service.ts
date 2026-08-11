import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GenericModel } from '../models/genericModel';

@Injectable({
  providedIn: 'root',
})
export class GenericService {


private apiUrl = environment.apiUrl + "generics";

  constructor(private http: HttpClient) {}

  save(data: GenericModel): Observable<GenericModel> {
    return this.http.post<GenericModel>(this.apiUrl, data);
  }

  getAll(): Observable<GenericModel[]> {
    return this.http.get<GenericModel[]>(this.apiUrl);
  }

  getById(id: number): Observable<GenericModel> {
    return this.http.get<GenericModel>(`${this.apiUrl}/${id}`);
  }

  update(id: number, data: GenericModel): Observable<GenericModel> {
    return this.http.put<GenericModel>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

}
