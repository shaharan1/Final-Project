package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MealMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealMasterResponse;
import java.util.List;

public interface MealMasterService {
    MealMasterResponse createMealMaster(MealMasterRequest request);
    MealMasterResponse getMealMasterById(Long id);
    List<MealMasterResponse> getAllMealMasters();
    List<MealMasterResponse> getActiveMealMasters();
    MealMasterResponse updateMealMaster(Long id, MealMasterRequest request);
    void deleteMealMaster(Long id);
}
