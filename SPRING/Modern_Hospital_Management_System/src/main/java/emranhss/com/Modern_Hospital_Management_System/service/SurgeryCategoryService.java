package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryCategoryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryCategoryResponse;

import java.util.List;

public interface SurgeryCategoryService {

    SurgeryCategoryResponse create(SurgeryCategoryRequest request);

    SurgeryCategoryResponse getById(Long id);

    List<SurgeryCategoryResponse> getAll();

    List<SurgeryCategoryResponse> getActive();

    SurgeryCategoryResponse update(Long id, SurgeryCategoryRequest request);

    void delete(Long id);
}
