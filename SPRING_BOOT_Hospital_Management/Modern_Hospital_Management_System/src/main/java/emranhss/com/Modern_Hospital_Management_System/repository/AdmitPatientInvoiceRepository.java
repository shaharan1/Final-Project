package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.AdmitPatientInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmitPatientInvoiceRepository extends JpaRepository<AdmitPatientInvoice, Long> {

    Optional<AdmitPatientInvoice> findByAdmittedPatientId(Long admittedPatientId);

}
