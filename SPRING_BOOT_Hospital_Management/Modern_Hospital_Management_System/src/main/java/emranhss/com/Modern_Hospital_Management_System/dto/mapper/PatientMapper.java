package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.request.PatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PatientResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {


    public Patient toEntity(PatientRequest request) {
        if (request == null) return null;
        Patient patient = new Patient();
        BeanUtils.copyProperties(request, patient);
        return patient;
    }

    public PatientResponse toResponse(Patient patient) {
        if (patient == null) return null;
        PatientResponse response = new PatientResponse();
        BeanUtils.copyProperties(patient, response);
        return response;
    }

    public void updateEntityFromRequest(PatientRequest request, Patient patient) {
        if (request == null || patient == null) return;
        BeanUtils.copyProperties(request, patient, "id", "patientCode");
    }
}
