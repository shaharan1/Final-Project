package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.Bed;
import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {


    List<Bed> findByStatus(BedStatus status);

    List<Bed> findByWardId(Long wardId);
}
