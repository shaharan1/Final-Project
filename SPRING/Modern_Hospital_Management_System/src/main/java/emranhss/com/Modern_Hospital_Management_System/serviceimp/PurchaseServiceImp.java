package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PurchaseMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.PurchaseRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SupplierRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PurchaseService;
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
    private final PurchaseMapper mapper;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        // Validate if the supplier exists in the database
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.getSupplierId()));

        Purchase purchase = mapper.toEntity(request);
        purchase.setSupplier(supplier);

        return mapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase invoice not found with ID: " + id));
        return mapper.toResponse(purchase);
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
    public void updatePurchaseTotalAmount(Long id, double amount) {
        // Fetch purchase order and add the new item subtotal to the main invoice total
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase invoice not found with ID: " + id));

        purchase.setTotalAmount(purchase.getTotalAmount() + amount);
        purchaseRepository.save(purchase);
    }

    @Override
    @Transactional
    public void deletePurchase(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase invoice not found with ID: " + id));
        purchaseRepository.delete(purchase);
    }
}
