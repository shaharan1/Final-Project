package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.PharmacySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacySaleRepository extends JpaRepository<PharmacySale, Long> {
}
