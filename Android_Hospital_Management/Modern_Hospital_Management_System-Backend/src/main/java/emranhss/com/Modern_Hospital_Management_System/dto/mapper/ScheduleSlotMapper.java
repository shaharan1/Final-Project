package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.ScheduleSlotResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.ScheduleSlot;
import org.springframework.stereotype.Component;

@Component
public class ScheduleSlotMapper {


    public ScheduleSlotResponse toResponse(ScheduleSlot slot) {
        if (slot == null) return null;

        ScheduleSlotResponse resp = new ScheduleSlotResponse();
        resp.setId(slot.getId());
        resp.setDate(slot.getDate());
        resp.setStartTime(slot.getStartTime());
        resp.setEndTime(slot.getEndTime());
        resp.setIsBooked(slot.getIsBooked());

        if (slot.getDoctor() != null) {
            resp.setDoctorId(slot.getDoctor().getId());
            resp.setDoctorName(slot.getDoctor().getUser().getName());
            resp.setDoctorSpecialization(slot.getDoctor().getSpecialization());
        }

        return resp;
    }
}
