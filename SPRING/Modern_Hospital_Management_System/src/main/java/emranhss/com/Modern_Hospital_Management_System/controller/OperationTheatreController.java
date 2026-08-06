package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.OperationTheatreRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OperationTheatreResponse;
import emranhss.com.Modern_Hospital_Management_System.service.OperationTheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operation-theatres")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OperationTheatreController {

    private final OperationTheatreService operationTheatreService;

    @PostMapping
    public ResponseEntity<OperationTheatreResponse> create(@RequestBody OperationTheatreRequest request) {
        return new ResponseEntity<>(operationTheatreService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationTheatreResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(operationTheatreService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OperationTheatreResponse>> getAll() {
        return ResponseEntity.ok(operationTheatreService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<OperationTheatreResponse>> getActive() {
        return ResponseEntity.ok(operationTheatreService.getActive());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OperationTheatreResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(operationTheatreService.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperationTheatreResponse> update(@PathVariable Long id, @RequestBody OperationTheatreRequest request) {
        return ResponseEntity.ok(operationTheatreService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        operationTheatreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
