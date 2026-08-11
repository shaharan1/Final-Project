package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryScheduleResponse {

    private Long surgeryId;
    private String surgeryNumber;
    private String surgeryName;
    private Long operationTheatreId;
    private String operationTheatreName;
    private Long patientId;
    private String patientName;
    private String patientCode;
    private Long surgeonId;
    private String surgeonName;
    private LocalDate surgeryDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;
    private String status;
}
