package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.MealMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MealMasterRepository extends JpaRepository<MealMaster, Long> {
    List<MealMaster> findByActiveTrue();
    List<MealMaster> findByCategory(String category);
}
