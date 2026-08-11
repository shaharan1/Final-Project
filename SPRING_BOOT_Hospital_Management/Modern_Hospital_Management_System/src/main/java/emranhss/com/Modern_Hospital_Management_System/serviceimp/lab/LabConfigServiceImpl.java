package emranhss.com.Modern_Hospital_Management_System.serviceimp.lab;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.LabReportMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.InterpretationRuleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabRuleRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.ReferenceRangeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.TestParameterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.*;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.ResultType;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.lab.LabConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabConfigServiceImpl implements LabConfigService {

    private final TestMasterRepository testMasterRepository;
    private final TestParameterRepository testParameterRepository;
    private final ReferenceRangeRepository referenceRangeRepository;
    private final InterpretationRuleRepository interpretationRuleRepository;
    private final LabRuleRepository labRuleRepository;

    @Override
    @Transactional(readOnly = true)
    public TestMasterDetailResponse getTestMasterDetail(Long testMasterId) {
        TestMaster tm = testMasterRepository.findById(testMasterId)
                .orElseThrow(() -> new ResourceNotFoundException("Test master not found with ID: " + testMasterId));
        TestMasterDetailResponse resp = LabReportMapper.toMasterDetail(tm);
        List<TestParameter> parameters = testParameterRepository.findByTestMasterIdOrderByDisplayOrderAsc(testMasterId);
        resp.setParameters(parameters.stream()
                .map(LabReportMapper::toParameterResponse)
                .collect(Collectors.toList()));
        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestParameterResponse> getAllParameters() {
        return testParameterRepository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(LabReportMapper::toParameterResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TestParameterResponse getParameter(Long id) {
        TestParameter p = testParameterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test parameter not found with ID: " + id));
        return LabReportMapper.toParameterResponse(p);
    }

    @Override
    @Transactional
    public TestParameterResponse createParameter(TestParameterRequest request) {
        TestMaster tm = testMasterRepository.findById(request.getTestMasterId())
                .orElseThrow(() -> new ResourceNotFoundException("Test master not found with ID: " + request.getTestMasterId()));

        TestParameter parameter = new TestParameter();
        parameter.setTestMaster(tm);
        applyParameterFields(parameter, request);
        populateRanges(parameter, request);
        populateRules(parameter, request);
        return LabReportMapper.toParameterResponse(testParameterRepository.save(parameter));
    }

    @Override
    @Transactional
    public TestParameterResponse updateParameter(Long id, TestParameterRequest request) {
        TestParameter parameter = testParameterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test parameter not found with ID: " + id));

        if (request.getTestMasterId() != null) {
            TestMaster tm = testMasterRepository.findById(request.getTestMasterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Test master not found with ID: " + request.getTestMasterId()));
            parameter.setTestMaster(tm);
        }
        applyParameterFields(parameter, request);
        parameter.getReferenceRanges().clear();
        parameter.getInterpretationRules().clear();
        populateRanges(parameter, request);
        populateRules(parameter, request);
        return LabReportMapper.toParameterResponse(testParameterRepository.save(parameter));
    }

    @Override
    @Transactional
    public void deleteParameter(Long id) {
        if (!testParameterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Test parameter not found with ID: " + id);
        }
        testParameterRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ReferenceRangeResponse createRange(ReferenceRangeRequest request) {
        ReferenceRange range = new ReferenceRange();
        range.setTestParameter(loadParameter(request.getTestParameterId()));
        applyRangeFields(range, request);
        return LabReportMapper.toRangeResponse(referenceRangeRepository.save(range));
    }

    @Override
    @Transactional
    public ReferenceRangeResponse updateRange(Long id, ReferenceRangeRequest request) {
        ReferenceRange range = referenceRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reference range not found with ID: " + id));
        applyRangeFields(range, request);
        return LabReportMapper.toRangeResponse(referenceRangeRepository.save(range));
    }

    @Override
    @Transactional
    public void deleteRange(Long id) {
        if (!referenceRangeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reference range not found with ID: " + id);
        }
        referenceRangeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public InterpretationRuleResponse createRule(InterpretationRuleRequest request) {
        InterpretationRule rule = new InterpretationRule();
        rule.setTestParameter(loadParameter(request.getTestParameterId()));
        applyRuleFields(rule, request);
        return LabReportMapper.toRuleResponse(interpretationRuleRepository.save(rule));
    }

    @Override
    @Transactional
    public InterpretationRuleResponse updateRule(Long id, InterpretationRuleRequest request) {
        InterpretationRule rule = interpretationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interpretation rule not found with ID: " + id));
        applyRuleFields(rule, request);
        return LabReportMapper.toRuleResponse(interpretationRuleRepository.save(rule));
    }

    @Override
    @Transactional
    public void deleteRule(Long id) {
        if (!interpretationRuleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Interpretation rule not found with ID: " + id);
        }
        interpretationRuleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRuleResponse> getAllLabRules() {
        return labRuleRepository.findAll().stream()
                .map(this::toRuleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabRuleResponse createLabRule(LabRuleRequest request) {
        LabRule rule = new LabRule();
        applyLabRuleFields(rule, request);
        return toRuleResponse(labRuleRepository.save(rule));
    }

    @Override
    @Transactional
    public LabRuleResponse updateLabRule(Long id, LabRuleRequest request) {
        LabRule rule = labRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab rule not found with ID: " + id));
        applyLabRuleFields(rule, request);
        return toRuleResponse(labRuleRepository.save(rule));
    }

    @Override
    @Transactional
    public void deleteLabRule(Long id) {
        if (!labRuleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lab rule not found with ID: " + id);
        }
        labRuleRepository.deleteById(id);
    }

    // ---------- helpers ----------

    private TestParameter loadParameter(Long id) {
        return testParameterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test parameter not found with ID: " + id));
    }

    private void applyParameterFields(TestParameter parameter, TestParameterRequest request) {
        if (request.getParameterName() != null) parameter.setParameterName(request.getParameterName());
        if (request.getParameterCode() != null) parameter.setParameterCode(request.getParameterCode());
        parameter.setUnit(request.getUnit());
        if (request.getResultType() != null) {
            parameter.setResultType(ResultType.valueOf(request.getResultType().toUpperCase()));
        }
        parameter.setAllowedValues(request.getAllowedValues());
        parameter.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        parameter.setDecimalPrecision(request.getDecimalPrecision() != null ? request.getDecimalPrecision() : 1);
        parameter.setNormalText(request.getNormalText());
        parameter.setActive(request.getActive() != null ? request.getActive() : true);
    }

    private void populateRanges(TestParameter parameter, TestParameterRequest request) {
        if (request.getReferenceRanges() == null) return;
        for (ReferenceRangeRequest rr : request.getReferenceRanges()) {
            ReferenceRange range = new ReferenceRange();
            range.setTestParameter(parameter);
            applyRangeFields(range, rr);
            parameter.getReferenceRanges().add(range);
        }
    }

    private void populateRules(TestParameter parameter, TestParameterRequest request) {
        if (request.getInterpretationRules() == null) return;
        for (InterpretationRuleRequest ir : request.getInterpretationRules()) {
            InterpretationRule rule = new InterpretationRule();
            rule.setTestParameter(parameter);
            applyRuleFields(rule, ir);
            parameter.getInterpretationRules().add(rule);
        }
    }

    private void applyRangeFields(ReferenceRange range, ReferenceRangeRequest request) {
        range.setGenderScope(request.getGenderScope() != null ? request.getGenderScope() : "ANY");
        range.setMinAgeYears(request.getMinAgeYears());
        range.setMaxAgeYears(request.getMaxAgeYears());
        range.setMinValue(request.getMinValue());
        range.setMaxValue(request.getMaxValue());
        range.setCriticalLow(request.getCriticalLow());
        range.setCriticalHigh(request.getCriticalHigh());
        range.setDisplayRange(request.getDisplayRange());
        range.setPriority(request.getPriority() != null ? request.getPriority() : 0);
        range.setActive(request.getActive() != null ? request.getActive() : true);
    }

    private void applyRuleFields(InterpretationRule rule, InterpretationRuleRequest request) {
        rule.setParameterStatus(request.getParameterStatus());
        rule.setValueMatch(request.getValueMatch());
        rule.setInterpretationText(request.getInterpretationText());
        rule.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        rule.setActive(request.getActive() != null ? request.getActive() : true);
    }

    private void applyLabRuleFields(LabRule rule, LabRuleRequest request) {
        rule.setRuleCode(request.getRuleCode());
        rule.setRuleName(request.getRuleName());
        rule.setConditions(request.getConditions());
        rule.setFinalImpression(request.getFinalImpression());
        rule.setRecommendation(request.getRecommendation());
        rule.setPriority(request.getPriority() != null ? request.getPriority() : 0);
        rule.setActive(request.getActive() != null ? request.getActive() : true);
    }

    private LabRuleResponse toRuleResponse(LabRule rule) {
        LabRuleResponse resp = new LabRuleResponse();
        resp.setId(rule.getId());
        resp.setRuleCode(rule.getRuleCode());
        resp.setRuleName(rule.getRuleName());
        resp.setConditions(rule.getConditions());
        resp.setFinalImpression(rule.getFinalImpression());
        resp.setRecommendation(rule.getRecommendation());
        resp.setPriority(rule.getPriority());
        resp.setActive(rule.getActive());
        return resp;
    }
}
