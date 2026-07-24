package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import emranhss.com.Modern_Hospital_Management_System.dto.request.GenericRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.GenericResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Generic;
import emranhss.com.Modern_Hospital_Management_System.repository.GenericRepository;
import emranhss.com.Modern_Hospital_Management_System.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenericServiceImp implements GenericService {
    private final GenericRepository genericRepository;


    @Override
    public GenericResponse create(GenericRequest gr) {
        Generic g = new Generic();
        g.setGenericName(gr.getGenericName());
        Generic save = genericRepository.save(g);

        GenericResponse gt = new GenericResponse();
        gt.setId(save.getId());
        gt.setGenericName(save.getGenericName());

        return gt;
    }

    @Override
    public List<GenericResponse> getAll() {

        return genericRepository.findAll()
                .stream()
                .map(g -> new GenericResponse(
                        g.getId(),
                        g.getGenericName()))
                .toList();
    }
}
