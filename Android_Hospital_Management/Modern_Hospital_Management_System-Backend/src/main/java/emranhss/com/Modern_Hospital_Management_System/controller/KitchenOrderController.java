package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.KitchenOrder;
import emranhss.com.Modern_Hospital_Management_System.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kitchen/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
public class KitchenOrderController {

    private final KitchenOrderService kitchenOrderService;

    @PostMapping
    public ResponseEntity<KitchenOrder> create(@RequestBody KitchenOrder order) {
        return new ResponseEntity<>(kitchenOrderService.create(order), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitchenOrder> getById(@PathVariable Long id) {
        return ResponseEntity.ok(kitchenOrderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<KitchenOrder>> getAll() {
        return ResponseEntity.ok(kitchenOrderService.getAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<KitchenOrder>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(kitchenOrderService.getByStatus(status));
    }

    @GetMapping("/meal-time/{mealTime}")
    public ResponseEntity<List<KitchenOrder>> getByMealTime(@PathVariable String mealTime) {
        return ResponseEntity.ok(kitchenOrderService.getByMealTime(mealTime));
    }

    @GetMapping("/today")
    public ResponseEntity<List<KitchenOrder>> getTodayOrders() {
        return ResponseEntity.ok(kitchenOrderService.getTodayOrders());
    }

    @GetMapping("/count/{status}")
    public ResponseEntity<Long> getCountByStatus(@PathVariable String status) {
        return ResponseEntity.ok(kitchenOrderService.getCountByStatus(status));
    }

    @GetMapping("/today-count")
    public ResponseEntity<Long> getTodayOrderCount() {
        return ResponseEntity.ok(kitchenOrderService.getTodayOrderCount());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<KitchenOrder> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(kitchenOrderService.updateStatus(id, status, notes));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitchenOrder> update(@PathVariable Long id, @RequestBody KitchenOrder order) {
        return ResponseEntity.ok(kitchenOrderService.update(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitchenOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
