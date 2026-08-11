package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.LabRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabRuleRepository extends JpaRepository<LabRule, Long> {
    List<LabRule> findByActiveTrueOrderByPriorityAsc();
}
