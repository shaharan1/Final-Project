package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

public class PdfDoctorSection {

    public static void add(
            Document document,
            Prescription prescription
    ) throws Exception {

        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        table.setSpacingAfter(10);

        PdfPCell cell = new PdfPCell();

        cell.setPadding(8);

        //==============================
        // Doctor Title
        //==============================

        Paragraph title = new Paragraph(
                "DOCTOR INFORMATION",
                PdfStyle.SECTION_FONT
        );

        cell.addElement(title);

        //==============================
        // Doctor Name
        //==============================

        cell.addElement(

                new Paragraph(

                        "Dr. " +
                                prescription.getDoctor().getUser().getName(),

                        PdfStyle.VALUE_FONT

                )

        );

        //==============================
        // Specialization
        //==============================

        cell.addElement(

                new Paragraph(

                        "Specialization : "
                                + prescription.getDoctor().getSpecialization(),

                        PdfStyle.VALUE_FONT

                )

        );

        //==============================
        // Qualification
        //==============================

        if (prescription.getDoctor().getDoctorDepartment() != null) {

            cell.addElement(

                    new Paragraph(

                            "Qualification : "
                                    + prescription.getDoctor().getDoctorDepartment(),

                            PdfStyle.VALUE_FONT

                    )

            );

        }

        //==============================
        // BMDC Registration
        //==============================

        if (prescription.getDoctor().getRegistrationNumber() != null) {

            cell.addElement(

                    new Paragraph(

                            "BMDC No : "
                                    + prescription.getDoctor().getRegistrationNumber(),

                            PdfStyle.VALUE_FONT

                    )

            );

        }

        //==============================
        // Mobile
        //==============================

        if (prescription.getDoctor().getUser().getPhone() != null) {

            cell.addElement(

                    new Paragraph(

                            "Mobile : "
                                    + prescription.getDoctor().getUser().getPhone(),

                            PdfStyle.VALUE_FONT

                    )

            );

        }

        table.addCell(cell);

        document.add(table);

    }

}