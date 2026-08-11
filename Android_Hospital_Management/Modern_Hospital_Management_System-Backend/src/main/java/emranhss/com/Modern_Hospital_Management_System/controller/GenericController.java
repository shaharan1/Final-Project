package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.GenericRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.GenericResponse;
import emranhss.com.Modern_Hospital_Management_System.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generics")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GenericController {

    private final GenericService genericService;

    @PostMapping
    public ResponseEntity<GenericResponse> save(@RequestBody GenericRequest gr){
        return new ResponseEntity<>(genericService.create(gr), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(genericService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable Long id, @RequestBody GenericRequest gr) {
        return ResponseEntity.ok(genericService.update(id, gr));
    }

    @GetMapping
    public List<GenericResponse> getAll() {
        return genericService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genericService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
