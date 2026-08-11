package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationTheatreResponse {
    private Long id;
    private String otCode;
    private String otName;
    private String location;
    private String equipmentAvailable;
    private Integer capacity;
    private String status;
    private Boolean active;
}
