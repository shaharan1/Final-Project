package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import emranhss.com.Modern_Hospital_Management_System.dto.request.GenericRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.GenericResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Generic;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.GenericRepository;
import emranhss.com.Modern_Hospital_Management_System.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenericServiceImp implements GenericService {
    private final GenericRepository genericRepository;

    @Override
    @Transactional
    public GenericResponse create(GenericRequest gr) {
        Generic g = new Generic();
        g.setGenericName(gr.getGenericName());
        return toResponse(genericRepository.save(g));
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse getById(Long id) {
        Generic g = genericRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Generic not found with ID: " + id));
        return toResponse(g);
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, GenericRequest gr) {
        Generic g = genericRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Generic not found with ID: " + id));
        g.setGenericName(gr.getGenericName());
        return toResponse(genericRepository.save(g));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenericResponse> getAll() {
        return genericRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Generic g = genericRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Generic not found with ID: " + id));
        genericRepository.delete(g);
    }

    private GenericResponse toResponse(Generic g) {
        return new GenericResponse(g.getId(), g.getGenericName());
    }
}
