package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryDashboardResponse;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/surgery-dashboard")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SurgeryDashboardController {

    private final SurgeryDashboardService surgeryDashboardService;

    @GetMapping
    public ResponseEntity<SurgeryDashboardResponse> getDashboardSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(surgeryDashboardService.getDashboardSummary(date));
    }
}
