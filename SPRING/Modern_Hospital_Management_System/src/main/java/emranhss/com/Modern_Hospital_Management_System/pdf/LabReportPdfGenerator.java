package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class LabReportPdfGenerator {

    private static final Font HOSPITAL_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(25, 118, 210));
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font VALUE_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font RESULT_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(25, 118, 210));
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);

    public static ByteArrayOutputStream generate(Tests test) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addHeader(document);
        addReportInfo(document, test);
        addPatientInfo(document, test.getPatient());
        addDoctorInfo(document, test.getPrescribedBy());
        addTestResult(document, test);
        addWorkflowTimeline(document, test);
        addFooter(document, test);

        document.close();
        return out;
    }

    private static void addHeader(Document document) throws Exception {
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1.2f, 6f});
        header.setSpacingAfter(10);

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Image logo = loadLogo();
        if (logo != null) {
            logo.scaleAbsolute(80, 80);
            logoCell.addElement(logo);
        }
        header.addCell(logoCell);

        Paragraph hospital = new Paragraph("ELITE CARE HOSPITAL", HOSPITAL_FONT);
        Paragraph address = new Paragraph("House #25, Road #12, Dhanmondi, Dhaka-1209", VALUE_FONT);
        Paragraph phone = new Paragraph("Phone : +880 1711-123456", VALUE_FONT);

        Paragraph title = new Paragraph("LABORATORY REPORT", TITLE_FONT);
        title.setSpacingBefore(8);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoCell.addElement(hospital);
        infoCell.addElement(address);
        infoCell.addElement(phone);

        PdfPTable rule = new PdfPTable(1);
        rule.setWidthPercentage(100);
        rule.setSpacingBefore(4);
        PdfPCell ruleCell = new PdfPCell();
        ruleCell.setFixedHeight(1.5f);
        ruleCell.setBackgroundColor(new BaseColor(25, 118, 210));
        ruleCell.setBorder(Rectangle.NO_BORDER);
        rule.addCell(ruleCell);
        infoCell.addElement(rule);
        infoCell.addElement(title);
        header.addCell(infoCell);

        document.add(header);
    }

    private static void addReportInfo(Document document, Tests test) throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        table.setSpacingAfter(6);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

        addInfoCell(table, "Test Code", test.getTestMaster().getTestCode());
        addInfoCell(table, "Order Date", test.getOrderedDate() != null ? sdf.format(java.sql.Timestamp.valueOf(test.getOrderedDate())) : "N/A");
        addInfoCell(table, "Status", test.getOrderStatus());

        document.add(table);
    }

    private static void addPatientInfo(Document document, Patient patient) throws Exception {
        PdfPTable banner = sectionBanner("PATIENT INFORMATION");
        document.add(banner);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingAfter(8);

        addInfoCell(table, "Patient Code", patient.getPatientCode());
        addInfoCell(table, "Name", patient.getName());
        addInfoCell(table, "Gender", patient.getGender());
        addInfoCell(table, "Blood Group", patient.getBloodGroup() != null ? patient.getBloodGroup() : "N/A");

        document.add(table);
    }

    private static void addDoctorInfo(Document document, Doctor doctor) throws Exception {
        PdfPTable banner = sectionBanner("REFERRING DOCTOR");
        document.add(banner);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingAfter(8);

        addInfoCell(table, "Doctor", "Dr. " + doctor.getUser().getName());
        addInfoCell(table, "Specialization", doctor.getSpecialization() != null ? doctor.getSpecialization() : "N/A");
        addInfoCell(table, "Department", doctor.getDoctorDepartment() != null ? doctor.getDoctorDepartment().getDepartmentName() : "N/A");

        document.add(table);
    }

    private static void addTestResult(Document document, Tests test) throws Exception {
        PdfPTable banner = sectionBanner("TEST RESULTS");
        document.add(banner);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        table.setSpacingAfter(8);

        // Test Name
        PdfPCell nameCell = new PdfPCell();
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setPadding(8);
        Paragraph p = new Paragraph("Test: " + test.getTestMaster().getTestName(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        nameCell.addElement(p);
        table.addCell(nameCell);
        document.add(table);

        // Results table
        PdfPTable resultTable = new PdfPTable(3);
        resultTable.setWidthPercentage(100);
        resultTable.setWidths(new float[]{4f, 3f, 3f});
        resultTable.setSpacingAfter(8);

        // Header row
        addResultHeader(resultTable, "Result Value");
        addResultHeader(resultTable, "Normal Range");
        addResultHeader(resultTable, "Status");

        // Value row
        PdfPCell valCell = new PdfPCell(new Phrase(
                test.getResultValue() != null ? test.getResultValue() : "Pending",
                RESULT_FONT));
        valCell.setPadding(8);
        valCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultTable.addCell(valCell);

        PdfPCell rangeCell = new PdfPCell(new Phrase(
                test.getTestMaster().getNormalRange() != null ? test.getTestMaster().getNormalRange() : "N/A",
                VALUE_FONT));
        rangeCell.setPadding(8);
        rangeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultTable.addCell(rangeCell);

        String status = test.getResultValue() != null ? "Completed" : "Pending";
        PdfPCell statusCell = new PdfPCell(new Phrase(status,
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,
                        status.equals("Completed") ? new BaseColor(40, 167, 69) : BaseColor.ORANGE)));
        statusCell.setPadding(8);
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultTable.addCell(statusCell);

        document.add(resultTable);

        // Notes
        if (test.getResultNotes() != null && !test.getResultNotes().isEmpty()) {
            PdfPTable notesTable = new PdfPTable(1);
            notesTable.setWidthPercentage(100);
            notesTable.setSpacingAfter(8);
            PdfPCell notesCell = new PdfPCell();
            notesCell.setBorder(Rectangle.NO_BORDER);
            notesCell.setPadding(8);
            notesCell.addElement(new Paragraph("Notes:", LABEL_FONT));
            notesCell.addElement(new Paragraph(test.getResultNotes(), VALUE_FONT));
            notesTable.addCell(notesCell);
            document.add(notesTable);
        }
    }

    private static void addWorkflowTimeline(Document document, Tests test) throws Exception {
        PdfPTable banner = sectionBanner("WORKFLOW TIMELINE");
        document.add(banner);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f, 6f});
        table.setSpacingAfter(8);
        table.setSpacingBefore(4);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

        if (test.getSampleCollectedDate() != null) {
            addTimelineRow(table, "Sample Collected", sdf.format(java.sql.Timestamp.valueOf(test.getSampleCollectedDate()))
                    + " by " + (test.getSampleCollectorName() != null ? test.getSampleCollectorName() : "N/A")
                    + " (" + (test.getSampleType() != null ? test.getSampleType() : "N/A") + ")");
        }
        if (test.getSampleReceivedDate() != null) {
            addTimelineRow(table, "Sample Received", sdf.format(java.sql.Timestamp.valueOf(test.getSampleReceivedDate()))
                    + " by " + (test.getSampleReceivedBy() != null ? test.getSampleReceivedBy() : "N/A"));
        }
        if (test.getTestingStartDate() != null) {
            addTimelineRow(table, "Testing Started", sdf.format(java.sql.Timestamp.valueOf(test.getTestingStartDate())));
        }
        if (test.getResultEnteredDate() != null) {
            addTimelineRow(table, "Result Entered", sdf.format(java.sql.Timestamp.valueOf(test.getResultEnteredDate()))
                    + " by " + (test.getResultEnteredBy() != null ? test.getResultEnteredBy() : "N/A"));
        }
        if (test.getVerifiedDate() != null) {
            addTimelineRow(table, "Verified", sdf.format(java.sql.Timestamp.valueOf(test.getVerifiedDate()))
                    + " by " + (test.getVerifiedBy() != null ? test.getVerifiedBy() : "N/A"));
        }

        document.add(table);
    }

    private static void addFooter(Document document, Tests test) throws Exception {
        document.add(new Paragraph(" "));

        PdfPTable footer = new PdfPTable(1);
        footer.setWidthPercentage(100);
        footer.setSpacingBefore(20);

        PdfPCell lineCell = new PdfPCell();
        lineCell.setFixedHeight(1f);
        lineCell.setBackgroundColor(new BaseColor(25, 118, 210));
        lineCell.setBorder(Rectangle.NO_BORDER);
        footer.addCell(lineCell);

        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setPadding(8);

        Paragraph disclaimer = new Paragraph(
                "This report is generated by Elite Care Hospital Laboratory. "
                + "For any queries, contact the lab department.",
                SMALL_FONT);
        textCell.addElement(disclaimer);

        if (test.getVerifiedBy() != null) {
            Paragraph sig = new Paragraph("\n\nVerified by: " + test.getVerifiedBy(), LABEL_FONT);
            textCell.addElement(sig);
        }

        footer.addCell(textCell);
        document.add(footer);
    }

    private static void addResultHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, SECTION_FONT));
        cell.setBackgroundColor(new BaseColor(25, 118, 210));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addTimelineRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setPadding(6);
        labelCell.setBorder(Rectangle.BOTTOM);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, VALUE_FONT));
        valueCell.setPadding(6);
        valueCell.setBorder(Rectangle.BOTTOM);
        table.addCell(valueCell);
    }

    private static void addInfoCell(PdfPTable table, String label, String value) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(6);
        cell.setBorder(Rectangle.BOTTOM);
        cell.addElement(new Paragraph(label, LABEL_FONT));
        cell.addElement(new Paragraph(value != null ? value : "N/A", VALUE_FONT));
        table.addCell(cell);
    }

    private static PdfPTable sectionBanner(String text) {
        PdfPTable banner = new PdfPTable(1);
        banner.setWidthPercentage(100);
        banner.setSpacingBefore(8);
        banner.setSpacingAfter(6);
        PdfPCell cell = new PdfPCell(new Phrase(text, SECTION_FONT));
        cell.setBackgroundColor(new BaseColor(25, 118, 210));
        cell.setPadding(6);
        cell.setBorder(Rectangle.NO_BORDER);
        banner.addCell(cell);
        return banner;
    }

    private static Image loadLogo() {
        try {
            InputStream is = LabReportPdfGenerator.class.getResourceAsStream("/images/logo/elite_care_hospital_Logo.png");
            if (is == null) return null;
            byte[] bytes = is.readAllBytes();
            return Image.getInstance(bytes);
        } catch (Exception e) {
            return null;
        }
    }
}
