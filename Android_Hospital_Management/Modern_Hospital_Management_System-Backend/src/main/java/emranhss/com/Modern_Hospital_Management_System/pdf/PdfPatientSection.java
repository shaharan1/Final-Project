package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

import java.time.format.DateTimeFormatter;

public class PdfPatientSection {

    public static void add(
            Document document,
            Prescription prescription
    ) throws Exception {

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setSpacingAfter(12);

        table.setWidths(new float[]{1,1});

        //==========================
        // Patient Information
        //==========================

        PdfPCell patientCell = new PdfPCell();

        patientCell.setPadding(8);

        patientCell.addElement(
                new Paragraph(
                        "PATIENT INFORMATION",
                        PdfStyle.SECTION_FONT
                )
        );

        patientCell.addElement(new Paragraph(
                "Name : " +
                        prescription.getPatient().getName(),
                PdfStyle.VALUE_FONT
        ));

        patientCell.addElement(new Paragraph(
                "Age : " +
                        prescription.getPatient().getDateOfBirth(),
                PdfStyle.VALUE_FONT
        ));

        patientCell.addElement(new Paragraph(
                "Gender : " +
                        prescription.getPatient().getGender(),
                PdfStyle.VALUE_FONT
        ));

        patientCell.addElement(new Paragraph(
                "Phone : " +
                        prescription.getPatient().getPhone(),
                PdfStyle.VALUE_FONT
        ));

        patientCell.addElement(new Paragraph(
                "Blood Group : " +
                        prescription.getPatient().getBloodGroup(),
                PdfStyle.VALUE_FONT
        ));

        table.addCell(patientCell);

        //==========================
        // Visit Information
        //==========================

        PdfPCell visitCell = new PdfPCell();

        visitCell.setPadding(8);

        visitCell.addElement(
                new Paragraph(
                        "VISIT INFORMATION",
                        PdfStyle.SECTION_FONT
                )
        );

        visitCell.addElement(new Paragraph(
                "Prescription No : RX-" +
                        prescription.getId(),
                PdfStyle.VALUE_FONT
        ));

        visitCell.addElement(new Paragraph(
                "Appointment : " +
                        prescription.getAppointment().getAppointmentNumber(),
                PdfStyle.VALUE_FONT
        ));

        visitCell.addElement(new Paragraph(
                "Date : " +
                        prescription.getCreatedDate().format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy")
                        ),
                PdfStyle.VALUE_FONT
        ));

        table.addCell(visitCell);

        document.add(table);

    }

}