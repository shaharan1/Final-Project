import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import { LabAnalytics } from '../../../../models/reports/analytics.model';
import { toLabelValue } from '../../../../models/reports/chart.util';

Chart.register(...registerables);

@Component({
  selector: 'app-lab-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lab-reports.component.html',
  styleUrls: ['./lab-reports.component.css']
})
export class LabReportsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('dailyTrendChart') dailyTrendChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('categoryChart') categoryChartRef!: ElementRef<HTMLCanvasElement>;

  private analyticsService = inject(AnalyticsService);
  private router = inject(Router);

  data: LabAnalytics | null = null;
  loading = true;
  error = '';

  private charts: Chart[] = [];
  get testCategoryDistributionArray(): { key: string; value: number }[] {
    const d = this.data?.testCategoryDistribution as any;
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
    this.analyticsService.getLabAnalytics().subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
        setTimeout(() => this.initCharts(), 100);
      },
      error: () => {
        this.error = 'Failed to load lab analytics';
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
    this.initCategoryChart();
  }

  private initDailyTrendChart(): void {
    if (!this.dailyTrendChartRef || !this.data) return;
    const { labels, values } = toLabelValue(this.data.dailyTestTrend, 'date', 'count');
    const chart = new Chart(this.dailyTrendChartRef.nativeElement, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Tests',
          data: values,
          borderColor: '#0dcaf0',
          backgroundColor: 'rgba(13, 202, 240, 0.1)',
          fill: true,
          tension: 0.4,
          pointBackgroundColor: '#0dcaf0',
          pointBorderColor: '#fff',
          pointRadius: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { ticks: { color: '#6c757d', maxTicksLimit: 10 }, grid: { color: '#e9ecef' } },
          y: { ticks: { color: '#6c757d' }, grid: { color: '#e9ecef' } }
        }
      }
    });
    this.charts.push(chart);
  }

  private initCategoryChart(): void {
    if (!this.categoryChartRef || !this.data) return;
    const { labels, values } = toLabelValue(this.data.testCategoryDistribution, 'category', 'count');
    const chart = new Chart(this.categoryChartRef.nativeElement, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          data: values,
          backgroundColor: [
            'rgba(13, 110, 253, 0.7)', 'rgba(25, 135, 84, 0.7)',
            'rgba(255, 193, 7, 0.7)', 'rgba(220, 53, 69, 0.7)',
            'rgba(102, 16, 242, 0.7)', 'rgba(13, 202, 240, 0.7)',
            'rgba(253, 126, 20, 0.7)', 'rgba(20, 184, 166, 0.7)'
          ],
          borderColor: '#ffffff',
          borderWidth: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'bottom', labels: { color: '#495057', padding: 12 } } }
      }
    });
    this.charts.push(chart);
  }
}
