package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import emranhss.com.Modern_Hospital_Management_System.entity.PrescriptionItem;

public class PdfMedicineTable {

    public static void add(
            Document document,
            Prescription prescription
    ) throws Exception {

        //==============================
        // Section Title
        //==============================

        Paragraph title = new Paragraph(
                "PRESCRIBED MEDICINES",
                PdfStyle.SECTION_FONT
        );

        title.setSpacingBefore(5);
        title.setSpacingAfter(5);

        document.add(title);

        //==============================
        // Table
        //==============================

        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);

        table.setWidths(new float[]{
                0.8f,
                4f,
                1.5f,
                1.8f,
                3f
        });

        //==============================
        // Header
        //==============================

        addHeader(table, "SL");
        addHeader(table, "Medicine");
        addHeader(table, "Dose");
        addHeader(table, "Duration");
        addHeader(table, "Instruction");

        //==============================
        // Data
        //==============================

        int sl = 1;

        for (PrescriptionItem item : prescription.getPrescriptionItems()) {

            addCell(table, String.valueOf(sl++));

            addCell(table,
                    item.getMedicine().getMedicineName());

            addCell(table,
                    item.getDosage());

            addCell(table,
                    item.getDuration());

            addCell(table,
                    item.getInstruction());

        }

        document.add(table);

    }

    //==================================
    // Header Cell
    //==================================

    private static void addHeader(
            PdfPTable table,
            String text
    ) {

        PdfPCell cell = new PdfPCell(
                new Phrase(text, PdfStyle.LABEL_FONT)
        );

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPadding(6);

        table.addCell(cell);

    }

    //==================================
    // Data Cell
    //==================================

    private static void addCell(
            PdfPTable table,
            String text
    ) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text == null ? "-" : text,
                        PdfStyle.VALUE_FONT
                )
        );

        cell.setPadding(5);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell);

    }

}