package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AppointmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AppointmentResponse;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);
    AppointmentResponse cancelAppointment(Long id);
    List<AppointmentResponse> viewSchedule(LocalDate date);
    List<AppointmentResponse> getDoctorWiseAppointments(Long doctorId);
    //    -----------Filter Appointments---------
    List<AppointmentResponse> filterAppointments(Long doctorId, LocalDate date);
    // Functions for dynamic public fee display on checkout
    Double calculateFeeForPhone(String phone, Long doctorId);
    Boolean isReturningPatient(String phone);

    List<AppointmentResponse> getAllAppointments();
    AppointmentResponse getAppointmentById(Long id);

    AppointmentResponse getByAppointmentNumber(String appointmentNumber);





}
