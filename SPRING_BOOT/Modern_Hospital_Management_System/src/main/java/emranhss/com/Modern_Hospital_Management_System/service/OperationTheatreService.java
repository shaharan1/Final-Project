package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.OperationTheatreRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OperationTheatreResponse;

import java.util.List;

public interface OperationTheatreService {

    OperationTheatreResponse create(OperationTheatreRequest request);

    OperationTheatreResponse getById(Long id);

    List<OperationTheatreResponse> getAll();

    List<OperationTheatreResponse> getActive();

    List<OperationTheatreResponse> getByStatus(String status);

    OperationTheatreResponse update(Long id, OperationTheatreRequest request);

    void delete(Long id);
}
