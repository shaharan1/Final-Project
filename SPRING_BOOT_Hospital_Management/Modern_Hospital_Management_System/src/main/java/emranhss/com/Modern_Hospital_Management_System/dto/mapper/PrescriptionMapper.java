package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionItemResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TestMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.repository.TestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PrescriptionMapper {

    private final TestsRepository testsRepository;

    public PrescriptionResponse toResponse(Prescription prescription) {
        if (prescription == null) {
            return null;
        }

        PrescriptionResponse response = new PrescriptionResponse();

        response.setId(prescription.getId());
        response.setPrescriptionNumber(prescription.getPrescriptionNumber());
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
        response.setDispensed(prescription.getDispensed() != null && prescription.getDispensed());

        if (prescription.getAppointment() != null) {
            response.setAppointmentId(prescription.getAppointment().getId());
        }

        if (prescription.getDoctor() != null) {
            response.setDoctorDesignation(prescription.getDoctor().getDesignation());
            response.setDoctorDepartment(prescription.getDoctor().getDoctorDepartment() != null
                    ? prescription.getDoctor().getDoctorDepartment().getDepartmentName() : null);

            if (prescription.getDoctor().getUser() != null) {
                response.setDoctorName(prescription.getDoctor().getUser().getName());
            }
            response.setDoctorSpecialization(prescription.getDoctor().getSpecialization());
        }

        if (prescription.getPatient() != null) {
            response.setPatientName(prescription.getPatient().getName());
            response.setPatientGender(prescription.getPatient().getGender());
            response.setPatientPhone(prescription.getPatient().getPhone());
            response.setPatientBloodGroup(prescription.getPatient().getBloodGroup());

            if (prescription.getPatient().getDateOfBirth() != null) {
                int age = Period.between(prescription.getPatient().getDateOfBirth(), LocalDate.now()).getYears();
                response.setPatientAge(age + " Years");
            }
        }

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

        // Map tests
        List<Tests> testsList = testsRepository.findByPrescriptionId(prescription.getId());
        if (testsList != null && !testsList.isEmpty()) {
            List<TestMasterResponse> testResponses = testsList.stream().map(t -> {
                TestMasterResponse tr = new TestMasterResponse();
                if (t.getTestMaster() != null) {
                    tr.setId(t.getTestMaster().getId());
                    tr.setTestCode(t.getTestMaster().getTestCode());
                    tr.setTestName(t.getTestMaster().getTestName());
                    tr.setStandardPrice(t.getTestMaster().getStandardPrice());
                    tr.setNormalRange(t.getTestMaster().getNormalRange());
                }
                return tr;
            }).collect(Collectors.toList());
            response.setTests(testResponses);
        } else {
            response.setTests(new ArrayList<>());
        }

        return response;
    }
}
