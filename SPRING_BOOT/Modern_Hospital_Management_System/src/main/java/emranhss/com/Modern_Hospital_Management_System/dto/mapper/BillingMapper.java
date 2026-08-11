package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Billing;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {

    public BillingResponse toResponse(Billing billing) {
        if (billing == null) return null;

        BillingResponse resp = new BillingResponse();
        BeanUtils.copyProperties(billing, resp);

        if (billing.getAdmittedPatient() != null) {
            resp.setAdmittedPatientId(billing.getAdmittedPatient().getId());
            if (billing.getAdmittedPatient().getPatient() != null) {
                resp.setPatientName(billing.getAdmittedPatient().getPatient().getName() + " " +
                        billing.getAdmittedPatient());
                resp.setPatientCode(billing.getAdmittedPatient().getPatient().getPatientCode());
            }
        }
        return resp;
    }
}
