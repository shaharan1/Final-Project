package emranhss.com.Modern_Hospital_Management_System.config;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.ResultType;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LabDataSeeder implements CommandLineRunner {

    private final TestMasterRepository testMasterRepository;
    private final TestParameterRepository testParameterRepository;
    private final ReferenceRangeRepository referenceRangeRepository;
    private final InterpretationRuleRepository interpretationRuleRepository;
    private final LabRuleRepository labRuleRepository;

    @Override
    public void run(String... args) {
        seedCbc();
        seedDengueProfile();
        seedHemoglobin();
        seedFastingGlucose();
        seedCreatinine();
        seedWbc();
        seedPlatelet();
        seedCovid();
        seedPregnancy();
        seedDengueRules();

        log.info("Lab data seeding completed.");
    }

    private TestMaster master(String code, String name, double price, String normalRange) {
        Optional<TestMaster> existing = testMasterRepository.findByTestCode(code);
        if (existing.isPresent()) return existing.get();
        TestMaster tm = new TestMaster();
        tm.setTestCode(code);
        tm.setTestName(name);
        tm.setStandardPrice(price);
        tm.setNormalRange(normalRange);
        tm.setActive(true);
        return testMasterRepository.save(tm);
    }

    private TestParameter parameter(TestMaster tm, String name, String code, String unit, ResultType type,
                                    String allowedValues, int order, int precision) {
        Optional<TestParameter> existing = testParameterRepository.findByTestMasterIdOrderByDisplayOrderAsc(tm.getId())
                .stream().filter(x -> x.getParameterCode().equals(code)).findFirst();
        if (existing.isPresent()) return existing.get();

        TestParameter p = new TestParameter();
        p.setTestMaster(tm);
        p.setParameterName(name);
        p.setParameterCode(code);
        p.setUnit(unit);
        p.setResultType(type);
        p.setAllowedValues(allowedValues);
        p.setDisplayOrder(order);
        p.setDecimalPrecision(precision);
        p.setActive(true);
        return testParameterRepository.save(p);
    }

    private ReferenceRange range(TestParameter p, String gender, Integer minAge, Integer maxAge,
                                 Double min, Double max, Double critLow, Double critHigh,
                                 String display, int priority) {
        ReferenceRange r = new ReferenceRange();
        r.setTestParameter(p);
        r.setGenderScope(gender);
        r.setMinAgeYears(minAge);
        r.setMaxAgeYears(maxAge);
        r.setMinValue(min);
        r.setMaxValue(max);
        r.setCriticalLow(critLow);
        r.setCriticalHigh(critHigh);
        r.setDisplayRange(display);
        r.setPriority(priority);
        r.setActive(true);
        return r;
    }

    private InterpretationRule rule(TestParameter p, String status, String match, String text, int order) {
        InterpretationRule r = new InterpretationRule();
        r.setTestParameter(p);
        r.setParameterStatus(status);
        r.setValueMatch(match);
        r.setInterpretationText(text);
        r.setDisplayOrder(order);
        r.setActive(true);
        return r;
    }

    private void saveParameter(TestParameter p, List<ReferenceRange> ranges, List<InterpretationRule> rules) {
        if (referenceRangeRepository.findByTestParameterIdOrderByPriorityAsc(p.getId()).isEmpty() && ranges != null) {
            ranges.forEach(referenceRangeRepository::save);
        }
        if (interpretationRuleRepository.findByTestParameterIdOrderByDisplayOrderAsc(p.getId()).isEmpty() && rules != null) {
            rules.forEach(interpretationRuleRepository::save);
        }
    }

    private void seedCbc() {
        TestMaster cbc = master("CBC", "Complete Blood Count (CBC)", 400, "See individual parameters");

        TestParameter hgb = parameter(cbc, "Hemoglobin", "HGB", "g/dL", ResultType.NUMERIC, null, 1, 1);
        saveParameter(hgb,
                List.of(
                        range(hgb, "MALE", null, null, 13.5, 17.5, 8.0, 20.0, "13.5 - 17.5", 2),
                        range(hgb, "FEMALE", null, null, 12.0, 15.5, 8.0, 20.0, "12.0 - 15.5", 2),
                        range(hgb, "ANY", 0, 12, 11.0, 16.0, 8.0, 20.0, "11.0 - 16.0 (Child)", 1)
                ),
                List.of(
                        rule(hgb, "CRITICAL_LOW", null, "Hemoglobin is critically low. Possible severe anemia. Blood transfusion may be required.", 1),
                        rule(hgb, "LOW", null, "Hemoglobin is below the normal range. Possible anemia.", 2),
                        rule(hgb, "HIGH", null, "Hemoglobin is above the normal range. Possible polycythemia.", 3),
                        rule(hgb, "CRITICAL_HIGH", null, "Hemoglobin is critically high. Possible severe polycythemia.", 4)
                ));

        TestParameter wbc = parameter(cbc, "WBC Count", "WBC", "/µL", ResultType.NUMERIC, null, 2, 0);
        saveParameter(wbc,
                List.of(range(wbc, "ANY", null, null, 4000.0, 11000.0, 2000.0, 50000.0, "4000 - 11000", 1)),
                List.of(
                        rule(wbc, "CRITICAL_LOW", null, "WBC count is critically low. Risk of infection is high. Immediate medical attention required.", 1),
                        rule(wbc, "LOW", null, "WBC count is below the normal range. Possible leukopenia.", 2),
                        rule(wbc, "HIGH", null, "WBC count is above the normal range. Possible leukocytosis or infection.", 3),
                        rule(wbc, "CRITICAL_HIGH", null, "WBC count is critically high. Possible severe infection or hematological condition.", 4)
                ));

        TestParameter plt = parameter(cbc, "Platelet Count", "PLATELET", "/µL", ResultType.NUMERIC, null, 3, 0);
        saveParameter(plt,
                List.of(range(plt, "ANY", null, null, 150000.0, 450000.0, 50000.0, 1000000.0, "150000 - 450000", 1)),
                List.of(
                        rule(plt, "CRITICAL_LOW", null, "Platelet count is critically low. High risk of bleeding. Immediate medical attention required.", 1),
                        rule(plt, "LOW", null, "Platelet count is below the normal range. Possible thrombocytopenia. Consider dengue or other hematological conditions.", 2),
                        rule(plt, "HIGH", null, "Platelet count is above the normal range. Possible thrombocytosis.", 3)
                ));

        TestParameter hct = parameter(cbc, "Hematocrit (PCV)", "HCT", "%", ResultType.NUMERIC, null, 4, 1);
        saveParameter(hct,
                List.of(
                        range(hct, "MALE", null, null, 40.0, 50.0, 25.0, 60.0, "40 - 50", 2),
                        range(hct, "FEMALE", null, null, 36.0, 46.0, 25.0, 60.0, "36 - 46", 2)
                ),
                List.of(
                        rule(hct, "LOW", null, "Hematocrit is below the normal range. Possible anemia.", 1),
                        rule(hct, "HIGH", null, "Hematocrit is above the normal range.", 2)
                ));

        TestParameter neut = parameter(cbc, "Neutrophils", "NEUT", "%", ResultType.NUMERIC, null, 5, 1);
        saveParameter(neut,
                List.of(range(neut, "ANY", null, null, 40.0, 70.0, null, null, "40 - 70", 1)),
                List.of(
                        rule(neut, "LOW", null, "Neutrophil percentage is below the normal range.", 1),
                        rule(neut, "HIGH", null, "Neutrophil percentage is above the normal range. Possible bacterial infection.", 2)
                ));

        TestParameter lymph = parameter(cbc, "Lymphocytes", "LYMPH", "%", ResultType.NUMERIC, null, 6, 1);
        saveParameter(lymph,
                List.of(range(lymph, "ANY", null, null, 20.0, 40.0, null, null, "20 - 40", 1)),
                List.of(
                        rule(lymph, "LOW", null, "Lymphocyte percentage is below the normal range.", 1),
                        rule(lymph, "HIGH", null, "Lymphocyte percentage is above the normal range. Possible viral infection.", 2)
                ));
    }

    private void seedDengueProfile() {
        TestMaster dengue = master("DENGUE", "Dengue Profile (NS1, IgM, IgG)", 1200, "NS1 / IgM / IgG");

        TestParameter ns1 = parameter(dengue, "Dengue NS1 Antigen", "NS1", null, ResultType.POSITIVE_NEGATIVE, "Positive,Negative", 1, 0);
        saveParameter(ns1, null, List.of(
                rule(ns1, "POSITIVE", "Positive", "Dengue NS1 antigen detected. Suggestive of acute dengue infection.", 1),
                rule(ns1, "NEGATIVE", "Negative", "Dengue NS1 antigen not detected.", 2)
        ));

        TestParameter igm = parameter(dengue, "Dengue IgM Antibody", "IGM", null, ResultType.POSITIVE_NEGATIVE, "Positive,Negative", 2, 0);
        saveParameter(igm, null, List.of(
                rule(igm, "POSITIVE", "Positive", "Dengue IgM antibody detected. Suggests recent or ongoing dengue infection.", 1),
                rule(igm, "NEGATIVE", "Negative", "Dengue IgM antibody not detected.", 2)
        ));

        TestParameter igg = parameter(dengue, "Dengue IgG Antibody", "IGG", null, ResultType.POSITIVE_NEGATIVE, "Positive,Negative", 3, 0);
        saveParameter(igg, null, List.of(
                rule(igg, "POSITIVE", "Positive", "Dengue IgG antibody detected. Suggests past dengue infection.", 1),
                rule(igg, "NEGATIVE", "Negative", "Dengue IgG antibody not detected.", 2)
        ));
    }

    private void seedHemoglobin() {
        TestMaster hb = master("HB", "Hemoglobin", 150, "13.5 - 17.5 g/dL (M) / 12.0 - 15.5 g/dL (F)");
        TestParameter p = parameter(hb, "Hemoglobin", "HGB", "g/dL", ResultType.NUMERIC, null, 1, 1);
        saveParameter(p,
                List.of(
                        range(p, "MALE", null, null, 13.5, 17.5, 8.0, 20.0, "13.5 - 17.5", 2),
                        range(p, "FEMALE", null, null, 12.0, 15.5, 8.0, 20.0, "12.0 - 15.5", 2),
                        range(p, "ANY", 0, 12, 11.0, 16.0, 8.0, 20.0, "11.0 - 16.0 (Child)", 1)
                ),
                List.of(
                        rule(p, "CRITICAL_LOW", null, "Hemoglobin is critically low. Possible severe anemia. Blood transfusion may be required.", 1),
                        rule(p, "LOW", null, "Hemoglobin is below the normal range. Possible anemia.", 2),
                        rule(p, "HIGH", null, "Hemoglobin is above the normal range. Possible polycythemia.", 3),
                        rule(p, "CRITICAL_HIGH", null, "Hemoglobin is critically high. Possible severe polycythemia.", 4)
                ));
    }

    private void seedFastingGlucose() {
        TestMaster g = master("FGLU", "Fasting Blood Glucose", 120, "70 - 99 mg/dL");
        TestParameter p = parameter(g, "Fasting Glucose", "FGLU", "mg/dL", ResultType.NUMERIC, null, 1, 1);
        saveParameter(p,
                List.of(range(p, "ANY", null, null, 70.0, 99.0, 40.0, 500.0, "70 - 99", 1)),
                List.of(
                        rule(p, "CRITICAL_LOW", null, "Fasting glucose is critically low. Risk of hypoglycemic emergency. Immediate attention required.", 1),
                        rule(p, "LOW", null, "Fasting glucose is below the normal range. Possible hypoglycemia.", 2),
                        rule(p, "HIGH", null, "Fasting glucose is above the normal range. Suggestive of impaired fasting glucose or diabetes mellitus.", 3),
                        rule(p, "CRITICAL_HIGH", null, "Fasting glucose is critically high. Possible hyperglycemic emergency. Immediate attention required.", 4)
                ));
    }

    private void seedCreatinine() {
        TestMaster c = master("CREAT", "Serum Creatinine", 150, "0.74 - 1.35 (M) / 0.59 - 1.04 (F) mg/dL");
        TestParameter p = parameter(c, "Creatinine", "CREAT", "mg/dL", ResultType.NUMERIC, null, 1, 2);
        saveParameter(p,
                List.of(
                        range(p, "MALE", null, null, 0.74, 1.35, 2.5, null, "0.74 - 1.35", 2),
                        range(p, "FEMALE", null, null, 0.59, 1.04, 2.5, null, "0.59 - 1.04", 2)
                ),
                List.of(
                        rule(p, "LOW", null, "Creatinine is below the normal range.", 1),
                        rule(p, "HIGH", null, "Creatinine is above the normal range. Possible renal impairment.", 2),
                        rule(p, "CRITICAL_HIGH", null, "Creatinine is critically high. Possible severe renal dysfunction. Immediate medical review required.", 3)
                ));
    }

    private void seedWbc() {
        TestMaster w = master("WBC", "WBC Count", 120, "4000 - 11000 /µL");
        TestParameter p = parameter(w, "WBC Count", "WBC", "/µL", ResultType.NUMERIC, null, 1, 0);
        saveParameter(p,
                List.of(range(p, "ANY", null, null, 4000.0, 11000.0, 2000.0, 50000.0, "4000 - 11000", 1)),
                List.of(
                        rule(p, "CRITICAL_LOW", null, "WBC count is critically low. Risk of infection is high. Immediate medical attention required.", 1),
                        rule(p, "LOW", null, "WBC count is below the normal range. Possible leukopenia.", 2),
                        rule(p, "HIGH", null, "WBC count is above the normal range. Possible leukocytosis or infection.", 3),
                        rule(p, "CRITICAL_HIGH", null, "WBC count is critically high. Possible severe infection or hematological condition.", 4)
                ));
    }

    private void seedPlatelet() {
        TestMaster pl = master("PLATELET", "Platelet Count", 150, "150000 - 450000 /µL");
        TestParameter p = parameter(pl, "Platelet Count", "PLATELET", "/µL", ResultType.NUMERIC, null, 1, 0);
        saveParameter(p,
                List.of(range(p, "ANY", null, null, 150000.0, 450000.0, 50000.0, 1000000.0, "150000 - 450000", 1)),
                List.of(
                        rule(p, "CRITICAL_LOW", null, "Platelet count is critically low. High risk of bleeding. Immediate medical attention required.", 1),
                        rule(p, "LOW", null, "Platelet count is below the normal range. Possible thrombocytopenia. Consider dengue or other hematological conditions.", 2),
                        rule(p, "HIGH", null, "Platelet count is above the normal range. Possible thrombocytosis.", 3)
                ));
    }

    private void seedCovid() {
        TestMaster cv = master("COVID-PCR", "COVID-19 RT-PCR", 1800, "Detected / Not Detected");
        TestParameter p = parameter(cv, "SARS-CoV-2 PCR", "COVID", null, ResultType.POSITIVE_NEGATIVE, "Detected,Not Detected", 1, 0);
        saveParameter(p, null, List.of(
                rule(p, "POSITIVE", "Detected", "SARS-CoV-2 RNA detected. Confirms active COVID-19 infection.", 1),
                rule(p, "NEGATIVE", "Not Detected", "SARS-CoV-2 RNA not detected.", 2)
        ));
    }

    private void seedPregnancy() {
        TestMaster pg = master("PREGNANCY", "Pregnancy Test (Urine HCG)", 100, "Positive / Negative");
        TestParameter p = parameter(pg, "HCG (Urine)", "PREGNANCY", null, ResultType.POSITIVE_NEGATIVE, "Positive,Negative", 1, 0);
        saveParameter(p, null, List.of(
                rule(p, "POSITIVE", "Positive", "HCG detected. Test is positive - suggestive of pregnancy.", 1),
                rule(p, "NEGATIVE", "Negative", "HCG not detected. Test is negative.", 2)
        ));
    }

    private void seedDengueRules() {
        if (labRuleRepository.count() > 0) {
            return;
        }

        LabRule r1 = new LabRule();
        r1.setRuleCode("DENGUE-1");
        r1.setRuleName("Classic Dengue (NS1+, Low Platelet + Low WBC)");
        r1.setConditions("[{\"parameterCode\":\"PLATELET\",\"operator\":\"LT\",\"value\":\"150000\"}," +
                "{\"parameterCode\":\"WBC\",\"operator\":\"LT\",\"value\":\"4000\"}," +
                "{\"parameterCode\":\"NS1\",\"operator\":\"EQ\",\"value\":\"Positive\"}]");
        r1.setFinalImpression("Strongly suggestive of Dengue infection.");
        r1.setRecommendation("Immediate physician review recommended. Start supportive management and monitor platelet count.");
        r1.setPriority(1);
        r1.setActive(true);
        labRuleRepository.save(r1);

        LabRule r2 = new LabRule();
        r2.setRuleCode("DENGUE-2");
        r2.setRuleName("Probable Recent Dengue (NS1-, IgM+)");
        r2.setConditions("[{\"parameterCode\":\"NS1\",\"operator\":\"EQ\",\"value\":\"Negative\"}," +
                "{\"parameterCode\":\"IGM\",\"operator\":\"EQ\",\"value\":\"Positive\"}]");
        r2.setFinalImpression("Probable recent dengue infection.");
        r2.setRecommendation("Clinical correlation advised. Supportive management and follow-up serology recommended.");
        r2.setPriority(2);
        r2.setActive(true);
        labRuleRepository.save(r2);

        LabRule r3 = new LabRule();
        r3.setRuleCode("DENGUE-3");
        r3.setRuleName("Dengue Markers Not Detected (NS1-, IgM-)");
        r3.setConditions("[{\"parameterCode\":\"NS1\",\"operator\":\"EQ\",\"value\":\"Negative\"}," +
                "{\"parameterCode\":\"IGM\",\"operator\":\"EQ\",\"value\":\"Negative\"}]");
        r3.setFinalImpression("Dengue markers not detected.");
        r3.setRecommendation("Consider alternative diagnosis and review clinical picture.");
        r3.setPriority(3);
        r3.setActive(true);
        labRuleRepository.save(r3);
    }
}
