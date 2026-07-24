package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;

public class PdfFooter {

    /**
     * ======================================
     * QR Code + Doctor Signature + Footer
     * ======================================
     */
    public static void add(
            Document document,
            Prescription prescription
    ) throws Exception {

        // =============================
        // QR Code
        // =============================

        BarcodeQRCode qr =
                new BarcodeQRCode(

                        "ELITE CARE HOSPITAL\n"
                                + "Prescription ID : "
                                + prescription.getId()

                                + "\nPatient : "
                                + prescription.getPatient().getName()

                                + "\nDoctor : "
                                + prescription.getDoctor().getUser().getName(),

                        120,
                        120,
                        null

                );

        Image qrImage = qr.getImage();

        qrImage.scaleAbsolute(80,80);

        // =============================
        // Footer Table
        // =============================

        PdfPTable footer =
                new PdfPTable(2);

        footer.setWidthPercentage(100);

        footer.setSpacingBefore(30);

        footer.setWidths(new float[]{1,1});

        // =============================
        // QR Cell
        // =============================

        PdfPCell qrCell =
                new PdfPCell();

        qrCell.setBorder(Rectangle.NO_BORDER);

        Paragraph verify =
                new Paragraph(
                        "SCAN TO VERIFY",
                        PdfStyle.TITLE_FONT
                );

        verify.setAlignment(Element.ALIGN_CENTER);

        qrCell.addElement(verify);

        qrImage.setAlignment(Image.ALIGN_CENTER);

        qrCell.addElement(qrImage);

        footer.addCell(qrCell);

        // =============================
        // Doctor Signature
        // =============================

        PdfPCell signCell =
                new PdfPCell();

        signCell.setBorder(Rectangle.NO_BORDER);

        Paragraph line =
                new Paragraph(
                        "____________________________"
                );

        line.setAlignment(Element.ALIGN_CENTER);

        signCell.addElement(line);

        Paragraph doctor =
                new Paragraph(

                        "Dr. "
                                + prescription.getDoctor().getUser().getName(),

                        PdfStyle.TITLE_FONT

                );

        doctor.setAlignment(Element.ALIGN_CENTER);

        signCell.addElement(doctor);

        Paragraph specialization =
                new Paragraph(

                        prescription.getDoctor().getSpecialization(),

                        PdfStyle.VALUE_FONT

                );

        specialization.setAlignment(Element.ALIGN_CENTER);

        signCell.addElement(specialization);

        Paragraph hospital =
                new Paragraph(

                        "ELITE CARE HOSPITAL",

                        PdfStyle.VALUE_FONT

                );

        hospital.setAlignment(Element.ALIGN_CENTER);

        signCell.addElement(hospital);

        footer.addCell(signCell);

        document.add(footer);

        // =============================
        // Footer Text
        // =============================

        Paragraph footerText =
                new Paragraph(

                        "\nELITE CARE HOSPITAL\n"
                                + "House #25, Road #12, Dhanmondi, Dhaka\n"
                                + "Phone : +8801711123456\n"
                                + "Email : info@elitecarehospital.com\n\n"
                                + "Thank you for choosing ELITE CARE HOSPITAL.\n"
                                + "Get Well Soon.\n\n"
                                + "Powered By Elite IT Institute",

                        PdfStyle.VALUE_FONT

                );

        footerText.setAlignment(Element.ALIGN_CENTER);

        document.add(footerText);

    }

}