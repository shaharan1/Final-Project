package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.MealMasterMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.MealMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MealMaster;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.MealMasterRepository;
import emranhss.com.Modern_Hospital_Management_System.service.MealMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealMasterServiceImpl implements MealMasterService {

    private final MealMasterRepository mealMasterRepository;
    private final MealMasterMapper mealMasterMapper;

    @Override
    @Transactional
    public MealMasterResponse createMealMaster(MealMasterRequest request) {
        MealMaster mealMaster = mealMasterMapper.toEntity(request);
        MealMaster saved = mealMasterRepository.save(mealMaster);
        return mealMasterMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MealMasterResponse getMealMasterById(Long id) {
        MealMaster mealMaster = mealMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Menu Item not found with id: " + id));
        return mealMasterMapper.toResponse(mealMaster);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealMasterResponse> getAllMealMasters() {
        return mealMasterRepository.findAll().stream()
                .map(mealMasterMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealMasterResponse> getActiveMealMasters() {
        return mealMasterRepository.findByActiveTrue().stream()
                .map(mealMasterMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MealMasterResponse updateMealMaster(Long id, MealMasterRequest request) {
        MealMaster mealMaster = mealMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Menu Item not found with id: " + id));
        mealMasterMapper.updateEntityFromRequest(request, mealMaster);
        MealMaster updated = mealMasterRepository.save(mealMaster);
        return mealMasterMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteMealMaster(Long id) {
        MealMaster mealMaster = mealMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal Menu Item not found with id: " + id));
        mealMasterRepository.delete(mealMaster);
    }
}
