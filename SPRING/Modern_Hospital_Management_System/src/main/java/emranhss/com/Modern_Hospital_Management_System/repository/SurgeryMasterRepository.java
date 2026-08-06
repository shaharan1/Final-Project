package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SurgeryMasterRepository extends JpaRepository<SurgeryMaster, Long> {

    Optional<SurgeryMaster> findBySurgeryCode(String surgeryCode);

    List<SurgeryMaster> findByActiveTrueOrderBySurgeryNameAsc();

    List<SurgeryMaster> findByCategoryId(Long categoryId);

    @Query("SELECT m FROM SurgeryMaster m WHERE m.active = true AND " +
            "(LOWER(m.surgeryName) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(m.surgeryCode) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "ORDER BY m.surgeryName ASC")
    List<SurgeryMaster> search(@Param("q") String q);

    @Query("SELECT m.category.name, COUNT(m), SUM(m.standardRate) FROM SurgeryMaster m " +
            "GROUP BY m.category.name ORDER BY COUNT(m) DESC")
    List<Object[]> countByCategory();
}
