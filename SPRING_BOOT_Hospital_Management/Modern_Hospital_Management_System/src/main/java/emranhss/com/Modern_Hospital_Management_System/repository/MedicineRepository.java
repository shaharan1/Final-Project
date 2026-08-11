package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {



//    List<Medicine> findByPrescriptionId(Long prescriptionId);

    List<Medicine> findByGenericId(Long genericId);

    List<Medicine> findByMedicineNameContainingIgnoreCase(String keyword);


}
