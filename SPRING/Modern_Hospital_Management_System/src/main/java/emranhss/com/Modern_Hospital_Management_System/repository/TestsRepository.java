package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TestsRepository extends JpaRepository<Tests, Long> {

    List<Tests> findByPrescriptionId(Long prescriptionId);
}
