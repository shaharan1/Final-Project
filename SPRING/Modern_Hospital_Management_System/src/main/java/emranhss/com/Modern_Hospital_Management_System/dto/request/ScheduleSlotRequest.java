package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleSlotRequest {

    private Long doctorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
