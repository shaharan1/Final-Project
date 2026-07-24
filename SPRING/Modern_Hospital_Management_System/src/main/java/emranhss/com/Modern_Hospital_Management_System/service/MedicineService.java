package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineResponse;

import java.util.List;

public interface MedicineService {


    MedicineResponse create(MedicineRequest request);

    MedicineResponse getById(Long id);

    List<MedicineResponse> getAll();

//    List<MedicineResponse> getByPrescriptionId(Long prescriptionId);

    MedicineResponse update(Long id, MedicineRequest request);

    void delete(Long id);

    List<MedicineResponse>getMedicineByGeneric(Long Id);
    
    List<MedicineResponse> search(String keyword);
}
