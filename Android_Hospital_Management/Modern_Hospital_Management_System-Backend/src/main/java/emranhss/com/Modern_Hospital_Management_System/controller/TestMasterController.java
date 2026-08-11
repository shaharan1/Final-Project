package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.TestMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TestMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.service.TestMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TestMasterController {

    private final TestMasterService testMasterService;

    // Create Test
    @PostMapping
    public TestMasterResponse save(@RequestBody TestMasterRequest request) {
        return testMasterService.save(request);
    }

    // Get All Tests
    @GetMapping
    public List<TestMasterResponse> getAll() {
        return testMasterService.getAll();
    }

    // Get Single Test
    @GetMapping("/{id}")
    public TestMasterResponse getById(@PathVariable Long id) {
        return testMasterService.getById(id);
    }

    // Search Test
    @GetMapping("/search")
    public List<TestMasterResponse> search(
            @RequestParam String keyword) {
        return testMasterService.search(keyword);
    }

    // Update Test
    @PutMapping("/{id}")
    public TestMasterResponse update(
            @PathVariable Long id,
            @RequestBody TestMasterRequest request) {

        return testMasterService.update(id, request);
    }

    // Delete Test
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        testMasterService.delete(id);
    }

}