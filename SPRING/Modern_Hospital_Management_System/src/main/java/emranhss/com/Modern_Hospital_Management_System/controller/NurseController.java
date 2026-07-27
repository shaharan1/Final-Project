package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.NurseMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.NurseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.NurseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Nurse;
import emranhss.com.Modern_Hospital_Management_System.repository.NurseRepository;
import emranhss.com.Modern_Hospital_Management_System.service.NurseService;
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
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NurseController {

    private final NurseService nurseService;
    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;

    @Value("${image.upload.dir}")
    private String uploadDir;



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

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Nurse nurse = nurseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Nurse not found"));
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + "nurse/" + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            nurse.setPhoto("/images/nurse/" + filename);
            nurseRepository.save(nurse);
            return ResponseEntity.ok(nurseMapper.toResponse(nurse));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to upload photo: " + e.getMessage()));
        }
    }
}
