package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingResponse;

public interface BillingService {
    BillingResponse syncAndGetInpatientBilling(Long admittedPatientId);
    BillingResponse closeBillingLedger(Long admittedPatientId);
}
