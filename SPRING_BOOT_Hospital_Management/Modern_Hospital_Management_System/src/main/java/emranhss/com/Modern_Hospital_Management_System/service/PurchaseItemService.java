package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseItemResponse;
import java.util.List;

public interface PurchaseItemService {
    PurchaseItemResponse addPurchaseItem(PurchaseItemRequest request);
    PurchaseItemResponse getPurchaseItemById(Long id);
    List<PurchaseItemResponse> getItemsByPurchaseId(Long purchaseId);
    void deletePurchaseItem(Long id);
}
