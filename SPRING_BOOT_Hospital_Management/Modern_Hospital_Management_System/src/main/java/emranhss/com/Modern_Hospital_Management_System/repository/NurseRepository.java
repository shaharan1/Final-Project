package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    Optional<Nurse> findByUserEmail(String email);
    Optional<Nurse> findByRegistrationNumber(String registrationNumber);
    List<Nurse> findByAssignedWardAndActiveTrue(String assignedWard);
    List<Nurse> findByOnDutyTrueAndActiveTrue();


}
