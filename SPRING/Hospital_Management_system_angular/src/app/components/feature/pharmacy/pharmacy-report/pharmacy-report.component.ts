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

  private fmt(n: any): string {
    return (Number(n) || 0).toFixed(2);
  }

  generatePDF(): void {
    const doc = new jsPDF();
    doc.setFontSize(18);
    doc.text('Elite Care - Pharmacy Report', 14, 18);
    doc.setFontSize(11);
    let y = 32;
    const add = (t: string) => { doc.text(t, 14, y); y += 8; };
    if (this.activeTab === 'daily' && this.dailySalesResult) {
      add('Daily Sales Report - ' + this.dailyDate);
      add('Total Sales: ' + (this.dailySalesResult.totalSales ?? 0));
      add('Medicines Sold: ' + (this.dailySalesResult.totalCount ?? 0));
      add('Revenue: Tk ' + this.fmt(this.dailySalesResult.totalRevenue));
    } else if (this.activeTab === 'monthly' && this.monthlySalesResult) {
      add('Monthly Sales Report - ' + this.monthlyYear + '/' + this.monthlyMonth);
      add('Total Sales: ' + (this.monthlySalesResult.totalSales ?? 0));
      add('Medicines Sold: ' + (this.monthlySalesResult.totalCount ?? 0));
      add('Revenue: Tk ' + this.fmt(this.monthlySalesResult.totalRevenue));
      (this.monthlySalesResult.breakdown || []).forEach((b: any) => add('  ' + b.date + ' - Sales: ' + b.sales + ', Revenue: Tk ' + this.fmt(b.revenue)));
    } else if (this.activeTab === 'purchase' && this.purchaseReportResult) {
      add('Purchase Report - ' + this.purchaseStartDate + ' to ' + this.purchaseEndDate);
      add('Total Purchases: ' + (this.purchaseReportResult.totalPurchases ?? 0));
      add('Total Amount: Tk ' + this.fmt(this.purchaseReportResult.totalAmount));
    } else if (this.activeTab === 'stock' && this.stockReportResult) {
      add('Stock Report');
      add('Total Medicines: ' + (this.stockReportResult.totalMedicines ?? 0));
      add('Available Stock: ' + (this.stockReportResult.totalAvailableStock ?? 0));
      add('Low Stock: ' + (this.stockReportResult.lowStock ?? 0));
      add('Expired: ' + (this.stockReportResult.expired ?? 0));
      add('Expiring Soon: ' + (this.stockReportResult.expiringSoon ?? 0));
    } else if (this.activeTab === 'supplier' && this.supplierReportResult) {
      add('Supplier Report');
      (this.supplierReportResult.suppliers || []).forEach((s: any) => add(s.name + ' - Outstanding Dues: Tk ' + this.fmt(s.totalDue)));
    } else {
      add('No report data. Please generate a report first.');
    }
    doc.save('pharmacy-report.pdf');
  }
}
