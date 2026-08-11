package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryCategoryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryCategoryResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryCategory;
import org.springframework.stereotype.Component;

@Component
public class SurgeryCategoryMapper {

    public SurgeryCategory toEntity(SurgeryCategoryRequest request) {
        SurgeryCategory category = new SurgeryCategory();
        applyRequest(category, request);
        return category;
    }

    public void updateEntity(SurgeryCategory category, SurgeryCategoryRequest request) {
        applyRequest(category, request);
    }

    private void applyRequest(SurgeryCategory category, SurgeryCategoryRequest request) {
        category.setCode(request.getCode());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
    }

    public SurgeryCategoryResponse toResponse(SurgeryCategory category) {
        SurgeryCategoryResponse response = new SurgeryCategoryResponse();
        response.setId(category.getId());
        response.setCode(category.getCode());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setActive(category.getActive());
        response.setSortOrder(category.getSortOrder());
        return response;
    }
}
