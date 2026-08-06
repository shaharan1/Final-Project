package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.OperationTheatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperationTheatreRepository extends JpaRepository<OperationTheatre, Long> {

    Optional<OperationTheatre> findByOtCode(String otCode);

    List<OperationTheatre> findByActiveTrueOrderByOtNameAsc();

    List<OperationTheatre> findByStatus(String status);
}
