package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import java.util.List;

public interface PurchaseService {
    PurchaseResponse createPurchase(PurchaseRequest request);
    PurchaseResponse getPurchaseById(Long id);
    List<PurchaseResponse> getAllPurchases();
    PurchaseResponse approvePurchase(Long id);
    PurchaseResponse receivePurchase(Long id, String performedBy);
    PurchaseResponse cancelPurchase(Long id);
    void deletePurchase(Long id);
}
