package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PharmacyInventoryMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineStockRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.SupplierRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MedicineStockRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SupplierRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PharmacyInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyInventoryServiceImp implements PharmacyInventoryService {

    private final SupplierRepository supplierRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final PharmacyInventoryMapper mapper;




    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(request, supplier);
        return mapper.toResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicineStockResponse addMedicineToStock(MedicineStockRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier profile missing with ID: " + request.getSupplierId()));

        MedicineStock stock = new MedicineStock();
        BeanUtils.copyProperties(request, stock);
        stock.setSupplier(supplier);

        return mapper.toResponse(medicineStockRepository.save(stock));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineStockResponse> searchMedicines(String query) {
        return medicineStockRepository.findByMedicineNameContainingIgnoreCase(query).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineStockResponse> searchAllMedicines() {

        return medicineStockRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
