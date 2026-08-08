import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SurgeryCategory, SurgeryCategoryRequest } from '../../models/surgery/surgery-category.model';
import { SurgeryMaster, SurgeryMasterRequest } from '../../models/surgery/surgery-master.model';
import { OperationTheatre, OperationTheatreRequest } from '../../models/surgery/operation-theatre.model';
import { DoctorDiscount, DoctorDiscountRequest } from '../../models/surgery/doctor-discount.model';

@Injectable({ providedIn: 'root' })
export class SurgeryReferenceService {
  private categoriesApi = environment.apiUrl + 'surgery-categories';
  private mastersApi = environment.apiUrl + 'surgery-masters';
  private theatresApi = environment.apiUrl + 'operation-theatres';
  private discountsApi = environment.apiUrl + 'doctor-discounts';

  constructor(private http: HttpClient) {}

  // ===== Categories =====
  getCategories(): Observable<SurgeryCategory[]> {
    return this.http.get<SurgeryCategory[]>(this.categoriesApi);
  }

  getActiveCategories(): Observable<SurgeryCategory[]> {
    return this.http.get<SurgeryCategory[]>(`${this.categoriesApi}/active`);
  }

  createCategory(data: SurgeryCategoryRequest): Observable<SurgeryCategory> {
    return this.http.post<SurgeryCategory>(this.categoriesApi, data);
  }

  updateCategory(id: number, data: SurgeryCategoryRequest): Observable<SurgeryCategory> {
    return this.http.put<SurgeryCategory>(`${this.categoriesApi}/${id}`, data);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.categoriesApi}/${id}`);
  }

  // ===== Surgery Masters (Rate Card) =====
  getMasterById(id: number): Observable<SurgeryMaster> {
    return this.http.get<SurgeryMaster>(`${this.mastersApi}/${id}`);
  }

  getMasters(): Observable<SurgeryMaster[]> {
    return this.http.get<SurgeryMaster[]>(this.mastersApi);
  }

  getMastersByCategory(categoryId: number): Observable<SurgeryMaster[]> {
    return this.http.get<SurgeryMaster[]>(`${this.mastersApi}/category/${categoryId}`);
  }

  getActiveMasters(): Observable<SurgeryMaster[]> {
    return this.http.get<SurgeryMaster[]>(`${this.mastersApi}/active`);
  }

  searchMasters(q: string): Observable<SurgeryMaster[]> {
    return this.http.get<SurgeryMaster[]>(`${this.mastersApi}/search`, { params: { q } });
  }

  createMaster(data: SurgeryMasterRequest): Observable<SurgeryMaster> {
    return this.http.post<SurgeryMaster>(this.mastersApi, data);
  }

  updateMaster(id: number, data: SurgeryMasterRequest): Observable<SurgeryMaster> {
    return this.http.put<SurgeryMaster>(`${this.mastersApi}/${id}`, data);
  }

  deleteMaster(id: number): Observable<void> {
    return this.http.delete<void>(`${this.mastersApi}/${id}`);
  }

  // ===== Operation Theatres =====
  getTheatres(): Observable<OperationTheatre[]> {
    return this.http.get<OperationTheatre[]>(this.theatresApi);
  }

  getActiveTheatres(): Observable<OperationTheatre[]> {
    return this.http.get<OperationTheatre[]>(`${this.theatresApi}/active`);
  }

  createTheatre(data: OperationTheatreRequest): Observable<OperationTheatre> {
    return this.http.post<OperationTheatre>(this.theatresApi, data);
  }

  updateTheatre(id: number, data: OperationTheatreRequest): Observable<OperationTheatre> {
    return this.http.put<OperationTheatre>(`${this.theatresApi}/${id}`, data);
  }

  deleteTheatre(id: number): Observable<void> {
    return this.http.delete<void>(`${this.theatresApi}/${id}`);
  }

  // ===== Doctor Discounts =====
  getDiscounts(): Observable<DoctorDiscount[]> {
    return this.http.get<DoctorDiscount[]>(this.discountsApi);
  }

  getActiveDiscounts(): Observable<DoctorDiscount[]> {
    return this.http.get<DoctorDiscount[]>(`${this.discountsApi}/active`);
  }

  getDiscountByDoctorId(doctorId: number): Observable<DoctorDiscount> {
    return this.http.get<DoctorDiscount>(`${this.discountsApi}/doctor/${doctorId}`);
  }

  createDiscount(data: DoctorDiscountRequest): Observable<DoctorDiscount> {
    return this.http.post<DoctorDiscount>(this.discountsApi, data);
  }

  updateDiscount(id: number, data: DoctorDiscountRequest): Observable<DoctorDiscount> {
    return this.http.put<DoctorDiscount>(`${this.discountsApi}/${id}`, data);
  }

  deleteDiscount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.discountsApi}/${id}`);
  }
}
