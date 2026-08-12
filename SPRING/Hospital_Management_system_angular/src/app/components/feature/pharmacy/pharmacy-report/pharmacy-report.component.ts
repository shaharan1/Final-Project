import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { jsPDF } from 'jspdf';
import { PharmacyReportService } from '../../../../services/pharmacy-report.service';

@Component({
  selector: 'app-pharmacy-report',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './pharmacy-report.component.html',
  styleUrls: ['./pharmacy-report.component.css']
})
export class PharmacyReportComponent implements OnInit {

  activeTab = 'daily';

  dailyDate = '';
  monthlyYear = '';
  monthlyMonth = '';
  purchaseStartDate = '';
  purchaseEndDate = '';

  dailySalesResult: any = null;
  monthlySalesResult: any = null;
  purchaseReportResult: any = null;
  stockReportResult: any = null;
  supplierReportResult: any = null;

  loading = false;

  tabs = [
    { key: 'daily', label: 'Daily Sales' },
    { key: 'monthly', label: 'Monthly Sales' },
    { key: 'purchase', label: 'Purchase' },
    { key: 'stock', label: 'Stock' },
    { key: 'supplier', label: 'Supplier' }
  ];

  constructor(private pharmacyReportService: PharmacyReportService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const now = new Date();
    this.monthlyYear = now.getFullYear().toString();
    this.monthlyMonth = (now.getMonth() + 1).toString().padStart(2, '0');
    this.dailyDate = now.toISOString().split('T')[0];
    this.purchaseStartDate = new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0];
    this.purchaseEndDate = now.toISOString().split('T')[0];
    this.generateDailySales();
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    if (tab === 'daily') this.generateDailySales();
    else if (tab === 'monthly') this.generateMonthlySales();
    else if (tab === 'purchase') this.generatePurchaseReport();
    else if (tab === 'stock') this.generateStockReport();
    else if (tab === 'supplier') this.generateSupplierReport();
  }

  generateDailySales(): void {
    this.loading = true;
    this.pharmacyReportService.getDailySales(this.dailyDate).subscribe({
      next: (data: any) => { this.dailySalesResult = data; this.loading = false; this.cdr.markForCheck(); },
      error: () => { this.dailySalesResult = { totalSales: 0, totalCount: 0, totalRevenue: 0 }; this.loading = false; this.cdr.markForCheck(); }
    });
  }

  generateMonthlySales(): void {
    this.loading = true;
    this.pharmacyReportService.getMonthlySales(this.monthlyYear, this.monthlyMonth).subscribe({
      next: (data: any) => { this.monthlySalesResult = data; this.loading = false; this.cdr.markForCheck(); },
      error: () => { this.monthlySalesResult = { breakdown: [] }; this.loading = false; this.cdr.markForCheck(); }
    });
  }

  generatePurchaseReport(): void {
    this.loading = true;
    this.pharmacyReportService.getPurchaseReport(this.purchaseStartDate, this.purchaseEndDate).subscribe({
      next: (data: any) => { this.purchaseReportResult = data; this.loading = false; this.cdr.markForCheck(); },
      error: () => { this.purchaseReportResult = { totalPurchases: 0, totalAmount: 0 }; this.loading = false; this.cdr.markForCheck(); }
    });
  }

  generateStockReport(): void {
    this.loading = true;
    this.pharmacyReportService.getStockReport().subscribe({
      next: (data: any) => { this.stockReportResult = data; this.loading = false; this.cdr.markForCheck(); },
      error: () => { this.stockReportResult = { totalMedicines: 0, lowStock: 0, expired: 0 }; this.loading = false; this.cdr.markForCheck(); }
    });
  }

  generateSupplierReport(): void {
    this.loading = true;
    this.pharmacyReportService.getSupplierReport().subscribe({
      next: (data: any) => { this.supplierReportResult = data; this.loading = false; this.cdr.markForCheck(); },
      error: () => { this.supplierReportResult = { suppliers: [] }; this.loading = false; this.cdr.markForCheck(); }
    });
  }

  generatePDF(): void {
    const doc = new jsPDF('p', 'mm', 'a4');
    const PAGE_W = 210, M = 15, USABLE = PAGE_W - M * 2;
    const BLUE: [number, number, number] = [25, 118, 210];
    const money = (n: any) =>
      'BDT ' + (Number(n) || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

    let y = 0;

    const header = (title: string) => {
      doc.setFont('helvetica', 'bold'); doc.setFontSize(20); doc.setTextColor(BLUE[0], BLUE[1], BLUE[2]);
      doc.text('ELITE CARE HOSPITAL', M, 20);
      doc.setFont('helvetica', 'bold'); doc.setFontSize(15); doc.setTextColor(60);
      doc.text(title, PAGE_W - M, 20, { align: 'right' });
      doc.setFont('helvetica', 'normal'); doc.setFontSize(10); doc.setTextColor(90);
      doc.text('House #25, Road #12, Dhanmondi, Dhaka-1209', M, 27);
      doc.text('Phone : +880 1711-123456  |  Email : info@elitecarehospital.com', M, 32);
      doc.setFillColor(BLUE[0], BLUE[1], BLUE[2]); doc.rect(M, 36, USABLE, 1.2, 'F');
      y = 46;
    };

    const banner = (text: string) => {
      doc.setFillColor(BLUE[0], BLUE[1], BLUE[2]); doc.rect(M, y, USABLE, 7, 'F');
      doc.setFont('helvetica', 'bold'); doc.setFontSize(11); doc.setTextColor(255, 255, 255);
      doc.text(text, M + 3, y + 5);
      y += 11;
    };

    const drawTable = (
      headers: string[], widths: number[], rows: string[][],
      aligns: ('left' | 'right' | 'center')[] = []
    ) => {
      const rowH = 7;
      const top = y;
      const xs: number[] = [];
      let cx = M;
      for (const w of widths) { xs.push(cx); cx += w; }
      const totalW = widths.reduce((a, b) => a + b, 0);
      doc.setFillColor(BLUE[0], BLUE[1], BLUE[2]); doc.rect(M, top, totalW, rowH, 'F');
      doc.setFont('helvetica', 'bold'); doc.setFontSize(10); doc.setTextColor(255, 255, 255);
      for (let i = 0; i < headers.length; i++) {
        const a = aligns[i] || 'left';
        const tx = xs[i] + (a === 'right' ? widths[i] - 3 : a === 'center' ? widths[i] / 2 : 3);
        doc.text(headers[i], tx, top + 5, { align: a });
      }
      y += rowH;
      doc.setFont('helvetica', 'normal'); doc.setFontSize(10); doc.setTextColor(40);
      for (const row of rows) {
        for (let i = 0; i < row.length; i++) {
          const a = aligns[i] || 'left';
          const tx = xs[i] + (a === 'right' ? widths[i] - 3 : a === 'center' ? widths[i] / 2 : 3);
          doc.text(String(row[i] ?? ''), tx, y + 5, { align: a });
        }
        y += rowH;
      }
      doc.setFillColor(200, 200, 200);
      doc.rect(M, top, totalW, 0.2, 'F');
      doc.rect(M, y - 0.2, totalW, 0.2, 'F');
      doc.rect(M, top, 0.2, y - top, 'F');
      doc.rect(M + totalW - 0.2, top, 0.2, y - top, 'F');
      for (let i = 1; i < widths.length; i++) doc.rect(xs[i], top, 0.2, y - top, 'F');
      for (let r = 1; r <= rows.length; r++) doc.rect(M, top + r * rowH, totalW, 0.2, 'F');
      y += 6;
    };

    const footer = () => {
      const fy = 283;
      doc.setFont('helvetica', 'normal'); doc.setFontSize(10); doc.setTextColor(60);
      doc.text('Thank you for choosing', M, fy);
      doc.setFont('helvetica', 'bold'); doc.setFontSize(12);
      doc.text('ELITE CARE HOSPITAL', M, fy + 7);
      doc.setFont('helvetica', 'normal'); doc.setFontSize(10);
      doc.text('Get Well Soon', M, fy + 14);
      doc.setFont('helvetica', 'bold'); doc.setFontSize(13);
      doc.text('Authorized Signature', PAGE_W - M, fy + 7, { align: 'right' });
      doc.setFont('helvetica', 'normal'); doc.setFontSize(10);
      doc.text('Pharmacy / Store', PAGE_W - M, fy + 14, { align: 'right' });
      doc.setFillColor(120, 120, 120);
      doc.rect(PAGE_W - M - 55, fy + 4, 55, 0.3, 'F');
      doc.setFontSize(9); doc.setTextColor(120);
      doc.text('Powered By Elite IT Institute', PAGE_W / 2, 292, { align: 'center' });
    };

    header('PHARMACY REPORT');

    if (this.activeTab === 'daily' && this.dailySalesResult) {
      const d = this.dailySalesResult;
      banner('DAILY SALES REPORT');
      drawTable(['Particulars', 'Value'], [110, 70], [
        ['Report Date', d.date ?? this.dailyDate],
        ['Total Transactions', String(d.totalCount ?? d.totalTransactions ?? 0)],
        ['Total Revenue', money(d.totalRevenue ?? d.totalSales)]
      ], ['left', 'right']);
    } else if (this.activeTab === 'monthly' && this.monthlySalesResult) {
      const d = this.monthlySalesResult;
      const period = d.year + '/' + String(d.month).padStart(2, '0');
      banner('MONTHLY SALES REPORT');
      drawTable(['Particulars', 'Value'], [110, 70], [
        ['Period', period],
        ['Total Transactions', String(d.totalCount ?? d.totalTransactions ?? 0)],
        ['Total Revenue', money(d.totalRevenue ?? d.totalSales)]
      ], ['left', 'right']);
      banner('DAILY BREAKDOWN');
      drawTable(['Date', 'Sales', 'Revenue'], [90, 45, 45],
        (d.breakdown || []).map((b: any) => [b.date, String(b.sales), money(b.revenue)]),
        ['left', 'right', 'right']);
    } else if (this.activeTab === 'purchase' && this.purchaseReportResult) {
      const d = this.purchaseReportResult;
      banner('PURCHASE REPORT');
      drawTable(['Particulars', 'Value'], [110, 70], [
        ['Period', (d.startDate ?? this.purchaseStartDate) + ' to ' + (d.endDate ?? this.purchaseEndDate)],
        ['Total Purchases', String(d.totalPurchases ?? d.totalTransactions ?? 0)],
        ['Total Amount', money(d.totalAmount)]
      ], ['left', 'right']);
    } else if (this.activeTab === 'stock' && this.stockReportResult) {
      const d = this.stockReportResult;
      banner('STOCK REPORT');
      drawTable(['Metric', 'Value'], [120, 60], [
        ['Total Medicines', String(d.totalMedicines ?? 0)],
        ['Available Stock', String(d.totalAvailableStock ?? 0)],
        ['Low Stock', String(d.lowStock ?? 0)],
        ['Expired', String(d.expired ?? 0)],
        ['Expiring Soon', String(d.expiringSoon ?? 0)]
      ], ['left', 'right']);
    } else if (this.activeTab === 'supplier' && this.supplierReportResult) {
      const list: any[] = this.supplierReportResult || [];
      banner('SUPPLIER DUE REPORT');
      drawTable(['#', 'Supplier Name', 'Total Due', 'Status'], [15, 105, 35, 25],
        list.map((s, i) => [
          String(i + 1),
          (s.name || '').length > 38 ? (s.name as string).substring(0, 36) + '…' : (s.name || ''),
          money(s.totalDue),
          s.active ? 'Active' : 'Inactive'
        ]),
        ['center', 'left', 'right', 'center']);
    } else {
      banner('REPORT');
      doc.setFont('helvetica', 'normal'); doc.setFontSize(11); doc.setTextColor(80);
      doc.text('No report data available. Please generate a report first.', M, y + 4);
    }

    footer();
    doc.save('pharmacy-' + this.activeTab + '-report.pdf');
  }
}
