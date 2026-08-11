package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PurchaseItemMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PurchaseMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import emranhss.com.Modern_Hospital_Management_System.entity.PurchaseItem;
import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MedicineStockRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.PurchaseRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SupplierRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PurchaseService;
import emranhss.com.Modern_Hospital_Management_System.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImp implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final PurchaseMapper mapper;
    private final PurchaseItemMapper itemMapper;
    private final StockService stockService;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.getSupplierId()));

        Purchase purchase = mapper.toEntity(request);
        purchase.setSupplier(supplier);

        double total = 0.0;
        if (request.getItems() != null) {
            for (PurchaseItemRequest itemRequest : request.getItems()) {
                MedicineStock stock = medicineStockRepository.findById(itemRequest.getStockId())
                        .orElseThrow(() -> new ResourceNotFoundException("Medicine stock record not found with ID: " + itemRequest.getStockId()));
                PurchaseItem item = itemMapper.toEntity(itemRequest, purchase, stock);
                total += item.getSubtotal();
                purchase.getItems().add(item);
            }
        }

        double vat = request.getVat() != null ? request.getVat() : 0.0;
        double discount = request.getDiscount() != null ? request.getDiscount() : 0.0;
        double paid = request.getPaidAmount() != null ? request.getPaidAmount() : 0.0;

        purchase.setTotalAmount(total);
        purchase.setNetAmount(total + vat - discount);
        purchase.setPaidAmount(paid);
        purchase.setDueAmount(Math.max(0, purchase.getNetAmount() - paid));
        purchase.setPaymentStatus(derivePaymentStatus(paid, purchase.getNetAmount()));

        return mapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseResponse approvePurchase(Long id) {
        Purchase purchase = getEntity(id);
        if ("CANCELLED".equals(purchase.getStatus())) {
            throw new IllegalStateException("A cancelled purchase cannot be approved");
        }
        purchase.setStatus("APPROVED");
        return mapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public PurchaseResponse receivePurchase(Long id, String performedBy) {
        Purchase purchase = getEntity(id);
        if ("CANCELLED".equals(purchase.getStatus())) {
            throw new IllegalStateException("A cancelled purchase cannot be received");
        }
        if ("RECEIVED".equals(purchase.getStatus())) {
            return mapper.toResponse(purchase);
        }
        for (PurchaseItem item : purchase.getItems()) {
            stockService.increaseStock(
                    item.getMedicineStock().getId(),
                    item.getQuantity(),
                    "GRN receive - " + purchase.getInvoiceNo(),
                    performedBy != null ? performedBy : "SYSTEM");
        }
        purchase.setStatus("RECEIVED");
        return mapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public PurchaseResponse cancelPurchase(Long id) {
        Purchase purchase = getEntity(id);
        if ("RECEIVED".equals(purchase.getStatus())) {
            throw new IllegalStateException("A received purchase cannot be cancelled");
        }
        purchase.setStatus("CANCELLED");
        return mapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public void deletePurchase(Long id) {
        purchaseRepository.delete(getEntity(id));
    }

    private Purchase getEntity(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase invoice not found with ID: " + id));
    }

    private String derivePaymentStatus(double paid, double netAmount) {
        if (netAmount > 0 && paid >= netAmount) return "PAID";
        if (paid > 0) return "PARTIAL";
        return "PENDING";
    }
}
