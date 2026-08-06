package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorDiscountRepository extends JpaRepository<DoctorDiscount, Long> {

    Optional<DoctorDiscount> findByDoctorId(Long doctorId);

    Optional<DoctorDiscount> findByDoctorIdAndActiveTrue(Long doctorId);

    List<DoctorDiscount> findByActiveTrue();
}
