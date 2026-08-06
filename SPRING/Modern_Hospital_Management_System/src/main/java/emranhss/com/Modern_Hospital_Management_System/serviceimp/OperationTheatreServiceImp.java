package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.OperationTheatreMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.OperationTheatreRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OperationTheatreResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.OperationTheatre;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.OperationTheatreRepository;
import emranhss.com.Modern_Hospital_Management_System.service.OperationTheatreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationTheatreServiceImp implements OperationTheatreService {

    private final OperationTheatreRepository operationTheatreRepository;
    private final OperationTheatreMapper operationTheatreMapper;

    public OperationTheatreServiceImp(OperationTheatreRepository operationTheatreRepository,
                                      OperationTheatreMapper operationTheatreMapper) {
        this.operationTheatreRepository = operationTheatreRepository;
        this.operationTheatreMapper = operationTheatreMapper;
    }

    @Override
    public OperationTheatreResponse create(OperationTheatreRequest request) {
        OperationTheatre ot = operationTheatreMapper.toEntity(request);
        return operationTheatreMapper.toResponse(operationTheatreRepository.save(ot));
    }

    @Override
    public OperationTheatreResponse getById(Long id) {
        return operationTheatreMapper.toResponse(findOrThrow(id));
    }

    @Override
    public List<OperationTheatreResponse> getAll() {
        return operationTheatreRepository.findAll().stream()
                .map(operationTheatreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationTheatreResponse> getActive() {
        return operationTheatreRepository.findByActiveTrueOrderByOtNameAsc().stream()
                .map(operationTheatreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationTheatreResponse> getByStatus(String status) {
        return operationTheatreRepository.findByStatus(status).stream()
                .map(operationTheatreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OperationTheatreResponse update(Long id, OperationTheatreRequest request) {
        OperationTheatre ot = findOrThrow(id);
        operationTheatreMapper.updateEntity(ot, request);
        return operationTheatreMapper.toResponse(operationTheatreRepository.save(ot));
    }

    @Override
    public void delete(Long id) {
        OperationTheatre ot = findOrThrow(id);
        operationTheatreRepository.delete(ot);
    }

    private OperationTheatre findOrThrow(Long id) {
        return operationTheatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperationTheatre not found with id: " + id));
    }
}
