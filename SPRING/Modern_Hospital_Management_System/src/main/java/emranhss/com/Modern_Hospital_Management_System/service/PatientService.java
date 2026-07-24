package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PatientResponse;

import java.util.List;

public interface PatientService {


    PatientResponse createPatient(PatientRequest request);
    PatientResponse getPatientById(Long id);
    PatientResponse getPatientByCode(String code);
    List<PatientResponse> getAllPatients();
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
}
