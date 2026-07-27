package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.PatientDietAlert;
import java.util.List;

public interface PatientDietAlertService {
    PatientDietAlert create(PatientDietAlert alert);
    PatientDietAlert getById(Long id);
    List<PatientDietAlert> getAll();
    List<PatientDietAlert> getActive();
    List<PatientDietAlert> getByPatientId(Long patientId);
    List<PatientDietAlert> getByAlertType(String alertType);
    PatientDietAlert updateStatus(Long id, String status);
    void delete(Long id);
    long getActiveCount();
}
