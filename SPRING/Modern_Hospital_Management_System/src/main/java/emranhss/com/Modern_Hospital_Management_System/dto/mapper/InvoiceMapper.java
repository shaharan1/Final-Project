package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.InvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Invoice;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Data
@Component
@RequiredArgsConstructor
public class InvoiceMapper {

    private final DiagnosticMapper diagnosticMapper;

    public InvoiceResponse toResponse(Invoice invoice) {
        if (invoice == null) return null;

        InvoiceResponse resp = new InvoiceResponse();
        BeanUtils.copyProperties(invoice, resp);

        if (invoice.getDoctor() != null) {
            resp.setReferredDoctorName(invoice.getDoctor().getUser().getName());
        }

        if (invoice.getTests() != null) {
            resp.setCompiledTests(invoice.getTests().stream()
                    .map(test -> {

                        // Using a dummy mapper object since outpatients don't have an AdmittedPatient entry

                        var dummyLink = new emranhss.com.Modern_Hospital_Management_System.entity.TestAdmitedPatient();
                        dummyLink.setTestOrder(test);
                        dummyLink.setBilledAmount(test.getTestMaster() != null ? test.getTestMaster().getStandardPrice() : 0.0);
                        return diagnosticMapper.toResponse(dummyLink);
                    }).collect(Collectors.toList()));
        }

        return resp;
    }

}
