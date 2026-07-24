package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByAdmittedPatientId(Long admittedPatientId);
    List<Meal> findByBillingStatus(String billingStatus);
}
