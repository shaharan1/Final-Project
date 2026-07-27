import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import { PatientAnalytics } from '../../../../models/reports/analytics.model';

Chart.register(...registerables);

@Component({
  selector: 'app-patient-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient-reports.component.html',
  styleUrls: ['./patient-reports.component.css']
})
export class PatientReportsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('bloodGroupChart') bloodGroupChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('ageGroupChart') ageGroupChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('monthlyTrendChart') monthlyTrendChartRef!: ElementRef<HTMLCanvasElement>;

  private analyticsService = inject(AnalyticsService);
  private router = inject(Router);

  data: PatientAnalytics | null = null;
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
    this.analyticsService.getPatientAnalytics().subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
        setTimeout(() => this.initCharts(), 100);
      },
      error: () => {
        this.error = 'Failed to load patient analytics';
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
    this.initBloodGroupChart();
    this.initAgeGroupChart();
    this.initMonthlyTrendChart();
  }

  private initBloodGroupChart(): void {
    if (!this.bloodGroupChartRef || !this.data) return;
    const labels = Object.keys(this.data.bloodGroupDistribution);
    const values = Object.values(this.data.bloodGroupDistribution);
    const chart = new Chart(this.bloodGroupChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Patients',
          data: values,
          backgroundColor: [
            'rgba(220, 53, 69, 0.7)', 'rgba(13, 110, 253, 0.7)',
            'rgba(25, 135, 84, 0.7)', 'rgba(255, 193, 7, 0.7)',
            'rgba(102, 16, 242, 0.7)', 'rgba(253, 126, 20, 0.7)',
            'rgba(13, 202, 240, 0.7)', 'rgba(111, 66, 193, 0.7)'
          ],
          borderColor: [
            '#dc3545', '#0d6efd', '#198754', '#ffc107',
            '#6610f2', '#fd7e14', '#0dcaf0', '#6f42c1'
          ],
          borderWidth: 1,
          borderRadius: 6
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          title: { display: false }
        },
        scales: {
          x: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } },
          y: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } }
        }
      }
    });
    this.charts.push(chart);
  }

  private initAgeGroupChart(): void {
    if (!this.ageGroupChartRef || !this.data) return;
    const labels = Object.keys(this.data.ageGroupDistribution);
    const values = Object.values(this.data.ageGroupDistribution);
    const chart = new Chart(this.ageGroupChartRef.nativeElement, {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          data: values,
          backgroundColor: [
            'rgba(13, 110, 253, 0.7)', 'rgba(102, 16, 242, 0.7)',
            'rgba(25, 135, 84, 0.7)', 'rgba(255, 193, 7, 0.7)',
            'rgba(220, 53, 69, 0.7)', 'rgba(13, 202, 240, 0.7)'
          ],
          borderColor: '#0a0e27',
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom', labels: { color: 'rgba(255,255,255,0.6)', padding: 16 } }
        }
      }
    });
    this.charts.push(chart);
  }

  private initMonthlyTrendChart(): void {
    if (!this.monthlyTrendChartRef || !this.data) return;
    const labels = this.data.monthlyRegistrationTrend.map(m => m.month);
    const values = this.data.monthlyRegistrationTrend.map(m => m.count);
    const chart = new Chart(this.monthlyTrendChartRef.nativeElement, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Registrations',
          data: values,
          borderColor: '#0d6efd',
          backgroundColor: 'rgba(13, 110, 253, 0.1)',
          fill: true,
          tension: 0.4,
          pointBackgroundColor: '#0d6efd',
          pointBorderColor: '#fff',
          pointRadius: 4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } },
          y: { ticks: { color: 'rgba(255,255,255,0.5)' }, grid: { color: 'rgba(255,255,255,0.05)' } }
        }
      }
    });
    this.charts.push(chart);
  }
}
