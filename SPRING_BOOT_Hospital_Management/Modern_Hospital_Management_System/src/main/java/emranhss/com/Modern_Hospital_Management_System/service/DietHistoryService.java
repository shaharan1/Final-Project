package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.DietHistory;
import java.util.List;

public interface DietHistoryService {
    DietHistory create(DietHistory history);
    List<DietHistory> getAll();
    List<DietHistory> getByPatientId(Long patientId);
    List<DietHistory> getByDietAssignmentId(Long assignmentId);
}
