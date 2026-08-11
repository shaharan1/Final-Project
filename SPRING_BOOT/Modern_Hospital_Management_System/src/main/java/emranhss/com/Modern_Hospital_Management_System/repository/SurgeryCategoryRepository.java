package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurgeryCategoryRepository extends JpaRepository<SurgeryCategory, Long> {
    Optional<SurgeryCategory> findByCode(String code);
    List<SurgeryCategory> findByActiveTrueOrderBySortOrderAsc();
    List<SurgeryCategory> findByActive(Boolean active);
}
