package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.OthersChargeMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.OthersChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OthersChargeResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.AdmittedPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.BedBooking;
import emranhss.com.Modern_Hospital_Management_System.entity.OthersCharge;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.AdmittedPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.BedBookingRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.OthersChargeRepository;
import emranhss.com.Modern_Hospital_Management_System.service.OthersChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // CORRECT SPRING IMPORT

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OthersChargeServiceImp implements OthersChargeService {

    private final OthersChargeRepository othersChargeRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final BedBookingRepository bedBookingRepository;
    private final OthersChargeMapper mapper;

    @Override
    @Transactional
    public OthersChargeResponse addCharge(OthersChargeRequest request) {
        AdmittedPatient admission = admittedPatientRepository.findById(request.getAdmittedPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Admitted patient record not found with ID: " + request.getAdmittedPatientId()));

        OthersCharge charge = new OthersCharge();
        charge.setCategory(request.getCategory());
        charge.setDescription(request.getDescription());
        charge.setUnitPrice(request.getUnitPrice());
        charge.setQuantity(request.getQuantity());
        charge.setAdmittedPatient(admission);
        charge.setEnteredBy(request.getEnteredBy());
        charge.setBillingStatus("PENDING");

        if (request.getBedBookingId() != null) {
            BedBooking booking = bedBookingRepository.findById(request.getBedBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bed booking entry not found with ID: " + request.getBedBookingId()));
            charge.setBedBooking(booking);
        }

        OthersCharge savedCharge = othersChargeRepository.save(charge);
        return mapper.toResponse(savedCharge);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OthersChargeResponse> getChargesByAdmission(Long admittedPatientId) {
        if (!admittedPatientRepository.existsById(admittedPatientId)) {
            throw new ResourceNotFoundException("Admitted patient record not found with ID: " + admittedPatientId);
        }

        return othersChargeRepository.findByAdmittedPatientId(admittedPatientId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OthersChargeResponse> getUnbilledChargesByAdmission(Long admittedPatientId) {
        if (!admittedPatientRepository.existsById(admittedPatientId)) {
            throw new ResourceNotFoundException("Admitted patient record not found with ID: " + admittedPatientId);
        }

        return othersChargeRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING").stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
