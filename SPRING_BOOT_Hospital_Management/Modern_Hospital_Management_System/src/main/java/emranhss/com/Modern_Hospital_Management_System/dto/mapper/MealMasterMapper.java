package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MealMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MealMaster;
import org.springframework.stereotype.Component;

@Component
public class MealMasterMapper {

    public MealMasterResponse toResponse(MealMaster entity) {
        if (entity == null) return null;

        MealMasterResponse response = new MealMasterResponse();
        response.setId(entity.getId());
        response.setCategory(entity.getCategory());
        response.setType(entity.getType());
        response.setName(entity.getName());
        response.setDetails(entity.getDetails());
        response.setPrice(entity.getPrice());
        response.setActive(entity.getActive());
        return response;
    }

    public MealMaster toEntity(MealMasterRequest request) {
        if (request == null) return null;

        MealMaster entity = new MealMaster();
        entity.setCategory(request.getCategory());
        entity.setType(request.getType());
        entity.setName(request.getName());
        entity.setDetails(request.getDetails());
        entity.setPrice(request.getPrice());
        entity.setActive(request.getActive() != null ? request.getActive() : true);
        return entity;
    }

    public void updateEntityFromRequest(MealMasterRequest request, MealMaster entity) {
        if (request == null || entity == null) return;

        if (request.getCategory() != null) entity.setCategory(request.getCategory());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDetails() != null) entity.setDetails(request.getDetails());
        if (request.getPrice() != null) entity.setPrice(request.getPrice());
        if (request.getActive() != null) entity.setActive(request.getActive());
    }
}
