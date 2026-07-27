package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ProfileUpdateRequestDTO;
import emranhss.com.Modern_Hospital_Management_System.dto.response.LoginResponseDTO;
import emranhss.com.Modern_Hospital_Management_System.entity.User;
import emranhss.com.Modern_Hospital_Management_System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${image.upload.dir}")
    private String uploadDir;

    private String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    @GetMapping
    public ResponseEntity<LoginResponseDTO> getProfile() {
        User user = userRepository.findByEmail(getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(toDTO(user));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequestDTO dto) {
        try {
            User user = userRepository.findByEmail(getCurrentEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (dto.getName() != null && !dto.getName().isBlank()) {
                user.setName(dto.getName());
            }
            if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
                user.setPhone(dto.getPhone());
            }
            userRepository.save(user);
            return ResponseEntity.ok(toDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update profile: " + e.getMessage()));
        }
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            User user = userRepository.findByEmail(getCurrentEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            user.setImage("/images/" + filename);
            userRepository.save(user);
            return ResponseEntity.ok(toDTO(user));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");
        if (currentPassword == null || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("Current and new password are required");
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed successfully");
    }

    private LoginResponseDTO toDTO(User user) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setUserId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole().name());
        dto.setImage(user.getImage());
        return dto;
    }
}
