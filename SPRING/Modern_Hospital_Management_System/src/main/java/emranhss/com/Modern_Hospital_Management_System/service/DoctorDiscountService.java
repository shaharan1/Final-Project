package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDiscountRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDiscountResponse;

import java.util.List;

public interface DoctorDiscountService {

    DoctorDiscountResponse create(DoctorDiscountRequest request);

    DoctorDiscountResponse getById(Long id);

    DoctorDiscountResponse getByDoctorId(Long doctorId);

    List<DoctorDiscountResponse> getAll();

    List<DoctorDiscountResponse> getActive();

    DoctorDiscountResponse update(Long id, DoctorDiscountRequest request);

    void delete(Long id);
}
