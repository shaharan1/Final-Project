package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SupplierRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse create(SupplierRequest request);
    SupplierResponse update(Long id, SupplierRequest request);
    SupplierResponse getById(Long id);
    List<SupplierResponse> getAll();
    List<SupplierResponse> getActive();
    List<SupplierResponse> search(String keyword);
    List<SupplierResponse> getWithDue();
    void delete(Long id);
}
