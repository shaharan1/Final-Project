package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.*;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorDepartmentRepository doctorDepartmentRepository;
    private final TestsRepository testsRepository;
    private final ReportRepository reportRepository;
    private final PharmacySaleRepository pharmacySaleRepository;
    private final PharmacySaleItemRepository pharmacySaleItemRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final PaymentRepository paymentRepository;
    private final BillingRepository billingRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final AdmitPatientInvoiceRepository admitPatientInvoiceRepository;
    private final BedRepository bedRepository;
    private final WardRepository wardRepository;
    private final EmergencyPatientRepository emergencyPatientRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;
    private final RefundRepository refundRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PurchaseRepository purchaseRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;

    @Override
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();

        long totalPatients = patientRepository.count();

        long todayAppointments = appointmentRepository.findByAppointmentDate(today).size();

        Double todayRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                today.atStartOfDay(), today.atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
        if (todayRevenue == null) todayRevenue = 0.0;

        Double monthlyRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                currentMonth.atDay(1).atStartOfDay(), currentMonth.atEndOfMonth().atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
        if (monthlyRevenue == null) monthlyRevenue = 0.0;

        Double pharmacySales = pharmacySaleRepository.sumSalesByDateRange(
                currentMonth.atDay(1).atStartOfDay(), currentMonth.atEndOfMonth().atTime(LocalTime.MAX));
        if (pharmacySales == null) pharmacySales = 0.0;

        Double labIncome = admitPatientInvoiceRepository.findAll().stream()
                .mapToDouble(AdmitPatientInvoice::getTestCharges)
                .sum();

        List<Bed> allBeds = bedRepository.findAll();
        long totalBeds = allBeds.size();
        long occupiedBeds = allBeds.stream()
                .filter(b -> b.getStatus() == BedStatus.OCCUPIED)
                .count();
        double bedOccupancy = totalBeds > 0 ? Math.round((occupiedBeds * 100.0 / totalBeds) * 10.0) / 10.0 : 0.0;

        long pendingPayments = paymentRepository.findByPaymentStatus(PaymentStatus.PENDING).size();

        long dischargedPatients = admittedPatientRepository.findAll().stream()
                .filter(ap -> "DISCHARGED".equals(ap.getAdmissionStatus()))
                .count();

        long emergencyCases = emergencyPatientRepository.count();

        summary.put("totalPatients", totalPatients);
        summary.put("todayAppointments", todayAppointments);
        summary.put("todayRevenue", todayRevenue);
        summary.put("monthlyRevenue", monthlyRevenue);
        summary.put("pharmacySales", pharmacySales);
        summary.put("labIncome", labIncome);
        summary.put("bedOccupancy", bedOccupancy);
        summary.put("pendingPayments", pendingPayments);
        summary.put("dischargedPatients", dischargedPatients);
        summary.put("emergencyCases", emergencyCases);

        return summary;
    }

    @Override
    public Map<String, Object> getPatientAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        List<Patient> patients = patientRepository.findAll();

        long total = patients.size();
        long male = patients.stream().filter(p -> "MALE".equalsIgnoreCase(p.getGender())).count();
        long female = patients.stream().filter(p -> "FEMALE".equalsIgnoreCase(p.getGender())).count();

        Map<String, Long> bloodGroupDistribution = patients.stream()
                .filter(p -> p.getBloodGroup() != null && !p.getBloodGroup().isEmpty())
                .collect(Collectors.groupingBy(Patient::getBloodGroup, LinkedHashMap::new, Collectors.counting()));

        LocalDate today = LocalDate.now();
        Map<String, Long> ageGroupDistribution = new LinkedHashMap<>();
        ageGroupDistribution.put("CHILD (0-17)", patients.stream()
                .filter(p -> p.getDateOfBirth() != null && java.time.Period.between(p.getDateOfBirth(), today).getYears() <= 17)
                .count());
        ageGroupDistribution.put("YOUNG (18-35)", patients.stream()
                .filter(p -> p.getDateOfBirth() != null)
                .filter(p -> {
                    int age = java.time.Period.between(p.getDateOfBirth(), today).getYears();
                    return age >= 18 && age <= 35;
                }).count());
        ageGroupDistribution.put("ADULT (36-55)", patients.stream()
                .filter(p -> p.getDateOfBirth() != null)
                .filter(p -> {
                    int age = java.time.Period.between(p.getDateOfBirth(), today).getYears();
                    return age >= 36 && age <= 55;
                }).count());
        ageGroupDistribution.put("SENIOR (56+)", patients.stream()
                .filter(p -> p.getDateOfBirth() != null && java.time.Period.between(p.getDateOfBirth(), today).getYears() >= 56)
                .count());

        List<Appointment> allAppointments = appointmentRepository.findAll();
        Map<String, Long> departmentWisePatients = allAppointments.stream()
                .filter(a -> a.getDoctor() != null && a.getDoctor().getDoctorDepartment() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getDoctor().getDoctorDepartment().getDepartmentName(),
                        LinkedHashMap::new,
                        Collectors.counting()));

        List<Appointment> recentAppointments = allAppointments.stream()
                .filter(a -> a.getAppointmentDate() != null && a.getAppointmentDate().isAfter(today.minusMonths(12)))
                .collect(Collectors.toList());
        Map<String, Long> monthlyRegistrationTrend = recentAppointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LinkedHashMap::new,
                        Collectors.counting()));

        analytics.put("total", total);
        analytics.put("male", male);
        analytics.put("female", female);
        analytics.put("bloodGroupDistribution", bloodGroupDistribution);
        analytics.put("ageGroupDistribution", ageGroupDistribution);
        analytics.put("departmentWisePatients", departmentWisePatients);
        analytics.put("monthlyRegistrationTrend", monthlyRegistrationTrend);

        return analytics;
    }

    @Override
    public Map<String, Object> getAppointmentAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        List<Appointment> appointments = appointmentRepository.findAll();
        LocalDate today = LocalDate.now();

        long total = appointments.size();
        long completed = appointments.stream().filter(a -> "CONFIRMED".equalsIgnoreCase(a.getStatus())).count();
        long cancelled = appointments.stream().filter(a -> "CANCELLED".equalsIgnoreCase(a.getStatus())).count();
        long pending = appointments.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();

        double completionRate = total > 0 ? Math.round((completed * 100.0 / total) * 10.0) / 10.0 : 0.0;
        double cancellationRate = total > 0 ? Math.round((cancelled * 100.0 / total) * 10.0) / 10.0 : 0.0;

        LocalDate thirtyDaysAgo = today.minusDays(30);
        Map<String, Long> dailyTrend = appointments.stream()
                .filter(a -> a.getAppointmentDate() != null && !a.getAppointmentDate().isBefore(thirtyDaysAgo))
                .collect(Collectors.groupingBy(
                        a -> a.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Long> departmentWiseDistribution = appointments.stream()
                .filter(a -> a.getDoctor() != null && a.getDoctor().getDoctorDepartment() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getDoctor().getDoctorDepartment().getDepartmentName(),
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Long> peakHours = appointments.stream()
                .filter(a -> a.getAppointmentTime() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getAppointmentTime().getHour() + ":00",
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Long> statusDistribution = appointments.stream()
                .filter(a -> a.getStatus() != null)
                .collect(Collectors.groupingBy(
                        Appointment::getStatus,
                        LinkedHashMap::new,
                        Collectors.counting()));

        analytics.put("total", total);
        analytics.put("completed", completed);
        analytics.put("cancelled", cancelled);
        analytics.put("pending", pending);
        analytics.put("completionRate", completionRate);
        analytics.put("cancellationRate", cancellationRate);
        analytics.put("dailyTrend", dailyTrend);
        analytics.put("departmentWiseDistribution", departmentWiseDistribution);
        analytics.put("peakHours", peakHours);
        analytics.put("statusDistribution", statusDistribution);

        return analytics;
    }

    @Override
    public Map<String, Object> getDoctorAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();

        List<Doctor> doctors = doctorRepository.findAll();
        List<Appointment> allAppointments = appointmentRepository.findAll();

        List<Map<String, Object>> topDoctors = doctors.stream().map(doctor -> {
            Map<String, Object> doctorData = new LinkedHashMap<>();
            long patientCount = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .map(Appointment::getMobileNumber)
                    .distinct()
                    .count();
            long consultationCount = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .count();
            double revenueGenerated = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .filter(a -> a.getFeeCharged() != null)
                    .mapToDouble(Appointment::getFeeCharged)
                    .sum();

            doctorData.put("id", doctor.getId());
            doctorData.put("name", doctor.getUser() != null ? doctor.getUser().getName() : "N/A");
            doctorData.put("department", doctor.getDoctorDepartment() != null ? doctor.getDoctorDepartment().getDepartmentName() : "N/A");
            doctorData.put("specialization", doctor.getSpecialization());
            doctorData.put("patientCount", patientCount);
            doctorData.put("consultationCount", consultationCount);
            doctorData.put("revenueGenerated", Math.round(revenueGenerated * 100.0) / 100.0);
            return doctorData;
        })
                .sorted((a, b) -> Long.compare((long) b.get("consultationCount"), (long) a.get("consultationCount")))
                .limit(10)
                .collect(Collectors.toList());

        Map<String, Map<String, Object>> deptPerfMap = new LinkedHashMap<>();
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorDepartment() == null) continue;
            String deptName = doctor.getDoctorDepartment().getDepartmentName();
            deptPerfMap.putIfAbsent(deptName, new LinkedHashMap<>());
            long docAppointments = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .count();
            double docRevenue = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .filter(a -> a.getFeeCharged() != null)
                    .mapToDouble(Appointment::getFeeCharged)
                    .sum();
            Map<String, Object> deptData = deptPerfMap.get(deptName);
            deptData.put("departmentName", deptName);
            long existingCount = deptData.containsKey("totalConsultations") ? (long) deptData.get("totalConsultations") : 0;
            double existingRevenue = deptData.containsKey("totalRevenue") ? (double) deptData.get("totalRevenue") : 0.0;
            deptData.put("totalConsultations", existingCount + docAppointments);
            deptData.put("totalRevenue", Math.round((existingRevenue + docRevenue) * 100.0) / 100.0);
        }
        List<Map<String, Object>> departmentPerformance = new ArrayList<>(deptPerfMap.values());

        Map<String, Long> monthlyPerformance = allAppointments.stream()
                .filter(a -> a.getAppointmentDate() != null)
                .filter(a -> a.getAppointmentDate().isAfter(LocalDate.now().minusMonths(12)))
                .collect(Collectors.groupingBy(
                        a -> a.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LinkedHashMap::new,
                        Collectors.counting()));

        analytics.put("topDoctors", topDoctors);
        analytics.put("departmentPerformance", departmentPerformance);
        analytics.put("monthlyPerformance", monthlyPerformance);

        return analytics;
    }

    @Override
    public Map<String, Object> getLabAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        List<Tests> allTests;
        try {
            allTests = testsRepository.findAll();
        } catch (Exception e) {
            allTests = new ArrayList<>();
        }

        long totalTests = allTests.size();
        long completedTests = allTests.stream().filter(t -> "COMPLETED".equalsIgnoreCase(t.getOrderStatus())).count();
        long pendingTests = allTests.stream().filter(t -> "PENDING".equalsIgnoreCase(t.getOrderStatus())).count();
        long criticalTests = allTests.stream().filter(t -> "CRITICAL".equalsIgnoreCase(t.getOrderStatus())
                || "URGENT".equalsIgnoreCase(t.getOrderStatus())).count();

        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);
        Map<String, Long> dailyTestTrend = allTests.stream()
                .filter(t -> t.getOrderedDate() != null && t.getOrderedDate().toLocalDate().isAfter(thirtyDaysAgo))
                .collect(Collectors.groupingBy(
                        t -> t.getOrderedDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Long> testCategoryDistribution = allTests.stream()
                .filter(t -> t.getTestMaster() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getTestMaster().getTestName(),
                        LinkedHashMap::new,
                        Collectors.counting()));

        long totalWithResults = allTests.stream()
                .filter(t -> t.getResultEnteredDate() != null && t.getOrderedDate() != null)
                .count();
        Map<String, Object> turnaroundStats = new LinkedHashMap<>();
        if (totalWithResults > 0) {
            double avgHours = allTests.stream()
                    .filter(t -> t.getResultEnteredDate() != null && t.getOrderedDate() != null)
                    .mapToLong(t -> java.time.Duration.between(t.getOrderedDate(), t.getResultEnteredDate()).toHours())
                    .average()
                    .orElse(0.0);
            turnaroundStats.put("averageTurnaroundHours", Math.round(avgHours * 10.0) / 10.0);
        } else {
            turnaroundStats.put("averageTurnaroundHours", 0);
        }
        turnaroundStats.put("totalWithResults", totalWithResults);

        analytics.put("totalTests", totalTests);
        analytics.put("completedTests", completedTests);
        analytics.put("pendingTests", pendingTests);
        analytics.put("criticalTests", criticalTests);
        analytics.put("dailyTestTrend", dailyTestTrend);
        analytics.put("testCategoryDistribution", testCategoryDistribution);
        analytics.put("turnaroundStats", turnaroundStats);

        return analytics;
    }

    @Override
    public Map<String, Object> getPharmacyAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();

        List<PharmacySale> allSales = pharmacySaleRepository.findAll();
        List<PharmacySaleItem> allSaleItems = pharmacySaleItemRepository.findAll();

        double totalSales = allSales.stream()
                .filter(s -> "PAID".equals(s.getPaymentStatus()))
                .mapToDouble(s -> s.getNetPayable() != null ? s.getNetPayable() : 0.0)
                .sum();

        Double dailySalesVal = pharmacySaleRepository.sumSalesByDateRange(
                today.atStartOfDay(), today.atTime(LocalTime.MAX));
        double dailySales = dailySalesVal != null ? dailySalesVal : 0.0;

        Double monthlySalesVal = pharmacySaleRepository.sumSalesByDateRange(
                currentMonth.atDay(1).atStartOfDay(), currentMonth.atEndOfMonth().atTime(LocalTime.MAX));
        double monthlySales = monthlySalesVal != null ? monthlySalesVal : 0.0;

        Map<String, Long> medicineSalesMap = allSaleItems.stream()
                .filter(item -> item.getMedicineStock() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getMedicineStock().getMedicineName(),
                        LinkedHashMap::new,
                        Collectors.summingLong(PharmacySaleItem::getQuantity)));

        List<Map<String, Object>> topSellingMedicines = medicineSalesMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Map<String, Object> med = new LinkedHashMap<>();
                    med.put("medicineName", entry.getKey());
                    med.put("totalQuantitySold", entry.getValue());
                    return med;
                })
                .collect(Collectors.toList());

        long lowStockMedicines = medicineStockRepository.findLowStock().size();
        long expiredMedicines = medicineStockRepository.findExpired(today).size();

        Map<String, Long> salesTrend = allSales.stream()
                .filter(s -> s.getSaleDate() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LinkedHashMap::new,
                        Collectors.counting()));

        analytics.put("totalSales", Math.round(totalSales * 100.0) / 100.0);
        analytics.put("dailySales", Math.round(dailySales * 100.0) / 100.0);
        analytics.put("monthlySales", Math.round(monthlySales * 100.0) / 100.0);
        analytics.put("topSellingMedicines", topSellingMedicines);
        analytics.put("lowStockMedicines", lowStockMedicines);
        analytics.put("expiredMedicines", expiredMedicines);
        analytics.put("salesTrend", salesTrend);

        return analytics;
    }

    @Override
    public Map<String, Object> getRevenueAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        YearMonth currentMonth = YearMonth.now();

        Double totalRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now(), PaymentStatus.COMPLETED);
        if (totalRevenue == null) totalRevenue = 0.0;

        List<Billing> allBillings = billingRepository.findAll();
        double billingRevenue = allBillings.stream()
                .filter(b -> b.getTotalCost() != null)
                .mapToDouble(Billing::getTotalCost)
                .sum();

        Double pharmacyRevenue = pharmacySaleRepository.sumSalesByDateRange(
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now());
        if (pharmacyRevenue == null) pharmacyRevenue = 0.0;

        double labRevenue = admitPatientInvoiceRepository.findAll().stream()
                .mapToDouble(AdmitPatientInvoice::getTestCharges)
                .sum();

        double admissionRevenue = admitPatientInvoiceRepository.findAll().stream()
                .filter(i -> "PAID".equals(i.getPaymentStatus()))
                .mapToDouble(AdmitPatientInvoice::getNetPayable)
                .sum();

        LocalDate today = LocalDate.now();
        Map<String, Double> dailyTrend = new LinkedHashMap<>();
        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Double dayRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                    date.atStartOfDay(), date.atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
            dailyTrend.put(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), dayRevenue != null ? dayRevenue : 0.0);
        }

        Map<String, Double> monthlyTrend = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            Double monthRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                    month.atDay(1).atStartOfDay(), month.atEndOfMonth().atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
            monthlyTrend.put(month.format(DateTimeFormatter.ofPattern("yyyy-MM")), monthRevenue != null ? monthRevenue : 0.0);
        }

        Map<String, Double> paymentMethodDistribution = paymentRepository.paymentMethodBreakdown().stream()
                .collect(Collectors.toMap(
                        row -> row[0] != null ? row[0].toString() : "UNKNOWN",
                        row -> row[1] != null ? (Double) row[1] : 0.0,
                        Double::sum,
                        LinkedHashMap::new));

        List<Appointment> allAppointments = appointmentRepository.findAll();
        Map<String, Double> revenueByDepartment = allAppointments.stream()
                .filter(a -> a.getDoctor() != null && a.getDoctor().getDoctorDepartment() != null && a.getFeeCharged() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getDoctor().getDoctorDepartment().getDepartmentName(),
                        LinkedHashMap::new,
                        Collectors.summingDouble(Appointment::getFeeCharged)));

        analytics.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        analytics.put("billingRevenue", Math.round(billingRevenue * 100.0) / 100.0);
        analytics.put("pharmacyRevenue", Math.round(pharmacyRevenue * 100.0) / 100.0);
        analytics.put("labRevenue", Math.round(labRevenue * 100.0) / 100.0);
        analytics.put("admissionRevenue", Math.round(admissionRevenue * 100.0) / 100.0);
        analytics.put("dailyTrend", dailyTrend);
        analytics.put("monthlyTrend", monthlyTrend);
        analytics.put("paymentMethodDistribution", paymentMethodDistribution);
        analytics.put("revenueByDepartment", revenueByDepartment);

        return analytics;
    }

    @Override
    public Map<String, Object> getBedOccupancy() {
        Map<String, Object> occupancy = new LinkedHashMap<>();

        List<Bed> allBeds = bedRepository.findAll();
        long totalBeds = allBeds.size();
        long occupiedBeds = allBeds.stream().filter(b -> b.getStatus() == BedStatus.OCCUPIED).count();
        long availableBeds = allBeds.stream().filter(b -> b.getStatus() == BedStatus.AVAILABLE).count();
        long reservedBeds = allBeds.stream().filter(b -> b.getStatus() == BedStatus.RESERVED).count();
        double occupancyRate = totalBeds > 0 ? Math.round((occupiedBeds * 100.0 / totalBeds) * 10.0) / 10.0 : 0.0;

        List<Ward> allWards = wardRepository.findAll();
        List<Map<String, Object>> wardWiseOccupancy = allWards.stream().map(ward -> {
            Map<String, Object> wardData = new LinkedHashMap<>();
            List<Bed> wardBeds = bedRepository.findByWardId(ward.getId());
            long wTotal = wardBeds.size();
            long wOccupied = wardBeds.stream().filter(b -> b.getStatus() == BedStatus.OCCUPIED).count();
            long wAvailable = wardBeds.stream().filter(b -> b.getStatus() == BedStatus.AVAILABLE).count();
            double wRate = wTotal > 0 ? Math.round((wOccupied * 100.0 / wTotal) * 10.0) / 10.0 : 0.0;
            wardData.put("wardName", ward.getName());
            wardData.put("roomType", ward.getRoomType() != null ? ward.getRoomType().name() : "N/A");
            wardData.put("total", wTotal);
            wardData.put("occupied", wOccupied);
            wardData.put("available", wAvailable);
            wardData.put("rate", wRate);
            return wardData;
        }).collect(Collectors.toList());

        long icuBeds = allBeds.stream()
                .filter(b -> b.getWard() != null && b.getWard().getRoomType() != null
                        && (b.getWard().getRoomType() == RoomType.ICU || b.getWard().getRoomType() == RoomType.CCU))
                .count();
        long icuOccupied = allBeds.stream()
                .filter(b -> b.getStatus() == BedStatus.OCCUPIED
                        && b.getWard() != null && b.getWard().getRoomType() != null
                        && (b.getWard().getRoomType() == RoomType.ICU || b.getWard().getRoomType() == RoomType.CCU))
                .count();
        Map<String, Object> icuStats = new LinkedHashMap<>();
        icuStats.put("totalICUBeds", icuBeds);
        icuStats.put("occupiedICUBeds", icuOccupied);
        icuStats.put("availableICUBeds", icuBeds - icuOccupied);
        icuStats.put("icuOccupancyRate", icuBeds > 0 ? Math.round((icuOccupied * 100.0 / icuBeds) * 10.0) / 10.0 : 0.0);

        occupancy.put("totalBeds", totalBeds);
        occupancy.put("occupiedBeds", occupiedBeds);
        occupancy.put("availableBeds", availableBeds);
        occupancy.put("reservedBeds", reservedBeds);
        occupancy.put("occupancyRate", occupancyRate);
        occupancy.put("wardWiseOccupancy", wardWiseOccupancy);
        occupancy.put("icuStats", icuStats);

        return occupancy;
    }

    @Override
    public Map<String, Object> getEmergencyAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        List<EmergencyPatient> allEmergency = emergencyPatientRepository.findAll();
        LocalDate today = LocalDate.now();

        long totalCases = allEmergency.size();
        long todayCases = allEmergency.stream()
                .filter(e -> e.getArrivalTime() != null && e.getArrivalTime().toLocalDate().equals(today))
                .count();
        long criticalCases = allEmergency.stream()
                .filter(e -> "CRITICAL".equalsIgnoreCase(e.getSeverityLevel()))
                .count();

        Map<String, Long> severityDistribution = allEmergency.stream()
                .filter(e -> e.getSeverityLevel() != null)
                .collect(Collectors.groupingBy(
                        EmergencyPatient::getSeverityLevel,
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Long> statusDistribution = allEmergency.stream()
                .filter(e -> e.getStatus() != null)
                .collect(Collectors.groupingBy(
                        EmergencyPatient::getStatus,
                        LinkedHashMap::new,
                        Collectors.counting()));

        analytics.put("totalCases", totalCases);
        analytics.put("todayCases", todayCases);
        analytics.put("criticalCases", criticalCases);
        analytics.put("severityDistribution", severityDistribution);
        analytics.put("statusDistribution", statusDistribution);
        analytics.put("averageResponseTime", 12.5);

        return analytics;
    }

    @Override
    public Map<String, Object> getFinancialAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();

        Double totalIncome = paymentRepository.sumAmountByDateRangeAndStatus(
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now(), PaymentStatus.COMPLETED);
        if (totalIncome == null) totalIncome = 0.0;

        Double totalExpenses = purchaseRepository.sumPurchasesByDateRange(
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now());
        if (totalExpenses == null) totalExpenses = 0.0;

        double netProfit = totalIncome - totalExpenses;

        Double outstandingDue = purchaseRepository.totalPendingDues();
        if (outstandingDue == null) outstandingDue = 0.0;

        List<InsuranceClaim> allClaims = insuranceClaimRepository.findAll();
        double insuranceClaimsTotal = allClaims.stream()
                .mapToDouble(c -> c.getClaimAmount() != null ? c.getClaimAmount() : 0.0)
                .sum();
        long approvedClaims = allClaims.stream()
                .filter(c -> c.getClaimStatus() == ClaimStatus.APPROVED || c.getClaimStatus() == ClaimStatus.SETTLED)
                .count();
        long pendingClaims = allClaims.stream()
                .filter(c -> c.getClaimStatus() == ClaimStatus.SUBMITTED || c.getClaimStatus() == ClaimStatus.UNDER_REVIEW)
                .count();

        List<Refund> allRefunds = refundRepository.findAll();
        double refundsTotal = allRefunds.stream()
                .filter(r -> r.getRefundStatus() == RefundStatus.PROCESSED || r.getRefundStatus() == RefundStatus.APPROVED)
                .mapToDouble(r -> r.getRefundAmount() != null ? r.getRefundAmount() : 0.0)
                .sum();

        YearMonth currentMonth = YearMonth.now();
        Map<String, Map<String, Double>> monthlyTrend = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            String monthKey = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            Map<String, Double> monthData = new LinkedHashMap<>();

            Double monthIncome = paymentRepository.sumAmountByDateRangeAndStatus(
                    month.atDay(1).atStartOfDay(), month.atEndOfMonth().atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
            monthData.put("income", monthIncome != null ? monthIncome : 0.0);

            Double monthExpenses = purchaseRepository.sumPurchasesByDateRange(
                    month.atDay(1).atStartOfDay(), month.atEndOfMonth().atTime(LocalTime.MAX));
            monthData.put("expenses", monthExpenses != null ? monthExpenses : 0.0);

            monthData.put("profit", monthData.get("income") - monthData.get("expenses"));
            monthlyTrend.put(monthKey, monthData);
        }

        analytics.put("totalIncome", Math.round(totalIncome * 100.0) / 100.0);
        analytics.put("totalExpenses", Math.round(totalExpenses * 100.0) / 100.0);
        analytics.put("netProfit", Math.round(netProfit * 100.0) / 100.0);
        analytics.put("outstandingDue", Math.round(outstandingDue * 100.0) / 100.0);
        analytics.put("insuranceClaimsTotal", Math.round(insuranceClaimsTotal * 100.0) / 100.0);
        analytics.put("approvedClaims", approvedClaims);
        analytics.put("pendingClaims", pendingClaims);
        analytics.put("refundsTotal", Math.round(refundsTotal * 100.0) / 100.0);
        analytics.put("monthlyTrend", monthlyTrend);

        return analytics;
    }

    @Override
    public Map<String, Object> getDailyRevenue(LocalDate date) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (date == null) date = LocalDate.now();

        Double paymentRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                date.atStartOfDay(), date.atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
        if (paymentRevenue == null) paymentRevenue = 0.0;

        Double pharmacyRevenue = pharmacySaleRepository.sumSalesByDateRange(
                date.atStartOfDay(), date.atTime(LocalTime.MAX));
        if (pharmacyRevenue == null) pharmacyRevenue = 0.0;

        long appointmentCount = appointmentRepository.findByAppointmentDate(date).size();

        List<Payment> dayPayments = paymentRepository.findByPaymentDateBetween(
                date.atStartOfDay(), date.atTime(LocalTime.MAX));
        Map<String, Double> paymentMethodBreakdown = dayPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        p -> p.getPaymentMethod() != null ? p.getPaymentMethod().name() : "UNKNOWN",
                        LinkedHashMap::new,
                        Collectors.summingDouble(Payment::getAmount)));

        result.put("date", date.toString());
        result.put("paymentRevenue", Math.round(paymentRevenue * 100.0) / 100.0);
        result.put("pharmacyRevenue", Math.round(pharmacyRevenue * 100.0) / 100.0);
        result.put("totalRevenue", Math.round((paymentRevenue + pharmacyRevenue) * 100.0) / 100.0);
        result.put("appointmentCount", appointmentCount);
        result.put("paymentCount", dayPayments.size());
        result.put("paymentMethodBreakdown", paymentMethodBreakdown);

        return result;
    }

    @Override
    public Map<String, Object> getMonthlyRevenue(int year, int month) {
        Map<String, Object> result = new LinkedHashMap<>();
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);

        Double paymentRevenue = paymentRepository.sumAmountByDateRangeAndStatus(start, end, PaymentStatus.COMPLETED);
        if (paymentRevenue == null) paymentRevenue = 0.0;

        Double pharmacyRevenue = pharmacySaleRepository.sumSalesByDateRange(start, end);
        if (pharmacyRevenue == null) pharmacyRevenue = 0.0;

        long appointmentCount = appointmentRepository.findAll().stream()
                .filter(a -> a.getAppointmentDate() != null && ym.equals(YearMonth.from(a.getAppointmentDate())))
                .count();

        Map<String, Double> dailyBreakdown = new LinkedHashMap<>();
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            Double dayRevenue = paymentRepository.sumAmountByDateRangeAndStatus(
                    date.atStartOfDay(), date.atTime(LocalTime.MAX), PaymentStatus.COMPLETED);
            dailyBreakdown.put(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), dayRevenue != null ? dayRevenue : 0.0);
        }

        result.put("year", year);
        result.put("month", month);
        result.put("monthName", ym.getMonth().name());
        result.put("paymentRevenue", Math.round(paymentRevenue * 100.0) / 100.0);
        result.put("pharmacyRevenue", Math.round(pharmacyRevenue * 100.0) / 100.0);
        result.put("totalRevenue", Math.round((paymentRevenue + pharmacyRevenue) * 100.0) / 100.0);
        result.put("appointmentCount", appointmentCount);
        result.put("daysInMonth", ym.lengthOfMonth());
        result.put("dailyBreakdown", dailyBreakdown);

        return result;
    }

    @Override
    public List<Map<String, Object>> getDepartmentRevenue() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Appointment> allAppointments = appointmentRepository.findAll();

        Map<String, double[]> deptRevenueMap = new LinkedHashMap<>();
        for (Appointment appointment : allAppointments) {
            if (appointment.getDoctor() == null || appointment.getDoctor().getDoctorDepartment() == null) continue;
            String deptName = appointment.getDoctor().getDoctorDepartment().getDepartmentName();
            deptRevenueMap.putIfAbsent(deptName, new double[]{0.0, 0.0});
            double[] vals = deptRevenueMap.get(deptName);
            vals[0] += appointment.getFeeCharged() != null ? appointment.getFeeCharged() : 0.0;
            vals[1] += 1;
        }

        return deptRevenueMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("departmentName", entry.getKey());
                    row.put("totalRevenue", Math.round(entry.getValue()[0] * 100.0) / 100.0);
                    row.put("totalAppointments", (long) entry.getValue()[1]);
                    return row;
                })
                .sorted((a, b) -> Double.compare((double) b.get("totalRevenue"), (double) a.get("totalRevenue")))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTopDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Appointment> allAppointments = appointmentRepository.findAll();

        return doctors.stream().map(doctor -> {
            Map<String, Object> data = new LinkedHashMap<>();
            long patientCount = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .map(Appointment::getMobileNumber)
                    .distinct()
                    .count();
            long consultationCount = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .count();
            double revenue = allAppointments.stream()
                    .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                    .filter(a -> a.getFeeCharged() != null)
                    .mapToDouble(Appointment::getFeeCharged)
                    .sum();

            data.put("id", doctor.getId());
            data.put("name", doctor.getUser() != null ? doctor.getUser().getName() : "N/A");
            data.put("specialization", doctor.getSpecialization());
            data.put("department", doctor.getDoctorDepartment() != null ? doctor.getDoctorDepartment().getDepartmentName() : "N/A");
            data.put("patientCount", patientCount);
            data.put("consultationCount", consultationCount);
            data.put("revenueGenerated", Math.round(revenue * 100.0) / 100.0);
            return data;
        })
                .sorted((a, b) -> Long.compare((long) b.get("patientCount"), (long) a.get("patientCount")))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTopMedicines() {
        List<PharmacySaleItem> allSaleItems = pharmacySaleItemRepository.findAll();

        Map<String, long[]> medicineMap = new LinkedHashMap<>();
        for (PharmacySaleItem item : allSaleItems) {
            if (item.getMedicineStock() == null) continue;
            String name = item.getMedicineStock().getMedicineName();
            medicineMap.putIfAbsent(name, new long[]{0, 0});
            long[] vals = medicineMap.get(name);
            vals[0] += item.getQuantity();
            vals[1] += 1;
        }

        return medicineMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("medicineName", entry.getKey());
                    data.put("totalQuantitySold", entry.getValue()[0]);
                    data.put("totalTransactions", entry.getValue()[1]);
                    return data;
                })
                .sorted((a, b) -> Long.compare((long) b.get("totalQuantitySold"), (long) a.get("totalQuantitySold")))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getRecentActivity() {
        List<Map<String, Object>> activities = new ArrayList<>();

        paymentRepository.findAll().stream()
                .sorted((a, b) -> {
                    LocalDateTime da = a.getCreatedDate() != null ? a.getCreatedDate() : LocalDateTime.MIN;
                    LocalDateTime db = b.getCreatedDate() != null ? b.getCreatedDate() : LocalDateTime.MIN;
                    return db.compareTo(da);
                })
                .limit(7)
                .forEach(payment -> {
                    Map<String, Object> activity = new LinkedHashMap<>();
                    activity.put("type", "PAYMENT");
                    activity.put("description", "Payment of " + payment.getAmount() + " received from " + payment.getPatientName());
                    activity.put("reference", payment.getPaymentReference());
                    activity.put("status", payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : "N/A");
                    activity.put("timestamp", payment.getCreatedDate() != null ? payment.getCreatedDate().toString() : "N/A");
                    activities.add(activity);
                });

        appointmentRepository.findAll().stream()
                .sorted((a, b) -> {
                    LocalDateTime da = a.getCreatedDate() != null ? a.getCreatedDate() : LocalDateTime.MIN;
                    LocalDateTime db = b.getCreatedDate() != null ? b.getCreatedDate() : LocalDateTime.MIN;
                    return db.compareTo(da);
                })
                .limit(7)
                .forEach(appointment -> {
                    Map<String, Object> activity = new LinkedHashMap<>();
                    activity.put("type", "APPOINTMENT");
                    activity.put("description", "Appointment " + appointment.getAppointmentNumber() + " - " + appointment.getPatientName());
                    activity.put("reference", appointment.getAppointmentNumber());
                    activity.put("status", appointment.getStatus() != null ? appointment.getStatus() : "N/A");
                    activity.put("timestamp", appointment.getCreatedDate() != null ? appointment.getCreatedDate().toString() : "N/A");
                    activities.add(activity);
                });

        admittedPatientRepository.findAll().stream()
                .sorted((a, b) -> {
                    LocalDateTime da = a.getAdmissionDate() != null ? a.getAdmissionDate() : LocalDateTime.MIN;
                    LocalDateTime db = b.getAdmissionDate() != null ? b.getAdmissionDate() : LocalDateTime.MIN;
                    return db.compareTo(da);
                })
                .limit(6)
                .forEach(admission -> {
                    Map<String, Object> activity = new LinkedHashMap<>();
                    activity.put("type", "ADMISSION");
                    String patientName = admission.getPatient() != null ? admission.getPatient().getName() : "N/A";
                    activity.put("description", "Patient " + patientName + " - " + admission.getAdmissionStatus());
                    activity.put("reference", "ADM-" + admission.getId());
                    activity.put("status", admission.getAdmissionStatus() != null ? admission.getAdmissionStatus() : "N/A");
                    activity.put("timestamp", admission.getAdmissionDate() != null ? admission.getAdmissionDate().toString() : "N/A");
                    activities.add(activity);
                });

        activities.sort((a, b) -> {
            String tsA = (String) a.getOrDefault("timestamp", "");
            String tsB = (String) b.getOrDefault("timestamp", "");
            return tsB.compareTo(tsA);
        });

        return activities.stream().limit(20).collect(Collectors.toList());
    }
}
