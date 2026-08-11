package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.OthersChargeResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.OthersCharge;
import org.springframework.stereotype.Component;

@Component
public class OthersChargeMapper {

    public OthersChargeResponse toResponse(OthersCharge charge) {
        if (charge == null) return null;

        OthersChargeResponse resp = new OthersChargeResponse();
        resp.setId(charge.getId());
        resp.setCategory(charge.getCategory());
        resp.setDescription(charge.getDescription());
        resp.setUnitPrice(charge.getUnitPrice());
        resp.setQuantity(charge.getQuantity());
        resp.setAmount(charge.getAmount());
        resp.setBillingStatus(charge.getBillingStatus());
        resp.setEnteredBy(charge.getEnteredBy());
        resp.setCreatedDate(charge.getCreatedDate());

        if (charge.getAdmittedPatient() != null) {
            resp.setAdmittedPatientId(charge.getAdmittedPatient().getId());
            if (charge.getAdmittedPatient().getPatient() != null) {
                resp.setPatientName(charge.getAdmittedPatient().getPatient().getName() + " " +
                        charge.getAdmittedPatient().getPatient());
            }
        }

        if (charge.getBedBooking() != null) {
            resp.setBedBookingId(charge.getBedBooking().getId());
            if (charge.getBedBooking().getBed() != null) {
                resp.setBedNumber(charge.getBedBooking().getBed().getBedNumber());
            }
        }

        return resp;
    }
}
