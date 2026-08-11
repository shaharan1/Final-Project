package emranhss.com.Modern_Hospital_Management_System.service;

import com.itextpdf.text.DocumentException;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PrescriptionRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionItemResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionResponse;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import java.io.IOException;

public interface PrescriptionService {

    PrescriptionResponse createPrescription(PrescriptionRequest request);
    PrescriptionResponse getPrescriptionById(Long id);
    List<PrescriptionResponse> getPrescriptionsByPatientId(Long patientId);



    byte[] generatePdf(Long id);
}
