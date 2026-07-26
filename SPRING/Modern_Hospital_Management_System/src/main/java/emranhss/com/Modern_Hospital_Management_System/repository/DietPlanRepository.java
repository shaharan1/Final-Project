package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findByActiveTrue();
    List<DietPlan> findByDietType(String dietType);
    List<DietPlan> findByDietTypeAndActiveTrue(String dietType);
    @Query("SELECT dp FROM DietPlan dp WHERE LOWER(dp.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(dp.dietType) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<DietPlan> search(@Param("keyword") String keyword);
    long countByActiveTrue();
    long countByDietType(String dietType);
}
