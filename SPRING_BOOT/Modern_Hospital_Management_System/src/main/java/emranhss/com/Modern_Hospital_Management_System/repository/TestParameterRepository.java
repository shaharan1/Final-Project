package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestParameterRepository extends JpaRepository<TestParameter, Long> {
    List<TestParameter> findByTestMasterIdOrderByDisplayOrderAsc(Long testMasterId);
    List<TestParameter> findByActiveTrueOrderByDisplayOrderAsc();
    void deleteByTestMasterId(Long testMasterId);
}
