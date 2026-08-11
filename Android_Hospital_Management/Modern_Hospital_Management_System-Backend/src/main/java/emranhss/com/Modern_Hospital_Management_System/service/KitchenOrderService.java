package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.KitchenOrder;
import java.util.List;

public interface KitchenOrderService {
    KitchenOrder create(KitchenOrder order);
    KitchenOrder getById(Long id);
    List<KitchenOrder> getAll();
    List<KitchenOrder> getByStatus(String status);
    List<KitchenOrder> getByMealTime(String mealTime);
    List<KitchenOrder> getTodayOrders();
    KitchenOrder updateStatus(Long id, String status, String notes);
    KitchenOrder update(Long id, KitchenOrder order);
    void delete(Long id);
    long getCountByStatus(String status);
    long getTodayOrderCount();
}
