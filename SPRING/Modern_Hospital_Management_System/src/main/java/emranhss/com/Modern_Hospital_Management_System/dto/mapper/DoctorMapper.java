package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Data
public class DoctorMapper {


    public Doctor toEntity(DoctorRequest request) {
        if (request == null) return null;
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(request, doctor);
        return doctor;
    }

    public  DoctorResponse toResponse(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        DoctorResponse response = new DoctorResponse();

        response.setId(doctor.getId());

        // User
        if (doctor.getUser() != null) {
            response.setName(doctor.getUser().getName());
            response.setEmail(doctor.getUser().getEmail());
            response.setPhone(doctor.getUser().getPhone());
        }

        // Doctor
        response.setGender(doctor.getGender());
        response.setStatus(doctor.getStatus());
        response.setStudy(doctor.getStudy());
        response.setSpecialization(doctor.getSpecialization());
        response.setDesignation(doctor.getDesignation());
        response.setRegistrationNumber(doctor.getRegistrationNumber());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setConsultationFee(doctor.getConsultationFee());
        response.setFollowUpFee(doctor.getFollowUpFee());
        response.setAvailableDays(doctor.getAvailableDays());
        response.setDutyHours(doctor.getDutyHours());
        response.setChamber(doctor.getChamber());
        response.setJoinDate(doctor.getJoinDate());
        response.setPhoto(doctor.getPhoto());

        // Department
        if (doctor.getDoctorDepartment() != null) {
            response.setDepartmentId(doctor.getDoctorDepartment().getId());
            response.setDepartmentName(doctor.getDoctorDepartment().getDepartmentName());
            response.setDescription(doctor.getDoctorDepartment().getDescription());



        }

        return response;
    }



    public static DoctorResponse toDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        DoctorResponse response = new DoctorResponse();

        response.setId(doctor.getId());

        // User
        if (doctor.getUser() != null) {
            response.setName(doctor.getUser().getName());
            response.setEmail(doctor.getUser().getEmail());
            response.setPhone(doctor.getUser().getPhone());
        }

        // Doctor
        response.setGender(doctor.getGender());
        response.setStatus(doctor.getStatus());
        response.setStudy(doctor.getStudy());
        response.setSpecialization(doctor.getSpecialization());
        response.setDesignation(doctor.getDesignation());
        response.setRegistrationNumber(doctor.getRegistrationNumber());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setConsultationFee(doctor.getConsultationFee());
        response.setFollowUpFee(doctor.getFollowUpFee());
        response.setAvailableDays(doctor.getAvailableDays());
        response.setDutyHours(doctor.getDutyHours());
        response.setChamber(doctor.getChamber());
        response.setJoinDate(doctor.getJoinDate());
        response.setPhoto(doctor.getPhoto());

        // Department
        if (doctor.getDoctorDepartment() != null) {
            response.setDepartmentId(doctor.getDoctorDepartment().getId());
            response.setDepartmentName(doctor.getDoctorDepartment().getDepartmentName());
            response.setDescription(doctor.getDoctorDepartment().getDescription());



        }

        return response;
    }



    public void updateEntityFromRequest(DoctorRequest request, Doctor doctor) {
        if (request == null || doctor == null) return;
        BeanUtils.copyProperties(request, doctor, "id");
    }
}
