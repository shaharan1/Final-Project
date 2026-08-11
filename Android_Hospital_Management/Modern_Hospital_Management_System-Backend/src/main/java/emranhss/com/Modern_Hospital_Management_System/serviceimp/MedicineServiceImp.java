package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import emranhss.com.Modern_Hospital_Management_System.dto.mapper.MedicineMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Generic;
import emranhss.com.Modern_Hospital_Management_System.entity.Medicine;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.GenericRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.MedicineRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.PrescriptionRepository;
import emranhss.com.Modern_Hospital_Management_System.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineServiceImp implements MedicineService {



    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineMapper medicineMapper;
    private final GenericRepository genericRepository;





    @Override
    public MedicineResponse create(MedicineRequest request){

        Generic generic=resolveGeneric(request.getGenericId());

        Prescription prescription=
                resolvePrescription(request.getPrescriptionId());

        Medicine medicine=
                medicineMapper.toEntity(
                        request,
                        generic,
                        prescription);

        Medicine saved=
                medicineRepository.save(medicine);

        return medicineMapper.toResponse(saved);

    }

    @Override
    public MedicineResponse getById(Long id) {
        Medicine medicine = findMedicineOrThrow(id);
        return medicineMapper.toResponse(medicine);
    }

    @Override
    public List<MedicineResponse> getAll() {
        return medicineRepository.findAll()
                .stream()
                .map(medicineMapper::toResponse)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<MedicineResponse> getByPrescriptionId(Long prescriptionId) {
//        return medicineRepository.findByPrescriptionId(prescriptionId)
//                .stream()
//                .map(medicineMapper::toResponse)
//                .collect(Collectors.toList());
//    }

    @Override
    public MedicineResponse update(Long id, MedicineRequest request) {

        Medicine medicine = findMedicineOrThrow(id);

        Generic generic = resolveGeneric(request.getGenericId());

        Prescription prescription = resolvePrescription(request.getPrescriptionId());

        medicineMapper.updateEntity(
                medicine,
                request,
                generic,
                prescription
        );

        Medicine updated = medicineRepository.save(medicine);

        return medicineMapper.toResponse(updated);
    }



    @Override
    public void delete(Long id) {
        Medicine medicine = findMedicineOrThrow(id);
        medicineRepository.delete(medicine);
    }

    private Medicine findMedicineOrThrow(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
    }

    private Prescription resolvePrescription(Long prescriptionId) {
        if (prescriptionId == null) {
            return null;
        }
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));
    }

    public List<MedicineResponse> getMedicineByGeneric(Long id){

        return medicineRepository
                .findByGenericId(id)
                .stream()
                .map(medicineMapper::toResponse)
                .toList();

    }

    @Override
    public List<MedicineResponse> search(String keyword) {
        return medicineRepository
                .findByMedicineNameContainingIgnoreCase(keyword)
                .stream()
                .map(medicineMapper::toResponse)
                .collect(Collectors.toList());

    }

    private Generic resolveGeneric(Long genericId) {

        if (genericId == null) {
            return null;
        }

        return genericRepository.findById(genericId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Generic not found with id: " + genericId));
    }
}
