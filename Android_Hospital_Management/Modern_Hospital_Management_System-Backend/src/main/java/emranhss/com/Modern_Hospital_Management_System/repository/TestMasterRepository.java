package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.TestMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TestMasterRepository extends JpaRepository<TestMaster, Long> {

    Optional<TestMaster> findByTestCode(String testCode);

    List<TestMaster> findByTestNameContainingIgnoreCaseOrTestCodeContainingIgnoreCase(
            String testName,
            String testCode
    );

}
