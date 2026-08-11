package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.AppointmentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setAppointmentNumber(appointment.getAppointmentNumber());
        response.setStatus(appointment.getStatus());

        // Map landing page form data/guest parameters
        response.setPatientName(appointment.getPatientName());
        response.setMobileNumber(appointment.getMobileNumber());
        response.setSpecialization(appointment.getSpecialization());

//        -----------------------------------------
//        response.setName(appointment.getName());
//        response.setPhone(appointment.getPhone());

        // Map core appointment parameters
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setProblemDescription(appointment.getProblemDescription());
        response.setFeeCharged(appointment.getFeeCharged()); // Synced with entity field

        // Map financial gateway metrics
        response.setPaymentMethod(appointment.getPaymentMethod());
        response.setTransactionId(appointment.getTransactionId());

        // Map optional registered patient entity context safely
        if (appointment.getPatient() != null) {
            response.setRegisteredPatientId(appointment.getPatient().getId());
            // Overwrite with full name if profile exists
            response.setPatientName(appointment.getPatient().getName());
        }

        // Map core assigned doctor entity context safely
        if (appointment.getDoctor() != null) {
            response.setDoctorId(appointment.getDoctor().getId());
            response.setDoctorName(appointment.getDoctor().getUser().getName());
            response.setDoctorSpecialization(appointment.getDoctor().getSpecialization());
            response.setDoctorChamber(appointment.getDoctor().getChamber());
        }

        // Map unique reference slot boundary safely
        if (appointment.getScheduleSlot() != null) {
            response.setScheduleSlotId(appointment.getScheduleSlot().getId());
            response.setSlotIsBooked(appointment.getScheduleSlot().getIsBooked());
        }

        return response;
    }
}
