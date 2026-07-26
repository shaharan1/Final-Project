package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.KitchenOrder;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.KitchenOrderRepository;
import emranhss.com.Modern_Hospital_Management_System.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenOrderServiceImp implements KitchenOrderService {

    private final KitchenOrderRepository kitchenOrderRepository;

    @Override
    @Transactional
    public KitchenOrder create(KitchenOrder order) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        long todayCount = kitchenOrderRepository.countTodayOrders(startOfDay, endOfDay);
        String orderNumber = "KO-" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%03d", todayCount + 1);
        order.setOrderNumber(orderNumber);
        return kitchenOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public KitchenOrder getById(Long id) {
        return kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KitchenOrder not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KitchenOrder> getAll() {
        return kitchenOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KitchenOrder> getByStatus(String status) {
        return kitchenOrderRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KitchenOrder> getByMealTime(String mealTime) {
        return kitchenOrderRepository.findByMealTime(mealTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KitchenOrder> getTodayOrders() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return kitchenOrderRepository.findTodayOrders(startOfDay, endOfDay);
    }

    @Override
    @Transactional
    public KitchenOrder updateStatus(Long id, String status, String notes) {
        KitchenOrder existing = kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KitchenOrder not found with id: " + id));
        existing.setStatus(status);
        if (notes != null) {
            existing.setKitchenNotes(notes);
        }
        LocalDateTime now = LocalDateTime.now();
        switch (status) {
            case "PREPARING":
                existing.setPreparingAt(now);
                break;
            case "COOKING":
                existing.setCookingAt(now);
                break;
            case "READY":
                existing.setReadyAt(now);
                break;
            case "DELIVERED":
                existing.setDeliveredAt(now);
                break;
            case "CANCELLED":
                existing.setCancelledAt(now);
                break;
        }
        return kitchenOrderRepository.save(existing);
    }

    @Override
    @Transactional
    public KitchenOrder update(Long id, KitchenOrder order) {
        KitchenOrder existing = kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KitchenOrder not found with id: " + id));
        existing.setPatient(order.getPatient());
        existing.setAdmittedPatient(order.getAdmittedPatient());
        existing.setDietPlan(order.getDietPlan());
        existing.setDietAssignment(order.getDietAssignment());
        existing.setWard(order.getWard());
        existing.setBedNumber(order.getBedNumber());
        existing.setMealTime(order.getMealTime());
        existing.setMealType(order.getMealType());
        existing.setDietType(order.getDietType());
        existing.setPriority(order.getPriority());
        existing.setKitchenNotes(order.getKitchenNotes());
        existing.setPreparedBy(order.getPreparedBy());
        existing.setDeliveredBy(order.getDeliveredBy());
        existing.setSpecialDiet(order.getSpecialDiet());
        return kitchenOrderRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        KitchenOrder existing = kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KitchenOrder not found with id: " + id));
        existing.setStatus("CANCELLED");
        existing.setCancelledAt(LocalDateTime.now());
        kitchenOrderRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountByStatus(String status) {
        return kitchenOrderRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTodayOrderCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return kitchenOrderRepository.countTodayOrders(startOfDay, endOfDay);
    }
}
