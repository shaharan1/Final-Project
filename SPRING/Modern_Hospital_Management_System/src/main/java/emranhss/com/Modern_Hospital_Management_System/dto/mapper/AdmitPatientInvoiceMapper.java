package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.AdmitPatientInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.AdmitPatientInvoice;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AdmitPatientInvoiceMapper {


    public AdmitPatientInvoiceResponse toResponse(AdmitPatientInvoice invoice) {
        if (invoice == null) return null;

        AdmitPatientInvoiceResponse resp = new AdmitPatientInvoiceResponse();
        BeanUtils.copyProperties(invoice, resp);

        if (invoice.getAdmittedPatient() != null) {
            resp.setAdmittedPatientId(invoice.getAdmittedPatient().getId());
            if (invoice.getAdmittedPatient().getPatient() != null) {
                resp.setPatientName(invoice.getAdmittedPatient().getPatient().getName());

                resp.setPatientCode(invoice.getAdmittedPatient().getPatient().getPatientCode());
            }
        }
        return resp;
    }

}
