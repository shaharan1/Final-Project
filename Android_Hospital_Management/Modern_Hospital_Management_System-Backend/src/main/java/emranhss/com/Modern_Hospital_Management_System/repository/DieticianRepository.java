package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Dietician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DieticianRepository extends JpaRepository<Dietician, Long> {
    List<Dietician> findByActiveTrue();
    Optional<Dietician> findByUserId(Long userId);
    Optional<Dietician> findByLicenseNumber(String licenseNumber);
    @Query("SELECT d FROM Dietician d WHERE LOWER(d.specialization) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Dietician> search(@Param("keyword") String keyword);
    long countByActiveTrue();
}
