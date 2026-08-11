package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.MealMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.MealRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Meal;
import emranhss.com.Modern_Hospital_Management_System.entity.MealMaster;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MealMasterRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.MealRepository;
import emranhss.com.Modern_Hospital_Management_System.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealMasterRepository mealMasterRepository;
    private final MealMapper mealMapper;

    @Override
    @Transactional
    public MealResponse logMealConsumption(MealRequest request) {
        // Fetch current snapshot cost value from system master catalog to prevent historic price inflation shifts
        MealMaster masterCatalogItem = mealMasterRepository.findById(request.getMealMasterId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Meal Master Catalog Definition: " + request.getMealMasterId()));

        Meal meal = mealMapper.toEntity(request);
        meal.setMealCost(masterCatalogItem.getPrice());

        if (meal.getServedAt() == null) {
            meal.setServedAt(LocalDateTime.now());
        }

        Meal savedLog = mealRepository.save(meal);
        return mealMapper.toResponse(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public MealResponse getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal record transaction entry target unresolvable: " + id));
        return mealMapper.toResponse(meal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealResponse> getAllMeals() {
        return mealRepository.findAll().stream()
                .map(mealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealResponse> getMealsByPatient(Long admittedPatientId) {
        return mealRepository.findByAdmittedPatientId(admittedPatientId).stream()
                .map(mealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MealResponse updateMealLog(Long id, MealRequest request) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal log variant mismatch: " + id));

        mealMapper.updateEntityFromRequest(request, meal);
        Meal updatedLog = mealRepository.save(meal);
        return mealMapper.toResponse(updatedLog);
    }

    @Override
    @Transactional
    public void deleteMealLog(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal tracking sequence pointer null execution blocked: " + id));
        mealRepository.delete(meal);
    }
}
