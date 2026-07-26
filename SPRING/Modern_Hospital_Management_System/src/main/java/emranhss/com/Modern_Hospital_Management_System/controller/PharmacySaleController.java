package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PharmacySaleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.PharmacySale;
import emranhss.com.Modern_Hospital_Management_System.repository.PharmacySaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy/sales")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PharmacySaleController {

    private final emranhss.com.Modern_Hospital_Management_System.service.PharmacySaleService pharmacySaleService;
    private final PharmacySaleRepository pharmacySaleRepository;

    @PostMapping
    public ResponseEntity<PharmacySaleResponse> processSale(@RequestBody PharmacySaleRequest request) {
        return new ResponseEntity<>(pharmacySaleService.processMedicineSale(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PharmacySaleResponse>> getAll() {
        List<PharmacySaleResponse> sales = pharmacySaleRepository.findAllByOrderBySaleDateDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacySaleResponse> getById(@PathVariable Long id) {
        PharmacySale sale = pharmacySaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        return ResponseEntity.ok(toResponse(sale));
    }

    private PharmacySaleResponse toResponse(PharmacySale p) {
        PharmacySaleResponse r = new PharmacySaleResponse();
        r.setId(p.getId());
        r.setSaleInvoiceNo(p.getSaleInvoiceNo());
        r.setPatientType(p.getPatientType());
        r.setPatientName(p.getPatientName());
        r.setPatientPhone(p.getPatientPhone());
        r.setPatientId(p.getPatientId());
        r.setDoctorId(p.getDoctorId());
        r.setDoctorName(p.getDoctorName());
        r.setPrescriptionId(p.getPrescriptionId());
        r.setTotalAmount(p.getTotalAmount());
        r.setDiscount(p.getDiscount());
        r.setVat(p.getVat());
        r.setNetPayable(p.getNetPayable());
        r.setPaidAmount(p.getPaidAmount());
        r.setChangeAmount(p.getChangeAmount());
        r.setPaymentMethod(p.getPaymentMethod());
        r.setPaymentStatus(p.getPaymentStatus());
        r.setSaleType(p.getSaleType());
        r.setSaleDate(p.getSaleDate());
        if (p.getBilling() != null) r.setBillingId(p.getBilling().getId());
        if (p.getItems() != null) {
            r.setItems(p.getItems().stream().map(i -> {
                emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleItemResponse ir = new emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleItemResponse();
                ir.setId(i.getId());
                if (i.getMedicineStock() != null) {
                    ir.setMedicineStockId(i.getMedicineStock().getId());
                    ir.setMedicineName(i.getMedicineStock().getMedicineName());
                    ir.setBatchNumber(i.getMedicineStock().getBatchNumber());
                }
                ir.setQuantity(i.getQuantity());
                ir.setUnitPrice(i.getUnitPrice());
                ir.setDiscount(i.getDiscount());
                ir.setSubtotal(i.getSubtotal());
                return ir;
            }).collect(Collectors.toList()));
        }
        return r;
    }
}
