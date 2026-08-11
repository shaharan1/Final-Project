package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

public class PdfVitalSection {

    /**
     * ============================================
     * Patient Vital Information
     * ============================================
     */
    public static void add(

            Document document,

            Prescription prescription

    ) throws Exception {

        //------------------------------------------------
        // Section Title
        //------------------------------------------------

        document.add(PdfStyle.sectionBanner("VITAL SIGNS"));


        //------------------------------------------------
        // Table
        //------------------------------------------------

        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100);

        table.setWidths(new float[]{1,1,1,1});

        table.setSpacingAfter(10);


        // Header
        table.addCell(createHeader("BP"));
        table.addCell(createHeader("Pulse"));
        table.addCell(createHeader("Temp"));
        table.addCell(createHeader("Weight"));



        // Data
        table.addCell(createValue(
                prescription.getBloodPressure()
        ));

        table.addCell(createValue(
                prescription.getPulseRate()
        ));

        table.addCell(createValue(
                prescription.getBodyTemperature()
        ));

        table.addCell(createValue(
                prescription.getWeight()
        ));

        document.add(table);
    }

    private static PdfPCell createHeader(String text){

        PdfPCell cell =
                new PdfPCell(new Phrase(
                        text,
                        PdfStyle.LABEL_FONT
                ));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPadding(5);

        return cell;
    }

    private static PdfPCell createValue(String text){

        PdfPCell cell =
                new PdfPCell(new Phrase(
                        text == null ? "-" : text,
                        PdfStyle.VALUE_FONT
                ));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPadding(5);

        return cell;
    }

}