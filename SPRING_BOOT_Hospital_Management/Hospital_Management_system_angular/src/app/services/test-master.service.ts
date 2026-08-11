import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { TestMasterModel } from '../models/testMasterModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TestMasterService {


  private api = environment.apiUrl + "tests";

  constructor(private http: HttpClient) { }

  save(data: TestMasterModel): Observable<TestMasterModel> {
    return this.http.post<TestMasterModel>(this.api, data);
  }

  update(id:number,data:TestMasterModel):Observable<TestMasterModel>{
    return this.http.put<TestMasterModel>(`${this.api}/${id}`,data);
  }

  getAll():Observable<TestMasterModel[]>{
    return this.http.get<TestMasterModel[]>(this.api);
  }

  getById(id:number):Observable<TestMasterModel>{
    return this.http.get<TestMasterModel>(`${this.api}/${id}`);
  }

  delete(id:number){
    return this.http.delete(`${this.api}/${id}`);
  }

  search(keyword:string){
    return this.http.get<TestMasterModel[]>(
      `${this.api}/search?keyword=${keyword}`
    );
  }


}
