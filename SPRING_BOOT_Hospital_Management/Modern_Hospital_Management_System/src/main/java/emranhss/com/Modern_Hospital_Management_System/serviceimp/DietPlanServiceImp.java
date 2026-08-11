package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.DietPlan;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DietPlanRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DietPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DietPlanServiceImp implements DietPlanService {

    private final DietPlanRepository dietPlanRepository;

    @Override
    @Transactional
    public DietPlan create(DietPlan dietPlan) {
        return dietPlanRepository.save(dietPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public DietPlan getById(Long id) {
        return dietPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietPlan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietPlan> getAll() {
        return dietPlanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietPlan> getActive() {
        return dietPlanRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietPlan> getByDietType(String dietType) {
        return dietPlanRepository.findByDietType(dietType);
    }

    @Override
    @Transactional
    public DietPlan update(Long id, DietPlan dietPlan) {
        DietPlan existing = dietPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietPlan not found with id: " + id));
        existing.setName(dietPlan.getName());
        existing.setDietType(dietPlan.getDietType());
        existing.setDescription(dietPlan.getDescription());
        existing.setCreatedByDietician(dietPlan.getCreatedByDietician());
        existing.setApprovedByDoctor(dietPlan.getApprovedByDoctor());
        existing.setBreakfast(dietPlan.getBreakfast());
        existing.setBreakfastTime(dietPlan.getBreakfastTime());
        existing.setMorningSnacks(dietPlan.getMorningSnacks());
        existing.setMorningSnacksTime(dietPlan.getMorningSnacksTime());
        existing.setLunch(dietPlan.getLunch());
        existing.setLunchTime(dietPlan.getLunchTime());
        existing.setEveningSnacks(dietPlan.getEveningSnacks());
        existing.setEveningSnacksTime(dietPlan.getEveningSnacksTime());
        existing.setDinner(dietPlan.getDinner());
        existing.setDinnerTime(dietPlan.getDinnerTime());
        existing.setNightDiet(dietPlan.getNightDiet());
        existing.setNightDietTime(dietPlan.getNightDietTime());
        existing.setTotalCalories(dietPlan.getTotalCalories());
        existing.setProtein(dietPlan.getProtein());
        existing.setCarbohydrate(dietPlan.getCarbohydrate());
        existing.setFat(dietPlan.getFat());
        existing.setFiber(dietPlan.getFiber());
        existing.setSodium(dietPlan.getSodium());
        existing.setPotassium(dietPlan.getPotassium());
        existing.setWaterIntakeMl(dietPlan.getWaterIntakeMl());
        existing.setVitaminRecommendation(dietPlan.getVitaminRecommendation());
        existing.setDoctorRecommendation(dietPlan.getDoctorRecommendation());
        existing.setDieticianNotes(dietPlan.getDieticianNotes());
        existing.setPricePerDay(dietPlan.getPricePerDay());
        existing.setActive(dietPlan.getActive());
        return dietPlanRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DietPlan existing = dietPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietPlan not found with id: " + id));
        existing.setActive(false);
        dietPlanRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietPlan> search(String keyword) {
        return dietPlanRepository.search(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveCount() {
        return dietPlanRepository.countByActiveTrue();
    }
}
