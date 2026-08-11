package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DoctorDepartmentRepository extends JpaRepository<DoctorDepartment, Long> {
    
    Optional<DoctorDepartment> findByDepartmentNameIgnoreCase(String departmentName);

    DoctorDepartment findById(long id);
}
