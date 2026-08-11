package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

@Data
public class FacilityResponse {



    private Long id;
    private String name;
    private Double standardCharge;
    private Boolean active;
}
