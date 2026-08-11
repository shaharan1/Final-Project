package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.Dietician;
import java.util.List;

public interface DieticianService {
    Dietician create(Dietician dietician);
    Dietician getById(Long id);
    List<Dietician> getAll();
    List<Dietician> getActive();
    Dietician update(Long id, Dietician dietician);
    void delete(Long id);
    List<Dietician> search(String keyword);
    long getActiveCount();
}
