package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PrescriptionPdfGenerator {

    public static byte[] generate(

            Prescription prescription,

            List<Tests> tests

    ) {

        try {

            //==========================================================
            // Create PDF Stream
            //==========================================================

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //==========================================================
            // A4 Page
            //==========================================================

            Document document =
                    new Document(PageSize.A4,25,25,25,25);

            PdfWriter.getInstance(document,out);

            document.open();

            //==========================================================
            // 1. Hospital Header
            //==========================================================

            PdfHeader.add(document);

            //==========================================================
            // 2. Patient Section
            //==========================================================

            PdfPatientSection.add(
                    document,
                    prescription
            );

            //==========================================================
            // 3. Doctor Section
            //==========================================================

            PdfDoctorSection.add(
                    document,
                    prescription
            );

            //==========================================================
            // 4. Vital Section
            //==========================================================

            PdfVitalSection.add(
                    document,
                    prescription
            );


            //==========================================================
            // 4. Diagnosis Section
            //==========================================================

            PdfDiagnosisSection.add(
                    document, prescription
                    
            );

            //==========================================================
            // 5. Medicine Table
            //==========================================================

            PdfMedicineTable.add(
                    document,
                    prescription
            );

            //==========================================================
            // 6. Laboratory Tests
            //==========================================================

            PdfTestTable.add(
                    document,
                    tests
            );

            //==========================================================
            // 7. Advice Section
            //==========================================================

            PdfAdviceSection.add(
                    document,
                    prescription
            );

            //==========================================================
            // 8. Footer
            //==========================================================

            PdfFooter.add(
                    document,
                    prescription
            );

            //==========================================================
            // Finish PDF
            //==========================================================

            document.close();

            return out.toByteArray();

        }
        catch (Exception e){

            throw new RuntimeException(e);

        }

    }

}