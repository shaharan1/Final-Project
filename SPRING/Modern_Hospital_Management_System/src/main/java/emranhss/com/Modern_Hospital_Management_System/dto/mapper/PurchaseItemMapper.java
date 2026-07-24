package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseItemResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.PurchaseItem;
import org.springframework.stereotype.Component;

@Component
public class PurchaseItemMapper {

    public PurchaseItem toEntity(PurchaseItemRequest request) {
        if (request == null) return null;

        PurchaseItem item = new PurchaseItem();
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        // Subtotal স্বয়ংক্রিয়ভাবে হিসাব করা হচ্ছে: quantity * unitPrice
        item.setSubtotal(request.getQuantity() * request.getUnitPrice());
        return item;
    }

    public PurchaseItemResponse toResponse(PurchaseItem item) {
        if (item == null) return null;

        PurchaseItemResponse response = new PurchaseItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());

        if (item.getPurchase() != null) {
            response.setPurchaseId(item.getPurchase().getId());
        }

        if (item.getMedicineStock() != null) {
            response.setStockId(item.getMedicineStock().getId());
            response.setMedicineName(item.getMedicineStock().getMedicineName()); // MedicineStock-এ এই ফিল্ডটি আছে ধরে নেওয়া হয়েছে
        }

        return response;
    }
}
