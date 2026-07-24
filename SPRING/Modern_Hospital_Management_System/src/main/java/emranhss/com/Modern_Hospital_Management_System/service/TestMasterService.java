package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.TestMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TestMasterResponse;

import java.util.List;

public interface TestMasterService {

    TestMasterResponse save(TestMasterRequest request);

    TestMasterResponse update(Long id, TestMasterRequest request);

    void delete(Long id);

    TestMasterResponse getById(Long id);

    List<TestMasterResponse> getAll();

    List<TestMasterResponse> search(String keyword);
}
