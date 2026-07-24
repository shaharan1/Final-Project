package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineStockRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.SupplierRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Medicine;
import emranhss.com.Modern_Hospital_Management_System.service.MedicineService;
import emranhss.com.Modern_Hospital_Management_System.service.PharmacyInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PharmacyInventoryController {


    private final PharmacyInventoryService inventoryService;
    private final MedicineService medicineService;

    @PostMapping("/suppliers")
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody SupplierRequest request) {
        return new ResponseEntity<>(inventoryService.createSupplier(request), HttpStatus.CREATED);
    }

    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(inventoryService.getAllSuppliers());
    }

    @PostMapping("/stock")
    public ResponseEntity<MedicineStockResponse> addMedicineToStock(@RequestBody MedicineStockRequest request) {
        return new ResponseEntity<>(inventoryService.addMedicineToStock(request), HttpStatus.CREATED);
    }


    @GetMapping("/stock/{search}")
    public ResponseEntity<List<MedicineStockResponse>> searchMedicines(
            @PathVariable("search") String search) {

        return ResponseEntity.ok(inventoryService.searchMedicines(search));
    }

    @GetMapping("/stock")
    public ResponseEntity<List<MedicineStockResponse>> getAllMedicine(
            ) {

        List<MedicineStockResponse> li= inventoryService.searchAllMedicines();

        return ResponseEntity.ok(li);
    }


}
