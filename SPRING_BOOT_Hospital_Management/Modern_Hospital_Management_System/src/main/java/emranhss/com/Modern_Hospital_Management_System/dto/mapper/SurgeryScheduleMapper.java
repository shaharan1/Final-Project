package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryScheduleResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Surgery;
import org.springframework.stereotype.Component;

@Component
public class SurgeryScheduleMapper {

    public SurgeryScheduleResponse toResponse(Surgery surgery) {
        SurgeryScheduleResponse response = new SurgeryScheduleResponse();
        response.setSurgeryId(surgery.getId());
        response.setSurgeryNumber(surgery.getSurgeryNumber());
        response.setPriority(surgery.getPriority());
        response.setStatus(surgery.getStatus());
        response.setSurgeryDate(surgery.getSurgeryDate());
        response.setStartTime(surgery.getStartTime());
        response.setEndTime(surgery.getEndTime());

        if (surgery.getSurgeryMaster() != null) {
            response.setSurgeryName(surgery.getSurgeryMaster().getSurgeryName());
        }
        if (surgery.getOperationTheatre() != null) {
            response.setOperationTheatreId(surgery.getOperationTheatre().getId());
            response.setOperationTheatreName(surgery.getOperationTheatre().getOtName());
        }
        if (surgery.getPatient() != null) {
            response.setPatientId(surgery.getPatient().getId());
            response.setPatientName(surgery.getPatient().getName());
            response.setPatientCode(surgery.getPatient().getPatientCode());
        }
        if (surgery.getSurgeon() != null) {
            response.setSurgeonId(surgery.getSurgeon().getId());
            response.setSurgeonName(surgery.getSurgeon().getUser().getName());
        }
        return response;
    }
}
