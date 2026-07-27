import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

@Injectable({ providedIn: 'root' })
export class InvoiceGeneratorService {
  generatePdf(billForm: any, billItems: any[], discountPercent: number, taxRate: number): void {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    const pageW = doc.internal.pageSize.getWidth();
    const margin = 15;
    const contentW = pageW - margin * 2;
    let y = 20;

    const headerBg = '#0d6efd';
    doc.setFillColor(13, 110, 253);
    doc.rect(0, 0, pageW, 32, 'F');
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(22);
    doc.setFont('helvetica', 'bold');
    doc.text('Elite Care Hospital', margin, 13);
    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.text('123 Medical Center Drive  •  Dhaka, Bangladesh  •  +880-2-XXXX-XXXX', margin, 22);
    doc.text('invoice@elitecare.com  •  www.elitecare.com', margin, 28);

    doc.setTextColor(26, 26, 46);
    doc.setFontSize(16);
    doc.setFont('helvetica', 'bold');
    doc.text('INVOICE', pageW - margin - 40, 14);
    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');

    const invInfo = [
      ['Bill #:', billForm.billNumber || 'N/A'],
      ['Date:', new Date().toLocaleDateString('en-BD')],
      ['Status:', 'Generated'],
    ];
    invInfo.forEach(([label, val]) => {
      doc.text(label, pageW - margin - 40, y += 6);
      doc.text(String(val), pageW - margin, y - 6);
    });

    y = 38;
    doc.setDrawColor(200);
    doc.setLineWidth(0.5);
    doc.line(margin, y, pageW - margin, y);
    y += 6;

    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.text('Bill To:', margin, y);
    y += 6;
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    const patientInfo = [
      ['Patient:', billForm.patientName || 'N/A'],
      ['Phone:', billForm.phone || 'N/A'],
      ['Address:', billForm.address || 'N/A'],
      ['Age:', billForm.age || '-'],
      ['Gender:', billForm.gender || 'N/A'],
    ];
    patientInfo.forEach(([label, val]) => {
      doc.setFont('helvetica', 'bold');
      doc.text(label + ' ', margin, y);
      doc.setFont('helvetica', 'normal');
      doc.text(String(val), margin + 30, y);
      y += 5;
    });

    y += 3;
    const tableData = billItems.map(item => [
      String(item.category || ''),
      String(item.description || ''),
      String(item.qty || 0),
      '৳' + Number(item.unitPrice || 0).toFixed(2),
      String(item.discount || 0) + '%',
      '৳' + Number(item.amount || 0).toFixed(2),
    ]);

    autoTable(doc, {
      startY: y,
      head: [['Category', 'Description', 'Qty', 'Unit Price', 'Disc %', 'Amount']],
      body: tableData,
      margin: { left: margin, right: margin },
      headStyles: { fillColor: [13, 110, 253], textColor: [255, 255, 255], fontSize: 8, fontStyle: 'bold' },
      bodyStyles: { fontSize: 8 },
      alternateRowStyles: { fillColor: [240, 245, 255] },
      columnStyles: {
        0: { cellWidth: 22 },
        1: { cellWidth: 55 },
        2: { cellWidth: 12, halign: 'center' },
        3: { cellWidth: 22, halign: 'right' },
        4: { cellWidth: 14, halign: 'center' },
        5: { cellWidth: 25, halign: 'right' },
      },
      theme: 'grid',
      didParseCell: (data: any) => {
        if (data.column.index === 5) {
          data.cell.styles.halign = 'right';
        }
      },
    });

    y = (doc as any).lastAutoTable.finalY + 6;

    const subtotal = billItems.reduce((s: number, i: any) => s + (i.amount || 0), 0);
    const discountAmt = subtotal * ((discountPercent || 0) / 100);
    const taxAmt = (subtotal - discountAmt) * (taxRate || 0.18);
    const total = subtotal - discountAmt + taxAmt;

    doc.setFontSize(9);
    const summaryLines = [
      ['Subtotal:', '৳' + subtotal.toFixed(2)],
      ['Discount (' + (discountPercent || 0) + '%):', '- ৳' + discountAmt.toFixed(2)],
      ['Tax (' + ((taxRate || 0.18) * 100).toFixed(0) + '%):', '৳' + taxAmt.toFixed(2)],
    ];
    summaryLines.forEach(([label, val]) => {
      doc.setFont('helvetica', 'normal');
      doc.text(label, pageW - margin - 35, y);
      doc.text(val, pageW - margin, y);
      y += 5;
    });

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(11);
    doc.setFillColor(230, 240, 255);
    doc.rect(margin, y - 1, contentW, 7, 'F');
    doc.text('Total:', pageW - margin - 35, y + 3);
    doc.setTextColor(13, 110, 253);
    doc.text('৳' + total.toFixed(2), pageW - margin, y + 3);
    doc.setTextColor(26, 26, 46);

    if (billForm.notes) {
      y += 12;
      doc.setFontSize(8);
      doc.setFont('helvetica', 'bold');
      doc.text('Notes:', margin, y);
      doc.setFont('helvetica', 'normal');
      doc.setFontSize(7);
      const notesLines = doc.splitTextToSize(billForm.notes, contentW - 50);
      doc.text(notesLines, margin + 50, y);
    }

    y = doc.internal.pageSize.getHeight() - 15;
    doc.setDrawColor(200);
    doc.line(margin, y, pageW - margin, y);
    doc.setFontSize(7);
    doc.setTextColor(128);
    doc.setFont('helvetica', 'normal');
    doc.text('Thank you for choosing Elite Care Hospital!', margin, y + 5);
    doc.text('This is a computer-generated invoice.', pageW - margin - 40, y + 5);
    doc.text('© ' + new Date().getFullYear() + ' Elite Care Hospital. All rights reserved.', margin, y + 9);

    doc.save(`Invoice-${billForm.billNumber || 'bill'}.pdf`);
  }
}