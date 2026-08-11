package emranhss.com.Modern_Hospital_Management_System.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoice;
import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoiceItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InvoicePdfGenerator {

    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

    public static byte[] generate(BillingInvoice invoice) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4, 25, 25, 25, 25);

        PdfWriter writer = PdfWriter.getInstance(document, out);

        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter w, Document doc) {
                try {
                    PdfContentByte canvas = w.getDirectContent();
                    float pageWidth = doc.getPageSize().getWidth();
                    float left = doc.leftMargin();
                    float bottomY = 25;

                    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                    BaseFont bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

                    canvas.setFontAndSize(bf, 10);
                    canvas.showTextAligned(Element.ALIGN_LEFT, "Thank you for choosing", left, bottomY + 36, 0);
                    canvas.setFontAndSize(bfBold, 12);
                    canvas.showTextAligned(Element.ALIGN_LEFT, "ELITE CARE HOSPITAL", left, bottomY + 24, 0);
                    canvas.setFontAndSize(bf, 10);
                    canvas.showTextAligned(Element.ALIGN_LEFT, "Get Well Soon", left, bottomY + 12, 0);

                    canvas.setRGBColorStroke(200, 200, 200);
                    canvas.setLineWidth(0.5f);
                    float lineLeft = pageWidth - 18 - 150;
                    canvas.moveTo(lineLeft, bottomY + 24);
                    canvas.lineTo(pageWidth - 18, bottomY + 24);
                    canvas.stroke();

                    canvas.setFontAndSize(bfBold, 13);
                    canvas.showTextAligned(Element.ALIGN_LEFT, "Authorized Signature", lineLeft, bottomY + 10, 0);
                    canvas.setFontAndSize(bf, 10);
                    canvas.showTextAligned(Element.ALIGN_LEFT, "Billing / Accounts", lineLeft, bottomY - 2, 0);

                    canvas.setFontAndSize(bf, 9);
                    String powered = "Powered By Elite IT Institute";
                    float pw = bf.getWidthPoint(powered, 9);
                    canvas.showTextAligned(Element.ALIGN_CENTER, powered, pageWidth / 2, bottomY - 18, 0);
                } catch (Exception ignored) {
                }
            }
        });

        document.open();

        // ==========================================================
        // 1. Watermark (PAID / PARTIAL / DRAFT / CANCELLED)
        // ==========================================================

        addWatermark(writer, invoice);

        // ==========================================================
        // 2. Hospital Header + INVOICE Title
        // ==========================================================

        addHeader(document);

        // ==========================================================
        // 3. Bill To / Invoice Information
        // ==========================================================

        addInvoiceInfo(document, invoice);

        // ==========================================================
        // 4. Invoice Items Table
        // ==========================================================

        addItemsTable(document, invoice);

        // ==========================================================
        // 5. Summary Section
        // ==========================================================

        addSummary(document, invoice);

        // ==========================================================
        // 6. Notes
        // ==========================================================

        if (invoice.getNotes() != null && !invoice.getNotes().trim().isEmpty()) {
            document.add(PdfStyle.sectionBanner("NOTES"));
            Paragraph notes = new Paragraph(invoice.getNotes(), PdfStyle.VALUE_FONT);
            notes.setSpacingAfter(8);
            document.add(notes);
        }

        // ==========================================================
        // Footer is drawn by PageEvent (onEndPage) at fixed bottom position
        // ==========================================================

        document.close();

        return out.toByteArray();
    }

    // ==========================================================
    // Watermark
    // ==========================================================

    private static void addWatermark(PdfWriter writer, BillingInvoice invoice) throws DocumentException, IOException {
        String status = invoice.getPaymentStatus() != null ? invoice.getPaymentStatus() : "UNPAID";
        String text = switch (status) {
            case "PAID" -> "PAID";
            case "PARTIAL" -> "PARTIAL PAYMENT";
            case "REFUNDED" -> "REFUNDED";
            case "CANCELLED" -> "CANCELLED";
            default -> "DRAFT";
        };

        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

        PdfContentByte canvas = writer.getDirectContentUnder();
        Rectangle page = writer.getPageSize();

        canvas.saveState();
        canvas.beginText();
        canvas.setFontAndSize(bf, 42);
        canvas.setColorFill(new BaseColor(210, 210, 210));
        canvas.showTextAligned(Element.ALIGN_CENTER, text, (page.getLeft() + page.getRight()) / 2, (page.getTop() + page.getBottom()) / 2, 45);
        canvas.endText();
        canvas.restoreState();
    }

    // ==========================================================
    // Header (Logo + Hospital Info + INVOICE title)
    // ==========================================================

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
            logo.scaleAbsolute(90, 90);
            logoCell.addElement(logo);
        }
        header.addCell(logoCell);

        Paragraph hospital = new Paragraph("ELITE CARE HOSPITAL", PdfStyle.HOSPITAL_FONT);
        Paragraph address = new Paragraph("House #25, Road #12, Dhanmondi, Dhaka-1209", PdfStyle.VALUE_FONT);
        Paragraph phone = new Paragraph("Phone : +880 1711-123456  |  Email : info@elitecarehospital.com", PdfStyle.VALUE_FONT);

        Paragraph title = new Paragraph("INVOICE", PdfStyle.TITLE_FONT);
        title.setAlignment(Element.ALIGN_RIGHT);
        title.setSpacingBefore(6);

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
        ruleCell.setFixedHeight(1.2f);
        ruleCell.setBackgroundColor(new BaseColor(25, 118, 210));
        ruleCell.setBorder(Rectangle.NO_BORDER);
        rule.addCell(ruleCell);
        infoCell.addElement(rule);

        infoCell.addElement(title);
        header.addCell(infoCell);

        document.add(header);
    }

    private static Image loadLogo() {
        try {
            InputStream is = PdfHeader.class.getResourceAsStream("/images/logo/elite_care_hospital_Logo.png");
            if (is == null) return null;
            byte[] bytes = is.readAllBytes();
            return Image.getInstance(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    // ==========================================================
    // Bill To / Invoice Information
    // ==========================================================

    private static void addInvoiceInfo(Document document, BillingInvoice invoice) throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);
        table.setWidths(new float[]{1, 1});

        // ----- Bill To -----
        PdfPCell patientCell = new PdfPCell();
        patientCell.setPadding(8);

        patientCell.addElement(PdfStyle.sectionBanner("BILL TO"));

        if (invoice.getPatient() != null) {
            addLine(patientCell, "Name : " + nvl(invoice.getPatient().getName()));
            addLine(patientCell, "Patient ID : " + nvl(invoice.getPatient().getPatientCode()));
            addLine(patientCell, "Phone : " + nvl(invoice.getPatient().getPhone()));
            addLine(patientCell, "Address : " + nvl(invoice.getPatient().getAddress()));
        }
        table.addCell(patientCell);

        // ----- Invoice Info -----
        PdfPCell invCell = new PdfPCell();
        invCell.setPadding(8);

        invCell.addElement(PdfStyle.sectionBanner("INVOICE INFORMATION"));

        addLine(invCell, "Invoice No : " + nvl(invoice.getInvoiceNumber()));
        addLine(invCell, "Date : " + (invoice.getCreatedDate() != null
                ? invoice.getCreatedDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
                : "-"));
        addLine(invCell, "Type : " + nvl(invoice.getInvoiceType()));
        addLine(invCell, "Status : " + nvl(invoice.getInvoiceStatus()));
        if (invoice.getReferringDoctor() != null && invoice.getReferringDoctor().getUser() != null) {
            addLine(invCell, "Doctor : " + nvl(invoice.getReferringDoctor().getUser().getName()));
        }
        table.addCell(invCell);

        document.add(table);
    }

    private static void addLine(PdfPCell cell, String text) {
        cell.addElement(new Paragraph(text, PdfStyle.VALUE_FONT));
    }

    // ==========================================================
    // Items Table
    // ==========================================================

    private static void addItemsTable(Document document, BillingInvoice invoice) throws Exception {
        document.add(PdfStyle.sectionBanner("INVOICE ITEMS"));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.6f, 3.4f, 1.6f, 0.9f, 1.4f, 1.2f, 1.6f});
        table.setSpacingAfter(8);

        String[] headers = {"#", "Description", "Category", "Qty", "Unit Price", "Disc %", "Amount"};
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, headerFont));
            c.setBackgroundColor(new BaseColor(25, 118, 210));
            c.setPadding(5);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        List<BillingInvoiceItem> items = invoice.getItems();
        int idx = 1;
        if (items != null) {
            for (BillingInvoiceItem item : items) {
                if (!"ACTIVE".equals(item.getItemStatus())) continue;
                PdfPCell c1 = cell(String.valueOf(idx++), Element.ALIGN_CENTER);
                PdfPCell c2 = cell(nvl(item.getDescription()), Element.ALIGN_LEFT);
                PdfPCell c3 = cell(nvl(item.getCategoryCode()), Element.ALIGN_LEFT);
                PdfPCell c4 = cell(String.valueOf(item.getQuantity()), Element.ALIGN_CENTER);
                PdfPCell c5 = cell(money(item.getUnitPrice()), Element.ALIGN_RIGHT);
                PdfPCell c6 = cell(money(item.getDiscountPercent()), Element.ALIGN_RIGHT);
                PdfPCell c7 = cell(money(item.getAmount()), Element.ALIGN_RIGHT);
                table.addCell(c1);
                table.addCell(c2);
                table.addCell(c3);
                table.addCell(c4);
                table.addCell(c5);
                table.addCell(c6);
                table.addCell(c7);
            }
        }

        document.add(table);
    }

    private static PdfPCell cell(String text, int align) {
        PdfPCell c = new PdfPCell(new Phrase(text, PdfStyle.VALUE_FONT));
        c.setPadding(4);
        c.setHorizontalAlignment(align);
        return c;
    }

    // ==========================================================
    // Summary Section
    // ==========================================================

    private static void addSummary(Document document, BillingInvoice invoice) throws Exception {
        PdfPTable summary = new PdfPTable(2);
        summary.setWidthPercentage(100);
        summary.setSpacingBefore(4);
        summary.setSpacingAfter(10);
        summary.setWidths(new float[]{3, 1});

        PdfPCell spacer = new PdfPCell();
        spacer.setBorder(Rectangle.NO_BORDER);
        summary.addCell(spacer);

        PdfPCell totals = new PdfPCell();
        totals.setBorder(Rectangle.BOX);
        totals.setPadding(6);

        addTotalLine(totals, "Subtotal", money(invoice.getSubtotal()));
        if (invoice.getDiscountPercent() != null && invoice.getDiscountPercent() > 0) {
            addTotalLine(totals, "Discount (" + money(invoice.getDiscountPercent()) + "%)", "(" + money(invoice.getDiscountAmount()) + ")");
        } else {
            addTotalLine(totals, "Discount", money(invoice.getDiscountAmount()));
        }
        if (invoice.getTaxRate() != null && invoice.getTaxRate() > 0) {
            addTotalLine(totals, "Tax (" + money(invoice.getTaxRate()) + "%)", money(invoice.getTaxAmount()));
        } else {
            addTotalLine(totals, "Tax", money(invoice.getTaxAmount()));
        }

        Paragraph net = new Paragraph("NET AMOUNT", PdfStyle.LABEL_FONT);
        Paragraph netVal = new Paragraph("BDT " + money(invoice.getNetAmount()), PdfStyle.TITLE_FONT);
        netVal.setAlignment(Element.ALIGN_RIGHT);
        totals.addElement(net);
        totals.addElement(netVal);

        addTotalLine(totals, "Already Paid", "(" + money(invoice.getTotalPaid()) + ")");
        addTotalLine(totals, "Due Amount", money(invoice.getDueAmount()));

        summary.addCell(totals);
        document.add(summary);
    }

    private static void addTotalLine(PdfPCell cell, String label, String value) throws DocumentException {
        PdfPTable row = new PdfPTable(2);
        row.setWidthPercentage(100);
        row.setWidths(new float[]{1.4f, 1f});
        PdfPCell l = new PdfPCell(new Phrase(label, PdfStyle.VALUE_FONT));
        l.setBorder(Rectangle.NO_BORDER);
        PdfPCell v = new PdfPCell(new Phrase(value, PdfStyle.VALUE_FONT));
        v.setBorder(Rectangle.NO_BORDER);
        v.setHorizontalAlignment(Element.ALIGN_RIGHT);
        row.addCell(l);
        row.addCell(v);
        cell.addElement(row);
    }

    // ==========================================================
    // Footer
    // ==========================================================

    private static void addFooter(Document document) throws Exception {
        PdfPTable footer = new PdfPTable(2);
        footer.setWidthPercentage(100);
        footer.setWidths(new float[]{1, 1});

        PdfPCell thanksCell = new PdfPCell();
        thanksCell.setBorder(Rectangle.NO_BORDER);
        thanksCell.addElement(new Paragraph("Thank you for choosing", PdfStyle.VALUE_FONT));
        thanksCell.addElement(new Paragraph("ELITE CARE HOSPITAL", PdfStyle.LABEL_FONT));
        thanksCell.addElement(new Paragraph("Get Well Soon", PdfStyle.VALUE_FONT));

        PdfPCell signCell = new PdfPCell();
        signCell.setBorder(Rectangle.NO_BORDER);
        Paragraph line = new Paragraph("____________________________");
        line.setAlignment(Element.ALIGN_CENTER);
        signCell.addElement(line);
        Paragraph auth = new Paragraph(nvl("Authorized Signature"), PdfStyle.TITLE_FONT);
        auth.setAlignment(Element.ALIGN_CENTER);
        signCell.addElement(auth);
        Paragraph role = new Paragraph("Billing / Accounts", PdfStyle.VALUE_FONT);
        role.setAlignment(Element.ALIGN_CENTER);
        signCell.addElement(role);

        footer.addCell(thanksCell);
        footer.addCell(signCell);
        document.add(footer);

        Paragraph powered = new Paragraph("Powered By Elite IT Institute", PdfStyle.VALUE_FONT);
        powered.setAlignment(Element.ALIGN_CENTER);
        document.add(powered);
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    private static String nvl(String s) {
        return s == null || s.trim().isEmpty() ? "-" : s;
    }

    private static String money(Double d) {
        return MONEY.format(d == null ? 0.0 : d);
    }
}
