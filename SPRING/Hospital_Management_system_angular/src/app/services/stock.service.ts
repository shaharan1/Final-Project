import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MedicineStockModel } from '../models/medicine-stock.model';
import { StockAdjustmentModel, StockHistoryModel } from '../models/stock-adjustment.model';

@Injectable({ providedIn: 'root' })
export class StockService {
  private api = environment.apiUrl + 'stock';
  constructor(private http: HttpClient) {}
  addStock(data: MedicineStockModel): Observable<MedicineStockModel> { return this.http.post<MedicineStockModel>(this.api, data); }
  updateStock(id: number, data: MedicineStockModel): Observable<MedicineStockModel> { return this.http.put<MedicineStockModel>(`${this.api}/${id}`, data); }
  getById(id: number): Observable<MedicineStockModel> { return this.http.get<MedicineStockModel>(`${this.api}/${id}`); }
  getAll(): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(this.api); }
  getAvailable(): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/available`); }
  search(keyword: string): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/search?keyword=${encodeURIComponent(keyword)}`); }
  getLowStock(): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/low-stock`); }
  getExpired(): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/expired`); }
  getExpiringSoon(days: number = 30): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/expiring-soon?days=${days}`); }
  getByBatch(batch: string): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/batch/${batch}`); }
  getBySupplier(supplierId: number): Observable<MedicineStockModel[]> { return this.http.get<MedicineStockModel[]>(`${this.api}/supplier/${supplierId}`); }
  getByBarcode(barcode: string): Observable<MedicineStockModel> { return this.http.get<MedicineStockModel>(`${this.api}/barcode/${barcode}`); }
  adjustStock(data: StockAdjustmentModel): Observable<MedicineStockModel> { return this.http.post<MedicineStockModel>(`${this.api}/adjust`, data); }
  getStockHistory(stockId: number): Observable<StockHistoryModel[]> { return this.http.get<StockHistoryModel[]>(`${this.api}/${stockId}/history`); }
  delete(id: number): Observable<any> { return this.http.delete(`${this.api}/${id}`); }
}
