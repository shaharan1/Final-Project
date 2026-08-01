package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseItemResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PurchaseMapper {

    private final PurchaseItemMapper itemMapper;

    public Purchase toEntity(PurchaseRequest request) {
        if (request == null) return null;
        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(java.time.LocalDateTime.now());
        purchase.setInvoiceNo("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        purchase.setTotalAmount(0.0);
        purchase.setVat(request.getVat() != null ? request.getVat() : 0.0);
        purchase.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0.0);
        purchase.setPaidAmount(request.getPaidAmount() != null ? request.getPaidAmount() : 0.0);
        purchase.setPaymentMethod(request.getPaymentMethod());
        purchase.setNotes(request.getNotes());
        purchase.setStatus("PENDING");
        return purchase;
    }

    public PurchaseResponse toResponse(Purchase purchase) {
        if (purchase == null) return null;
        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setInvoiceNo(purchase.getInvoiceNo());
        response.setPurchaseDate(purchase.getPurchaseDate());
        response.setTotalAmount(purchase.getTotalAmount());
        response.setVat(purchase.getVat());
        response.setDiscount(purchase.getDiscount());
        response.setNetAmount(purchase.getNetAmount());
        response.setPaidAmount(purchase.getPaidAmount());
        response.setDueAmount(purchase.getDueAmount());
        response.setStatus(purchase.getStatus());
        response.setPaymentStatus(purchase.getPaymentStatus());
        response.setPaymentMethod(purchase.getPaymentMethod());
        response.setNotes(purchase.getNotes());
        if (purchase.getSupplier() != null) {
            response.setSupplierId(purchase.getSupplier().getId());
            response.setSupplierName(purchase.getSupplier().getName());
        }
        if (purchase.getItems() != null) {
            List<PurchaseItemResponse> itemResponses = purchase.getItems().stream()
                    .map(itemMapper::toResponse)
                    .collect(Collectors.toList());
            response.setItems(itemResponses);
        }
        return response;
    }
}
