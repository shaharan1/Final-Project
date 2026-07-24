package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.ReportResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Report;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class ReportMapper {

    public Report toEntity(ReportRequest request) {
        if (request == null) return null;

        Report report = new Report();
        report.setReportResult(request.getReportResult());
        report.setDescription(request.getDescription());
        report.setSampleId(request.getSampleId());
        report.setInterpretation(request.getInterpretation());
        report.setPreparedBy(request.getPreparedBy());
        report.setTestDate(request.getTestDate());
        report.setDeliveryDate(request.getDeliveryDate());
        report.setCreateDate(new Date());
        return report;
    }

    public ReportResponse toResponse(Report report) {
        if (report == null) return null;

        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setReportResult(report.getReportResult());
        response.setDescription(report.getDescription());
        response.setSampleId(report.getSampleId());
        response.setInterpretation(report.getInterpretation());
        response.setPreparedBy(report.getPreparedBy());
        response.setTestDate(report.getTestDate());
        response.setCreateDate(report.getCreateDate());
        response.setDeliveryDate(report.getDeliveryDate());

        if (report.getPatient() != null) {
            response.setPatientId(report.getPatient().getId());
            response.setPatientCode(report.getPatient().getPatientCode());
            response.setPatientFullName(report.getPatient().getName());
            response.setPatientPhone(report.getPatient().getPhone());
            response.setPatientGender(report.getPatient().getGender());
            response.setPatientBloodGroup(report.getPatient().getBloodGroup());
        }

        if (report.getDoctor() != null) {
            response.setDoctorId(report.getDoctor().getId());
            response.setDoctorName(report.getDoctor().getUser().getName());
            response.setDoctorSpecialization(report.getDoctor().getSpecialization());
        }
        return response;
    }
}
