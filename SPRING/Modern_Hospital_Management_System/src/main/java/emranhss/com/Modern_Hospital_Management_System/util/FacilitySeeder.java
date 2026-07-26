package emranhss.com.Modern_Hospital_Management_System.util;

import emranhss.com.Modern_Hospital_Management_System.entity.Facility;
import emranhss.com.Modern_Hospital_Management_System.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FacilitySeeder implements CommandLineRunner {

    private final FacilityRepository facilityRepository;

    @Override
    public void run(String... args) {
        if (facilityRepository.count() > 0) return;

        List.of(
                new FacilityData("Centralized Oxygen", 200.0),
                new FacilityData("Ventilator", 500.0),
                new FacilityData("Cardiac Monitor", 150.0),
                new FacilityData("AC (Air Conditioned)", 100.0),
                new FacilityData("Non-AC (Fan Cooled)", 0.0),
                new FacilityData("TV (Television)", 50.0),
                new FacilityData("WiFi", 30.0),
                new FacilityData("Nurse Call Bell", 20.0),
                new FacilityData("Wheelchair Access", 0.0),
                new FacilityData("Private Bathroom", 80.0),
                new FacilityData("Shower", 40.0),
                new FacilityData("Bed Side Table", 10.0),
                new FacilityData("Extra Bed (Attendant)", 150.0),
                new FacilityData("X-Ray Machine", 300.0),
                new FacilityData("Dialysis Machine", 600.0),
                new FacilityData("ECG Machine", 200.0),
                new FacilityData("Pulse Oximeter", 50.0),
                new FacilityData("Infusion Pump", 120.0),
                new FacilityData("Suction Machine", 180.0),
                new FacilityData("Defibrillator", 250.0)
        ).forEach(fd -> {
            Facility f = new Facility();
            f.setName(fd.name);
            f.setStandardCharge(fd.charge);
            f.setActive(true);
            facilityRepository.save(f);
        });

        System.out.println("=== FacilitySeeder: 20 default facilities created ===");
    }

    private record FacilityData(String name, double charge) {}
}
