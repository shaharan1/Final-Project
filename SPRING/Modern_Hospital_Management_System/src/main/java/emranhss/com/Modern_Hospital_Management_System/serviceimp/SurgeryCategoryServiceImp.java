package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.SurgeryCategoryMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryCategoryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryCategoryResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryCategory;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.SurgeryCategoryRepository;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurgeryCategoryServiceImp implements SurgeryCategoryService {

    private final SurgeryCategoryRepository surgeryCategoryRepository;
    private final SurgeryCategoryMapper surgeryCategoryMapper;

    public SurgeryCategoryServiceImp(SurgeryCategoryRepository surgeryCategoryRepository,
                                     SurgeryCategoryMapper surgeryCategoryMapper) {
        this.surgeryCategoryRepository = surgeryCategoryRepository;
        this.surgeryCategoryMapper = surgeryCategoryMapper;
    }

    @Override
    public SurgeryCategoryResponse create(SurgeryCategoryRequest request) {
        SurgeryCategory category = surgeryCategoryMapper.toEntity(request);
        return surgeryCategoryMapper.toResponse(surgeryCategoryRepository.save(category));
    }

    @Override
    public SurgeryCategoryResponse getById(Long id) {
        return surgeryCategoryMapper.toResponse(findOrThrow(id));
    }

    @Override
    public List<SurgeryCategoryResponse> getAll() {
        return surgeryCategoryRepository.findAll().stream()
                .map(surgeryCategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryCategoryResponse> getActive() {
        return surgeryCategoryRepository.findByActiveTrueOrderBySortOrderAsc().stream()
                .map(surgeryCategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SurgeryCategoryResponse update(Long id, SurgeryCategoryRequest request) {
        SurgeryCategory category = findOrThrow(id);
        surgeryCategoryMapper.updateEntity(category, request);
        return surgeryCategoryMapper.toResponse(surgeryCategoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        SurgeryCategory category = findOrThrow(id);
        surgeryCategoryRepository.delete(category);
    }

    private SurgeryCategory findOrThrow(Long id) {
        return surgeryCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SurgeryCategory not found with id: " + id));
    }
}
