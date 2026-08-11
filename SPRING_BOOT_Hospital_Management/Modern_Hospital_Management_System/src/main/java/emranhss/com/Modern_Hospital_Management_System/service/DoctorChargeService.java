package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorChargeResponse;

import java.util.List;

public interface DoctorChargeService {




    DoctorChargeResponse create(DoctorChargeRequest request);

    DoctorChargeResponse getById(Long id);

    List<DoctorChargeResponse> getAll();

    DoctorChargeResponse update(Long id, DoctorChargeRequest request);

    void delete(Long id);
}
