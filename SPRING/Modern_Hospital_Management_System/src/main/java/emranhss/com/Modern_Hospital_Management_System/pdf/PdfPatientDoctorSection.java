package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PdfPatientDoctorSection {

    public static void add(
            Document document,
            Prescription prescription
    ) throws Exception {

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setWidths(new float[]{1.4f,1f});

        table.setSpacingAfter(8);

        //==============================
        // Patient Cell (LEFT)
        //==============================

        PdfPCell patientCell = new PdfPCell();

        patientCell.setPadding(8);

        patientCell.setBorder(Rectangle.BOX);

        patientCell.addElement(
                new Paragraph(
                        "PATIENT INFORMATION",
                        PdfStyle.SECTION_FONT
                )
        );

        patientCell.addElement(new Paragraph(
                "Name : " + prescription.getPatient().getName(),
                PdfStyle.VALUE_FONT));

        int age = Period.between(
                prescription.getPatient().getDateOfBirth(),
                LocalDate.now()
        ).getYears();

        patientCell.addElement(new Paragraph(
                "Age : " + age + " yrs",
                PdfStyle.VALUE_FONT));

        patientCell.addElement(new Paragraph(
                "Gender : " + prescription.getPatient().getGender(),
                PdfStyle.VALUE_FONT));

        patientCell.addElement(new Paragraph(
                "Blood Group : " + prescription.getPatient().getBloodGroup(),
                PdfStyle.VALUE_FONT));

        patientCell.addElement(new Paragraph(
                "Phone : " + prescription.getPatient().getPhone(),
                PdfStyle.VALUE_FONT));

        table.addCell(patientCell);

        //==============================
        // Doctor Cell (RIGHT)
        //==============================

        PdfPCell doctorCell = new PdfPCell();

        doctorCell.setPadding(8);

        doctorCell.setBorder(Rectangle.BOX);

        doctorCell.addElement(
                new Paragraph(
                        "CONSULTANT",
                        PdfStyle.SECTION_FONT
                )
        );

        doctorCell.addElement(new Paragraph(
                "Dr. " + prescription.getDoctor().getUser().getName(),
                PdfStyle.LABEL_FONT));

        doctorCell.addElement(new Paragraph(
                prescription.getDoctor().getSpecialization(),
                PdfStyle.VALUE_FONT));

        doctorCell.addElement(new Paragraph(
                "Date : " +
                        prescription.getCreatedDate().format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy")),
                PdfStyle.VALUE_FONT));

        doctorCell.addElement(new Paragraph(
                "Fee : " +
                        prescription.getDoctor().getConsultationFee(),
                PdfStyle.VALUE_FONT));

        table.addCell(doctorCell);

        document.add(table);

    }

}