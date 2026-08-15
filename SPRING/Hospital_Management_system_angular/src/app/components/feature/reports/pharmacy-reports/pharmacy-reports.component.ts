import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import { PharmacyAnalytics } from '../../../../models/reports/analytics.model';

Chart.register(...registerables);

@Component({
  selector: 'app-pharmacy-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './pharmacy-reports.component.html',
  styleUrls: ['./pharmacy-reports.component.css']
})
export class PharmacyReportsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('salesTrendChart') salesTrendChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('topMedicinesChart') topMedicinesChartRef!: ElementRef<HTMLCanvasElement>;

  private analyticsService = inject(AnalyticsService);
  private router = inject(Router);

  data: PharmacyAnalytics | null = null;
  loading = true;
  error = '';

  private charts: Chart[] = [];

  ngOnInit(): void {
    this.loadData();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {
    this.charts.forEach(c => c.destroy());
  }

  loadData(): void {
    this.loading = true;
    this.error = '';
    this.analyticsService.getPharmacyAnalytics().subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
        setTimeout(() => this.initCharts(), 100);
      },
      error: () => {
        this.error = 'Failed to load pharmacy analytics';
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/reports']);
  }

  exportPdf(): void {
    if (!this.data) return;
    const win = window.open('', '_blank');
    if (!win) {
      alert('Please allow pop-ups to export the PDF.');
      return;
    }
    win.document.write(this.buildPrintHtml());
    win.document.close();
    win.focus();
    setTimeout(() => win.print(), 400);
  }

  exportExcel(): void {
    if (!this.data) return;
    const rows = [
      '<table border="1" cellspacing="0" cellpadding="6">',
      '<tr><th colspan="2" style="background:#198754;color:#fff;">Pharmacy Report</th></tr>',
      this.statRow('Total Sales', this.fmt(this.data.totalSales)),
      this.statRow('Daily Sales', this.fmt(this.data.dailySales)),
      this.statRow('Monthly Sales', this.fmt(this.data.monthlySales)),
      this.statRow('Low Stock Medicines', String(this.data.lowStockMedicines)),
      this.statRow('Expired Medicines', String(this.data.expiredMedicines)),
      '<tr><th colspan="2" style="background:#198754;color:#fff;">Top Selling Medicines</th></tr>',
      '<tr><th>Medicine</th><th>Quantity Sold</th></tr>',
      ...this.data.topSellingMedicines.map(m =>
        `<tr><td>${this.esc(m.medicineName)}</td><td>${m.totalQuantitySold}</td></tr>`),
      '</table>'
    ].join('');
    this.downloadFile(rows, 'pharmacy-report.xls', 'application/vnd.ms-excel');
  }

  exportCsv(): void {
    if (!this.data) return;
    const rows: (string | number)[][] = [
      ['Pharmacy Report'],
      ['Metric', 'Value'],
      ['Total Sales', this.fmt(this.data.totalSales)],
      ['Daily Sales', this.fmt(this.data.dailySales)],
      ['Monthly Sales', this.fmt(this.data.monthlySales)],
      ['Low Stock Medicines', this.data.lowStockMedicines],
      ['Expired Medicines', this.data.expiredMedicines],
      [],
      ['Top Selling Medicines', 'Quantity Sold'],
      ...this.data.topSellingMedicines.map(m => [m.medicineName, m.totalQuantitySold])
    ];
    const csv = rows
      .map(r => r.map(c => `"${String(c).replace(/"/g, '""')}"`).join(','))
      .join('\r\n');
    this.downloadFile(csv, 'pharmacy-report.csv', 'text/csv;charset=utf-8;');
  }

  printReport(): void { window.print(); }

  private initCharts(): void {
    if (!this.data) return;
    this.initSalesTrendChart();
    this.initTopMedicinesChart();
  }

  private initSalesTrendChart(): void {
    if (!this.salesTrendChartRef || !this.data) return;
    const entries = Object.entries(this.data.salesTrend || {});
    const labels = entries.map(([date]) => date);
    const values = entries.map(([, amount]) => amount);
    const chart = new Chart(this.salesTrendChartRef.nativeElement, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Sales',
          data: values,
          borderColor: '#198754',
          backgroundColor: 'rgba(25, 135, 84, 0.1)',
          fill: true,
          tension: 0.4,
          pointBackgroundColor: '#198754',
          pointBorderColor: '#fff',
          pointRadius: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { ticks: { color: 'rgba(255,255,255,0.5)', maxTicksLimit: 10 }, grid: { color: 'rgba(255,255,255,0.05)' } },
          y: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } }
        }
      }
    });
    this.charts.push(chart);
  }

  private initTopMedicinesChart(): void {
    if (!this.topMedicinesChartRef || !this.data) return;
    const meds = this.data.topSellingMedicines.slice(0, 10);
    const labels = meds.map(m => m.medicineName);
    const values = meds.map(m => m.totalQuantitySold);
    const chart = new Chart(this.topMedicinesChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Quantity Sold',
          data: values,
          backgroundColor: 'rgba(25, 135, 84, 0.7)',
          borderColor: '#198754',
          borderWidth: 1,
          borderRadius: 6
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } },
          y: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } }
        }
      }
    });
    this.charts.push(chart);
  }
}
