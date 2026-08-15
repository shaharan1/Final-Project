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
      '<tr><th colspan="3" style="background:#198754;color:#fff;">Top Selling Medicines</th></tr>',
      '<tr><th>Medicine</th><th>Quantity Sold</th><th>Revenue</th></tr>',
      ...this.data.topSellingMedicines.map(m =>
        `<tr><td>${this.esc(m.name)}</td><td>${m.quantity}</td><td>${this.fmt(m.revenue)}</td></tr>`),
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
      ['Top Selling Medicines', 'Quantity Sold', 'Revenue'],
      ...this.data.topSellingMedicines.map(m => [m.name, m.quantity, this.fmt(m.revenue)])
    ];
    const csv = rows
      .map(r => r.map(c => `"${String(c).replace(/"/g, '""')}"`).join(','))
      .join('\r\n');
    this.downloadFile(csv, 'pharmacy-report.csv', 'text/csv;charset=utf-8;');
  }

  printReport(): void { window.print(); }

  private buildPrintHtml(): string {
    const d = this.data!;
    const stats = `
      <div style="display:flex;flex-wrap:wrap;gap:12px;margin:16px 0;">
        ${this.printStat('Total Sales', this.fmt(d.totalSales))}
        ${this.printStat('Daily Sales', this.fmt(d.dailySales))}
        ${this.printStat('Monthly Sales', this.fmt(d.monthlySales))}
        ${this.printStat('Low Stock', String(d.lowStockMedicines))}
        ${this.printStat('Expired', String(d.expiredMedicines))}
      </div>`;
    const table = `
      <h3>Top Selling Medicines</h3>
      <table border="1" cellspacing="0" cellpadding="6" style="width:100%;border-collapse:collapse;font-size:13px;">
        <thead><tr style="background:#198754;color:#fff;"><th>Medicine</th><th>Quantity Sold</th><th>Revenue</th></tr></thead>
        <tbody>
          ${d.topSellingMedicines.map(m =>
            `<tr><td>${this.esc(m.name)}</td><td>${m.quantity}</td><td>${this.fmt(m.revenue)}</td></tr>`).join('')}
        </tbody>
      </table>`;
    return `<!DOCTYPE html><html><head><title>Pharmacy Report</title></head>
      <body style="font-family:Arial, sans-serif; color:#111; padding:24px;">
        <h1 style="color:#198754; margin:0 0 4px;">Pharmacy Report</h1>
        <p style="color:#666; margin:0 0 8px;">Generated on ${new Date().toLocaleString()}</p>
        ${stats}
        ${table}
        <hr style="margin-top:24px;">
        <p style="color:#999; font-size:12px;">Elite Care Hospital — Pharmacy Management System</p>
        <script>window.onload = function(){ window.print(); };</script>
      </body></html>`;
  }

  private printStat(label: string, value: string): string {
    return `<div style="flex:1;min-width:140px;border:1px solid #ddd;border-radius:8px;padding:10px;">
      <div style="font-size:12px;color:#666;">${label}</div>
      <div style="font-size:18px;font-weight:700;color:#198754;">${value}</div></div>`;
  }

  private statRow(label: string, value: string): string {
    return `<tr><td style="font-weight:600;">${this.esc(label)}</td><td>${this.esc(value)}</td></tr>`;
  }

  private fmt(n: number): string {
    return (n || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  private esc(s: string): string {
    return String(s).replace(/[&<>"']/g, c =>
      ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c] as string));
  }

  private downloadFile(content: string, filename: string, mime: string): void {
    const blob = new Blob(['﻿' + content], { type: mime });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  }

  private initCharts(): void {
    if (!this.data) return;
    this.initSalesTrendChart();
    this.initTopMedicinesChart();
  }

  private initSalesTrendChart(): void {
    if (!this.salesTrendChartRef || !this.data) return;
    const labels = this.data.salesTrend.map(s => s.date);
    const values = this.data.salesTrend.map(s => s.amount);
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
    const labels = meds.map(m => m.name);
    const values = meds.map(m => m.quantity);
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
