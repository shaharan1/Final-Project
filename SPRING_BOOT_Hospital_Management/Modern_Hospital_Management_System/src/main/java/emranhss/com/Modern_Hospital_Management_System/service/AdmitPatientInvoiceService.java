package emranhss.com.Modern_Hospital_Management_System.service;


import emranhss.com.Modern_Hospital_Management_System.dto.request.AdmitPatientInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AdmitPatientInvoiceResponse;

public interface AdmitPatientInvoiceService {

    AdmitPatientInvoiceResponse calculateDraftInvoice(Long admittedPatientId);
    AdmitPatientInvoiceResponse finalizeAndSaveInvoice(AdmitPatientInvoiceRequest request);

}
