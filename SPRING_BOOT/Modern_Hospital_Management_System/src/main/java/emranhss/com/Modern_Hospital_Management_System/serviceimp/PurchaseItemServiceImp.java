package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PurchaseItemMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseItemResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import emranhss.com.Modern_Hospital_Management_System.entity.PurchaseItem;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MedicineStockRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.PurchaseItemRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.PurchaseRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PurchaseItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseItemServiceImp implements PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseRepository purchaseRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final PurchaseItemMapper mapper;

    @Override
    @Transactional
    public PurchaseItemResponse addPurchaseItem(PurchaseItemRequest request) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase profile not found with ID: " + request.getPurchaseId()));

        MedicineStock stock = medicineStockRepository.findById(request.getStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine stock record not found with ID: " + request.getStockId()));

        PurchaseItem item = mapper.toEntity(request);
        item.setPurchase(purchase);
        item.setMedicineStock(stock);



        return mapper.toResponse(purchaseItemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseItemResponse getPurchaseItemById(Long id) {
        PurchaseItem item = purchaseItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase item record missing with ID: " + id));
        return mapper.toResponse(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseItemResponse> getItemsByPurchaseId(Long purchaseId) {
        return purchaseItemRepository.findByPurchaseId(purchaseId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePurchaseItem(Long id) {
        PurchaseItem item = purchaseItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase item record missing with ID: " + id));
        purchaseItemRepository.delete(item);
    }
}
