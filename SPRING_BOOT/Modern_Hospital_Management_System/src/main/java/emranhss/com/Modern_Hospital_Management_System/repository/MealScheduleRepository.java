package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.MealSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealScheduleRepository extends JpaRepository<MealSchedule, Long> {
    List<MealSchedule> findByStatusOrderByServingTimeAsc(String status);
    Optional<MealSchedule> findByMealName(String mealName);
    Optional<MealSchedule> findByCurrentMealTrue();
    List<MealSchedule> findAllByOrderByServingTimeAsc();
}
