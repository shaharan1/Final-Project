package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
