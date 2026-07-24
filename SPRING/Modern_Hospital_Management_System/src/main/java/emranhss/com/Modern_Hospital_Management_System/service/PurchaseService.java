package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import java.util.List;

public interface PurchaseService {
    PurchaseResponse createPurchase(PurchaseRequest request);
    PurchaseResponse getPurchaseById(Long id);
    List<PurchaseResponse> getAllPurchases();

    // Method to dynamically update invoice total when items are added or removed
    void updatePurchaseTotalAmount(Long id, double amount);
    void deletePurchase(Long id);
}
