package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MealRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealResponse;
import java.util.List;

public interface MealService {
    MealResponse logMealConsumption(MealRequest request);
    MealResponse getMealById(Long id);
    List<MealResponse> getAllMeals();
    List<MealResponse> getMealsByPatient(Long admittedPatientId);
    MealResponse updateMealLog(Long id, MealRequest request);
    void deleteMealLog(Long id);
}
