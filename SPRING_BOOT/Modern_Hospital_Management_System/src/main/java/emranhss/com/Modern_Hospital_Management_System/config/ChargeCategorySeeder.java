package emranhss.com.Modern_Hospital_Management_System.config;

import emranhss.com.Modern_Hospital_Management_System.entity.ChargeCategory;
import emranhss.com.Modern_Hospital_Management_System.repository.ChargeCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChargeCategorySeeder implements CommandLineRunner {

    private final ChargeCategoryRepository chargeCategoryRepository;

    @Override
    public void run(String... args) {
        if (chargeCategoryRepository.count() > 0) {
            log.info("Charge categories already seeded. Skipping.");
            return;
        }

        Map<String, String[]> categories = Map.ofEntries(
            Map.entry("WARD_BED", new String[]{"Ward / Bed Charges", "Daily room and bed rental charges"}),
            Map.entry("DOCTOR_CONSULTATION", new String[]{"Doctor Consultation", "Physician consultation and visit fees"}),
            Map.entry("MEDICINE", new String[]{"Medicine", "Pharmacy medicine charges"}),
            Map.entry("LAB_TEST", new String[]{"Lab Tests", "Diagnostic laboratory test charges"}),
            Map.entry("IMAGING", new String[]{"Imaging / Radiology", "X-Ray, CT Scan, MRI, Ultrasound charges"}),
            Map.entry("DIET_MEALS", new String[]{"Diet / Meals", "Patient diet plan and meal charges"}),
            Map.entry("SURGERY", new String[]{"Surgery", "Surgical procedure charges"}),
            Map.entry("EMERGENCY", new String[]{"Emergency Services", "Emergency department service charges"}),
            Map.entry("NURSING", new String[]{"Nursing Care", "Specialized nursing care charges"}),
            Map.entry("AMBULANCE", new String[]{"Ambulance", "Ambulance service and transport charges"}),
            Map.entry("PHYSIOTHERAPY", new String[]{"Physiotherapy", "Physiotherapy session charges"}),
            Map.entry("BLOOD_BANK", new String[]{"Blood Bank", "Blood and blood component charges"}),
            Map.entry("OXYGEN", new String[]{"Oxygen / Respiratory", "Oxygen therapy and respiratory support charges"}),
            Map.entry("ICU_CCU", new String[]{"ICU / CCU", "Intensive Care / Coronary Care Unit charges"}),
            Map.entry("PROCEDURE", new String[]{"Procedure / Supplies", "Minor procedures and medical supplies"}),
            Map.entry("ROOM_SERVICE", new String[]{"Room Service", "Additional room service and amenities"}),
            Map.entry("ADMINISTRATIVE", new String[]{"Administrative Fees", "Hospital administrative and registration fees"}),
            Map.entry("ADMISSION", new String[]{"Admission Fees", "Patient admission processing fees"}),
            Map.entry("DISCHARGE", new String[]{"Discharge Fees", "Patient discharge processing fees"}),
            Map.entry("INSURANCE", new String[]{"Insurance Co-payment", "Insurance co-payment and coverage adjustments"}),
            Map.entry("ADVANCE_DEPOSIT", new String[]{"Advance Deposit", "Patient advance deposit and prepayments"}),
            Map.entry("OTHER", new String[]{"Other Charges", "Miscellaneous and unclassified charges"})
        );

        int order = 1;
        for (Map.Entry<String, String[]> entry : categories.entrySet()) {
            ChargeCategory cat = new ChargeCategory();
            cat.setCode(entry.getKey());
            cat.setName(entry.getValue()[0]);
            cat.setDescription(entry.getValue()[1]);
            cat.setActive(true);
            cat.setSortOrder(order++);
            cat.setDefaultUnitPrice(0.0);
            chargeCategoryRepository.save(cat);
        }

        log.info("Seeded {} charge categories successfully.", categories.size());
    }
}
