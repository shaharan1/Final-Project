package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.GenericRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.GenericResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.service.GenericService;
import emranhss.com.Modern_Hospital_Management_System.service.MedicineService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(genericService.create(gr));
    }

    @GetMapping
    public List<GenericResponse> getAll() {

        return genericService.getAll();

    }

}