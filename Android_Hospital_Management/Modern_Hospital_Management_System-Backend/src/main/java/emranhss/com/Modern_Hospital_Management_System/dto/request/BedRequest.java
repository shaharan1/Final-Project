package emranhss.com.Modern_Hospital_Management_System.dto.request;


import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;
import lombok.Data;

import java.util.Set;

@Data
public class BedRequest {


    private String bedNumber;
    private Long wardId;
    private BedStatus status;
    private Set<Long> facilityIds;
}
