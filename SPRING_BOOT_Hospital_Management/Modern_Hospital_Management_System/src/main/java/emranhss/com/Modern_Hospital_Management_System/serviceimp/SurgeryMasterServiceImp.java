package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.SurgeryMasterMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryCategory;
import emranhss.com.Modern_Hospital_Management_System.entity.SurgeryMaster;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.SurgeryCategoryRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.SurgeryMasterRepository;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryMasterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurgeryMasterServiceImp implements SurgeryMasterService {

    private final SurgeryMasterRepository surgeryMasterRepository;
    private final SurgeryCategoryRepository surgeryCategoryRepository;
    private final SurgeryMasterMapper surgeryMasterMapper;

    public SurgeryMasterServiceImp(SurgeryMasterRepository surgeryMasterRepository,
                                   SurgeryCategoryRepository surgeryCategoryRepository,
                                   SurgeryMasterMapper surgeryMasterMapper) {
        this.surgeryMasterRepository = surgeryMasterRepository;
        this.surgeryCategoryRepository = surgeryCategoryRepository;
        this.surgeryMasterMapper = surgeryMasterMapper;
    }

    @Override
    public SurgeryMasterResponse create(SurgeryMasterRequest request) {
        SurgeryCategory category = resolveCategory(request.getCategoryId());
        SurgeryMaster master = surgeryMasterMapper.toEntity(request, category);
        return surgeryMasterMapper.toResponse(surgeryMasterRepository.save(master));
    }

    @Override
    public SurgeryMasterResponse getById(Long id) {
        return surgeryMasterMapper.toResponse(findOrThrow(id));
    }

    @Override
    public List<SurgeryMasterResponse> getAll() {
        return surgeryMasterRepository.findAll().stream()
                .map(surgeryMasterMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryMasterResponse> getByCategoryId(Long categoryId) {
        return surgeryMasterRepository.findByCategoryId(categoryId).stream()
                .map(surgeryMasterMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryMasterResponse> getActive() {
        return surgeryMasterRepository.findByActiveTrueOrderBySurgeryNameAsc().stream()
                .map(surgeryMasterMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryMasterResponse> search(String q) {
        return surgeryMasterRepository.search(q).stream()
                .map(surgeryMasterMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SurgeryMasterResponse update(Long id, SurgeryMasterRequest request) {
        SurgeryMaster master = findOrThrow(id);
        SurgeryCategory category = resolveCategory(request.getCategoryId());
        surgeryMasterMapper.updateEntity(master, request, category);
        return surgeryMasterMapper.toResponse(surgeryMasterRepository.save(master));
    }

    @Override
    public void delete(Long id) {
        SurgeryMaster master = findOrThrow(id);
        surgeryMasterRepository.delete(master);
    }

    private SurgeryMaster findOrThrow(Long id) {
        return surgeryMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SurgeryMaster not found with id: " + id));
    }

    private SurgeryCategory resolveCategory(Long categoryId) {
        if (categoryId == null) {
            throw new ResourceNotFoundException("Surgery category is required");
        }
        return surgeryCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery category not found with id: " + categoryId));
    }
}
