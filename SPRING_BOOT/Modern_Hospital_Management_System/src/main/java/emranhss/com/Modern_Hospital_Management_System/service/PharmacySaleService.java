package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PharmacySaleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleResponse;

public interface PharmacySaleService {
    PharmacySaleResponse processMedicineSale(PharmacySaleRequest request);
}
