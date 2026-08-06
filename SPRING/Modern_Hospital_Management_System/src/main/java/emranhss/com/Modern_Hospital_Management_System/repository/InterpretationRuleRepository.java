package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.InterpretationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterpretationRuleRepository extends JpaRepository<InterpretationRule, Long> {
    List<InterpretationRule> findByTestParameterIdOrderByDisplayOrderAsc(Long testParameterId);
    void deleteByTestParameterId(Long testParameterId);
}
