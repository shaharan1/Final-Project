package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.OfficeStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficeStaffRepository extends JpaRepository<OfficeStaff, Long> {

    Optional<OfficeStaff> findByEmail(String email);

    List<OfficeStaff> findByDepartment(String department);

    List<OfficeStaff> findByPosition(String position);

}