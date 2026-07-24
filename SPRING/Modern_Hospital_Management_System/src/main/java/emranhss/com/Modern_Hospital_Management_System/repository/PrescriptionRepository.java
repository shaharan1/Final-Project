package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {


    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorId(Long doctorId);



}
