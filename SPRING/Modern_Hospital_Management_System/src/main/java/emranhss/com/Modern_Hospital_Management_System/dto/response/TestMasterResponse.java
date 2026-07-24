package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

@Data
public class TestMasterResponse {

    private Long id;

    private String testCode;

    private String testName;

    private double standardPrice;

    private String normalRange;

    private Boolean active;

}
