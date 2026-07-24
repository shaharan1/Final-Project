package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Generic;
import emranhss.com.Modern_Hospital_Management_System.entity.Medicine;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public Medicine toEntity(
            MedicineRequest request,
            Generic generic,
            Prescription prescription){

        Medicine medicine=new Medicine();

        medicine.setMedicineName(request.getMedicineName());
        medicine.setGeneric(generic);
        medicine.setDosage(request.getDosage());
//        medicine.setPrescription(prescription);

        return medicine;

    }

    public void updateEntity(Medicine medicine,
                             MedicineRequest request,
                             Generic generic,
                             Prescription prescription) {

        medicine.setMedicineName(request.getMedicineName());
        medicine.setGeneric(generic);
        medicine.setDosage(request.getDosage());
//        medicine.setPrescription(prescription);
    }

    public MedicineResponse toResponse(Medicine medicine) {

        MedicineResponse response = new MedicineResponse();

        response.setId(medicine.getId());
        response.setMedicineName(medicine.getMedicineName());

        if (medicine.getGeneric() != null) {
            response.setGenericId(medicine.getGeneric().getId());
            response.setGenericName(medicine.getGeneric().getGenericName());
        }

        response.setDosage(medicine.getDosage());

//        if (medicine.getPrescription() != null) {
//            response.setPrescriptionId(medicine.getPrescription().getId());
//        }

        return response;
    }
}