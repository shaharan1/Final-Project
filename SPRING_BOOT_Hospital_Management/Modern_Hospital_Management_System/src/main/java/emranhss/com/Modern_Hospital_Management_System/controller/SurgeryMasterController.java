package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surgery-masters")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SurgeryMasterController {

    private final SurgeryMasterService surgeryMasterService;

    @PostMapping
    public ResponseEntity<SurgeryMasterResponse> create(@RequestBody SurgeryMasterRequest request) {
        return new ResponseEntity<>(surgeryMasterService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurgeryMasterResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(surgeryMasterService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SurgeryMasterResponse>> getAll() {
        return ResponseEntity.ok(surgeryMasterService.getAll());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SurgeryMasterResponse>> getByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(surgeryMasterService.getByCategoryId(categoryId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SurgeryMasterResponse>> getActive() {
        return ResponseEntity.ok(surgeryMasterService.getActive());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SurgeryMasterResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(surgeryMasterService.search(q));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurgeryMasterResponse> update(@PathVariable Long id, @RequestBody SurgeryMasterRequest request) {
        return ResponseEntity.ok(surgeryMasterService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        surgeryMasterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
