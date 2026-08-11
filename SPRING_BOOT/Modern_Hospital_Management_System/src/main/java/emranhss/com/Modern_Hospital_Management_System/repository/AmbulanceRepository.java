package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    Optional<Ambulance> findByAmbulanceNumber(String ambulanceNumber);
    List<Ambulance> findByStatus(String status);
    List<Ambulance> findByIsActive(Boolean isActive);
    long countByStatus(String status);
}
