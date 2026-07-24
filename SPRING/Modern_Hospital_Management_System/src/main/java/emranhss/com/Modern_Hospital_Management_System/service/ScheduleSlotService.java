package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ScheduleSlotRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.ScheduleSlotResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleSlotService {
    ScheduleSlotResponse createSlot(ScheduleSlotRequest request);
    List<ScheduleSlotResponse> getAvailableSlotsByDoctor(Long doctorId, LocalDate date);
    ScheduleSlotResponse toggleSlotBookingStatus(Long id, Boolean isBooked);

    //  show all schedule slots
    List<ScheduleSlotResponse> getAllSlots();


    // Find a specific slot
    List<ScheduleSlotResponse> findByDoctorDateAndStartTime(
            Long doctorId,
            LocalDate date,
            LocalTime startTime
    );

    // Find all available slots
    List<ScheduleSlotResponse> findAvailableSlots(
            Long doctorId,
            LocalDate date
    );
    // Find all available slots
    List<ScheduleSlotResponse> findBookedSlots(
            Long doctorId,
            LocalDate date
    );


}
