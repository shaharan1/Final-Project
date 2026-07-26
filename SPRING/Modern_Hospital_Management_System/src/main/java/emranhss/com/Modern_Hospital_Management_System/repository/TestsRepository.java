package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestsRepository extends JpaRepository<Tests, Long> {

    List<Tests> findByPrescriptionId(Long prescriptionId);

    List<Tests> findByPatientId(Long patientId);

    List<Tests> findByPrescribedById(Long doctorId);

    List<Tests> findByOrderStatus(String orderStatus);

    List<Tests> findByPatientIdAndOrderStatus(Long patientId, String orderStatus);

    List<Tests> findByPrescribedByIdAndOrderStatus(Long doctorId, String orderStatus);

    long countByOrderStatus(String orderStatus);
}
