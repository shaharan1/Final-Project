package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.ReferenceRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferenceRangeRepository extends JpaRepository<ReferenceRange, Long> {
    List<ReferenceRange> findByTestParameterIdOrderByPriorityAsc(Long testParameterId);
    void deleteByTestParameterId(Long testParameterId);
}
