import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

@Injectable({ providedIn: 'root' })
export class ExportService {

  async exportToPdf(elementId: string, filename: string = 'report'): Promise<void> {
    const element = document.getElementById(elementId);
    if (!element) return;

    // Temporarily apply print-friendly styles
    element.classList.add('exporting');

    try {
      const canvas = await html2canvas(element, {
        scale: 2,
        useCORS: true,
        backgroundColor: '#0a0e27',
        logging: false,
        windowWidth: 1200
      });

      const imgData = canvas.toDataURL('image/png');
      const imgWidth = 210; // A4 width in mm
      const imgHeight = (canvas.height * imgWidth) / canvas.width;

      const pdf = new jsPDF('p', 'mm', 'a4');

      // Add header
      pdf.setFontSize(16);
      pdf.setFont('helvetica', 'bold');
      pdf.text('Elite Care Hospital - ' + filename, 105, 15, { align: 'center' });
      pdf.setFontSize(10);
      pdf.setFont('helvetica', 'normal');
      pdf.text('Generated: ' + new Date().toLocaleString(), 105, 22, { align: 'center' });
      pdf.setDrawColor(13, 110, 253);
      pdf.setLineWidth(0.5);
      pdf.line(20, 25, 190, 25);

      // Add image
      const yOffset = 30;
      let position = yOffset;

      if (imgHeight > (297 - yOffset - 10)) {
        // Multi-page
        let remainingHeight = imgHeight;
        while (remainingHeight > 0) {
          pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
          remainingHeight -= (297 - yOffset - 10);
          if (remainingHeight > 0) {
            pdf.addPage();
            position = yOffset - (297 - yOffset - 10) * ((imgHeight - remainingHeight) / imgHeight);
          }
        }
      } else {
        pdf.addImage(imgData, 'PNG', 0, yOffset, imgWidth, imgHeight);
      }

      // Footer
      const pageCount = pdf.getNumberOfPages();
      for (let i = 1; i <= pageCount; i++) {
        pdf.setPage(i);
        pdf.setFontSize(8);
        pdf.setTextColor(128);
        pdf.text(`Page ${i} of ${pageCount}`, 105, 290, { align: 'center' });
        pdf.text('Elite Care Hospital - Confidential', 105, 294, { align: 'center' });
      }

      pdf.save(`${filename}_${new Date().toISOString().slice(0, 10)}.pdf`);
    } finally {
      element.classList.remove('exporting');
    }
  }

  exportToExcel(headers: string[], data: any[][], filename: string = 'report'): void {
    const wsData = [headers, ...data];
    const ws = XLSX.utils.aoa_to_sheet(wsData);

    // Auto-width columns
    const colWidths = headers.map((h, i) => {
      const maxLen = Math.max(h.length, ...data.map(row => String(row[i] || '').length));
      return { wch: Math.min(maxLen + 2, 40) };
    });
    ws['!cols'] = colWidths;

    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Report');

    const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });
    const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    saveAs(blob, `${filename}_${new Date().toISOString().slice(0, 10)}.xlsx`);
  }

  exportToCsv(headers: string[], data: any[][], filename: string = 'report'): void {
    const csvContent = [
      headers.join(','),
      ...data.map(row => row.map(cell => {
        const val = String(cell ?? '');
        return val.includes(',') ? `"${val}"` : val;
      }).join(','))
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    saveAs(blob, `${filename}_${new Date().toISOString().slice(0, 10)}.csv`);
  }

  printReport(elementId: string): void {
    const element = document.getElementById(elementId);
    if (!element) return;

    const printWindow = window.open('', '_blank');
    if (!printWindow) return;

    printWindow.document.write(`
      <html>
      <head>
        <title>Elite Care Hospital - Report</title>
        <style>
          body { font-family: 'Inter', Arial, sans-serif; background: #fff; color: #333; padding: 20px; }
          table { width: 100%; border-collapse: collapse; margin: 16px 0; }
          th, td { border: 1px solid #ddd; padding: 8px 12px; text-align: left; font-size: 12px; }
          th { background: #0d6efd; color: white; }
          tr:nth-child(even) { background: #f5f5f5; }
          h1 { color: #0d6efd; font-size: 18px; }
          h2 { color: #333; font-size: 14px; margin-top: 24px; }
          .kpi-row { display: flex; gap: 16px; flex-wrap: wrap; margin: 12px 0; }
          .kpi-item { border: 1px solid #ddd; border-radius: 8px; padding: 12px 16px; min-width: 120px; }
          .kpi-value { font-size: 20px; font-weight: bold; color: #0d6efd; }
          .kpi-label { font-size: 11px; color: #666; }
          @media print { body { padding: 0; } }
        </style>
      </head>
      <body>
        <h1>Elite Care Hospital</h1>
        <p style="color: #666; font-size: 11px;">Generated: ${new Date().toLocaleString()}</p>
        ${element.innerHTML}
        <script>window.onload=function(){window.print();}<\/script>
      </body>
      </html>
    `);
    printWindow.document.close();
  }
}
