package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Ambulance;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AmbulanceMapper {

    public Ambulance toEntity(AmbulanceRequest request) {
        if (request == null) return null;
        Ambulance entity = new Ambulance();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public AmbulanceResponse toResponse(Ambulance entity) {
        if (entity == null) return null;
        AmbulanceResponse response = new AmbulanceResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public void updateEntityFromRequest(AmbulanceRequest request, Ambulance entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "ambulanceNumber", "createdAt", "updatedAt");
    }
}
