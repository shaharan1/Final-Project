package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineStockRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.StockAdjustmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.StockHistoryResponse;

import java.util.List;

public interface StockService {
    MedicineStockResponse addStock(MedicineStockRequest request);
    MedicineStockResponse updateStock(Long id, MedicineStockRequest request);
    MedicineStockResponse getById(Long id);
    List<MedicineStockResponse> getAll();
    List<MedicineStockResponse> getAvailable();
    List<MedicineStockResponse> search(String keyword);
    List<MedicineStockResponse> getLowStock();
    List<MedicineStockResponse> getExpired();
    List<MedicineStockResponse> getExpiringSoon(int days);
    List<MedicineStockResponse> getByBatch(String batch);
    List<MedicineStockResponse> getBySupplier(Long supplierId);
    MedicineStockResponse getByBarcode(String barcode);
    MedicineStockResponse adjustStock(StockAdjustmentRequest request);
    List<StockHistoryResponse> getStockHistory(Long stockId);
    void decreaseStock(Long stockId, int quantity);
    void increaseStock(Long stockId, int quantity, String reason, String performedBy);
    void delete(Long id);
}
