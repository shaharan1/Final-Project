package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceResponse;

import java.util.List;

public interface AmbulanceService {

    AmbulanceResponse create(AmbulanceRequest request);

    AmbulanceResponse getById(Long id);

    List<AmbulanceResponse> getAll();

    AmbulanceResponse update(Long id, AmbulanceRequest request);

    AmbulanceResponse updateStatus(Long id, String status);

    void delete(Long id);

    List<AmbulanceResponse> getByStatus(String status);

    Long getAvailableCount();
}
