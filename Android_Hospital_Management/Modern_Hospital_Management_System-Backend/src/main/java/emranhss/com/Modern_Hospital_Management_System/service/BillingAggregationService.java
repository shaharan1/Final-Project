package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.ChargeCategory;
import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoice;
import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoiceItem;

import java.util.List;

public interface BillingAggregationService {

    List<BillingInvoiceItem> aggregateWardCharges(BillingInvoice invoice, Long admittedPatientId);

    List<BillingInvoiceItem> aggregateDoctorCharges(BillingInvoice invoice, Long admittedPatientId);

    List<BillingInvoiceItem> aggregatePharmacyCharges(BillingInvoice invoice, Long patientId, Long admittedPatientId);

    List<BillingInvoiceItem> aggregateLabCharges(BillingInvoice invoice, Long patientId, Long admittedPatientId);

    List<BillingInvoiceItem> aggregateDietCharges(BillingInvoice invoice, Long patientId, Long admittedPatientId);

    List<BillingInvoiceItem> aggregateOtherCharges(BillingInvoice invoice, Long admittedPatientId);

    List<BillingInvoiceItem> aggregateAll(BillingInvoice invoice, Long patientId, Long admittedPatientId);
}
