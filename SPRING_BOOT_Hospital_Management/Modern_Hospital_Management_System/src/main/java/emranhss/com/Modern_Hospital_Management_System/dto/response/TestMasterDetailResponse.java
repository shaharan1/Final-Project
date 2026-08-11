package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestMasterDetailResponse {
    private Long id;
    private String testCode;
    private String testName;
    private double standardPrice;
    private String normalRange;
    private Boolean active;
    private List<TestParameterResponse> parameters = new ArrayList<>();
}
