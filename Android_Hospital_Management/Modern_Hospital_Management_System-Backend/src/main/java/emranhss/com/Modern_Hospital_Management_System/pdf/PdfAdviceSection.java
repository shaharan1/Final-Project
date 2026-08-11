package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

import java.time.format.DateTimeFormatter;

public class PdfAdviceSection {

    public static void add(

            Document document,

            Prescription prescription

    ) throws Exception {

        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setPadding(8);

        //==========================
        // Title
        //==========================

        Paragraph title = new Paragraph(

                "DOCTOR'S ADVICE",

                PdfStyle.SECTION_FONT

        );

        title.setSpacingAfter(5);

        cell.addElement(title);

        //==========================
        // Notes
        //==========================

        String advice =
                prescription.getNotes() == null
                        ? "-"
                        : prescription.getNotes();

        cell.addElement(

                new Paragraph(

                        advice,

                        PdfStyle.VALUE_FONT

                )

        );

        cell.addElement(new Paragraph(" "));

        //==========================
        // Follow Up
        //==========================

        String followUp = "Not Scheduled";

        if (prescription.getNextFollowUpDate() != null) {

            followUp = prescription
                    .getNextFollowUpDate()
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        }

        Paragraph follow = new Paragraph(

                "Next Follow-Up : " + followUp,

                PdfStyle.LABEL_FONT

        );

        cell.addElement(follow);

        table.addCell(cell);

        document.add(table);

    }

}