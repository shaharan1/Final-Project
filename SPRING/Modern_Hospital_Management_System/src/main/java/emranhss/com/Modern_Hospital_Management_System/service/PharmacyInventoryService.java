package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineStockRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.SupplierRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;

import java.util.List;

public interface PharmacyInventoryService {


    SupplierResponse createSupplier(SupplierRequest request);
    List<SupplierResponse> getAllSuppliers();
    MedicineStockResponse addMedicineToStock(MedicineStockRequest request);
    List<MedicineStockResponse> searchMedicines(String query);
    List<MedicineStockResponse> searchAllMedicines();
}
