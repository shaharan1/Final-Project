package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyTimelineRepository extends JpaRepository<EmergencyTimeline, Long> {
    List<EmergencyTimeline> findByEmergencyPatientIdOrderByEventTimeAsc(Long emergencyPatientId);
    List<EmergencyTimeline> findByEventType(String eventType);
    List<EmergencyTimeline> findTop20ByOrderByEventTimeDesc();
}
