package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorNotificationResponse {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String patientName;
    private String patientCode;
    private Long testOrderId;
    private String title;
    private String message;
    private String type;
    private String severity;
    private Boolean isRead;
    private LocalDateTime createdDate;
}
