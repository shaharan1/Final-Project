package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import org.springframework.stereotype.Component;

@Component
public class SurgeryMapper {

    public Surgery toEntity(SurgeryRequest request, Patient patient, AdmittedPatient admittedPatient,
                            Doctor surgeon, Doctor assistantSurgeon, Doctor anesthesiologist,
                            DoctorDepartment department, SurgeryCategory category,
                            SurgeryMaster surgeryMaster, OperationTheatre operationTheatre,
                            String surgeryNumber) {
        Surgery surgery = new Surgery();
        surgery.setSurgeryNumber(surgeryNumber);
        applyRequest(surgery, request, patient, admittedPatient, surgeon, assistantSurgeon,
                anesthesiologist, department, category, surgeryMaster, operationTheatre);
        return surgery;
    }

    public void updateEntity(Surgery surgery, SurgeryRequest request, Patient patient, AdmittedPatient admittedPatient,
                             Doctor surgeon, Doctor assistantSurgeon, Doctor anesthesiologist,
                             DoctorDepartment department, SurgeryCategory category,
                             SurgeryMaster surgeryMaster, OperationTheatre operationTheatre) {
        applyRequest(surgery, request, patient, admittedPatient, surgeon, assistantSurgeon,
                anesthesiologist, department, category, surgeryMaster, operationTheatre);
    }

    private void applyRequest(Surgery surgery, SurgeryRequest request, Patient patient, AdmittedPatient admittedPatient,
                              Doctor surgeon, Doctor assistantSurgeon, Doctor anesthesiologist,
                              DoctorDepartment department, SurgeryCategory category,
                              SurgeryMaster surgeryMaster, OperationTheatre operationTheatre) {
        surgery.setPatient(patient);
        surgery.setAdmittedPatient(admittedPatient);
        surgery.setSurgeon(surgeon);
        surgery.setAssistantSurgeon(assistantSurgeon);
        surgery.setAnesthesiologist(anesthesiologist);
        surgery.setDepartment(department);
        surgery.setCategory(category);
        surgery.setSurgeryMaster(surgeryMaster);
        surgery.setOperationTheatre(operationTheatre);

        surgery.setSurgeryDate(request.getSurgeryDate());
        surgery.setStartTime(request.getStartTime());
        surgery.setEndTime(request.getEndTime());
        surgery.setEstimatedDurationMin(request.getEstimatedDurationMin());
        surgery.setPriority(request.getPriority());
        surgery.setAnesthesiaType(request.getAnesthesiaType());
        surgery.setClinicalNotes(request.getClinicalNotes());
        surgery.setPreOperativeDiagnosis(request.getPreOperativeDiagnosis());
        surgery.setPostOperativeDiagnosis(request.getPostOperativeDiagnosis());
        surgery.setCancellationReason(request.getCancellationReason());

        if (request.getStatus() != null) {
            surgery.setStatus(request.getStatus());
        }

        surgery.setSurgeryCharge(request.getSurgeryCharge());
        surgery.setOtCharge(request.getOtCharge());
        surgery.setSurgeonFee(request.getSurgeonFee());
        surgery.setAssistantSurgeonFee(request.getAssistantSurgeonFee());
        surgery.setAnesthesiaFee(request.getAnesthesiaFee());
        surgery.setNursingCharge(request.getNursingCharge());
        surgery.setEquipmentCharge(request.getEquipmentCharge());
        surgery.setConsumableCharge(request.getConsumableCharge());
        surgery.setIcuCharge(request.getIcuCharge());
        surgery.setWardCabinCharge(request.getWardCabinCharge());
        surgery.setMedicineCharge(request.getMedicineCharge());
        surgery.setLaboratoryCharge(request.getLaboratoryCharge());
        surgery.setRadiologyCharge(request.getRadiologyCharge());

        surgery.setDiscountPercent(request.getDiscountPercent());
        surgery.setVatRate(request.getVatRate());
        surgery.setInsuranceCoverage(request.getInsuranceCoverage());
        surgery.setAdvancePaid(request.getAdvancePaid());
    }

    public SurgeryResponse toResponse(Surgery surgery) {
        return toResponse(surgery, null);
    }

