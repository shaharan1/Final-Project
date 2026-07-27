package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.DietPlan;
import java.util.List;

public interface DietPlanService {
    DietPlan create(DietPlan dietPlan);
    DietPlan getById(Long id);
    List<DietPlan> getAll();
    List<DietPlan> getActive();
    List<DietPlan> getByDietType(String dietType);
    DietPlan update(Long id, DietPlan dietPlan);
    void delete(Long id);
    List<DietPlan> search(String keyword);
    long getActiveCount();
}
