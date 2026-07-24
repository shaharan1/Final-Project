package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

public class PdfDiagnosisSection {

    public static void add(
            Document document,
            Prescription prescription
    ) throws Exception {

        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        table.setSpacingBefore(5);

        table.setSpacingAfter(10);

        PdfPCell cell = new PdfPCell();

        cell.setPadding(8);

        //==============================
        // Title
        //==============================

        Paragraph title = new Paragraph(
                "CLINICAL FINDINGS",
                PdfStyle.SECTION_FONT
        );

        title.setSpacingAfter(5);

        cell.addElement(title);

        //==============================
        // Diagnosis
        //==============================

        cell.addElement(
                new Paragraph(
                        "Diagnosis : "
                                + (prescription.getDiagnosis() == null
                                ? "-"
                                : prescription.getDiagnosis()),
                        PdfStyle.VALUE_FONT
                )
        );

        //==============================
        // Chief Complaint
        //==============================

        cell.addElement(
                new Paragraph(
                        "Chief Complaint : "
                                + (prescription.getChiefComplaints() == null
                                ? "-"
                                : prescription.getChiefComplaints()),
                        PdfStyle.VALUE_FONT
                )
        );

        //==============================
        // Symptoms
        //==============================

        cell.addElement(
                new Paragraph(
                        "Symptoms : "
                                + (prescription.getSymptoms() == null
                                ? "-"
                                : prescription.getSymptoms()),
                        PdfStyle.VALUE_FONT
                )
        );

        table.addCell(cell);

        document.add(table);

    }
}