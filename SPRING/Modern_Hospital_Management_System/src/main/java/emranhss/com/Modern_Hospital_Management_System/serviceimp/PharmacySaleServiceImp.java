package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PharmacySaleItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PharmacySaleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.PharmacySaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PharmacySaleServiceImp implements PharmacySaleService {

    private final PharmacySaleRepository pharmacySaleRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final BillingRepository billingRepository; // Injecting your existing billing repository

    @Override
    @Transactional
    public PharmacySaleResponse processMedicineSale(PharmacySaleRequest request) {
        PharmacySale sale = new PharmacySale();
        sale.setSaleInvoiceNo("PHM-INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setPatientType(request.getPatientType());

        double runningTotal = 0.0;
        Billing targetBilling = null;

        // 1. Process and attach your existing Billing ledger if the patient is an Inpatient
        if ("INPATIENT".equalsIgnoreCase(request.getPatientType())) {
            targetBilling = billingRepository.findById(request.getBillingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Active billing account not found with ID: " + request.getBillingId()));
            sale.setBilling(targetBilling);
            sale.setPaymentStatus("PENDING_BILLING"); // Cost will be collected at checkout clearance stage
        } else {
            sale.setPaymentStatus("PAID"); // Direct retail outpatient cash settlement checkout
        }

        // 2. Loop through requested medicines to perform stock verification and price calculation
        for (PharmacySaleItemRequest itemReq : request.getItems()) {
            MedicineStock stock = medicineStockRepository.findById(itemReq.getMedicineStockId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medicine stock not found with ID: " + itemReq.getMedicineStockId()));

            // Core Validation Rule: Ensure adequate stock quantity is present
            if (stock.getStockQuantity() < itemReq.getQuantity()) {
                throw new IllegalStateException("Insufficient stock level for: " + stock.getMedicineName() + ". Available: " + stock.getStockQuantity());
            }

            // Deduct sold quantity from the medicine inventory tracker statefully
            stock.setStockQuantity(stock.getStockQuantity() - itemReq.getQuantity());
            medicineStockRepository.save(stock);

            // Compute prices using the entity's salePrice field
            PharmacySaleItem saleItem = new PharmacySaleItem();
            saleItem.setPharmacySale(sale);
            saleItem.setMedicineStock(stock);
            saleItem.setQuantity(itemReq.getQuantity());
            saleItem.setUnitPrice(stock.getSalePrice());

            double itemSubtotal = stock.getSalePrice() * itemReq.getQuantity();
            saleItem.setSubtotal(itemSubtotal);
            runningTotal += itemSubtotal;

            sale.getSaleItems().add(saleItem);
        }

        // 3. Complete main invoice financial deductions calculations
        double discountAmt = request.getDiscount() != null ? request.getDiscount() : 0.0;
        sale.setTotalAmount(runningTotal);
        sale.setDiscount(discountAmt);
        sale.setNetPayable(runningTotal - discountAmt);

        // 4. TRIGGER AUTOMATION: Update your existing Billing record's medicineCost dynamically
        if (targetBilling != null) {
            double currentMedicineCost = targetBilling.getMedicineCost() != null ? targetBilling.getMedicineCost() : 0.0;
            targetBilling.setMedicineCost(currentMedicineCost + sale.getNetPayable());
            targetBilling.calculateTotalCost(); // Execute your built-in total recalculation logic helper
            billingRepository.save(targetBilling);
        }

        PharmacySale savedSale = pharmacySaleRepository.save(sale);

        // 5. Build and return sanitized response payload DTO structure
        PharmacySaleResponse response = new PharmacySaleResponse();
        response.setId(savedSale.getId());
        response.setSaleInvoiceNo(savedSale.getSaleInvoiceNo());
        response.setPatientType(savedSale.getPatientType());
        response.setTotalAmount(savedSale.getTotalAmount());
        response.setDiscount(savedSale.getDiscount());
        response.setNetPayable(savedSale.getNetPayable());
        response.setPaymentStatus(savedSale.getPaymentStatus());
        response.setSaleDate(savedSale.getSaleDate());
        if (savedSale.getBilling() != null) {
            response.setBillingId(savedSale.getBilling().getId());
        }

        return response;
    }
}
