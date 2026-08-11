import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
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

  constructor(private pharmacyReportService: PharmacyReportService) {}

  ngOnInit(): void {
    const now = new Date();
    this.monthlyYear = now.getFullYear().toString();
    this.monthlyMonth = (now.getMonth() + 1).toString().padStart(2, '0');
    this.dailyDate = now.toISOString().split('T')[0];
    this.purchaseStartDate = new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0];
    this.purchaseEndDate = now.toISOString().split('T')[0];
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  generateDailySales(): void {
    this.loading = true;
    this.pharmacyReportService.getDailySales(this.dailyDate).subscribe({
      next: (data: any) => { this.dailySalesResult = data; this.loading = false; },
      error: () => { this.dailySalesResult = { totalSales: 0, totalCount: 0, totalRevenue: 0 }; this.loading = false; }
    });
  }

  generateMonthlySales(): void {
    this.loading = true;
    this.pharmacyReportService.getMonthlySales(this.monthlyYear, this.monthlyMonth).subscribe({
      next: (data: any) => { this.monthlySalesResult = data; this.loading = false; },
      error: () => { this.monthlySalesResult = { breakdown: [] }; this.loading = false; }
    });
  }

  generatePurchaseReport(): void {
    this.loading = true;
    this.pharmacyReportService.getPurchaseReport(this.purchaseStartDate, this.purchaseEndDate).subscribe({
      next: (data: any) => { this.purchaseReportResult = data; this.loading = false; },
      error: () => { this.purchaseReportResult = { totalPurchases: 0, totalAmount: 0 }; this.loading = false; }
    });
  }

  generateStockReport(): void {
    this.loading = true;
    this.pharmacyReportService.getStockReport().subscribe({
      next: (data: any) => { this.stockReportResult = data; this.loading = false; },
      error: () => { this.stockReportResult = { totalMedicines: 0, lowStock: 0, expired: 0 }; this.loading = false; }
    });
  }

  generateSupplierReport(): void {
    this.loading = true;
    this.pharmacyReportService.getSupplierReport().subscribe({
      next: (data: any) => { this.supplierReportResult = data; this.loading = false; },
      error: () => { this.supplierReportResult = { suppliers: [] }; this.loading = false; }
    });
  }

  printReport(): void {
    window.print();
  }
}
