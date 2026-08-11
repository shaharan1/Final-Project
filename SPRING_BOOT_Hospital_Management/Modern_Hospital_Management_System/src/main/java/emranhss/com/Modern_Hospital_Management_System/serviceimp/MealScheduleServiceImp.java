package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.MealSchedule;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MealScheduleRepository;
import emranhss.com.Modern_Hospital_Management_System.service.MealScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealScheduleServiceImp implements MealScheduleService {

    private final MealScheduleRepository mealScheduleRepository;

    @Override
    @Transactional
    public MealSchedule create(MealSchedule schedule) {
        return mealScheduleRepository.save(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public MealSchedule getById(Long id) {
        return mealScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MealSchedule not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealSchedule> getAll() {
        return mealScheduleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealSchedule> getActive() {
        return mealScheduleRepository.findByStatusOrderByServingTimeAsc("ACTIVE");
    }

    @Override
    @Transactional
    public MealSchedule update(Long id, MealSchedule schedule) {
        MealSchedule existing = mealScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MealSchedule not found with id: " + id));
        existing.setMealName(schedule.getMealName());
        existing.setServingTime(schedule.getServingTime());
        existing.setPreparationStartTime(schedule.getPreparationStartTime());
        existing.setPreparationEndTime(schedule.getPreparationEndTime());
        existing.setStatus(schedule.getStatus());
        existing.setTotalOrdersToday(schedule.getTotalOrdersToday());
        existing.setCompletedOrders(schedule.getCompletedOrders());
        existing.setPendingOrders(schedule.getPendingOrders());
        existing.setCancelledOrders(schedule.getCancelledOrders());
        existing.setNotes(schedule.getNotes());
        existing.setCurrentMeal(schedule.getCurrentMeal());
        return mealScheduleRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MealSchedule existing = mealScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MealSchedule not found with id: " + id));
        existing.setStatus("INACTIVE");
        mealScheduleRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public MealSchedule getCurrentMeal() {
        return mealScheduleRepository.findByCurrentMealTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No current meal found"));
    }
}
