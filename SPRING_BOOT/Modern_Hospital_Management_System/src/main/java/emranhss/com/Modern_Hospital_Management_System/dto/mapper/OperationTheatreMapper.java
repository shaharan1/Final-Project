package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.OperationTheatreRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OperationTheatreResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.OperationTheatre;
import org.springframework.stereotype.Component;

@Component
public class OperationTheatreMapper {

    public OperationTheatre toEntity(OperationTheatreRequest request) {
        OperationTheatre ot = new OperationTheatre();
        applyRequest(ot, request);
        return ot;
    }

    public void updateEntity(OperationTheatre ot, OperationTheatreRequest request) {
        applyRequest(ot, request);
    }

    private void applyRequest(OperationTheatre ot, OperationTheatreRequest request) {
        ot.setOtCode(request.getOtCode());
        ot.setOtName(request.getOtName());
        ot.setLocation(request.getLocation());
        ot.setEquipmentAvailable(request.getEquipmentAvailable());
        if (request.getCapacity() != null) {
            ot.setCapacity(request.getCapacity());
        }
        if (request.getStatus() != null) {
            ot.setStatus(request.getStatus());
        }
        if (request.getActive() != null) {
            ot.setActive(request.getActive());
        }
    }

    public OperationTheatreResponse toResponse(OperationTheatre ot) {
        OperationTheatreResponse response = new OperationTheatreResponse();
        response.setId(ot.getId());
        response.setOtCode(ot.getOtCode());
        response.setOtName(ot.getOtName());
        response.setLocation(ot.getLocation());
        response.setEquipmentAvailable(ot.getEquipmentAvailable());
        response.setCapacity(ot.getCapacity());
        response.setStatus(ot.getStatus());
        response.setActive(ot.getActive());
        return response;
    }
}
