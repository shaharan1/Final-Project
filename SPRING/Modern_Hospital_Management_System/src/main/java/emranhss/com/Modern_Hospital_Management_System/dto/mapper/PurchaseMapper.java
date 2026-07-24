package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

@Component
public class PurchaseMapper {

    public Purchase toEntity(PurchaseRequest request) {
        if (request == null) return null;

        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(request.getPurchaseDate());


        purchase.setInvoiceNo("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        purchase.setTotalAmount(0.0); // প্রাথমিক অবস্থায় টোটাল অ্যামাউন্ট ০ থাকবে, আইটেম এড হলে আপডেট হবে
        purchase.setCreatedAt(new Date());
        return purchase;
    }

    public PurchaseResponse toResponse(Purchase purchase) {
        if (purchase == null) return null;

        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setInvoiceNo(purchase.getInvoiceNo());
        response.setPurchaseDate(purchase.getPurchaseDate());
        response.setTotalAmount(purchase.getTotalAmount());
        response.setCreatedAt(purchase.getCreatedAt());

        if (purchase.getSupplier() != null) {
            response.setSupplierId(purchase.getSupplier().getId());
            response.setSupplierName(purchase.getSupplier().getName());
        }

        return response;
    }
}
