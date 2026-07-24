package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;

import java.util.List;

public class PdfTestTable {

    public static void add(

            Document document,

            List<Tests> tests

    ) throws Exception {

        //========================================
        // Section Title
        //========================================

        Paragraph title = new Paragraph(

                "LABORATORY INVESTIGATIONS",

                PdfStyle.SECTION_FONT

        );

        title.setSpacingBefore(5);

        title.setSpacingAfter(5);

        document.add(title);

        //========================================
        // If No Test
        //========================================

        if (tests == null || tests.isEmpty()) {

            Paragraph noTest = new Paragraph(

                    "No Investigation Advised",

                    PdfStyle.VALUE_FONT

            );

            noTest.setSpacingAfter(10);

            document.add(noTest);

            return;

        }

        //========================================
        // Table
        //========================================

        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100);

        table.setWidths(new float[]{

                2f,

                5f,

                2f,

                3f

        });

        //========================================
        // Header
        //========================================

        addHeader(table, "Code");

        addHeader(table, "Test Name");

        addHeader(table, "Status");

        addHeader(table, "Normal Range");

        //========================================
        // Data
        //========================================

        for (Tests test : tests) {

            addCell(

                    table,

                    test.getTestMaster().getTestCode()

            );

            addCell(

                    table,

                    test.getTestMaster().getTestName()

            );

            addCell(

                    table,

                    test.getOrderStatus()

            );

            addCell(

                    table,

                    test.getTestMaster().getNormalRange()

            );

        }

        document.add(table);

    }

    //========================================
    // Header Cell
    //========================================

    private static void addHeader(

            PdfPTable table,

            String text

    ) {

        PdfPCell cell = new PdfPCell(

                new Phrase(

                        text,

                        PdfStyle.LABEL_FONT

                )

        );

        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setPadding(6);

        table.addCell(cell);

    }

    //========================================
    // Data Cell
    //========================================

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