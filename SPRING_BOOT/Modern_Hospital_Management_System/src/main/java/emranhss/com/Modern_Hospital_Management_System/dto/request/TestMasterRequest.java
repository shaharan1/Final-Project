package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.Data;

@Data
public class TestMasterRequest {

    private String testCode;
    private String testName;
    private double standardPrice;
    private String normalRange;
}
