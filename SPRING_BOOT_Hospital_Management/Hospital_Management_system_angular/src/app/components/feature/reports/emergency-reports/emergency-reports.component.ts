import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import { EmergencyAnalytics } from '../../../../models/reports/analytics.model';

Chart.register(...registerables);

@Component({
  selector: 'app-emergency-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './emergency-reports.component.html',
  styleUrls: ['./emergency-reports.component.css']
})
export class EmergencyReportsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('severityChart') severityChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('statusChart') statusChartRef!: ElementRef<HTMLCanvasElement>;

  private analyticsService = inject(AnalyticsService);
  private router = inject(Router);

  data: EmergencyAnalytics | null = null;
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
    this.analyticsService.getEmergencyAnalytics().subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
        setTimeout(() => this.initCharts(), 100);
      },
      error: () => {
        this.error = 'Failed to load emergency analytics';
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
    this.initSeverityChart();
    this.initStatusChart();
  }

  private initSeverityChart(): void {
    if (!this.severityChartRef || !this.data) return;
    const labels = this.data.severityDistribution.map(s => s.level);
    const values = this.data.severityDistribution.map(s => s.count);
    const chart = new Chart(this.severityChartRef.nativeElement, {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          data: values,
          backgroundColor: [
            'rgba(220, 53, 69, 0.7)', 'rgba(253, 126, 20, 0.7)',
            'rgba(255, 193, 7, 0.7)', 'rgba(25, 135, 84, 0.7)',
            'rgba(13, 110, 253, 0.7)'
          ],
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

  private initStatusChart(): void {
    if (!this.statusChartRef || !this.data) return;
    const labels = this.data.statusDistribution.map(s => s.status);
    const values = this.data.statusDistribution.map(s => s.count);
    const chart = new Chart(this.statusChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Cases',
          data: values,
          backgroundColor: 'rgba(220, 53, 69, 0.7)',
          borderColor: '#dc3545',
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
}
