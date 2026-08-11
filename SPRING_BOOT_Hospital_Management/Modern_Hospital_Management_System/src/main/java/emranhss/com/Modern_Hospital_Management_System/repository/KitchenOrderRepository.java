package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
    List<KitchenOrder> findByStatus(String status);
    List<KitchenOrder> findByMealTime(String mealTime);
    List<KitchenOrder> findByMealTimeAndStatus(String mealTime, String status);
    List<KitchenOrder> findByPatientId(Long patientId);
    List<KitchenOrder> findByWardId(Long wardId);
    List<KitchenOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<KitchenOrder> findByOrderNumber(String orderNumber);
    Optional<KitchenOrder> findByOrderNumberAndIdNot(String orderNumber, Long id);
    long countByStatus(String status);
    long countByMealTime(String mealTime);
    @Query("SELECT ko FROM KitchenOrder ko WHERE ko.createdAt >= :startOfDay AND ko.createdAt < :endOfDay ORDER BY ko.createdAt DESC")
    List<KitchenOrder> findTodayOrders(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    @Query("SELECT COUNT(ko) FROM KitchenOrder ko WHERE ko.createdAt >= :startOfDay AND ko.createdAt < :endOfDay")
    long countTodayOrders(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
