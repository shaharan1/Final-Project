package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationTheatreRequest {
    private String otCode;
    private String otName;
    private String location;
    private String equipmentAvailable;
    private Integer capacity;
    private String status;
    private Boolean active;
}
