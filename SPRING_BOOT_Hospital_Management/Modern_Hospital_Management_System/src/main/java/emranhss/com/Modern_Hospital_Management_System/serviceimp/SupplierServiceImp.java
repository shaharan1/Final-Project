package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SupplierRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.SupplierRepository;
import emranhss.com.Modern_Hospital_Management_System.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImp implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public SupplierResponse create(SupplierRequest request) {
        Supplier s = new Supplier();
        BeanUtils.copyProperties(request, s);
        return toResponse(supplierRepository.save(s));
    }

    @Override
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier s = supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        BeanUtils.copyProperties(request, s, "id", "createdDate", "totalDue");
        return toResponse(supplierRepository.save(s));
    }

    @Override
    public SupplierResponse getById(Long id) {
        return toResponse(supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found")));
    }

    @Override
    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getActive() {
        return supplierRepository.findByActiveTrue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> search(String keyword) {
        return supplierRepository.search(keyword).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getWithDue() {
        return supplierRepository.findSuppliersWithDue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Supplier s = supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        s.setActive(false);
        supplierRepository.save(s);
    }

    private SupplierResponse toResponse(Supplier s) {
        SupplierResponse r = new SupplierResponse();
        BeanUtils.copyProperties(s, r);
        return r;
    }
}
