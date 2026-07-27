package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.DietAssignment;
import java.util.List;

public interface DietAssignmentService {
    DietAssignment create(DietAssignment assignment);
    DietAssignment getById(Long id);
    List<DietAssignment> getAll();
    List<DietAssignment> getByStatus(String status);
    List<DietAssignment> getByPatientId(Long patientId);
    List<DietAssignment> getByAdmittedPatientId(Long admittedPatientId);
    List<DietAssignment> getByDieticianId(Long dieticianId);
    List<DietAssignment> getActiveAssignments();
    DietAssignment update(Long id, DietAssignment assignment);
    void delete(Long id);
    long getActiveCount();
}
