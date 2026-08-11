package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.InpatientTestResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.TestAdmitedPatient;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticMapper {


    public InpatientTestResponse toResponse(TestAdmitedPatient entry) {
        if (entry == null) return null;

        InpatientTestResponse resp = new InpatientTestResponse();
        resp.setTestAdmissionId(entry.getId());
        resp.setBilledAmount(entry.getBilledAmount());
        resp.setBillingStatus(entry.getBillingStatus());

        if (entry.getTestOrder() != null) {
            resp.setTestOrderId(entry.getTestOrder().getId());
            resp.setOrderStatus(entry.getTestOrder().getOrderStatus());
            resp.setOrderedDate(entry.getTestOrder().getOrderedDate());

            if (entry.getTestOrder().getTestMaster() != null) {
                resp.setTestCode(entry.getTestOrder().getTestMaster().getTestCode());
                resp.setTestName(entry.getTestOrder().getTestMaster().getTestName());
            }
            if (entry.getTestOrder().getPatient() != null) {
                resp.setPatientName(entry.getTestOrder().getPatient().getName() );
            }
            if (entry.getTestOrder().getPrescribedBy() != null) {
                resp.setDoctorName(entry.getTestOrder().getPrescribedBy().getUser().getName());
            }
        }
        return resp;
    }
}
