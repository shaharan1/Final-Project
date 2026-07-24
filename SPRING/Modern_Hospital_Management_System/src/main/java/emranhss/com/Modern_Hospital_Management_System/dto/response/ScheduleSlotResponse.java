package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleSlotResponse {


    private Long id;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isBooked;
}
