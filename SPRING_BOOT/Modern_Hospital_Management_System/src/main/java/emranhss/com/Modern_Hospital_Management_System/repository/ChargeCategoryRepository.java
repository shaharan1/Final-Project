package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.ChargeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargeCategoryRepository extends JpaRepository<ChargeCategory, Long> {
    Optional<ChargeCategory> findByCode(String code);
    List<ChargeCategory> findByActiveTrueOrderBySortOrder();
    boolean existsByCode(String code);
}
