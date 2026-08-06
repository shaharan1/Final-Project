package emranhss.com.Modern_Hospital_Management_System.config;

import emranhss.com.Modern_Hospital_Management_System.entity.OperationTheatre;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryCategory;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryMaster;
import emranhss.com.Modern_Hospital_Management_System.repository.OperationTheatreRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SurgeryCategoryRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SurgeryMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SurgeryDataSeeder implements CommandLineRunner {

    private final SurgeryCategoryRepository surgeryCategoryRepository;
    private final SurgeryMasterRepository surgeryMasterRepository;
    private final OperationTheatreRepository operationTheatreRepository;

    @Override
    public void run(String... args) {
        if (surgeryCategoryRepository.count() > 0 && operationTheatreRepository.count() > 0) {
            log.info("Surgery data already seeded. Skipping.");
            return;
        }

        Map<String, String[]> categories = Map.ofEntries(
            Map.entry("GENERAL", new String[]{"General Surgery", "Common abdominal and general surgical procedures"}),
            Map.entry("CARDIAC", new String[]{"Cardiac Surgery", "Heart and cardiovascular surgical procedures"}),
            Map.entry("ORTHOPEDIC", new String[]{"Orthopedic Surgery", "Bone, joint and musculoskeletal procedures"}),
            Map.entry("NEURO", new String[]{"Neurosurgery", "Brain, spine and nervous system procedures"}),
            Map.entry("ENT", new String[]{"ENT Surgery", "Ear, nose and throat surgical procedures"}),
            Map.entry("EYE", new String[]{"Eye Surgery", "Ophthalmic surgical procedures"}),
            Map.entry("GYNAECOLOGY", new String[]{"Gynaecology & Obstetrics", "Female reproductive system and childbirth procedures"}),
            Map.entry("UROLOGY", new String[]{"Urology Surgery", "Urinary tract and male reproductive procedures"}),
            Map.entry("PLASTIC", new String[]{"Plastic Surgery", "Reconstructive and cosmetic surgical procedures"}),
            Map.entry("DENTAL", new String[]{"Dental Surgery", "Oral and maxillofacial surgical procedures"})
        );

        Map<String, SurgeryCategory> categoryMap = new HashMap<>();
        int order = 1;
        for (Map.Entry<String, String[]> entry : categories.entrySet()) {
            if (!surgeryCategoryRepository.findByCode(entry.getKey()).isPresent()) {
                SurgeryCategory category = new SurgeryCategory();
                category.setCode(entry.getKey());
                category.setName(entry.getValue()[0]);
                category.setDescription(entry.getValue()[1]);
                category.setActive(true);
                category.setSortOrder(order);
                categoryMap.put(entry.getKey(), surgeryCategoryRepository.save(category));
            } else {
                categoryMap.put(entry.getKey(), surgeryCategoryRepository.findByCode(entry.getKey()).get());
            }
            order++;
        }

        Map<String, Map<String, Object>> masters = new HashMap<>();
        masters.put("GENERAL", master("GENERAL", "APPENDECTOMY", "Appendectomy", 25000.0, 8000.0, 6000.0, 4000.0, 5000.0, 3000.0, 4500.0, 48000.0, 60));
        masters.put("GENERAL", master("GENERAL", "CHOLECYSTECTOMY", "Laparoscopic Cholecystectomy", 35000.0, 8000.0, 6000.0, 4000.0, 8000.0, 4000.0, 4500.0, 60000.0, 90));
        masters.put("GENERAL", master("GENERAL", "HERNIORRHAPHY", "Hernia Repair", 28000.0, 8000.0, 6000.0, 4000.0, 5000.0, 3000.0, 4500.0, 52000.0, 60));
        masters.put("GENERAL", master("GENERAL", "SPLENECTOMY", "Splenectomy", 45000.0, 10000.0, 8000.0, 5000.0, 8000.0, 4000.0, 8000.0, 80000.0, 120));
        masters.put("CARDIAC", master("CARDIAC", "CABG", "Coronary Artery Bypass Grafting (CABG)", 150000.0, 25000.0, 20000.0, 15000.0, 30000.0, 20000.0, 25000.0, 280000.0, 300));
        masters.put("CARDIAC", master("CARDIAC", "VALVE_REPLACEMENT", "Heart Valve Replacement", 140000.0, 25000.0, 20000.0, 15000.0, 30000.0, 20000.0, 25000.0, 270000.0, 300));
        masters.put("ORTHOPEDIC", master("ORTHOPEDIC", "HIP_REPLACEMENT", "Total Hip Replacement", 80000.0, 15000.0, 12000.0, 8000.0, 15000.0, 10000.0, 12000.0, 150000.0, 150));
        masters.put("ORTHOPEDIC", master("ORTHOPEDIC", "KNEE_REPLACEMENT", "Total Knee Replacement", 90000.0, 15000.0, 12000.0, 8000.0, 15000.0, 10000.0, 12000.0, 160000.0, 150));
        masters.put("ORTHOPEDIC", master("ORTHOPEDIC", "FIXATION_FRACTURE", "Internal Fixation of Fracture", 35000.0, 10000.0, 8000.0, 6000.0, 8000.0, 6000.0, 6000.0, 70000.0, 120));
        masters.put("NEURO", master("NEURO", "CRANIOTOMY", "Craniotomy", 120000.0, 20000.0, 18000.0, 10000.0, 25000.0, 15000.0, 20000.0, 220000.0, 240));
        masters.put("NEURO", master("NEURO", "LAMINECTOMY", "Laminectomy", 75000.0, 15000.0, 12000.0, 8000.0, 15000.0, 10000.0, 15000.0, 145000.0, 180));
        masters.put("ENT", master("ENT", "TONSILLECTOMY", "Tonsillectomy", 18000.0, 5000.0, 4000.0, 3000.0, 3000.0, 2500.0, 3000.0, 35000.0, 45));
        masters.put("ENT", master("ENT", "SEPTOPLASTY", "Septoplasty", 22000.0, 5000.0, 4000.0, 3000.0, 3000.0, 2500.0, 3000.0, 40000.0, 60));
        masters.put("EYE", master("EYE", "CATARACT_PHACO", "Cataract Surgery (Phacoemulsification)", 25000.0, 5000.0, 4000.0, 3000.0, 8000.0, 2500.0, 3000.0, 45000.0, 45));
        masters.put("EYE", master("EYE", "STRABISMUS", "Strabismus Correction", 30000.0, 5000.0, 4000.0, 3000.0, 4000.0, 2500.0, 3000.0, 48000.0, 60));
        masters.put("GYNAECOLOGY", master("GYNAECOLOGY", "C_SECTION", "Caesarean Section", 30000.0, 8000.0, 6000.0, 4000.0, 5000.0, 3500.0, 4500.0, 55000.0, 60));
        masters.put("GYNAECOLOGY", master("GYNAECOLOGY", "HYSTERECTOMY", "Total Abdominal Hysterectomy", 45000.0, 10000.0, 8000.0, 5000.0, 8000.0, 5000.0, 6000.0, 80000.0, 120));
        masters.put("UROLOGY", master("UROLOGY", "TURP", "Transurethral Resection of Prostate (TURP)", 50000.0, 10000.0, 8000.0, 5000.0, 10000.0, 6000.0, 6000.0, 90000.0, 90));
        masters.put("UROLOGY", master("UROLOGY", "PCNL", "Percutaneous Nephrolithotomy (PCNL)", 65000.0, 12000.0, 10000.0, 6000.0, 12000.0, 8000.0, 8000.0, 115000.0, 120));
        masters.put("PLASTIC", master("PLASTIC", "RHINOPLASTY", "Rhinoplasty", 55000.0, 10000.0, 8000.0, 6000.0, 8000.0, 5000.0, 6000.0, 95000.0, 120));
        masters.put("PLASTIC", master("PLASTIC", "GRAFTING", "Skin Grafting", 30000.0, 8000.0, 6000.0, 4000.0, 6000.0, 4000.0, 5000.0, 58000.0, 90));
        masters.put("DENTAL", master("DENTAL", "IMPACTION", "Impacted Tooth Extraction", 8000.0, 3000.0, 2000.0, 1500.0, 1500.0, 1000.0, 1500.0, 17000.0, 30));
        masters.put("DENTAL", master("DENTAL", "JAW_FRACTURE", "Open Reduction of Jaw Fracture", 25000.0, 6000.0, 5000.0, 3000.0, 4000.0, 3000.0, 3500.0, 45000.0, 90));

        int masterCount = 0;
        for (Map.Entry<String, Map<String, Object>> entry : masters.entrySet()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = entry.getValue();
            String code = (String) data.get("code");
            if (surgeryMasterRepository.findBySurgeryCode(code).isPresent()) {
                continue;
            }
            SurgeryMaster master = new SurgeryMaster();
            master.setSurgeryCode(code);
            master.setSurgeryName((String) data.get("name"));
            master.setCategory(categoryMap.get(entry.getKey()));
            master.setStandardRate((Double) data.get("rate"));
            master.setOtCharge((Double) data.get("ot"));
            master.setAnesthesiaCharge((Double) data.get("anesthesia"));
            master.setNursingCharge((Double) data.get("nursing"));
            master.setEquipmentCharge((Double) data.get("equipment"));
            master.setConsumableCharge((Double) data.get("consumable"));
            master.setIcuCharge((Double) data.get("icu"));
            master.setPackageRate((Double) data.get("package"));
            master.setEstimatedDurationMin((Integer) data.get("duration"));
            master.setActive(true);
            surgeryMasterRepository.save(master);
            masterCount++;
        }

        seedOperationTheatres();
        log.info("Seeded {} surgery masters and {} categories successfully.", masterCount, categoryMap.size());
    }

    private Map<String, Object> master(String category, String code, String name, double rate,
                                       double ot, double anesthesia, double nursing,
                                       double equipment, double consumable, double icu,
                                       double packageRate, int duration) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("name", name);
        data.put("rate", rate);
        data.put("ot", ot);
        data.put("anesthesia", anesthesia);
        data.put("nursing", nursing);
        data.put("equipment", equipment);
        data.put("consumable", consumable);
        data.put("icu", icu);
        data.put("package", packageRate);
        data.put("duration", duration);
        return data;
    }

    private void seedOperationTheatres() {
        if (operationTheatreRepository.count() > 0) {
            return;
        }
        Map<String, String[]> theatres = Map.ofEntries(
            Map.entry("OT-001", new String[]{"OT 1 - General", "Ground Floor, Block A", "Cautery, Suction, Anesthesia Machine, Laparoscopy Tower", "AVAILABLE"}),
            Map.entry("OT-002", new String[]{"OT 2 - Cardiac", "Ground Floor, Block A", "Cardiopulmonary Bypass, Cautery, Defibrillator", "AVAILABLE"}),
            Map.entry("OT-003", new String[]{"OT 3 - Ortho", "Ground Floor, Block B", "C-Arm, Orthopedic Implant Set, Suction", "AVAILABLE"}),
            Map.entry("OT-004", new String[]{"OT 4 - Neuro", "First Floor, Block A", "Microscope, Craniotome, Neuro Cautery", "AVAILABLE"}),
            Map.entry("OT-005", new String[]{"OT 5 - Emergency", "Ground Floor, Emergency Wing", "Basic Surgical Set, Cautery, Anesthesia Machine", "AVAILABLE"})
        );
        int count = 0;
        for (Map.Entry<String, String[]> entry : theatres.entrySet()) {
            OperationTheatre ot = new OperationTheatre();
            ot.setOtCode(entry.getKey());
            ot.setOtName(entry.getValue()[0]);
            ot.setLocation(entry.getValue()[1]);
            ot.setEquipmentAvailable(entry.getValue()[2]);
            ot.setStatus(entry.getValue()[3]);
            ot.setCapacity(1);
            ot.setActive(true);
            operationTheatreRepository.save(ot);
            count++;
        }
        log.info("Seeded {} operation theatres successfully.", count);
    }
}
