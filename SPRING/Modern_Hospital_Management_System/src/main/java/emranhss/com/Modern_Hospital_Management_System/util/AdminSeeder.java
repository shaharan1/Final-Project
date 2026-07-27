package emranhss.com.Modern_Hospital_Management_System.util;

import emranhss.com.Modern_Hospital_Management_System.entity.User;
import emranhss.com.Modern_Hospital_Management_System.enums.Role;
import emranhss.com.Modern_Hospital_Management_System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@elitecare.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPhone("01700000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.Admin);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("[OK] Admin user created: " + adminEmail + " / admin123");
        }
    }
}
