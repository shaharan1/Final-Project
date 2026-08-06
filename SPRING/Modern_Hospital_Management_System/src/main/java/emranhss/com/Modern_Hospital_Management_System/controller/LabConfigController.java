package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.InterpretationRuleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabRuleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.ReferenceRangeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.TestParameterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.*;
import emranhss.com.Modern_Hospital_Management_System.service.lab.LabConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab/config")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LabConfigController {

    private final LabConfigService labConfigService;

    @GetMapping("/test-masters/{testMasterId}")
    public ResponseEntity<TestMasterDetailResponse> getTestMasterDetail(@PathVariable Long testMasterId) {
        return ResponseEntity.ok(labConfigService.getTestMasterDetail(testMasterId));
    }

    @GetMapping("/parameters")
    public ResponseEntity<List<TestParameterResponse>> getAllParameters() {
        return ResponseEntity.ok(labConfigService.getAllParameters());
    }

    @GetMapping("/parameters/{id}")
    public ResponseEntity<TestParameterResponse> getParameter(@PathVariable Long id) {
        return ResponseEntity.ok(labConfigService.getParameter(id));
    }

    @PostMapping("/parameters")
    public ResponseEntity<TestParameterResponse> createParameter(@RequestBody TestParameterRequest request) {
        return ResponseEntity.ok(labConfigService.createParameter(request));
    }

    @PutMapping("/parameters/{id}")
    public ResponseEntity<TestParameterResponse> updateParameter(@PathVariable Long id, @RequestBody TestParameterRequest request) {
        return ResponseEntity.ok(labConfigService.updateParameter(id, request));
    }

    @DeleteMapping("/parameters/{id}")
    public ResponseEntity<Void> deleteParameter(@PathVariable Long id) {
        labConfigService.deleteParameter(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reference-ranges")
    public ResponseEntity<ReferenceRangeResponse> createRange(@RequestBody ReferenceRangeRequest request) {
        return ResponseEntity.ok(labConfigService.createRange(request));
    }

    @PutMapping("/reference-ranges/{id}")
    public ResponseEntity<ReferenceRangeResponse> updateRange(@PathVariable Long id, @RequestBody ReferenceRangeRequest request) {
        return ResponseEntity.ok(labConfigService.updateRange(id, request));
    }

    @DeleteMapping("/reference-ranges/{id}")
    public ResponseEntity<Void> deleteRange(@PathVariable Long id) {
        labConfigService.deleteRange(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/interpretation-rules")
    public ResponseEntity<InterpretationRuleResponse> createRule(@RequestBody InterpretationRuleRequest request) {
        return ResponseEntity.ok(labConfigService.createRule(request));
    }

    @PutMapping("/interpretation-rules/{id}")
    public ResponseEntity<InterpretationRuleResponse> updateRule(@PathVariable Long id, @RequestBody InterpretationRuleRequest request) {
        return ResponseEntity.ok(labConfigService.updateRule(id, request));
    }

    @DeleteMapping("/interpretation-rules/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        labConfigService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rules")
    public ResponseEntity<List<LabRuleResponse>> getAllLabRules() {
        return ResponseEntity.ok(labConfigService.getAllLabRules());
    }

    @PostMapping("/rules")
    public ResponseEntity<LabRuleResponse> createLabRule(@RequestBody LabRuleRequest request) {
        return ResponseEntity.ok(labConfigService.createLabRule(request));
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<LabRuleResponse> updateLabRule(@PathVariable Long id, @RequestBody LabRuleRequest request) {
        return ResponseEntity.ok(labConfigService.updateLabRule(id, request));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteLabRule(@PathVariable Long id) {
        labConfigService.deleteLabRule(id);
        return ResponseEntity.noContent().build();
    }
}
