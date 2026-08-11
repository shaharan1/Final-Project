package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {



    // Automatically creates a MySQL query: SELECT * FROM doctors WHERE specialization = ?
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByDoctorDepartmentId(Long departmentId);
    Optional<Doctor> findByUserId(Long userId);

}
