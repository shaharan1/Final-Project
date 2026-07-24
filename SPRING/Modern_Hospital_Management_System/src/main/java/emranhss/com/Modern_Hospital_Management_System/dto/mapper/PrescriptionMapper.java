package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionItemResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Collectors;

@Component
public class PrescriptionMapper {

    public PrescriptionResponse toResponse(Prescription prescription) {
        if (prescription == null) {
            return null;
        }

        PrescriptionResponse response = new PrescriptionResponse();

        // 1. Map Base Prescription Fields
        response.setId(prescription.getId());
        response.setDiagnosis(prescription.getDiagnosis());
        response.setChiefComplaints(prescription.getChiefComplaints());
        response.setSymptoms(prescription.getSymptoms());
        response.setBloodPressure(prescription.getBloodPressure());
        response.setPulseRate(prescription.getPulseRate());
        response.setBodyTemperature(prescription.getBodyTemperature());
        response.setWeight(prescription.getWeight());
        response.setNotes(prescription.getNotes());
        response.setNextFollowUpDate(prescription.getNextFollowUpDate());
        response.setCreatedDate(prescription.getCreatedDate());

        // 2. Map Appointment Info
        if (prescription.getAppointment() != null) {
            response.setAppointmentId(prescription.getAppointment().getId());
        }

        // 3. Map Doctor Info
        if (prescription.getDoctor() != null && prescription.getDoctor().getUser() != null) {
            response.setDoctorName(prescription.getDoctor().getUser().getName());
            response.setDoctorSpecialization(prescription.getDoctor().getSpecialization());
        }

        // 4. Map Patient Info (Fixed Name String Bug)
        if (prescription.getPatient() != null) {
            response.setPatientName(prescription.getPatient().getName());

            if (prescription.getPatient().getDateOfBirth() != null) {
                int age = Period.between(prescription.getPatient().getDateOfBirth(), LocalDate.now()).getYears();
                response.setPatientAge(age + " Years");
            }
        }

        // 5. Map Prescription Items
        if (prescription.getPrescriptionItems() != null) {
            response.setPrescriptionItems(prescription.getPrescriptionItems().stream().map(item -> {
                PrescriptionItemResponse itemDto = new PrescriptionItemResponse();
                itemDto.setId(item.getId());
                itemDto.setMedicineType(item.getMedicineType());
                itemDto.setDosage(item.getDosage());
                itemDto.setDuration(item.getDuration());
                itemDto.setInstruction(item.getInstruction());

                if (item.getMedicine() != null) {
                    itemDto.setMedicineId(item.getMedicine().getId());
                    itemDto.setMedicineName(item.getMedicine().getMedicineName());
                }
                return itemDto;
            }).collect(Collectors.toList()));
        }

        return response;
    }
}
