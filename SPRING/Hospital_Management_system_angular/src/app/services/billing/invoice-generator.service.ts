import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

@Injectable({ providedIn: 'root' })
export class InvoiceGeneratorService {
  generatePdf(billForm: any, billItems: any[], discountPercent: number, taxRate: number): void {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    const pageW = doc.internal.pageSize.getWidth();
    const pageH = doc.internal.pageSize.getHeight();
    const margin = 18;
    const contentW = pageW - margin * 2;
    let y = 0;

    // ===================== BACKGROUND WATERMARK =====================
    const GState = (doc as any).GState;
    if (GState) {
      doc.setGState(new GState({ opacity: 0.03 }));
      doc.setFontSize(80);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(13, 110, 253);
      doc.text('PAID', pageW / 2, pageH / 2, { align: 'center', angle: 45 });
      doc.setGState(new GState({ opacity: 1 }));
    }

    // ===================== TOP GRADIENT HEADER =====================
    const headerH = 42;
    // Dark gradient base
    doc.setFillColor(10, 14, 39);
    doc.rect(0, 0, pageW, headerH, 'F');
    // Accent stripe
    doc.setFillColor(13, 110, 253);
    doc.rect(0, headerH - 3, pageW, 3, 'F');
    // Green accent line
    doc.setFillColor(25, 193, 132);
    doc.rect(0, headerH, pageW, 1, 'F');

    // Hospital name
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(22);
    doc.setFont('helvetica', 'bold');
    doc.text('MODERN HOSPITAL', margin, 16);
    // Tagline
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(180, 195, 220);
    doc.text('Healthcare & Medical Services', margin, 23);
    doc.text('Dhaka, Bangladesh  |  +880-2-XXXX-XXXX  |  info@modernhospital.com', margin, 28);

    // INVOICE badge on right
    doc.setFillColor(25, 193, 132);
    doc.roundedRect(pageW - margin - 42, 8, 42, 14, 3, 3, 'F');
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.text('INVOICE', pageW - margin - 21, 17, { align: 'center' });

    // Invoice details below badge
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(200, 215, 235);
    const invY = 28;
    doc.text('Bill #: ' + (billForm.billNumber || 'N/A'), pageW - margin - 42, invY);
    doc.text('Date: ' + new Date().toLocaleDateString('en-BD', { year: 'numeric', month: 'long', day: 'numeric' }), pageW - margin - 42, invY + 5);

    y = headerH + 10;

    // ===================== PATIENT INFO SECTION =====================
    // Section label
    doc.setFillColor(240, 248, 255);
    doc.roundedRect(margin, y, contentW, 8, 2, 2, 'F');
    doc.setFillColor(13, 110, 253);
    doc.rect(margin, y, 3, 8, 'F');
    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(13, 110, 253);
    doc.text('PATIENT INFORMATION', margin + 7, y + 5.5);
    y += 14;

    // Patient details in two columns
    const col1X = margin + 2;
    const col2X = pageW / 2 + 5;
    const lineH = 5.5;

    const leftInfo = [
      ['Patient Name', billForm.patientName || 'N/A'],
      ['Phone', billForm.phone || 'N/A'],
      ['Gender', billForm.gender || 'N/A'],
    ];
    const rightInfo = [
      ['Patient Code', billForm.patientCode || 'N/A'],
      ['Age', billForm.age ? billForm.age + ' years' : 'N/A'],
      ['Address', billForm.address || 'N/A'],
    ];

    leftInfo.forEach(([label, val], i) => {
      doc.setFontSize(8);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(100, 116, 139);
      doc.text(label + ':', col1X, y + i * lineH);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(30, 41, 59);
      doc.text(String(val).substring(0, 40), col1X + 32, y + i * lineH);
    });

    rightInfo.forEach(([label, val], i) => {
      doc.setFontSize(8);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(100, 116, 139);
      doc.text(label + ':', col2X, y + i * lineH);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(30, 41, 59);
      doc.text(String(val).substring(0, 40), col2X + 32, y + i * lineH);
    });

    y += 20;

    // ===================== ITEMS TABLE =====================
    // Section label
    doc.setFillColor(240, 248, 255);
    doc.roundedRect(margin, y, contentW, 8, 2, 2, 'F');
    doc.setFillColor(13, 110, 253);
    doc.rect(margin, y, 3, 8, 'F');
    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(13, 110, 253);
    doc.text('BILL ITEMS', margin + 7, y + 5.5);
    y += 12;

    const tableData = billItems.map((item, i) => [
      String(i + 1),
      String(item.category || '-'),
      String(item.description || '-'),
      String(item.qty || 0),
      '৳' + Number(item.unitPrice || 0).toLocaleString('en-BD', { minimumFractionDigits: 2 }),
      (item.discount || 0) + '%',
      '৳' + Number(item.amount || 0).toLocaleString('en-BD', { minimumFractionDigits: 2 }),
    ]);

    autoTable(doc, {
      startY: y,
      head: [['#', 'Category', 'Description', 'Qty', 'Unit Price', 'Disc', 'Amount']],
      body: tableData,
      margin: { left: margin, right: margin },
      headStyles: {
        fillColor: [10, 14, 39],
        textColor: [255, 255, 255],
        fontSize: 8,
        fontStyle: 'bold',
        halign: 'left',
        cellPadding: { top: 5, bottom: 5, left: 4, right: 4 },
      },
      bodyStyles: {
        fontSize: 8,
        textColor: [51, 65, 85],
        cellPadding: { top: 4, bottom: 4, left: 4, right: 4 },
      },
      alternateRowStyles: {
        fillColor: [248, 250, 252],
      },
      columnStyles: {
        0: { cellWidth: 10, halign: 'center', fontStyle: 'bold', textColor: [13, 110, 253] },
        1: { cellWidth: 28 },
        2: { cellWidth: 52 },
        3: { cellWidth: 14, halign: 'center' },
        4: { cellWidth: 26, halign: 'right' },
        5: { cellWidth: 16, halign: 'center', textColor: [220, 38, 38] },
        6: { cellWidth: 26, halign: 'right', fontStyle: 'bold', textColor: [5, 150, 105] },
      },
      theme: 'plain',
      didDrawCell: (data: any) => {
        // Draw subtle bottom border for each row
        if (data.section === 'body') {
          const { x, y: cellY, width, height } = data.cell;
          data.doc.setDrawColor(230, 235, 245);
          data.doc.setLineWidth(0.2);
          data.doc.line(x, cellY + height, x + width, cellY + height);
        }
      },
    });

    y = (doc as any).lastAutoTable.finalY + 8;

    // ===================== SUMMARY SECTION =====================
    const subtotal = billItems.reduce((s: number, i: any) => s + (i.amount || 0), 0);
    const discountAmt = subtotal * ((discountPercent || 0) / 100);
    const taxAmt = (subtotal - discountAmt) * (taxRate || 0.18);
    const total = subtotal - discountAmt + taxAmt;

    const summaryW = 75;
    const summaryX = pageW - margin - summaryW;

    // Summary box background
    doc.setFillColor(248, 250, 252);
    doc.roundedRect(summaryX - 5, y, summaryW + 5, 48, 3, 3, 'F');
    doc.setDrawColor(226, 232, 240);
    doc.setLineWidth(0.3);
    doc.roundedRect(summaryX - 5, y, summaryW + 5, 48, 3, 3, 'S');

    let sy = y + 8;
    const labelX = summaryX;
    const valX = summaryX + summaryW;

    // Subtotal
    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(100, 116, 139);
    doc.text('Subtotal', labelX, sy);
    doc.setTextColor(51, 65, 85);
    doc.text('৳' + subtotal.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
    sy += 7;

    // Discount
    if (discountPercent > 0) {
      doc.setTextColor(100, 116, 139);
      doc.text('Discount (' + discountPercent + '%)', labelX, sy);
      doc.setTextColor(220, 38, 38);
      doc.text('- ৳' + discountAmt.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
      sy += 7;
    }

    // Tax
    doc.setTextColor(100, 116, 139);
    doc.text('Tax (' + ((taxRate || 0.18) * 100).toFixed(0) + '%)', labelX, sy);
    doc.setTextColor(51, 65, 85);
    doc.text('৳' + taxAmt.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
    sy += 9;

    // Divider
    doc.setDrawColor(13, 110, 253);
    doc.setLineWidth(0.8);
    doc.line(labelX, sy, valX, sy);
    sy += 8;

    // TOTAL
    doc.setFontSize(13);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(10, 14, 39);
    doc.text('TOTAL', labelX, sy);
    doc.setTextColor(13, 110, 253);
    doc.text('৳' + total.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });

    // ===================== NOTES SECTION =====================
    if (billForm.notes) {
      y = Math.max(y + 54, sy + 10);
      doc.setFillColor(255, 251, 235);
      doc.roundedRect(margin, y, contentW, 16, 3, 3, 'F');
      doc.setDrawColor(253, 230, 138);
      doc.setLineWidth(0.3);
      doc.roundedRect(margin, y, contentW, 16, 3, 3, 'S');
      doc.setFontSize(8);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(180, 83, 9);
      doc.text('NOTES:', margin + 4, y + 5);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(146, 64, 14);
      const notesLines = doc.splitTextToSize(billForm.notes, contentW - 12);
      doc.text(notesLines.slice(0, 2), margin + 4, y + 11);
    }

    // ===================== FOOTER =====================
    const footerY = pageH - 20;
    // Footer line
    doc.setDrawColor(226, 232, 240);
    doc.setLineWidth(0.3);
    doc.line(margin, footerY, pageW - margin, footerY);

    // Thank you message
    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(13, 110, 253);
    doc.text('Thank you for choosing Modern Hospital!', margin, footerY + 6);

    doc.setFontSize(7);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(148, 163, 184);
    doc.text('This is a computer-generated invoice. For any queries, please contact billing@modernhospital.com', margin, footerY + 11);
    doc.text('© ' + new Date().getFullYear() + ' Modern Hospital Management System. All rights reserved.', margin, footerY + 15);

    // Payment status badge on right
    doc.setFillColor(240, 253, 244);
    doc.roundedRect(pageW - margin - 30, footerY + 2, 30, 8, 2, 2, 'F');
    doc.setFontSize(7);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(22, 163, 74);
    doc.text('GENERATED', pageW - margin - 15, footerY + 7, { align: 'center' });

    // ===================== SAVE =====================
    doc.save(`Invoice-${billForm.billNumber || 'bill'}.pdf`);
  }
}
