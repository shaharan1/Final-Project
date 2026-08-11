package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineResponse> create(@RequestBody MedicineRequest request) {
        MedicineResponse response = medicineService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAll() {
        return ResponseEntity.ok(medicineService.getAll());
    }

//    @GetMapping("/by-prescription/{prescriptionId}")
//    public ResponseEntity<List<MedicineResponse>> getByPrescriptionId(@PathVariable Long prescriptionId) {
//        return ResponseEntity.ok(medicineService.getByPrescriptionId(prescriptionId));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> update(@PathVariable Long id,
                                                   @RequestBody MedicineRequest request) {
        return ResponseEntity.ok(medicineService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generic/{id}")
    public List<MedicineResponse> getMedicineByGeneric(@PathVariable Long id){

        return medicineService.getMedicineByGeneric(id);

    }

    @GetMapping("/search")
    public List<MedicineResponse> search(@RequestParam String keyword) {
        return medicineService
                .search(keyword);
    }
}