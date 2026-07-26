package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.Insurance;

import java.util.List;

public interface InsuranceService {

    List<Insurance> getAll();

    Insurance getById(Long id);

    List<Insurance> getActive();

    Insurance create(Insurance insurance);

    Insurance update(Long id, Insurance insurance);

    void delete(Long id);

    List<Insurance> search(String keyword);
}
