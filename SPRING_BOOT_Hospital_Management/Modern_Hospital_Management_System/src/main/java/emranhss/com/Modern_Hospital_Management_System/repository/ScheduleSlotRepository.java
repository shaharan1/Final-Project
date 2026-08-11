package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {

    // 1. Works perfectly: Dynamically matches a single slot by doctor, date, and appointment start time
    Optional<ScheduleSlot> findByDoctorIdAndDateAndStartTime(Long doctorId, LocalDate date, LocalTime startTime);

    // Changed name to match standard query parameters.
    // Since the method name explicitly ends with "IsBookedFalse", Spring automatically knows to search for is_booked = false.
    // It only needs the doctorId and date parameters to complete the query.
    List<ScheduleSlot> findByDoctorIdAndDateAndIsBookedFalse(Long doctorId, LocalDate date);

    List<ScheduleSlot> findByDoctorIdAndDateAndIsBookedTrue(Long doctorId, LocalDate date);

    // Boolean isBooked(Boolean isBooked);
    // This is invalid syntax for a Spring Data JPA query declaration and will crash your application on startup.
    // To check if a slot is booked, you use standard JpaRepository options like findById(id) instead.
}
