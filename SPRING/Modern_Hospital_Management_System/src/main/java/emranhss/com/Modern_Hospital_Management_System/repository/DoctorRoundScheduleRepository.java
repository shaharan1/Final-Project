package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DoctorRoundSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRoundScheduleRepository extends JpaRepository<DoctorRoundSchedule, Long> {
    List<DoctorRoundSchedule> findByDoctorId(Long doctorId);
    List<DoctorRoundSchedule> findByWardId(Long wardId);
}
