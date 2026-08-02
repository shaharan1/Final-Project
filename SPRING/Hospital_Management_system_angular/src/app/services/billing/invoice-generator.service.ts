import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

@Injectable({ providedIn: 'root' })
export class InvoiceGeneratorService {
  generatePdf(billForm: any, billItems: any[], discountPercent: number, taxRate: number, activeInvoice?: any): void {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    const pageW = doc.internal.pageSize.getWidth();
    const pageH = doc.internal.pageSize.getHeight();
    const margin = 18;
    const contentW = pageW - margin * 2;
    let y = 0;

    const now = new Date();
    const dateStr = now.toLocaleDateString('en-BD', { year: 'numeric', month: 'long', day: 'numeric' });
    const timeStr = now.toLocaleTimeString('en-BD', { hour: '2-digit', minute: '2-digit' });

    const invNumber = billForm.invoiceNumber || billForm.billNumber || 'N/A';

    const computedItems = billItems.filter((i: any) => (i.itemStatus || 'ACTIVE') === 'ACTIVE').map((item: any, idx: number) => {
      const qty = Number(item.quantity ?? item.qty ?? 0);
      const price = Number(item.unitPrice ?? 0);
      const discPct = Number(item.discountPercent ?? item.discount ?? 0);
      const base = qty * price;
      const discAmt = base * discPct / 100;
      const amount = base - discAmt;
      return {
        index: idx + 1,
        categoryName: item.categoryName || item.category || item.categoryCode || '-',
        description: String(item.description || '-'),
        quantity: qty,
        unitPrice: price,
        discountPercent: discPct,
        amount: amount
      };
    });

    const subtotal = computedItems.reduce((s, i) => s + i.amount, 0);
    const discountAmt = subtotal * ((discountPercent || 0) / 100);
    const taxable = subtotal - discountAmt;
    const taxAmt = taxable * ((taxRate || 0) / 100);
    const total = taxable + taxAmt;
    const totalPaid = activeInvoice?.totalPaid || 0;
    const dueAmount = total - totalPaid;

    const invStatus = activeInvoice?.invoiceStatus || 'DRAFT';
    const payStatus = activeInvoice?.paymentStatus || 'UNPAID';

    // ===================== WATERMARK =====================
    try {
      const GState = (doc as any).GState;
      if (GState) {
        doc.setGState(new GState({ opacity: 0.03 }));
        doc.setFontSize(80);
        doc.setFont('helvetica', 'bold');
        doc.setTextColor(13, 110, 253);
        const label = payStatus === 'PAID' ? 'PAID' : payStatus === 'PARTIAL' ? 'PARTIAL' : invStatus;
        doc.text(label, pageW / 2, pageH / 2, { align: 'center', angle: 45 });
        doc.setGState(new GState({ opacity: 1 }));
      }
    } catch (_) {}

    // ===================== HEADER =====================
    const headerH = 42;
    doc.setFillColor(10, 14, 39);
    doc.rect(0, 0, pageW, headerH, 'F');
    doc.setFillColor(13, 110, 253);
    doc.rect(0, headerH - 3, pageW, 3, 'F');
    doc.setFillColor(25, 193, 132);
    doc.rect(0, headerH, pageW, 1, 'F');

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(22);
    doc.setFont('helvetica', 'bold');
    doc.text('ELITE CARE HOSPITAL', margin, 16);
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(180, 195, 220);
    doc.text('Healthcare & Medical Services', margin, 23);
    doc.text('Dhaka, Bangladesh  |  +880-2-XXXX-XXXX  |  info@elitecarehospital.com', margin, 28);

    doc.setFillColor(25, 193, 132);
    doc.roundedRect(pageW - margin - 42, 8, 42, 14, 3, 3, 'F');
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.text('INVOICE', pageW - margin - 21, 17, { align: 'center' });

    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(200, 215, 235);
    const invY = 28;
    doc.text('Bill #: ' + invNumber, pageW - margin - 42, invY);
    doc.text('Date: ' + dateStr, pageW - margin - 42, invY + 5);
    doc.text('Time: ' + timeStr, pageW - margin - 42, invY + 10);

    y = headerH + 10;

    // ===================== PATIENT INFO =====================
    doc.setFillColor(240, 248, 255);
    doc.roundedRect(margin, y, contentW, 8, 2, 2, 'F');
    doc.setFillColor(13, 110, 253);
    doc.rect(margin, y, 3, 8, 'F');
    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(13, 110, 253);
    doc.text('PATIENT INFORMATION', margin + 7, y + 5.5);
    y += 14;

    const col1X = margin + 2;
    const col2X = pageW / 2 + 5;
    const lineH = 5.5;

    const leftInfo: [string, string][] = [
      ['Patient Name', billForm.patientName || 'N/A'],
      ['Phone', billForm.phone || 'N/A'],
      ['Gender', billForm.gender || 'N/A'],
    ];
    const rightInfo: [string, string][] = [
      ['Patient Code', billForm.patientCode || 'N/A'],
      ['Age', billForm.age ? billForm.age + ' years' : 'N/A'],
      ['Invoice Type', billForm.invoiceType || 'N/A'],
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

    // ===================== BILL ITEMS TABLE =====================
    doc.setFillColor(240, 248, 255);
    doc.roundedRect(margin, y, contentW, 8, 2, 2, 'F');
    doc.setFillColor(13, 110, 253);
    doc.rect(margin, y, 3, 8, 'F');
    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(13, 110, 253);
    doc.text('BILL ITEMS (' + computedItems.length + ')', margin + 7, y + 5.5);
    y += 12;

    const tableData = computedItems.map((item) => [
      String(item.index),
      item.categoryName,
      item.description.substring(0, 60),
      String(item.quantity),
      '৳' + item.unitPrice.toLocaleString('en-BD', { minimumFractionDigits: 2 }),
      (item.discountPercent) + '%',
      '৳' + item.amount.toLocaleString('en-BD', { minimumFractionDigits: 2 }),
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
        if (data.section === 'body') {
          const { x: cx, y: cellY, width, height } = data.cell;
          data.doc.setDrawColor(230, 235, 245);
          data.doc.setLineWidth(0.2);
          data.doc.line(cx, cellY + height, cx + width, cellY + height);
        }
      },
    });

    y = (doc as any).lastAutoTable.finalY + 8;

    // ===================== SUMMARY =====================
    const summaryW = 75;
    const summaryX = pageW - margin - summaryW;

    const summaryLines = 2 + (discountPercent > 0 ? 1 : 0) + (taxRate > 0 ? 1 : 0) + (totalPaid > 0 ? 2 : 0);
    const summaryH = summaryLines * 7 + 16;

    doc.setFillColor(248, 250, 252);
    doc.roundedRect(summaryX - 5, y, summaryW + 5, summaryH, 3, 3, 'F');
    doc.setDrawColor(226, 232, 240);
    doc.setLineWidth(0.3);
    doc.roundedRect(summaryX - 5, y, summaryW + 5, summaryH, 3, 3, 'S');

    let sy = y + 8;
    const labelX = summaryX;
    const valX = summaryX + summaryW;

    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(100, 116, 139);
    doc.text('Subtotal', labelX, sy);
    doc.setTextColor(51, 65, 85);
    doc.text('৳' + subtotal.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
    sy += 7;

    if (discountPercent > 0) {
      doc.setTextColor(100, 116, 139);
      doc.text('Discount (' + discountPercent + '%)', labelX, sy);
      doc.setTextColor(220, 38, 38);
      doc.text('- ৳' + discountAmt.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
      sy += 7;
    }

    if (taxRate > 0) {
      doc.setTextColor(100, 116, 139);
      doc.text('Tax (' + taxRate + '%)', labelX, sy);
      doc.setTextColor(51, 65, 85);
      doc.text('৳' + taxAmt.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
      sy += 7;
    }

    sy += 2;
    doc.setDrawColor(13, 110, 253);
    doc.setLineWidth(0.8);
    doc.line(labelX, sy, valX, sy);
    sy += 8;

    doc.setFontSize(13);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(10, 14, 39);
    doc.text('TOTAL', labelX, sy);
    doc.setTextColor(13, 110, 253);
    doc.text('৳' + total.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });

    if (totalPaid > 0) {
      sy += 10;
      doc.setFontSize(9);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(100, 116, 139);
      doc.text('Paid', labelX, sy);
      doc.setTextColor(22, 163, 74);
      doc.text('৳' + totalPaid.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
      sy += 7;

      doc.setTextColor(100, 116, 139);
      doc.setFont('helvetica', 'bold');
      doc.text('Due', labelX, sy);
      const dueColor = dueAmount > 0 ? [220, 38, 38] : [22, 163, 74];
      doc.setTextColor(dueColor[0], dueColor[1], dueColor[2]);
      doc.text('৳' + dueAmount.toLocaleString('en-BD', { minimumFractionDigits: 2 }), valX, sy, { align: 'right' });
    }

    // ===================== NOTES =====================
    if (billForm.notes) {
      const noteY = Math.max(y + summaryH + 6, sy + 10);
      doc.setFillColor(255, 251, 235);
      doc.roundedRect(margin, noteY, contentW, 16, 3, 3, 'F');
      doc.setDrawColor(253, 230, 138);
      doc.setLineWidth(0.3);
      doc.roundedRect(margin, noteY, contentW, 16, 3, 3, 'S');
      doc.setFontSize(8);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(180, 83, 9);
      doc.text('NOTES:', margin + 4, noteY + 5);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(146, 64, 14);
      const notesLines = doc.splitTextToSize(billForm.notes, contentW - 12);
      doc.text(notesLines.slice(0, 2), margin + 4, noteY + 11);
    }

    // ===================== FOOTER =====================
    const footerY = pageH - 20;
    doc.setDrawColor(226, 232, 240);
    doc.setLineWidth(0.3);
    doc.line(margin, footerY, pageW - margin, footerY);

    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(13, 110, 253);
    doc.text('Thank you for choosing Elite Care Hospital!', margin, footerY + 6);

    doc.setFontSize(7);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(148, 163, 184);
    doc.text('This is a computer-generated invoice. For queries, please contact billing@elitecarehospital.com', margin, footerY + 11);
    doc.text('© ' + now.getFullYear() + ' Elite Care Hospital Management System. All rights reserved.', margin, footerY + 15);

    const statusLabel = payStatus === 'PAID' ? 'PAID' : payStatus === 'PARTIAL' ? 'PARTIAL' : invStatus;
    const statusColor = payStatus === 'PAID' ? [22, 163, 74] : payStatus === 'PARTIAL' ? [255, 193, 7] : [108, 117, 125];
    const statusBg = payStatus === 'PAID' ? [240, 253, 244] : payStatus === 'PARTIAL' ? [255, 251, 235] : [248, 250, 252];

    doc.setFillColor(statusBg[0], statusBg[1], statusBg[2]);
    doc.roundedRect(pageW - margin - 30, footerY + 2, 30, 8, 2, 2, 'F');
    doc.setFontSize(7);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(statusColor[0], statusColor[1], statusColor[2]);
    doc.text(statusLabel, pageW - margin - 15, footerY + 7, { align: 'center' });

    doc.save('Invoice-' + invNumber + '.pdf');
  }
}
