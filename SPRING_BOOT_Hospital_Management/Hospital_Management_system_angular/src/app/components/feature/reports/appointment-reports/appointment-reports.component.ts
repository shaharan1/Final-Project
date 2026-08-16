import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import { AppointmentAnalytics } from '../../../../models/reports/analytics.model';
import { toLabelValue } from '../../../../models/reports/chart.util';

Chart.register(...registerables);

@Component({
  selector: 'app-appointment-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './appointment-reports.component.html',
  styleUrls: ['./appointment-reports.component.css']
})
export class AppointmentReportsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('dailyTrendChart') dailyTrendChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('departmentChart') departmentChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('statusChart') statusChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('peakHoursChart') peakHoursChartRef!: ElementRef<HTMLCanvasElement>;

  private analyticsService = inject(AnalyticsService);
  private router = inject(Router);

  data: AppointmentAnalytics | null = null;
  loading = true;
  error = '';

  private charts: Chart[] = [];
  get dailyTrendArray(): { key: string; value: number }[] {
    const d = this.data?.dailyTrend as any;
    if (!d) return [];
    return Object.entries(d).map(([key, value]) => ({ key, value: Number(value ?? 0) }));
  }


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
    this.analyticsService.getAppointmentAnalytics().subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
        setTimeout(() => this.initCharts(), 100);
      },
      error: () => {
        this.error = 'Failed to load appointment analytics';
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

  private initCharts(): void {
    if (!this.data) return;
    this.initDailyTrendChart();
    this.initDepartmentChart();
    this.initStatusChart();
    this.initPeakHoursChart();
  }

  private initDailyTrendChart(): void {
    if (!this.dailyTrendChartRef || !this.data) return;
    const { labels, values } = toLabelValue(this.data.dailyTrend, 'date', 'count');
    const chart = new Chart(this.dailyTrendChartRef.nativeElement, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Appointments',
          data: values,
          borderColor: '#0d6efd',
          backgroundColor: 'rgba(13, 110, 253, 0.1)',
          fill: true,
          tension: 0.4,
          pointBackgroundColor: '#0d6efd',
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

  private initDepartmentChart(): void {
    if (!this.departmentChartRef || !this.data) return;
    const { labels, values } = toLabelValue(this.data.departmentWiseDistribution, 'department', 'count');
    const chart = new Chart(this.departmentChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Appointments',
          data: values,
          backgroundColor: 'rgba(102, 16, 242, 0.7)',
          borderColor: '#6610f2',
          borderWidth: 1,
          borderRadius: 6
        }]
      },
      options: {
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

  private initStatusChart(): void {
    if (!this.statusChartRef || !this.data) return;
    const { labels, values } = toLabelValue(this.data.statusDistribution, 'status', 'count');
    const chart = new Chart(this.statusChartRef.nativeElement, {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          data: values,
          backgroundColor: ['rgba(25, 135, 84, 0.7)', 'rgba(220, 53, 69, 0.7)', 'rgba(255, 193, 7, 0.7)', 'rgba(13, 110, 253, 0.7)'],
          borderColor: '#0a0e27',
          borderWidth: 2
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

  private initPeakHoursChart(): void {
    if (!this.peakHoursChartRef || !this.data) return;
    const { labels, values } = toLabelValue(this.data.peakHours, 'hour', 'count');
    const chart = new Chart(this.peakHoursChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Appointments',
          data: values,
          backgroundColor: 'rgba(253, 126, 20, 0.7)',
          borderColor: '#fd7e14',
          borderWidth: 1,
          borderRadius: 4
        }]
      },
      options: {
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
