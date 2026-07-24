package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.InputStream;

public class PdfHeader {

    public static void add(Document document) throws Exception {

        // =====================================================
        // Load Hospital Logo (from classpath, not a relative file path)
        // =====================================================

        Image logo = loadLogo();

        // =====================================================
        // Header Table (3 Columns)
        // =====================================================

        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1.2f, 5f, 1.6f});
        header.setSpacingAfter(10);

        // Logo Cell
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (logo != null) {
            logo.scaleAbsolute(90, 90);
            logoCell.addElement(logo);
        }
        header.addCell(logoCell);

        // Hospital Information
        Paragraph hospital = new Paragraph("ELITE CARE HOSPITAL", PdfStyle.HOSPITAL_FONT);
        Paragraph address = new Paragraph("House #25, Road #12, Dhanmondi, Dhaka-1209", PdfStyle.VALUE_FONT);
        Paragraph phone = new Paragraph("Phone : +880 1711-123456", PdfStyle.VALUE_FONT);
        Paragraph email = new Paragraph("Email : info@elitecarehospital.com", PdfStyle.VALUE_FONT);

        Paragraph title = new Paragraph("PRESCRIPTION", PdfStyle.TITLE_FONT);
        title.setSpacingBefore(6);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoCell.addElement(hospital);
        infoCell.addElement(address);
        infoCell.addElement(phone);
        infoCell.addElement(email);

        // subtle rule instead of a dashed-text "line"
        PdfPTable rule = new PdfPTable(1);
        rule.setWidthPercentage(100);
        rule.setSpacingBefore(4);
        PdfPCell ruleCell = new PdfPCell();
        ruleCell.setFixedHeight(1.2f);
        ruleCell.setBackgroundColor(new BaseColor(25, 118, 210));
        ruleCell.setBorder(Rectangle.NO_BORDER);
        rule.addCell(ruleCell);
        infoCell.addElement(rule);

        infoCell.addElement(title);
        header.addCell(infoCell);

        // Doctor Photo Cell — placeholder box, NOT self-referencing
        PdfPCell doctorPhotoCell = new PdfPCell();
        doctorPhotoCell.setBorder(Rectangle.BOX);
        doctorPhotoCell.setBorderColor(BaseColor.LIGHT_GRAY);
        doctorPhotoCell.setFixedHeight(90);
        doctorPhotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        doctorPhotoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph photoLabel = new Paragraph("Photo", PdfStyle.VALUE_FONT);
        photoLabel.setAlignment(Element.ALIGN_CENTER);
        doctorPhotoCell.addElement(photoLabel);

        header.addCell(doctorPhotoCell);

        document.add(header);
    }

    private static Image loadLogo() {
        try {
            // classpath resource: put file under src/main/resources/images/logo/...
            InputStream is = PdfHeader.class.getResourceAsStream(
                    "/images/logo/elite_care_hospital_Logo.png"
            );
            if (is == null) return null;
            byte[] bytes = is.readAllBytes();
            return Image.getInstance(bytes);
        } catch (Exception e) {
            // Don't let a missing logo kill the whole PDF
            return null;
        }
    }
}