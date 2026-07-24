package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorChargeResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.AdmittedPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.BedBooking;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorCharge;
import org.springframework.stereotype.Component;

@Component
public class DoctorChargeMapper {

    public DoctorCharge toEntity(DoctorChargeRequest request, Doctor doctor,
                                 BedBooking bedBooking, AdmittedPatient admittedPatient) {
        DoctorCharge charge = new DoctorCharge();
        charge.setDescription(request.getDescription());
        charge.setAmount(request.getAmount());
        charge.setVisitDate(request.getVisitDate());
        charge.setDoctor(doctor);
        charge.setBedBooking(bedBooking);
        charge.setAdmittedPatient(admittedPatient);

        // Ensure new transactions always default to pending status
        charge.setBillingStatus("PENDING");
        return charge;
    }

    public void updateEntity(DoctorCharge charge, DoctorChargeRequest request, Doctor doctor,
                             BedBooking bedBooking, AdmittedPatient admittedPatient) {
        charge.setDescription(request.getDescription());
        charge.setAmount(request.getAmount());
        charge.setVisitDate(request.getVisitDate());
        charge.setDoctor(doctor);
        charge.setBedBooking(bedBooking);
        charge.setAdmittedPatient(admittedPatient);
        // Usually, billingStatus is handled by the Invoice flow, so we avoid overriding it here unless requested
    }

    public DoctorChargeResponse toResponse(DoctorCharge charge) {
        DoctorChargeResponse response = new DoctorChargeResponse();
        response.setId(charge.getId());
        response.setDescription(charge.getDescription());
        response.setAmount(charge.getAmount());
        response.setVisitDate(charge.getVisitDate());

        // MAP THE NEW STATUS FIELD HERE
        response.setBillingStatus(charge.getBillingStatus());

        if (charge.getDoctor() != null) {
            response.setDoctorId(charge.getDoctor().getId());
            response.setDoctorName(charge.getDoctor().getUser().getName());
        }

        if (charge.getBedBooking() != null) {
            response.setBedBookingId(charge.getBedBooking().getId());
        }

        if (charge.getAdmittedPatient() != null) {
            response.setAdmittedPatientId(charge.getAdmittedPatient().getId());
            response.setAdmissionStatus(charge.getAdmittedPatient().getAdmissionStatus());
            if (charge.getAdmittedPatient().getPatient() != null) {
                response.setPatientName(charge.getAdmittedPatient().getPatient().getName());
            }
        }

        return response;
    }
}
