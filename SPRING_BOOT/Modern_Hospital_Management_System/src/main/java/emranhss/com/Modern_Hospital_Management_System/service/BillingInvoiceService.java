package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingPaymentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingDashboardSummaryResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingPaymentResponse;

import java.util.List;

public interface BillingInvoiceService {

    BillingInvoiceResponse createInvoice(BillingInvoiceRequest request);

    BillingInvoiceResponse getById(Long id);

    BillingInvoiceResponse getByInvoiceNumber(String invoiceNumber);

    List<BillingInvoiceResponse> getAllInvoices();

    List<BillingInvoiceResponse> getByPatientId(Long patientId);

    List<BillingInvoiceResponse> getByAdmittedPatientId(Long admittedPatientId);

    List<BillingInvoiceResponse> searchInvoices(String search);

    BillingInvoiceResponse updateInvoice(Long id, BillingInvoiceRequest request);

    BillingInvoiceResponse addItem(Long invoiceId, BillingInvoiceRequest.BillingInvoiceItemRequest item);

    BillingInvoiceResponse removeItem(Long invoiceId, Long itemId);

    BillingInvoiceResponse finalizeInvoice(Long id, String finalizedBy);

    BillingInvoiceResponse cancelInvoice(Long id);

    BillingInvoiceResponse syncFromModules(Long admittedPatientId);

    BillingPaymentResponse processPayment(BillingPaymentRequest request);

    List<BillingPaymentResponse> getPaymentsByInvoiceId(Long invoiceId);

    BillingDashboardSummaryResponse getDashboardSummary();

    byte[] generatePdf(Long id);
}
