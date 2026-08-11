package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.InvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.InvoiceResponse;

public interface InvoiceService {


    InvoiceResponse generateInvoice(InvoiceRequest request);
    InvoiceResponse getInvoiceById(Long id);

}
