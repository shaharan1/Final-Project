package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineStockRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.StockAdjustmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.StockHistoryResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import emranhss.com.Modern_Hospital_Management_System.entity.StockAdjustment;
import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MedicineStockRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.StockAdjustmentRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SupplierRepository;
import emranhss.com.Modern_Hospital_Management_System.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImp implements StockService {

    private final MedicineStockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final StockAdjustmentRepository adjustmentRepository;

    @Override
    public MedicineStockResponse addStock(MedicineStockRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        MedicineStock stock = new MedicineStock();
        BeanUtils.copyProperties(request, stock, "supplierId");
        stock.setSupplier(supplier);
        if (stock.getReservedQuantity() == null) stock.setReservedQuantity(0);
        if (stock.getDamagedQuantity() == null) stock.setDamagedQuantity(0);
        return toResponse(stockRepository.save(stock));
    }

    @Override
    public MedicineStockResponse updateStock(Long id, MedicineStockRequest request) {
        MedicineStock stock = stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
            stock.setSupplier(supplier);
        }
        stock.setMedicineName(request.getMedicineName());
        stock.setGenericName(request.getGenericName());
        stock.setStrength(request.getStrength());
        stock.setDosageForm(request.getDosageForm());
        stock.setBatchNumber(request.getBatchNumber());
        stock.setStockQuantity(request.getStockQuantity());
        stock.setPurchasePrice(request.getPurchasePrice());
        stock.setSalePrice(request.getSalePrice());
        stock.setVat(request.getVat());
        stock.setMinimumStockLevel(request.getMinimumStockLevel());
        stock.setReorderLevel(request.getReorderLevel());
        stock.setManufacturingDate(request.getManufacturingDate());
        stock.setExpiryDate(request.getExpiryDate());
        stock.setBarcode(request.getBarcode());
        return toResponse(stockRepository.save(stock));
    }

    @Override
    public MedicineStockResponse getById(Long id) {
        return toResponse(stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock not found")));
    }

    @Override
    public List<MedicineStockResponse> getAll() {
        return stockRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> getAvailable() {
        return stockRepository.findAvailableStock().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> search(String keyword) {
        return stockRepository.findByMedicineNameContainingIgnoreCase(keyword).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> getLowStock() {
        return stockRepository.findLowStock().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> getExpired() {
        return stockRepository.findExpired(LocalDate.now()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> getExpiringSoon(int days) {
        return stockRepository.findExpiringSoon(LocalDate.now(), LocalDate.now().plusDays(days)).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> getByBatch(String batch) {
        return stockRepository.findByBatchNumber(batch).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> getBySupplier(Long supplierId) {
        return stockRepository.findBySupplierId(supplierId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public MedicineStockResponse getByBarcode(String barcode) {
        MedicineStock stock = stockRepository.findByBarcode(barcode);
        if (stock == null) throw new ResourceNotFoundException("Medicine not found with barcode: " + barcode);
        return toResponse(stock);
    }

    @Override
    @Transactional
    public MedicineStockResponse adjustStock(StockAdjustmentRequest request) {
        MedicineStock stock = stockRepository.findById(request.getMedicineStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        int prevQty = stock.getStockQuantity();
        int newQty = prevQty + request.getQuantityChange();
        if (newQty < 0) throw new IllegalArgumentException("Stock cannot go below zero");
        stock.setStockQuantity(newQty);
        stockRepository.save(stock);

        StockAdjustment adj = new StockAdjustment();
        adj.setMedicineStock(stock);
        adj.setAdjustmentType(request.getAdjustmentType());
        adj.setQuantityChange(request.getQuantityChange());
        adj.setPreviousQuantity(prevQty);
        adj.setNewQuantity(newQty);
        adj.setReason(request.getReason());
        adj.setPerformedBy(request.getPerformedBy());
        adjustmentRepository.save(adj);

        return toResponse(stock);
    }

    @Override
    public List<StockHistoryResponse> getStockHistory(Long stockId) {
        return adjustmentRepository.findByMedicineStockId(stockId).stream()
                .map(a -> {
                    StockHistoryResponse r = new StockHistoryResponse();
                    r.setId(a.getId());
                    r.setMedicineName(a.getMedicineStock().getMedicineName());
                    r.setBatchNumber(a.getMedicineStock().getBatchNumber());
                    r.setAdjustmentType(a.getAdjustmentType());
                    r.setQuantityChange(a.getQuantityChange());
                    r.setPreviousQuantity(a.getPreviousQuantity());
                    r.setNewQuantity(a.getNewQuantity());
                    r.setReason(a.getReason());
                    r.setPerformedBy(a.getPerformedBy());
                    r.setAdjustedAt(a.getAdjustedAt());
                    return r;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void decreaseStock(Long stockId, int quantity) {
        MedicineStock stock = stockRepository.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        int available = stock.getStockQuantity() - (stock.getReservedQuantity() != null ? stock.getReservedQuantity() : 0) - (stock.getDamagedQuantity() != null ? stock.getDamagedQuantity() : 0);
        if (available < quantity) throw new IllegalArgumentException("Insufficient stock for " + stock.getMedicineName());
        stock.setStockQuantity(stock.getStockQuantity() - quantity);
        stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void increaseStock(Long stockId, int quantity, String reason, String performedBy) {
        MedicineStock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        int prevQty = stock.getStockQuantity();
        int newQty = prevQty + quantity;
        stock.setStockQuantity(newQty);
        stockRepository.save(stock);

        StockAdjustment adj = new StockAdjustment();
        adj.setMedicineStock(stock);
        adj.setAdjustmentType("PURCHASE_RECEIVE");
        adj.setQuantityChange(quantity);
        adj.setPreviousQuantity(prevQty);
        adj.setNewQuantity(newQty);
        adj.setReason(reason);
        adj.setPerformedBy(performedBy);
        adjustmentRepository.save(adj);
    }

    @Override
    public void delete(Long id) {
        MedicineStock stock = stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        stock.setActive(false);
        stockRepository.save(stock);
    }

    private MedicineStockResponse toResponse(MedicineStock m) {
        MedicineStockResponse r = new MedicineStockResponse();
        BeanUtils.copyProperties(m, r, "supplier");
        if (m.getSupplier() != null) {
            r.setSupplierId(m.getSupplier().getId());
            r.setSupplierName(m.getSupplier().getName());
        }
        int available = m.getStockQuantity() - (m.getReservedQuantity() != null ? m.getReservedQuantity() : 0) - (m.getDamagedQuantity() != null ? m.getDamagedQuantity() : 0);
        r.setAvailableQuantity(available);
        r.setExpired(m.isExpired());
        r.setExpiringSoon(m.isExpiringSoon(30));
        r.setLowStock(m.isLowStock());
        if (m.isExpired()) r.setInventoryStatus("EXPIRED");
        else if (m.isLowStock()) r.setInventoryStatus("LOW_STOCK");
        else r.setInventoryStatus("AVAILABLE");
        return r;
    }
}
