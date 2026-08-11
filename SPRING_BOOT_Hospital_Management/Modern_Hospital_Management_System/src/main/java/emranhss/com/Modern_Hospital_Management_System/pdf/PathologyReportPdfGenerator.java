package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.LabReportMapper;
import emranhss.com.Modern_Hospital_Management_System.entity.LabReport;
import emranhss.com.Modern_Hospital_Management_System.entity.LabReportResult;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.enums.ParameterStatus;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Base64;
import java.util.Date;

public class PathologyReportPdfGenerator {

    private static final Font HOSPITAL_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(25, 118, 210));
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    private static final Font VALUE_FONT = new Font(Font.FontFamily.HELVETICA, 9);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
    private static final BaseColor ABNORMAL_RED = new BaseColor(220, 53, 69);
    private static final BaseColor BRAND_BLUE = new BaseColor(25, 118, 210);
    private static final BaseColor LIGHT_BG = new BaseColor(242, 246, 251);

    public static byte[] generate(LabReport report) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addHeader(document, report);
        addPatientSection(document, report);
        addResultSection(document, report);
        addInterpretationSection(document, report);
        addSpecialistSection(document, report);
        addFooter(document, report);

        document.close();
        return out.toByteArray();
    }

    private static void addHeader(Document document, LabReport report) throws Exception {
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1.2f, 6f});
        header.setSpacingAfter(4);

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Image logo = loadLogo();
        if (logo != null) {
            logo.scaleAbsolute(70, 70);
            logoCell.addElement(logo);
        }
        header.addCell(logoCell);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.addElement(new Paragraph("ELITE CARE HOSPITAL", HOSPITAL_FONT));
        infoCell.addElement(new Paragraph("House #25, Road #12, Dhanmondi, Dhaka-1209", VALUE_FONT));
        infoCell.addElement(new Paragraph("Phone: +880 1711-123456  |  Email: info@elitecare.com", VALUE_FONT));
        header.addCell(infoCell);
        document.add(header);

        PdfPTable rule = new PdfPTable(1);
        rule.setWidthPercentage(100);
        rule.setSpacingBefore(2);
        rule.setSpacingAfter(4);
        PdfPCell ruleCell = new PdfPCell();
        ruleCell.setFixedHeight(2f);
        ruleCell.setBackgroundColor(BRAND_BLUE);
        ruleCell.setBorder(Rectangle.NO_BORDER);
        rule.addCell(ruleCell);
        document.add(rule);

        Paragraph title = new Paragraph("PATHOLOGY LABORATORY REPORT", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(6);
        title.setSpacingAfter(2);
        document.add(title);

        PdfPTable meta = new PdfPTable(3);
        meta.setWidthPercentage(100);
        meta.setSpacingAfter(8);
        addInfoCell(meta, "Report No", report.getReportNumber() != null ? report.getReportNumber() : "N/A");
        addInfoCell(meta, "Report Status", report.getReportStatus() != null ? report.getReportStatus().name() : "PENDING",
                colorFromHex(LabReportMapper.statusColor(report.getReportStatus())));
        Tests test = report.getTestOrder();
        addInfoCell(meta, "Test", (test != null && test.getTestMaster() != null) ? test.getTestMaster().getTestName() : "N/A");
        document.add(meta);
    }

    private static void addPatientSection(Document document, LabReport report) throws Exception {
        document.add(sectionBanner("PATIENT & REFERRAL INFORMATION"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingAfter(4);

        Tests test = report.getTestOrder();
        Patient patient = test != null ? test.getPatient() : null;
        String gender = patient != null ? patient.getGender() : null;
        String age = "";
        if (patient != null && patient.getDateOfBirth() != null) {
            age = String.valueOf(Period.between(patient.getDateOfBirth(), java.time.LocalDate.now()).getYears()) + " yrs";
        }

        addInfoCell(table, "Patient Code", patient != null ? patient.getPatientCode() : "N/A");
        addInfoCell(table, "Patient Name", patient != null ? patient.getName() : "N/A");
        addInfoCell(table, "Age / Gender", (age.isBlank() ? "N/A" : age) + " / " + (gender != null ? gender : "N/A"));
        addInfoCell(table, "Phone", patient != null ? patient.getPhone() : "N/A");

        document.add(table);

        PdfPTable ref = new PdfPTable(3);
        ref.setWidthPercentage(100);
        ref.setSpacingAfter(8);
        addInfoCell(ref, "Referring Doctor",
                test != null && test.getPrescribedBy() != null ? "Dr. " + test.getPrescribedBy().getUser().getName() : "N/A");
        addInfoCell(ref, "Specialization",
                test != null && test.getPrescribedBy() != null ? test.getPrescribedBy().getSpecialization() : "N/A");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        Date collected = test != null && test.getSampleCollectedDate() != null
                ? Date.from(test.getSampleCollectedDate().atZone(java.time.ZoneId.systemDefault()).toInstant()) : null;
        Date reported = report.getReportedDate() != null
                ? Date.from(report.getReportedDate().atZone(java.time.ZoneId.systemDefault()).toInstant()) : null;
        addInfoCell(ref, "Sample Collection Time", collected != null ? sdf.format(collected) : "N/A");
        addInfoCell(ref, "Report Time", reported != null ? sdf.format(reported) : "Pending verification");
        document.add(ref);
    }

    private static void addResultSection(Document document, LabReport report) throws Exception {
        document.add(sectionBanner("TEST RESULTS"));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.6f, 1.8f, 1.2f, 2.2f, 1.4f});
        table.setSpacingBefore(4);
        table.setSpacingAfter(8);

        addResultHeader(table, "Parameter");
        addResultHeader(table, "Result");
        addResultHeader(table, "Unit");
        addResultHeader(table, "Reference Range");
        addResultHeader(table, "Status");

        for (LabReportResult r : report.getResults()) {
            boolean abnormal = Boolean.TRUE.equals(r.getAbnormal());
            boolean critical = Boolean.TRUE.equals(r.getCritical());

            Font resultFont = critical
                    ? new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, ABNORMAL_RED)
                    : (abnormal ? new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, new BaseColor(253, 126, 20)) : VALUE_FONT);

            PdfPCell nameCell = cell(new Phrase(r.getParameterName(), BOLD_FONT));
            nameCell.setBackgroundColor(abnormal ? new BaseColor(253, 247, 242) : null);
            table.addCell(nameCell);

            String prefix = critical ? "! " : (abnormal ? "\u25B2 " : "");
            PdfPCell valueCell = cell(new Phrase(prefix + (r.getResultValue() != null ? r.getResultValue() : "—"), resultFont));
            if (abnormal) valueCell.setBackgroundColor(new BaseColor(253, 247, 242));
            table.addCell(valueCell);

            PdfPCell unitCell = cell(new Phrase(r.getUnit() != null ? r.getUnit() : "—", VALUE_FONT));
            if (abnormal) unitCell.setBackgroundColor(new BaseColor(253, 247, 242));
            table.addCell(unitCell);

            String rangeText = r.getReferenceRange() != null && r.getReferenceRange().getDisplayRange() != null
                    ? r.getReferenceRange().getDisplayRange() : "N/A";
            PdfPCell rangeCell = cell(new Phrase(rangeText, VALUE_FONT));
            if (abnormal) rangeCell.setBackgroundColor(new BaseColor(253, 247, 242));
            table.addCell(rangeCell);

            PdfPCell statusCell = cell(new Phrase(String.valueOf(r.getStatus()), resultFont));
            statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (abnormal) statusCell.setBackgroundColor(new BaseColor(253, 247, 242));
            table.addCell(statusCell);
        }

        document.add(table);
    }

    private static void addInterpretationSection(Document document, LabReport report) throws Exception {
        ReportStatus status = report.getReportStatus();
        BaseColor statusColor = colorFromHex(LabReportMapper.statusColor(status));
        BaseColor bgColor = statusColor == null ? BaseColor.LIGHT_GRAY
                : new BaseColor(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), 25);

        PdfPTable statusBanner = new PdfPTable(1);
        statusBanner.setWidthPercentage(100);
        statusBanner.setSpacingAfter(6);
        PdfPCell banner = new PdfPCell(new Phrase("OVERALL REPORT STATUS: " + (status != null ? status.name() : "PENDING"),
                new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, statusColor != null ? statusColor : BaseColor.DARK_GRAY)));
        banner.setPadding(8);
        banner.setBackgroundColor(bgColor);
        banner.setBorder(Rectangle.NO_BORDER);
        banner.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusBanner.addCell(banner);
        document.add(statusBanner);

        if (report.getFinalImpression() != null && !report.getFinalImpression().isBlank()) {
            document.add(sectionBanner("FINAL IMPRESSION"));
            PdfPTable imp = new PdfPTable(1);
            imp.setWidthPercentage(100);
            imp.setSpacingAfter(6);
            PdfPCell impCell = new PdfPCell(new Phrase(report.getFinalImpression(), VALUE_FONT));
            impCell.setPadding(8);
            impCell.setBorder(Rectangle.NO_BORDER);
            imp.addCell(impCell);
            document.add(imp);
        }

        if (report.getRecommendation() != null && !report.getRecommendation().isBlank()) {
            document.add(sectionBanner("RECOMMENDATION"));
            PdfPTable rec = new PdfPTable(1);
            rec.setWidthPercentage(100);
            rec.setSpacingAfter(8);
            PdfPCell recCell = new PdfPCell(new Phrase(report.getRecommendation(), VALUE_FONT));
            recCell.setPadding(8);
            recCell.setBorder(Rectangle.NO_BORDER);
            rec.addCell(recCell);
            document.add(rec);
        }
    }

    private static void addSpecialistSection(Document document, LabReport report) throws Exception {
        document.add(new Paragraph(" "));
        PdfPTable banner = sectionBanner("VERIFIED & SIGNED BY");
        document.add(banner);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 4f});
        table.setSpacingAfter(8);

        PdfPCell left = new PdfPCell();
        left.setBorder(Rectangle.NO_BORDER);
        left.setPadding(6);
        if (report.getSpecialistSignature() != null && !report.getSpecialistSignature().isBlank()) {
            Image sig = loadSignature(report.getSpecialistSignature());
            if (sig != null) {
                sig.scaleAbsolute(110, 55);
                left.addElement(sig);
            }
        } else {
            left.addElement(new Paragraph(" ", VALUE_FONT));
            left.addElement(new Paragraph("(signature)", SMALL_FONT));
        }
        table.addCell(left);

        PdfPCell right = new PdfPCell();
        right.setBorder(Rectangle.NO_BORDER);
        right.setPadding(6);
        right.addElement(new Paragraph(report.getSpecialistName() != null ? "Dr. " + report.getSpecialistName() : "Pending",
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        right.addElement(new Paragraph(report.getSpecialistDesignation() != null ? report.getSpecialistDesignation() : "Consultant",
                VALUE_FONT));
        right.addElement(new Paragraph("Specialist In-Charge", SMALL_FONT));
        table.addCell(right);

        document.add(table);
    }

    private static void addFooter(Document document, LabReport report) throws Exception {
        PdfPTable footer = new PdfPTable(1);
        footer.setWidthPercentage(100);
        footer.setSpacingBefore(12);

        PdfPCell lineCell = new PdfPCell();
        lineCell.setFixedHeight(1f);
        lineCell.setBackgroundColor(BRAND_BLUE);
        lineCell.setBorder(Rectangle.NO_BORDER);
        footer.addCell(lineCell);

        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setPadding(8);
        textCell.addElement(new Paragraph(
                "This report is auto-generated and verified by the laboratory. Results marked \u25B2 are abnormal, "
                        + "results marked \u0021 are critical. For any query, contact the laboratory department.",
                SMALL_FONT));
        footer.addCell(textCell);
        document.add(footer);
    }

    // ---------- helpers ----------

    private static BaseColor colorFromHex(String hex) {
        if (hex == null || hex.isBlank()) return BaseColor.LIGHT_GRAY;
        try {
            return new BaseColor(
                    Integer.valueOf(hex.substring(1, 3), 16),
                    Integer.valueOf(hex.substring(3, 5), 16),
                    Integer.valueOf(hex.substring(5, 7), 16));
        } catch (Exception e) {
            return BaseColor.LIGHT_GRAY;
        }
    }

    private static PdfPCell cell(Phrase phrase) {
        PdfPCell c = new PdfPCell(phrase);
        c.setPadding(6);
        return c;
    }

    private static void addResultHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, SECTION_FONT));
        cell.setBackgroundColor(BRAND_BLUE);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addInfoCell(PdfPTable table, String label, String value) {
        addInfoCell(table, label, value, null);
    }

    private static void addInfoCell(PdfPTable table, String label, String value, BaseColor valueColor) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        cell.setBorder(Rectangle.BOTTOM);
        cell.addElement(new Paragraph(label, LABEL_FONT));
        Font f = valueColor != null ? new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, valueColor) : VALUE_FONT;
        cell.addElement(new Paragraph(value != null ? value : "N/A", f));
        table.addCell(cell);
    }

    private static PdfPTable sectionBanner(String text) {
        PdfPTable banner = new PdfPTable(1);
        banner.setWidthPercentage(100);
        banner.setSpacingBefore(6);
        banner.setSpacingAfter(4);
        PdfPCell cell = new PdfPCell(new Phrase(text, SECTION_FONT));
        cell.setBackgroundColor(BRAND_BLUE);
        cell.setPadding(6);
        cell.setBorder(Rectangle.NO_BORDER);
        banner.addCell(cell);
        return banner;
    }

    private static Image loadLogo() {
        try {
            InputStream is = PathologyReportPdfGenerator.class.getResourceAsStream("/images/logo/elite_care_hospital_Logo.png");
            if (is == null) return null;
            return Image.getInstance(is.readAllBytes());
        } catch (Exception e) {
            return null;
        }
    }

    private static Image loadSignature(String signature) {
        try {
            String raw = signature;
            if (raw.contains(",")) {
                raw = raw.substring(raw.indexOf(',') + 1);
            }
            byte[] bytes = Base64.getDecoder().decode(raw);
            return Image.getInstance(bytes);
        } catch (Exception e) {
            return null;
        }
    }
}
