package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface SurgeryService {

    SurgeryResponse create(SurgeryRequest request);

    SurgeryResponse getById(Long id);

    List<SurgeryResponse> getAll();

    List<SurgeryResponse> getByPatientId(Long patientId);

    List<SurgeryResponse> getByAdmittedPatientId(Long admittedPatientId);

    List<SurgeryResponse> getByDateRange(LocalDate from, LocalDate to);

    List<SurgeryResponse> getByOperationTheatreAndDate(Long operationTheatreId, LocalDate date);

    List<SurgeryResponse> search(String q);

    SurgeryResponse update(Long id, SurgeryRequest request);

    SurgeryResponse updateStatus(Long id, String status, String cancellationReason);

    void delete(Long id);

    List<SurgeryScheduleResponse> getSchedule(LocalDate date);

    List<SurgeryScheduleResponse> getUpcomingSchedule();
}
