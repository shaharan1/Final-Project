package emranhss.com.Modern_Hospital_Management_System.util;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.Role;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DietaryDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DieticianRepository dieticianRepository;
    private final DietPlanRepository dietPlanRepository;
    private final MealScheduleRepository mealScheduleRepository;
    private final DietAssignmentRepository dietAssignmentRepository;
    private final KitchenOrderRepository kitchenOrderRepository;
    private final PatientDietAlertRepository patientDietAlertRepository;
    private final DietHistoryRepository dietHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0 && dieticianRepository.count() > 0) {
            System.out.println("[SKIP] Dietary data already seeded.");
            return;
        }

        System.out.println("[SEED] Seeding dietary data...");

        List<Patient> patients = seedPatients();
        List<User> dieticianUsers = seedDieticianUsers();
        List<Dietician> dieticians = seedDieticians(dieticianUsers);
        List<MealSchedule> schedules = seedMealSchedules();
        List<DietPlan> plans = seedDietPlans(dieticians);
        List<DietAssignment> assignments = seedDietAssignments(patients, plans, dieticians);
        seedKitchenOrders(patients, plans, assignments, schedules);
        seedDietAlerts(patients);
        seedDietHistory(patients, plans, assignments);

        System.out.println("[OK] Dietary data seeded successfully.");
    }

    private List<Patient> seedPatients() {
        if (patientRepository.count() > 0) return patientRepository.findAll();
        Object[][] data = {
            {"PT-2026001", "Rahima Khatun", "Female", LocalDate.of(1965, 3, 15), "B+", "Married", "01711001001", "rahima.khatun@gmail.com", "1965150312345", "12/A Mirpur Road, Dhaka", "Karim Uddin", "01711001010", "Husband"},
            {"PT-2026002", "Abdul Hamid", "Male", LocalDate.of(1972, 7, 22), "O+", "Married", "01711002002", "abdul.hamid@gmail.com", "1972220712345", "45 Banani, Dhaka", "Fatema Begum", "01711002010", "Wife"},
            {"PT-2026003", "Fatema Begum", "Female", LocalDate.of(1980, 11, 8), "A+", "Married", "01711003003", "fatema.b@gmail.com", "1980081112345", "78 Uttara, Dhaka", "Abdul Hamid", "01711003010", "Husband"},
            {"PT-2026004", "Kamal Hossain", "Male", LocalDate.of(1958, 1, 30), "AB-", "Widowed", "01711004004", "kamal.h@gmail.com", "1958300112345", "23 Dhanmondi, Dhaka", "Nargis Akter", "01711004010", "Daughter"},
            {"PT-2026005", "Nargis Akter", "Female", LocalDate.of(1988, 6, 12), "B-", "Single", "01711005005", "nargis.a@gmail.com", "1988120612345", "56 Gulshan, Dhaka", "Kamal Hossain", "01711005010", "Father"},
            {"PT-2026006", "Mohammad Rafiq", "Male", LocalDate.of(1975, 9, 5), "O-", "Married", "01711006006", "rafiq.m@gmail.com", "1975050912345", "89 Mohakhali, Dhaka", "Salma Khatun", "01711006010", "Wife"},
        };
        for (Object[] d : data) {
            Patient p = new Patient();
            p.setPatientCode((String) d[0]);
            p.setName((String) d[1]);
            p.setGender((String) d[2]);
            p.setDateOfBirth((LocalDate) d[3]);
            p.setBloodGroup((String) d[4]);
            p.setMaritalStatus((String) d[5]);
            p.setPhone((String) d[6]);
            p.setEmail((String) d[7]);
            p.setNationalId((String) d[8]);
            p.setAddress((String) d[9]);
            p.setEmergencyContactName((String) d[10]);
            p.setEmergencyContactNumber((String) d[11]);
            p.setRelationship((String) d[12]);
            patientRepository.save(p);
        }
        System.out.println("[OK] 6 patients seeded.");
        return patientRepository.findAll();
    }

    private List<User> seedDieticianUsers() {
        Object[][] data = {
            {"Dr. Nasreen Akter", "nasreen.diet@elitecare.com", "01812001001", "dietician123"},
            {"Dr. Mahmuda Khatun", "mahmuda.diet@elitecare.com", "01812002002", "dietician123"},
        };
        for (Object[] d : data) {
            if (userRepository.findByEmail((String) d[1]).isEmpty()) {
                User u = new User();
                u.setName((String) d[0]);
                u.setEmail((String) d[1]);
                u.setPhone((String) d[2]);
                u.setPassword(passwordEncoder.encode((String) d[3]));
                u.setRole(Role.Dietician);
                u.setActive(true);
                userRepository.save(u);
            }
        }
        System.out.println("[OK] 2 dietician users seeded.");
        return List.of(
            userRepository.findByEmail("nasreen.diet@elitecare.com").get(),
            userRepository.findByEmail("mahmuda.diet@elitecare.com").get()
        );
    }

    private List<Dietician> seedDieticians(List<User> users) {
        if (dieticianRepository.count() > 0) return dieticianRepository.findAll();
        Object[][] data = {
            {0, "Clinical Nutrition", "MSc Nutrition", 12, "Diet-BM-001", "01812001001", "Sun-Wed", "08:00-16:00"},
            {1, "Pediatric Nutrition", "PhD Dietetics", 8, "Diet-BM-002", "01812002002", "Mon-Thu", "09:00-17:00"},
        };
        for (Object[] d : data) {
            Dietician di = new Dietician();
            di.setUser(users.get((int) d[0]));
            di.setSpecialization((String) d[1]);
            di.setQualification((String) d[2]);
            di.setExperienceYears((int) d[3]);
            di.setLicenseNumber((String) d[4]);
            di.setPhone((String) d[5]);
            di.setAvailableDays((String) d[6]);
            di.setDutyHours((String) d[7]);
            di.setActive(true);
            dieticianRepository.save(di);
        }
        System.out.println("[OK] 2 dieticians seeded.");
        return dieticianRepository.findAll();
    }

    private List<MealSchedule> seedMealSchedules() {
        if (mealScheduleRepository.count() > 0) return mealScheduleRepository.findAll();
        Object[][] data = {
            {"Breakfast", LocalTime.of(7, 0), LocalTime.of(5, 30), LocalTime.of(6, 45), "ACTIVE", 45, 40, 3, 2, "Standard hospital breakfast", true},
            {"Morning Snacks", LocalTime.of(10, 30), LocalTime.of(10, 0), LocalTime.of(10, 15), "ACTIVE", 30, 28, 1, 1, "Light mid-morning refreshment", false},
            {"Lunch", LocalTime.of(12, 30), LocalTime.of(11, 0), LocalTime.of(12, 15), "ACTIVE", 65, 58, 5, 2, "Main meal of the day", false},
            {"Evening Snacks", LocalTime.of(16, 0), LocalTime.of(15, 30), LocalTime.of(15, 45), "ACTIVE", 35, 32, 2, 1, "Afternoon tea and snacks", false},
            {"Dinner", LocalTime.of(19, 0), LocalTime.of(17, 30), LocalTime.of(18, 45), "ACTIVE", 55, 50, 4, 1, "Light dinner meal", false},
            {"Night Diet", LocalTime.of(22, 0), LocalTime.of(21, 30), LocalTime.of(21, 45), "ACTIVE", 20, 18, 1, 1, "Supplementary night nutrition", false},
        };
        for (Object[] d : data) {
            MealSchedule m = new MealSchedule();
            m.setMealName((String) d[0]);
            m.setServingTime((LocalTime) d[1]);
            m.setPreparationStartTime((LocalTime) d[2]);
            m.setPreparationEndTime((LocalTime) d[3]);
            m.setStatus((String) d[4]);
            m.setTotalOrdersToday((int) d[5]);
            m.setCompletedOrders((int) d[6]);
            m.setPendingOrders((int) d[7]);
            m.setCancelledOrders((int) d[8]);
            m.setNotes((String) d[9]);
            m.setCurrentMeal((Boolean) d[10]);
            mealScheduleRepository.save(m);
        }
        System.out.println("[OK] 6 meal schedules seeded.");
        return mealScheduleRepository.findAll();
    }

    private List<DietPlan> seedDietPlans(List<Dietician> dieticians) {
        if (dietPlanRepository.count() > 0) return dietPlanRepository.findAll();
        Object[][] data = {
            {"Standard Regular Diet", "Regular", "Balanced nutrition for general patients", "Rice, Dal, Vegetables, Fish/Chicken", "07:00",
             "Fruits, Yogurt", "10:30", "Rice, Meat Curry, Salad, Dal", "12:30",
             "Biscuits, Tea", "16:00", "Roti, Vegetables, Rice", "19:00",
             "Milk, Biscuits", "22:00", 2100.0, 65.0, 300.0, 55.0, 25.0, 2000.0, 3500.0, 2500.0, "Multivitamin daily", "No restrictions", "Balanced meal plan"},
            {"Diabetic Diet Plan", "Diabetic", "Low sugar, high fiber diet for diabetic patients", "Whole wheat toast, Egg white, Unsweetened tea", "07:00",
             "Apple, Handful of nuts", "10:30", "Brown rice, Grilled chicken, Steamed vegetables", "12:30",
             "Cucumber, Green tea", "16:00", "Roti, Mixed vegetables, Dal", "19:00",
             "Warm milk (no sugar)", "22:00", 1800.0, 70.0, 220.0, 45.0, 35.0, 1500.0, 3000.0, 2800.0, "Vitamin B complex", "Strict sugar control", "Monitor blood sugar before meals"},
            {"Low Salt Cardiac Diet", "Cardiac", "Low sodium diet for heart patients", "Oatmeal with fruits, Skim milk", "07:00",
             "Fresh fruit salad", "10:30", "Steamed rice, Grilled fish, Boiled vegetables (no salt)", "12:30",
             "Unsalted crackers, Water", "16:00", "Roti, Paneer curry (low salt), Salad", "19:00",
             "Warm water with lemon", "22:00", 1900.0, 60.0, 280.0, 50.0, 30.0, 800.0, 3200.0, 2600.0, "Potassium supplement", "Sodium less than 2g per day", "Daily weight monitoring"},
            {"High Protein Recovery Diet", "HighProtein", "High protein diet for post-surgery recovery", "Protein shake, Boiled eggs, Whole wheat bread", "07:00",
             "Greek yogurt, Almonds", "10:30", "Chicken breast, Brown rice, Mixed vegetables, Lentil soup", "12:30",
             "Protein bar, Banana", "16:00", "Fish curry, Roti, Spinach, Rice", "19:00",
             "Casein protein, Warm milk", "22:00", 2500.0, 120.0, 280.0, 60.0, 28.0, 1800.0, 3500.0, 3000.0, "Protein supplements", "High protein intake", "Monitor albumin levels weekly"},
            {"Renal Diet Plan", "Renal", "Low protein, low potassium diet for kidney patients", "White bread, Egg, Low potassium juice", "07:00",
             "Rice cake, Apple slices", "10:30", "White rice, Small portion chicken, Low potassium vegetables", "12:30",
             "Unsalted pretzels, Water", "16:00", "Roti, Low potassium curry, Rice", "19:00",
             "Herbal tea, Low potassium biscuit", "22:00", 1800.0, 40.0, 260.0, 50.0, 20.0, 1000.0, 2000.0, 2200.0, "Iron supplement", "Restrict potassium and phosphorus", "Monthly renal function tests"},
            {"Soft Diet Plan", "Soft", "Easy to digest soft foods for post-operative patients", "Porridge, Warm milk", "07:00",
             "Banana mash, Yogurt", "10:30", "Khichuri, Boiled chicken, Mashed vegetables", "12:30",
             "Custard, Soft biscuit", "16:00", "Mashed rice, Dal soup, Soft fish", "19:00",
             "Warm milk, Soft toast", "22:00", 1700.0, 55.0, 240.0, 40.0, 15.0, 1500.0, 2800.0, 2400.0, "Vitamin C", "No hard or spicy food", "Gradual return to regular diet"},
            {"Pediatric Nutrition Plan", "Pediatric", "Age-appropriate nutrition for children", "Milk, Banana, Toast", "07:00",
             "Fruit puree, crackers", "10:30", "Rice, Chicken curry, Vegetables, Dal", "12:30",
             "Milkshake, Biscuits", "16:00", "Rice, Fish, Mashed potato", "19:00",
             "Warm milk", "22:00", 1600.0, 50.0, 230.0, 45.0, 20.0, 1200.0, 2500.0, 2000.0, "Calcium + Vitamin D", "Age-appropriate portions", "Monthly growth monitoring"},
            {"Liquid Diet Plan", "Liquid", "Full liquid diet for patients unable to eat solid food", "Strained fruit juice, Broth", "07:00",
             "Clear soup, Gelatin", "10:30", "Cream soup, Strained porridge, Milk", "12:30",
             "Fruit juice, Yogurt drink", "16:00", "Bone broth, Milk", "19:00",
             "Warm milk, Honey", "22:00", 1400.0, 35.0, 200.0, 30.0, 5.0, 1500.0, 2500.0, 2500.0, "Electrolyte supplements", "No solid food", "Daily weight check"},
        };
        for (Object[] d : data) {
            DietPlan dp = new DietPlan();
            dp.setName((String) d[0]);
            dp.setDietType((String) d[1]);
            dp.setDescription((String) d[2]);
            dp.setCreatedByDietician(dieticians.get(0));
            dp.setBreakfast((String) d[3]);
            dp.setBreakfastTime((String) d[4]);
            dp.setMorningSnacks((String) d[5]);
            dp.setMorningSnacksTime((String) d[6]);
            dp.setLunch((String) d[7]);
            dp.setLunchTime((String) d[8]);
            dp.setEveningSnacks((String) d[9]);
            dp.setEveningSnacksTime((String) d[10]);
            dp.setDinner((String) d[11]);
            dp.setDinnerTime((String) d[12]);
            dp.setNightDiet((String) d[13]);
            dp.setNightDietTime((String) d[14]);
            dp.setTotalCalories((Double) d[15]);
            dp.setProtein((Double) d[16]);
            dp.setCarbohydrate((Double) d[17]);
            dp.setFat((Double) d[18]);
            dp.setFiber((Double) d[19]);
            dp.setSodium((Double) d[20]);
            dp.setPotassium((Double) d[21]);
            dp.setWaterIntakeMl((Double) d[22]);
            dp.setVitaminRecommendation((String) d[23]);
            dp.setDoctorRecommendation((String) d[24]);
            dp.setDieticianNotes((String) d[25]);
            dp.setActive(true);
            dietPlanRepository.save(dp);
        }
        System.out.println("[OK] 8 diet plans seeded.");
        return dietPlanRepository.findAll();
    }

    private List<DietAssignment> seedDietAssignments(List<Patient> patients, List<DietPlan> plans, List<Dietician> dieticians) {
        if (dietAssignmentRepository.count() > 0) return dietAssignmentRepository.findAll();
        Object[][] data = {
            {0, 0, LocalDate.of(2026, 7, 1), null, "ACTIVE", "Regular diet for general recovery", "Monitor intake", 2100.0, 65.0},
            {1, 1, LocalDate.of(2026, 7, 2), LocalDate.of(2026, 7, 30), "ACTIVE", "Diabetic diet management", "Blood sugar before each meal", 1800.0, 75.0},
            {2, 2, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 20), "COMPLETED", "Cardiac recovery diet", "Sodium restriction", 1900.0, 62.0},
            {3, 3, LocalDate.of(2026, 7, 10), null, "ACTIVE", "Post-surgery high protein diet", "Track protein intake daily", 2500.0, 70.0},
            {4, 4, LocalDate.of(2026, 7, 8), LocalDate.of(2026, 8, 8), "ACTIVE", "Renal diet management", "Weekly blood tests", 1800.0, 55.0},
            {0, 5, LocalDate.of(2026, 7, 15), null, "ACTIVE", "Soft diet after procedure", "Gradual transition plan", 1700.0, 60.0},
            {5, 6, LocalDate.of(2026, 7, 12), null, "ON_HOLD", "Pediatric nutrition plan", "Family consultation needed", 1600.0, 50.0},
            {1, 7, LocalDate.of(2026, 7, 20), null, "ACTIVE", "Liquid diet pre-procedure", "NPO after midnight", 1400.0, 78.0},
        };
        for (Object[] d : data) {
            DietAssignment da = new DietAssignment();
            da.setPatient(patients.get((int) d[0]));
            da.setDietPlan(plans.get((int) d[1]));
            da.setDietician(dieticians.get(0));
            da.setStartDate((LocalDate) d[2]);
            da.setEndDate((LocalDate) d[3]);
            da.setStatus((String) d[4]);
            da.setReason((String) d[5]);
            da.setSpecialInstructions((String) d[6]);
            da.setTargetCalories((Double) d[7]);
            da.setTargetWeight((Double) d[8]);
            dietAssignmentRepository.save(da);
        }
        System.out.println("[OK] 8 diet assignments seeded.");
        return dietAssignmentRepository.findAll();
    }

    private void seedKitchenOrders(List<Patient> patients, List<DietPlan> plans, List<DietAssignment> assignments, List<MealSchedule> schedules) {
        if (kitchenOrderRepository.count() > 0) return;
        String[] statuses = {"PENDING", "PREPARING", "COOKING", "READY", "DELIVERED"};
        String[] priorities = {"NORMAL", "HIGH", "URGENT"};
        String[] mealTimes = {"BREAKFAST", "LUNCH", "DINNER", "MORNING_SNACKS", "EVENING_SNACKS", "NIGHT_DIET"};
        for (int i = 0; i < 12; i++) {
            Patient patient = patients.get(i % patients.size());
            DietPlan plan = plans.get(i % plans.size());
            DietAssignment assignment = assignments.get(i % assignments.size());
            MealSchedule schedule = schedules.get(i % schedules.size());
            KitchenOrder ko = new KitchenOrder();
            ko.setPatient(patient);
            ko.setDietPlan(plan);
            ko.setDietAssignment(assignment);
            ko.setMealTime(mealTimes[i % mealTimes.length]);
            ko.setMealType(plan.getBreakfast());
            ko.setDietType(plan.getDietType());
            ko.setPriority(priorities[i % priorities.length]);
            ko.setStatus(statuses[i % statuses.length]);
            ko.setBedNumber("BED-" + (101 + i));
            ko.setSpecialDiet(!plan.getDietType().equals("Regular"));
            ko.setKitchenNotes("Standard preparation for " + plan.getDietType() + " diet");
            kitchenOrderRepository.save(ko);
        }
        System.out.println("[OK] 12 kitchen orders seeded.");
    }

    private void seedDietAlerts(List<Patient> patients) {
        if (patientDietAlertRepository.count() > 0) return;
        Object[][] data = {
            {0, "DIABETIC", "Patient is diabetic - no sugar in meals", "HIGH", "ACTIVE", "No added sugar, low glycemic index foods only"},
            {1, "FOOD_ALLERGY", "Allergic to shellfish", "CRITICAL", "ACTIVE", "Strictly no shellfish or cross-contaminated items", "Shellfish"},
            {2, "LOW_SODIUM", "Cardiac patient - sodium restriction", "MEDIUM", "ACKNOWLEDGED", "Maximum 2g sodium per day"},
            {3, "NPO", "Nothing by mouth - pre-surgery", "CRITICAL", "ACTIVE", "Patient must not eat or drink anything"},
            {4, "FASTING", "Fasting for blood work tomorrow", "LOW", "RESOLVED", "Clear liquids only until 6 AM"},
            {0, "DIABETIC", "Blood sugar spike after lunch", "HIGH", "ACTIVE", "Adjust insulin and modify lunch portion"},
        };
        for (Object[] d : data) {
            PatientDietAlert a = new PatientDietAlert();
            a.setPatient(patients.get((int) d[0]));
            a.setAlertType((String) d[1]);
            a.setDescription((String) d[2]);
            a.setSeverity((String) d[3]);
            a.setStatus((String) d[4]);
            a.setSpecialInstructions((String) d[5]);
            if (d.length > 6) a.setAllergenName((String) d[6]);
            a.setCreatedBy("Dr. Nasreen Akter");
            if ("RESOLVED".equals(d[4])) a.setResolvedAt(LocalDateTime.now().minusDays(2));
            patientDietAlertRepository.save(a);
        }
        System.out.println("[OK] 6 diet alerts seeded.");
    }

    private void seedDietHistory(List<Patient> patients, List<DietPlan> plans, List<DietAssignment> assignments) {
        if (dietHistoryRepository.count() > 0) return;
        Object[][] data = {
            {0, 0, 0, "ASSIGNED", "Initial diet assignment - Regular diet plan", null, "Regular diet assigned", "Dr. Nasreen Akter", "Dietician", 65.0, 24.5},
            {1, 1, 1, "ASSIGNED", "Diabetic diet plan assigned", null, "Diabetic diet started", "Dr. Nasreen Akter", "Dietician", 78.0, 27.1},
            {2, 2, 2, "ASSIGNED", "Cardiac diet assigned post heart surgery", null, "Low salt cardiac diet", "Dr. Mahmuda Khatun", "Dietician", 70.0, 25.8},
            {3, 3, 3, "ASSIGNED", "High protein recovery diet after knee surgery", null, "High protein diet started", "Dr. Nasreen Akter", "Dietician", 82.0, 26.3},
            {1, 1, 1, "UPDATED", "Diet modified based on blood sugar readings", "Diabetic diet (1800 cal)", "Diabetic diet adjusted (1600 cal)", "Dr. Nasreen Akter", "Dietician", 77.5, 26.9},
            {0, 0, 0, "DOCTOR_RECOMMENDATION", "Doctor recommended soft diet after endoscopy", "Regular diet", "Soft diet for 5 days", "Dr. Mahmuda Khatun", "Doctor", 64.5, 24.3},
        };
        for (Object[] d : data) {
            DietHistory h = new DietHistory();
            h.setPatient(patients.get((int) d[0]));
            h.setDietAssignment(assignments.get((int) d[1]));
            h.setDietPlan(plans.get((int) d[2]));
            h.setActionType((String) d[3]);
            h.setDescription((String) d[4]);
            h.setPreviousValue((String) d[5]);
            h.setNewValue((String) d[6]);
            h.setPerformedBy((String) d[7]);
            h.setUserRole((String) d[8]);
            h.setWeightKg((Double) d[9]);
            h.setBmi((Double) d[10]);
            dietHistoryRepository.save(h);
        }
        System.out.println("[OK] 6 diet history records seeded.");
    }
}
