package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.AdmissionResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.AdmittedPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.BedBooking;
import org.springframework.stereotype.Component;

@Component
public class AdmissionMapper {

    public AdmissionResponse toResponse(AdmittedPatient admission) {

        AdmissionResponse response = new AdmissionResponse();

        response.setAdmissionId(admission.getId());
        response.setAdmissionDate(admission.getAdmissionDate());
        response.setStatus(admission.getAdmissionStatus());
        response.setInitialDiagnosis(admission.getInitialDiagnosis());

        if (admission.getPatient() != null) {
            response.setPatientId(admission.getPatient().getId());
            response.setPatientName(admission.getPatient().getName());
            response.setPatientCode(admission.getPatient().getPatientCode());
        }

        if (admission.getPrimaryDoctor() != null) {
            response.setDoctorName(admission.getPrimaryDoctor().getUser().getName());
        }

        return response;
    }

    public AdmissionResponse toResponse(AdmittedPatient admission, BedBooking booking) {

        AdmissionResponse response = toResponse(admission);

        if (booking != null && booking.getBed() != null) {

            response.setAssignedBedNumber(
                    booking.getBed().getBedNumber()
            );

            if (booking.getBed().getWard() != null) {

                response.setWardName(
                        booking.getBed().getWard().getName()
                );

            }
        }

        return response;
    }
}
