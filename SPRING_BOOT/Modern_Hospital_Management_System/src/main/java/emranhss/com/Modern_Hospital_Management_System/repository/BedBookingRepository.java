package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.BedBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BedBookingRepository extends JpaRepository<BedBooking, Long> {


    Optional<BedBooking> findByAdmittedPatientIdAndActiveTrue(Long admittedPatientId);

}
