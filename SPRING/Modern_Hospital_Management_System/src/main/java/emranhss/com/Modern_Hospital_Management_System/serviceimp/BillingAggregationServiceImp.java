package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.BillingAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingAggregationServiceImp implements BillingAggregationService {

    private final BedBookingRepository bedBookingRepository;
    private final WardRepository wardRepository;
    private final DoctorChargeRepository doctorChargeRepository;
    private final PharmacySaleRepository pharmacySaleRepository;
    private final PharmacySaleItemRepository pharmacySaleItemRepository;
    private final TestAdmitedPatientRepository testAdmitedPatientRepository;
    private final TestsRepository testsRepository;
    private final DietAssignmentRepository dietAssignmentRepository;
    private final OthersChargeRepository othersChargeRepository;
    private final ChargeCategoryRepository chargeCategoryRepository;
    private final AdmittedPatientRepository admittedPatientRepository;

    private ChargeCategory getCategory(String code) {
        return chargeCategoryRepository.findByCode(code).orElse(null);
    }

    private long computeDays(LocalDateTime start, LocalDateTime end) {
        if (start == null) return 1;
        LocalDateTime endTime = end != null ? end : LocalDateTime.now();
        long hours = Duration.between(start, endTime).toHours();
        long days = (hours / 24) + (hours % 24 > 0 ? 1 : 0);
        return Math.max(1, days);
    }

    private long computeDaysFromDates(LocalDate start, LocalDate end) {
        if (start == null) return 1;
        LocalDate endTime = end != null ? end : LocalDate.now();
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, endTime);
        return Math.max(1, days);
    }

    @Override
    public List<BillingInvoiceItem> aggregateWardCharges(BillingInvoice invoice, Long admittedPatientId) {
        List<BillingInvoiceItem> items = new ArrayList<>();
        if (admittedPatientId == null) return items;

        ChargeCategory category = getCategory("WARD_BED");
        BedBooking activeBooking = bedBookingRepository.findByAdmittedPatientIdAndActiveTrue(admittedPatientId).orElse(null);

        if (activeBooking != null && activeBooking.getBed() != null && activeBooking.getBed().getWard() != null) {
            Ward ward = activeBooking.getBed().getWard();
            long days = computeDays(activeBooking.getStartTime(), null);
            double dailyRate = ward.getBasePricePerDay() != null ? ward.getBasePricePerDay() : 0.0;
            double totalCost = days * dailyRate;

            if (totalCost > 0) {
                BillingInvoiceItem item = new BillingInvoiceItem();
                item.setInvoice(invoice);
                item.setChargeCategory(category);
                item.setCategoryCode("WARD_BED");
                item.setDescription(ward.getName() + " - Bed " + activeBooking.getBed().getId() + " (" + days + " days @ ৳" + dailyRate + "/day)");
                item.setQuantity((int) days);
                item.setUnitPrice(dailyRate);
                item.setDiscountPercent(0.0);
                item.setSourceModule("ADMISSION");
                item.setSourceId(activeBooking.getId());
                item.calculateAmount();
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<BillingInvoiceItem> aggregateDoctorCharges(BillingInvoice invoice, Long admittedPatientId) {
        List<BillingInvoiceItem> items = new ArrayList<>();
        if (admittedPatientId == null) return items;

        ChargeCategory category = getCategory("DOCTOR_CONSULTATION");
        List<DoctorCharge> charges = doctorChargeRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING");

        for (DoctorCharge charge : charges) {
            BillingInvoiceItem item = new BillingInvoiceItem();
            item.setInvoice(invoice);
            item.setChargeCategory(category);
            item.setCategoryCode("DOCTOR_CONSULTATION");

            String doctorName = charge.getDoctor() != null && charge.getDoctor().getUser() != null ? charge.getDoctor().getUser().getName() : "Doctor";
            String visitDate = charge.getVisitDate() != null ? charge.getVisitDate().toString() : "";
            item.setDescription(doctorName + " - " + (charge.getDescription() != null ? charge.getDescription() : "Consultation") + " (" + visitDate + ")");
            item.setQuantity(1);
            item.setUnitPrice(charge.getAmount());
            item.setDiscountPercent(0.0);
            item.setSourceModule("DOCTOR_CHARGE");
            item.setSourceId(charge.getId());
            item.calculateAmount();
            items.add(item);
        }
        return items;
    }

    @Override
    public List<BillingInvoiceItem> aggregatePharmacyCharges(BillingInvoice invoice, Long patientId, Long admittedPatientId) {
        List<BillingInvoiceItem> items = new ArrayList<>();
        if (patientId == null) return items;

        ChargeCategory category = getCategory("MEDICINE");
        List<PharmacySale> sales = pharmacySaleRepository.findAll().stream()
                .filter(s -> patientId.equals(s.getPatientId()))
                .toList();

        for (PharmacySale sale : sales) {
            if (sale.getItems() != null && !sale.getItems().isEmpty()) {
                for (PharmacySaleItem saleItem : sale.getItems()) {
                    BillingInvoiceItem item = new BillingInvoiceItem();
                    item.setInvoice(invoice);
                    item.setChargeCategory(category);
                    item.setCategoryCode("MEDICINE");

                    String medName = saleItem.getMedicineStock() != null ? saleItem.getMedicineStock().getMedicineName() : "Medicine";
                    item.setDescription(medName + " (" + sale.getSaleInvoiceNo() + ")");
                    item.setQuantity(saleItem.getQuantity());
                    item.setUnitPrice(saleItem.getUnitPrice());
                    item.setDiscountPercent(saleItem.getDiscount() > 0 ? (saleItem.getDiscount() / (saleItem.getQuantity() * saleItem.getUnitPrice()) * 100) : 0.0);
                    item.setSourceModule("PHARMACY");
                    item.setSourceId(sale.getId());
                    item.calculateAmount();
                    items.add(item);
                }
            } else {
                BillingInvoiceItem item = new BillingInvoiceItem();
                item.setInvoice(invoice);
                item.setChargeCategory(category);
                item.setCategoryCode("MEDICINE");
                item.setDescription("Pharmacy Sale - " + sale.getSaleInvoiceNo());
                item.setQuantity(1);
                item.setUnitPrice(sale.getNetPayable() != null ? sale.getNetPayable() : 0.0);
                item.setDiscountPercent(0.0);
                item.setSourceModule("PHARMACY");
                item.setSourceId(sale.getId());
                item.calculateAmount();
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<BillingInvoiceItem> aggregateLabCharges(BillingInvoice invoice, Long patientId, Long admittedPatientId) {
        List<BillingInvoiceItem> items = new ArrayList<>();
        if (patientId == null) return items;

        ChargeCategory category = getCategory("LAB_TEST");

        Set<Long> billedTestIds = new java.util.HashSet<>();
        if (admittedPatientId != null) {
            List<TestAdmitedPatient> existingTests = testAdmitedPatientRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING");
            for (TestAdmitedPatient test : existingTests) {
                BillingInvoiceItem item = new BillingInvoiceItem();
                item.setInvoice(invoice);
                item.setChargeCategory(category);
                item.setCategoryCode("LAB_TEST");

                String testName = "";
                double price = test.getBilledAmount();
                if (test.getTestOrder() != null) {
                    if (test.getTestOrder().getTestMaster() != null) {
                        testName = test.getTestOrder().getTestMaster().getTestName();
                        if (price <= 0) price = test.getTestOrder().getTestMaster().getStandardPrice();
                    }
                    billedTestIds.add(test.getTestOrder().getId());
                }
                item.setDescription(testName + " (Test #" + test.getId() + ")");
                item.setQuantity(1);
                item.setUnitPrice(price);
                item.setDiscountPercent(0.0);
                item.setSourceModule("LAB");
                item.setSourceId(test.getId());
                item.calculateAmount();
                if (item.getUnitPrice() > 0) items.add(item);
            }
        }

        List<Tests> patientTests = testsRepository.findByPatientId(patientId);
        for (Tests test : patientTests) {
            if (billedTestIds.contains(test.getId())) continue;
            if (test.getTestMaster() == null) continue;

            double price = test.getTestMaster().getStandardPrice();
            if (price <= 0) continue;

            BillingInvoiceItem item = new BillingInvoiceItem();
            item.setInvoice(invoice);
            item.setChargeCategory(category);
            item.setCategoryCode("LAB_TEST");
            item.setDescription(test.getTestMaster().getTestName() + " (Order #" + test.getId() + ")");
            item.setQuantity(1);
            item.setUnitPrice(price);
            item.setDiscountPercent(0.0);
            item.setSourceModule("LAB");
            item.setSourceId(test.getId());
            item.calculateAmount();
            items.add(item);
        }
        return items;
    }

    @Override
    public List<BillingInvoiceItem> aggregateDietCharges(BillingInvoice invoice, Long patientId, Long admittedPatientId) {
        List<BillingInvoiceItem> items = new ArrayList<>();
        if (patientId == null) return items;

        ChargeCategory category = getCategory("DIET_MEALS");
        List<DietAssignment> diets = new ArrayList<>();

        if (admittedPatientId != null) {
            diets.addAll(dietAssignmentRepository.findByAdmittedPatientId(admittedPatientId).stream()
                    .filter(d -> !"CANCELLED".equals(d.getStatus()))
                    .toList());
        }

        if (diets.isEmpty()) {
            diets.addAll(dietAssignmentRepository.findByPatientId(patientId).stream()
                    .filter(d -> !"CANCELLED".equals(d.getStatus()))
                    .toList());
        }

        for (DietAssignment diet : diets) {
            long days = computeDaysFromDates(diet.getStartDate(), diet.getEndDate());
            double pricePerDay = 0.0;
            if (diet.getDietPlan() != null && diet.getDietPlan().getPricePerDay() != null) {
                pricePerDay = diet.getDietPlan().getPricePerDay();
            }

            if (pricePerDay <= 0) {
                pricePerDay = 500.0;
            }

            String planName = diet.getDietPlan() != null ? diet.getDietPlan().getName() : "Diet Plan";
            String startDate = diet.getStartDate() != null ? diet.getStartDate().toString() : "";
            String endDate = diet.getEndDate() != null ? diet.getEndDate().toString() : "Ongoing";

            BillingInvoiceItem item = new BillingInvoiceItem();
            item.setInvoice(invoice);
            item.setChargeCategory(category);
            item.setCategoryCode("DIET_MEALS");
            item.setDescription(planName + " (" + startDate + " to " + endDate + ")");
            item.setQuantity((int) days);
            item.setUnitPrice(pricePerDay);
            item.setDiscountPercent(0.0);
            item.setSourceModule("DIET");
            item.setSourceId(diet.getId());
            item.calculateAmount();
            items.add(item);
        }
        return items;
    }

    @Override
    public List<BillingInvoiceItem> aggregateOtherCharges(BillingInvoice invoice, Long admittedPatientId) {
        List<BillingInvoiceItem> items = new ArrayList<>();
        if (admittedPatientId == null) return items;

        ChargeCategory category = getCategory("OTHER");
        List<OthersCharge> charges = othersChargeRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING");

        for (OthersCharge charge : charges) {
            BillingInvoiceItem item = new BillingInvoiceItem();
            item.setInvoice(invoice);
            item.setChargeCategory(category);
            item.setCategoryCode("OTHER");
            item.setDescription(charge.getDescription() + " [" + charge.getCategory() + "]");
            item.setQuantity(charge.getQuantity() != null ? charge.getQuantity() : 1);
            item.setUnitPrice(charge.getUnitPrice());
            item.setDiscountPercent(0.0);
            item.setSourceModule("OTHERS");
            item.setSourceId(charge.getId());
            item.calculateAmount();
            items.add(item);
        }
        return items;
    }

    @Override
    public List<BillingInvoiceItem> aggregateAll(BillingInvoice invoice, Long patientId, Long admittedPatientId) {
        List<BillingInvoiceItem> allItems = new ArrayList<>();
        allItems.addAll(aggregateWardCharges(invoice, admittedPatientId));
        allItems.addAll(aggregateDoctorCharges(invoice, admittedPatientId));
        allItems.addAll(aggregatePharmacyCharges(invoice, patientId, admittedPatientId));
        allItems.addAll(aggregateLabCharges(invoice, patientId, admittedPatientId));
        allItems.addAll(aggregateDietCharges(invoice, patientId, admittedPatientId));
        allItems.addAll(aggregateOtherCharges(invoice, admittedPatientId));

        for (BillingInvoiceItem item : allItems) {
            item.setInvoice(invoice);
        }
        return allItems;
    }
}
