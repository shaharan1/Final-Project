package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryCategory;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryMaster;
import org.springframework.stereotype.Component;

@Component
public class SurgeryMasterMapper {

    public SurgeryMaster toEntity(SurgeryMasterRequest request, SurgeryCategory category) {
        SurgeryMaster master = new SurgeryMaster();
        applyRequest(master, request, category);
        return master;
    }

    public void updateEntity(SurgeryMaster master, SurgeryMasterRequest request, SurgeryCategory category) {
        applyRequest(master, request, category);
    }

    private void applyRequest(SurgeryMaster master, SurgeryMasterRequest request, SurgeryCategory category) {
        master.setSurgeryCode(request.getSurgeryCode());
        master.setSurgeryName(request.getSurgeryName());
        master.setCategory(category);
        master.setStandardRate(request.getStandardRate());
        master.setOtCharge(request.getOtCharge());
        master.setAnesthesiaCharge(request.getAnesthesiaCharge());
        master.setNursingCharge(request.getNursingCharge());
        master.setEquipmentCharge(request.getEquipmentCharge());
        master.setConsumableCharge(request.getConsumableCharge());
        master.setIcuCharge(request.getIcuCharge());
        master.setPackageRate(request.getPackageRate());
        master.setEstimatedDurationMin(request.getEstimatedDurationMin());
        master.setNotes(request.getNotes());
        if (request.getActive() != null) {
            master.setActive(request.getActive());
        }
    }

    public SurgeryMasterResponse toResponse(SurgeryMaster master) {
        SurgeryMasterResponse response = new SurgeryMasterResponse();
        response.setId(master.getId());
        response.setSurgeryCode(master.getSurgeryCode());
        response.setSurgeryName(master.getSurgeryName());
        response.setStandardRate(master.getStandardRate());
        response.setOtCharge(master.getOtCharge());
        response.setAnesthesiaCharge(master.getAnesthesiaCharge());
        response.setNursingCharge(master.getNursingCharge());
        response.setEquipmentCharge(master.getEquipmentCharge());
        response.setConsumableCharge(master.getConsumableCharge());
        response.setIcuCharge(master.getIcuCharge());
        response.setPackageRate(master.getPackageRate());
        response.setEstimatedDurationMin(master.getEstimatedDurationMin());
        response.setNotes(master.getNotes());
        response.setActive(master.getActive());
        if (master.getCategory() != null) {
            response.setCategoryId(master.getCategory().getId());
            response.setCategoryName(master.getCategory().getName());
        }
        return response;
    }
}
