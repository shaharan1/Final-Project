package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.GenericRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.GenericResponse;

import java.util.List;

public interface GenericService {

    GenericResponse create(GenericRequest gr);
    List<GenericResponse> getAll();

}