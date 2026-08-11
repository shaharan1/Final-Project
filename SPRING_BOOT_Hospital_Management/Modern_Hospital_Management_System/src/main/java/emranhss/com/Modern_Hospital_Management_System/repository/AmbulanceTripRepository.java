package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.AmbulanceTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AmbulanceTripRepository extends JpaRepository<AmbulanceTrip, Long> {
    List<AmbulanceTrip> findByAmbulanceId(Long ambulanceId);
    List<AmbulanceTrip> findByStatus(String status);
    List<AmbulanceTrip> findByEmergencyPatientId(Long emergencyPatientId);
    List<AmbulanceTrip> findByDispatchTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByStatus(String status);
}
