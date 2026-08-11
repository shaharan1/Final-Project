package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseItemResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import emranhss.com.Modern_Hospital_Management_System.entity.PurchaseItem;
import org.springframework.stereotype.Component;

@Component
public class PurchaseItemMapper {

    public PurchaseItem toEntity(PurchaseItemRequest request) {
        if (request == null) return null;

        PurchaseItem item = new PurchaseItem();
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        item.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0.0);
        item.setVat(request.getVat() != null ? request.getVat() : 0.0);
        item.setBatchNumber(request.getBatchNumber());
        item.setManufacturingDate(request.getManufacturingDate());
        item.setExpiryDate(request.getExpiryDate());
        item.setSubtotal(calculateSubtotal(request));
        return item;
    }

    public PurchaseItem toEntity(PurchaseItemRequest request, Purchase purchase, MedicineStock stock) {
        PurchaseItem item = toEntity(request);
        item.setPurchase(purchase);
        item.setMedicineStock(stock);
        return item;
    }

    private double calculateSubtotal(PurchaseItemRequest request) {
        int qty = request.getQuantity() != null ? request.getQuantity() : 0;
        double price = request.getUnitPrice() != null ? request.getUnitPrice() : 0.0;
        double discount = request.getDiscount() != null ? request.getDiscount() : 0.0;
        return (qty * price) - discount;
    }

    public PurchaseItemResponse toResponse(PurchaseItem item) {
        if (item == null) return null;

        PurchaseItemResponse response = new PurchaseItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setDiscount(item.getDiscount());
        response.setVat(item.getVat());
        response.setBatchNumber(item.getBatchNumber());
        response.setManufacturingDate(item.getManufacturingDate());
        response.setExpiryDate(item.getExpiryDate());
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
