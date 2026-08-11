import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import { BedOccupancy } from '../../../../models/reports/analytics.model';

Chart.register(...registerables);

@Component({
  selector: 'app-bed-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './bed-reports.component.html',
  styleUrls: ['./bed-reports.component.css']
})
export class BedReportsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('wardChart') wardChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('icuChart') icuChartRef!: ElementRef<HTMLCanvasElement>;

  private analyticsService = inject(AnalyticsService);
  private router = inject(Router);

  data: BedOccupancy | null = null;
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
    this.analyticsService.getBedOccupancy().subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
        setTimeout(() => this.initCharts(), 100);
      },
      error: () => {
        this.error = 'Failed to load bed occupancy data';
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/reports']);
  }

  exportPdf(): void { alert('Export coming soon'); }
  exportExcel(): void { alert('Export coming soon'); }
  exportCsv(): void { alert('Export coming soon'); }
  printReport(): void { window.print(); }

  getOccupancyColor(rate: number): string {
    if (rate >= 90) return '#dc3545';
    if (rate >= 70) return '#ffc107';
    return '#198754';
  }

  private initCharts(): void {
    if (!this.data) return;
    this.initWardChart();
    this.initIcuChart();
  }

  private initWardChart(): void {
    if (!this.wardChartRef || !this.data) return;
    const labels = this.data.wardWiseOccupancy.map(w => w.ward);
    const occupied = this.data.wardWiseOccupancy.map(w => w.occupied);
    const available = this.data.wardWiseOccupancy.map(w => w.available);
    const chart = new Chart(this.wardChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: 'Occupied',
            data: occupied,
            backgroundColor: 'rgba(220, 53, 69, 0.7)',
            borderColor: '#dc3545',
            borderWidth: 1,
            borderRadius: 4
          },
          {
            label: 'Available',
            data: available,
            backgroundColor: 'rgba(25, 135, 84, 0.7)',
            borderColor: '#198754',
            borderWidth: 1,
            borderRadius: 4
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { labels: { color: 'rgba(255,255,255,0.6)' } } },
        scales: {
          x: { stacked: true, ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } },
          y: { stacked: true, ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } }
        }
      }
    });
    this.charts.push(chart);
  }

  private initIcuChart(): void {
    if (!this.icuChartRef || !this.data) return;
    const icu = this.data.icuStats;
    const chart = new Chart(this.icuChartRef.nativeElement, {
      type: 'doughnut',
      data: {
        labels: ['Occupied', 'Available'],
        datasets: [{
          data: [icu.occupied, icu.available],
          backgroundColor: ['rgba(220, 53, 69, 0.7)', 'rgba(25, 135, 84, 0.7)'],
          borderColor: '#0a0e27',
          borderWidth: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'bottom', labels: { color: 'rgba(255,255,255,0.6)', padding: 16 } } }
      }
    });
    this.charts.push(chart);
  }
}
