package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.MealSchedule;
import java.util.List;

public interface MealScheduleService {
    MealSchedule create(MealSchedule schedule);
    MealSchedule getById(Long id);
    List<MealSchedule> getAll();
    List<MealSchedule> getActive();
    MealSchedule update(Long id, MealSchedule schedule);
    void delete(Long id);
    MealSchedule getCurrentMeal();
}
