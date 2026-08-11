package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.OfficeStaffMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.OfficeStaffRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OfficeStaffResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.OfficeStaff;
import emranhss.com.Modern_Hospital_Management_System.repository.OfficeStaffRepository;
import emranhss.com.Modern_Hospital_Management_System.service.OfficeStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/office-staff")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OfficeStaffController {

    private final OfficeStaffService officeStaffService;
    private final OfficeStaffRepository officeStaffRepository;
    private final OfficeStaffMapper officeStaffMapper;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @PostMapping("/create")
    public ResponseEntity<OfficeStaffResponse> createOfficeStaff(
            @RequestBody OfficeStaffRequest request) {

        return ResponseEntity.ok(
                officeStaffService.createOfficeStaff(request));
    }

    @GetMapping
    public ResponseEntity<List<OfficeStaffResponse>> getAllOfficeStaff() {

        return ResponseEntity.ok(
                officeStaffService.getAllOfficeStaff());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficeStaffResponse> getOfficeStaffById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                officeStaffService.getOfficeStaffById(id));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<OfficeStaffResponse>> getByDepartment(
            @PathVariable String department) {

        return ResponseEntity.ok(
                officeStaffService.getOfficeStaffByDepartment(department));
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<OfficeStaffResponse>> getByPosition(
            @PathVariable String position) {

        return ResponseEntity.ok(
                officeStaffService.getOfficeStaffByPosition(position));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeStaffResponse> updateOfficeStaff(
            @PathVariable Long id,
            @RequestBody OfficeStaffRequest request) {

        return ResponseEntity.ok(
                officeStaffService.updateOfficeStaff(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOfficeStaff(
            @PathVariable Long id) {

        officeStaffService.deleteOfficeStaff(id);

        return ResponseEntity.ok("Office Staff Deleted Successfully");
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            OfficeStaff staff = officeStaffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Office Staff not found"));
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + "office-staff/" + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            staff.setPhoto("/images/office-staff/" + filename);
            officeStaffRepository.save(staff);
            return ResponseEntity.ok(officeStaffMapper.toResponse(staff));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to upload photo: " + e.getMessage()));
        }
    }

}