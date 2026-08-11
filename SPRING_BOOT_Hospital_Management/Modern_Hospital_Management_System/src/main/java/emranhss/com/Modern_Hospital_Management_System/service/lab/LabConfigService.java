package emranhss.com.Modern_Hospital_Management_System.service.lab;

import emranhss.com.Modern_Hospital_Management_System.dto.request.InterpretationRuleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabRuleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.ReferenceRangeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.TestParameterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.*;

import java.util.List;

public interface LabConfigService {

    TestMasterDetailResponse getTestMasterDetail(Long testMasterId);

    List<TestParameterResponse> getAllParameters();

    TestParameterResponse getParameter(Long id);

    TestParameterResponse createParameter(TestParameterRequest request);

    TestParameterResponse updateParameter(Long id, TestParameterRequest request);

    void deleteParameter(Long id);

    ReferenceRangeResponse createRange(ReferenceRangeRequest request);

    ReferenceRangeResponse updateRange(Long id, ReferenceRangeRequest request);

    void deleteRange(Long id);

    InterpretationRuleResponse createRule(InterpretationRuleRequest request);

    InterpretationRuleResponse updateRule(Long id, InterpretationRuleRequest request);

    void deleteRule(Long id);

    List<LabRuleResponse> getAllLabRules();

    LabRuleResponse createLabRule(LabRuleRequest request);

    LabRuleResponse updateLabRule(Long id, LabRuleRequest request);

    void deleteLabRule(Long id);
}