    public SurgeryResponse toResponse(Surgery surgery, BedBooking bedBooking) {
        SurgeryResponse response = new SurgeryResponse();
        response.setId(surgery.getId());
        response.setSurgeryNumber(surgery.getSurgeryNumber());
        response.setSurgeryDate(surgery.getSurgeryDate());
        response.setStartTime(surgery.getStartTime());
        response.setEndTime(surgery.getEndTime());
        response.setEstimatedDurationMin(surgery.getEstimatedDurationMin());
        response.setPriority(surgery.getPriority());
        response.setAnesthesiaType(surgery.getAnesthesiaType());
        response.setClinicalNotes(surgery.getClinicalNotes());
        response.setPreOperativeDiagnosis(surgery.getPreOperativeDiagnosis());
        response.setPostOperativeDiagnosis(surgery.getPostOperativeDiagnosis());
        response.setStatus(surgery.getStatus());
        response.setCancellationReason(surgery.getCancellationReason());

        if (surgery.getPatient() != null) {
            response.setPatientId(surgery.getPatient().getId());
            response.setPatientName(surgery.getPatient().getName());
            response.setPatientCode(surgery.getPatient().getPatientCode());
        }
        if (surgery.getAdmittedPatient() != null) {
            response.setAdmittedPatientId(surgery.getAdmittedPatient().getId());
            response.setAdmissionStatus(surgery.getAdmittedPatient().getAdmissionStatus());
        }
        if (surgery.getSurgeon() != null) {
            response.setSurgeonId(surgery.getSurgeon().getId());
            response.setSurgeonName(surgery.getSurgeon().getUser().getName());
        }
        if (surgery.getAssistantSurgeon() != null) {
            response.setAssistantSurgeonId(surgery.getAssistantSurgeon().getId());
            response.setAssistantSurgeonName(surgery.getAssistantSurgeon().getUser().getName());
        }
        if (surgery.getAnesthesiologist() != null) {
            response.setAnesthesiologistId(surgery.getAnesthesiologist().getId());
            response.setAnesthesiologistName(surgery.getAnesthesiologist().getUser().getName());
        }
        if (surgery.getDepartment() != null) {
            response.setDepartmentId(surgery.getDepartment().getId());
            response.setDepartmentName(surgery.getDepartment().getDepartmentName());
        }
        if (surgery.getCategory() != null) {
            response.setCategoryId(surgery.getCategory().getId());
            response.setCategoryName(surgery.getCategory().getName());
        }
        if (surgery.getSurgeryMaster() != null) {
            response.setSurgeryMasterId(surgery.getSurgeryMaster().getId());
            response.setSurgeryName(surgery.getSurgeryMaster().getSurgeryName());
            response.setSurgeryCode(surgery.getSurgeryMaster().getSurgeryCode());
        }
        if (surgery.getOperationTheatre() != null) {
            response.setOperationTheatreId(surgery.getOperationTheatre().getId());
            response.setOperationTheatreName(surgery.getOperationTheatre().getOtName());
            response.setOtCode(surgery.getOperationTheatre().getOtCode());
        }

        if (bedBooking != null && bedBooking.getBed() != null) {
            response.setBedId(bedBooking.getBed().getId());
            response.setBedNumber(bedBooking.getBed().getBedNumber());
            if (bedBooking.getBed().getWard() != null) {
                response.setWardId(bedBooking.getBed().getWard().getId());
                response.setWardName(bedBooking.getBed().getWard().getName());
            }
        }

        response.setSurgeryCharge(surgery.getSurgeryCharge());
        response.setOtCharge(surgery.getOtCharge());
        response.setSurgeonFee(surgery.getSurgeonFee());
        response.setAssistantSurgeonFee(surgery.getAssistantSurgeonFee());
        response.setAnesthesiaFee(surgery.getAnesthesiaFee());
        response.setNursingCharge(surgery.getNursingCharge());
        response.setEquipmentCharge(surgery.getEquipmentCharge());
        response.setConsumableCharge(surgery.getConsumableCharge());
        response.setIcuCharge(surgery.getIcuCharge());
        response.setWardCabinCharge(surgery.getWardCabinCharge());
        response.setMedicineCharge(surgery.getMedicineCharge());
        response.setLaboratoryCharge(surgery.getLaboratoryCharge());
        response.setRadiologyCharge(surgery.getRadiologyCharge());

        response.setDiscountPercent(surgery.getDiscountPercent());
        response.setDiscountAmount(surgery.getDiscountAmount());
        response.setVatRate(surgery.getVatRate());
        response.setVatAmount(surgery.getVatAmount());
        response.setInsuranceCoverage(surgery.getInsuranceCoverage());
        response.setAdvancePaid(surgery.getAdvancePaid());
        response.setSubtotal(surgery.getSubtotal());
        response.setTotalAmount(surgery.getTotalAmount());
        response.setFinalPayable(surgery.getFinalPayable());

        response.setBillingInvoiceId(surgery.getBillingInvoiceId());
        response.setBillingInvoiceNumber(surgery.getBillingInvoiceNumber());
        response.setBillingStatus(surgery.getBillingStatus());

        response.setCreatedDate(surgery.getCreatedDate());
        response.setLastUpdated(surgery.getLastUpdated());
        return response;
    }
}
