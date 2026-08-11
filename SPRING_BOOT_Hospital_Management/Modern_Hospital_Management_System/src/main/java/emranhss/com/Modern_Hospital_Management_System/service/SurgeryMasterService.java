package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryMasterResponse;

import java.util.List;

public interface SurgeryMasterService {

    SurgeryMasterResponse create(SurgeryMasterRequest request);

    SurgeryMasterResponse getById(Long id);

    List<SurgeryMasterResponse> getAll();

    List<SurgeryMasterResponse> getByCategoryId(Long categoryId);

    List<SurgeryMasterResponse> getActive();

    List<SurgeryMasterResponse> search(String q);

    SurgeryMasterResponse update(Long id, SurgeryMasterRequest request);

    void delete(Long id);
}
