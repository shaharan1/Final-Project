package emranhss.com.Modern_Hospital_Management_System.dto.response;


import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;
import lombok.Data;

import java.util.List;

@Data
public class BedResponse {


    private Long id;
    private String bedNumber;
    private Long wardId;
    private String wardName;
    private String roomType;
    private BedStatus status;
    private Double totalDailyCost; // Base Ward Fee + Individual Assigned Facility Charges
    private List<FacilityResponse> facilities;
}
