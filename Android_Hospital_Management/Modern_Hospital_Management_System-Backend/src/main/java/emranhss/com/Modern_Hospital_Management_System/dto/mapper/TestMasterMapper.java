package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.TestMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.TestMaster;
import org.springframework.stereotype.Component;

@Component
public class TestMasterMapper {


    public TestMasterResponse toResponse(TestMaster test){

        TestMasterResponse response = new TestMasterResponse();

        response.setId(test.getId());
        response.setTestCode(test.getTestCode());
        response.setTestName(test.getTestName());
        response.setStandardPrice(test.getStandardPrice());
        response.setNormalRange(test.getNormalRange());
        response.setActive(test.getActive());

        return response;
    }

}
