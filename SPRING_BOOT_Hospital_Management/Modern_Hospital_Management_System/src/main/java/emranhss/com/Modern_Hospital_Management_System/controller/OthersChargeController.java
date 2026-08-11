package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.OthersChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OthersChargeResponse;
import emranhss.com.Modern_Hospital_Management_System.service.OthersChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges/others")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OthersChargeController {


    private final OthersChargeService othersChargeService;

    @PostMapping
    public ResponseEntity<OthersChargeResponse> addCharge(@RequestBody OthersChargeRequest request) {
        return new ResponseEntity<>(othersChargeService.addCharge(request), HttpStatus.CREATED);
    }

    @GetMapping("/admission/{admittedPatientId}")
    public ResponseEntity<List<OthersChargeResponse>> getChargesByAdmission(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(othersChargeService.getChargesByAdmission(admittedPatientId));
    }

    @GetMapping("/admission/{admittedPatientId}/unbilled")
    public ResponseEntity<List<OthersChargeResponse>> getUnbilledChargesByAdmission(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(othersChargeService.getUnbilledChargesByAdmission(admittedPatientId));
    }
}
