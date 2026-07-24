package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineStockRepository extends JpaRepository<MedicineStock, Long> {

    List<MedicineStock> findByMedicineNameContainingIgnoreCase(String medicineName);


}
