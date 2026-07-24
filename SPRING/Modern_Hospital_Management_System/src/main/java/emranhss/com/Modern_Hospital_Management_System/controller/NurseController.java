package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.NurseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.NurseResponse;
import emranhss.com.Modern_Hospital_Management_System.service.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NurseController {

    private final NurseService nurseService;



    @PostMapping("/profile/create")
    public ResponseEntity<NurseResponse> createNurseProfile(@RequestBody NurseRequest request) {
        return ResponseEntity.ok(nurseService.saveNurseProfile(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NurseResponse> findNurseById(@PathVariable Long id) {
        return ResponseEntity.ok(nurseService.getNurseById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<NurseResponse> findNurseByEmail(@PathVariable String email) {
        return ResponseEntity.ok(nurseService.getNurseByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<NurseResponse>> fetchAllActiveNurses() {
        return ResponseEntity.ok(nurseService.getAllActiveNurses());
    }

    @GetMapping("/ward/{wardName}")
    public ResponseEntity<List<NurseResponse>> fetchNursesByWard(@PathVariable String wardName) {
        return ResponseEntity.ok(nurseService.getNursesByWard(wardName));
    }

    @GetMapping("/on-duty")
    public ResponseEntity<List<NurseResponse>> fetchOnDutyNurses() {
        return ResponseEntity.ok(nurseService.getOnDutyNurses());
    }

    @PutMapping("/{id}/duty-status")
    public ResponseEntity<NurseResponse> changeDutyStatus(@PathVariable Long id, @RequestParam boolean onDuty) {
        return ResponseEntity.ok(nurseService.updateNurseDutyStatus(id, onDuty));
    }

    @PutMapping("/{id}/active-status")
    public ResponseEntity<NurseResponse> changeActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(nurseService.toggleNurseActiveStatus(id, active));
    }
}
