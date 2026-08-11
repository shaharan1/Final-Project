import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, ChangeDetectorRef, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from '../../../../services/reports/analytics.service';
import {
  DashboardSummary, PatientAnalytics, AppointmentAnalytics, RevenueAnalytics, ActivityItem
} from '../../../../models/reports/analytics.model';

Chart.register(...registerables);

@Component({
  selector: 'app-reports-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reports-dashboard.component.html',
  styleUrls: ['./reports-dashboard.component.css']
})
export class ReportsDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('revenueChart') revenueChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('appointmentsChart') appointmentsChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('paymentChart') paymentChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('demographicsChart') demographicsChartRef!: ElementRef<HTMLCanvasElement>;

  summary: DashboardSummary | null = null;
  patientAnalytics: PatientAnalytics | null = null;
  appointmentAnalytics: AppointmentAnalytics | null = null;
  revenueAnalytics: RevenueAnalytics | null = null;
  recentActivity: ActivityItem[] = [];

  loading = true;
  error = '';
  animatedValues: Record<string, number> = {};
  private animationFrames: number[] = [];
  private charts: Chart[] = [];

  dateRange = 'month';
  selectedDepartment = 'all';

  subReports = [
    { title: 'Patient Analytics', description: 'Demographics, trends & registrations', icon: 'users', route: '/reports/patient', color: '#0d6efd' },
    { title: 'Appointment Analytics', description: 'Scheduling, completion & cancellations', icon: 'calendar', route: '/reports/appointment', color: '#198754' },
    { title: 'Doctor Performance', description: 'Consultations, revenue & rankings', icon: 'stethoscope', route: '/reports/doctor', color: '#6610f2' },
    { title: 'Revenue Analytics', description: 'Income streams & payment methods', icon: 'dollar', route: '/reports/revenue', color: '#fd7e14' },
    { title: 'Laboratory Analytics', description: 'Tests, turnaround & critical results', icon: 'flask', route: '/reports/lab', color: '#14b8a6' },
    { title: 'Pharmacy Analytics', description: 'Sales, stock levels & top medicines', icon: 'pill', route: '/reports/pharmacy', color: '#dc3545' },
    { title: 'Bed Occupancy', description: 'Ward utilization & ICU stats', icon: 'bed', route: '/reports/bed', color: '#0dcaf0' },
    { title: 'Emergency Analytics', description: 'Cases, severity & response times', icon: 'alert', route: '/reports/emergency', color: '#ffc107' }
  ];

  constructor(
    private analytics: AnalyticsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAllData();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {
    this.animationFrames.forEach(id => cancelAnimationFrame(id));
    this.charts.forEach(c => c.destroy());
  }

  loadAllData(): void {
    this.loading = true;
    this.error = '';

    let loaded = 0;
    const total = 5;
    const checkDone = () => {
      loaded++;
      if (loaded >= total) {
        this.loading = false;
        this.animateCounters();
        this.cdr.markForCheck();
        setTimeout(() => this.initCharts(), 100);
      }
    };

    this.analytics.getDashboardSummary().subscribe({
      next: (data) => { this.summary = data; checkDone(); },
      error: () => { checkDone(); }
    });
    this.analytics.getPatientAnalytics().subscribe({
      next: (data) => { this.patientAnalytics = data; checkDone(); },
      error: () => { checkDone(); }
    });
    this.analytics.getAppointmentAnalytics().subscribe({
      next: (data) => { this.appointmentAnalytics = data; checkDone(); },
      error: () => { checkDone(); }
    });
    this.analytics.getRevenueAnalytics().subscribe({
      next: (data) => { this.revenueAnalytics = data; checkDone(); },
      error: () => { checkDone(); }
    });
    this.analytics.getRecentActivity().subscribe({
      next: (data) => { this.recentActivity = data; checkDone(); },
      error: () => { checkDone(); }
    });
  }

  private animateCounters(): void {
    if (!this.summary) return;
    const targets: Record<string, number> = {
      totalPatients: this.summary.totalPatients || 0,
      todayAppointments: this.summary.todayAppointments || 0,
      todayRevenue: this.summary.todayRevenue || 0,
      monthlyRevenue: this.summary.monthlyRevenue || 0,
      pharmacySales: this.summary.pharmacySales || 0,
      labIncome: this.summary.labIncome || 0,
      bedOccupancy: this.summary.bedOccupancy || 0,
      pendingPayments: this.summary.pendingPayments || 0,
      dischargedPatients: this.summary.dischargedPatients || 0,
      emergencyCases: this.summary.emergencyCases || 0
    };
    for (const key of Object.keys(targets)) {
      this.animateValue(key, targets[key]);
    }
  }

  private animateValue(key: string, target: number): void {
    const duration = 1500;
    const startTime = performance.now();
    this.animatedValues[key] = 0;
    const step = (now: number) => {
      const elapsed = now - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      this.animatedValues[key] = Math.round(eased * target);
      this.cdr.markForCheck();
      if (progress < 1) {
        const id = requestAnimationFrame(step);
        this.animationFrames.push(id);
      }
    };
    const id = requestAnimationFrame(step);
    this.animationFrames.push(id);
  }

  getAnimated(key: string): number {
    return this.animatedValues[key] ?? 0;
  }

  private initCharts(): void {
    if (!this.revenueChartRef?.nativeElement) return;

    this.charts.forEach(c => c.destroy());
    this.charts = [];

    const textColor = '#94a3b8';
    const gridColor = 'rgba(148,163,184,0.1)';
    Chart.defaults.color = textColor;

    if (this.revenueAnalytics?.dailyTrend) {
      const labels = this.revenueAnalytics.dailyTrend.map(d => {
        const dt = new Date(d.date);
        return dt.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
      });
      const data = this.revenueAnalytics.dailyTrend.map(d => d.amount);
      this.charts.push(new Chart(this.revenueChartRef.nativeElement, {
        type: 'line',
        data: {
          labels,
          datasets: [{
            label: 'Revenue ($)',
            data,
            borderColor: '#0d6efd',
            backgroundColor: 'rgba(13,110,253,0.1)',
            fill: true,
            tension: 0.4,
            pointRadius: 3,
            pointBackgroundColor: '#0d6efd'
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: {
            x: { grid: { color: gridColor }, ticks: { maxTicksLimit: 10 } },
            y: { grid: { color: gridColor }, ticks: { callback: (v: any) => '৳' + v } }
          }
        }
      }));
    }

    if (this.appointmentAnalytics?.departmentWiseDistribution) {
      this.charts.push(new Chart(this.appointmentsChartRef.nativeElement, {
        type: 'bar',
        data: {
          labels: this.appointmentAnalytics.departmentWiseDistribution.map(d => d.department),
          datasets: [{
            label: 'Appointments',
            data: this.appointmentAnalytics.departmentWiseDistribution.map(d => d.count),
            backgroundColor: ['#0d6efd','#198754','#fd7e14','#6610f2','#dc3545','#14b8a6','#0dcaf0','#ffc107'],
            borderRadius: 6,
            borderSkipped: false
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: {
            x: { grid: { display: false } },
            y: { grid: { color: gridColor }, beginAtZero: true }
          }
        }
      }));
    }

    if (this.revenueAnalytics?.paymentMethodDistribution?.length) {
      const colors = ['#0d6efd','#198754','#fd7e14','#6610f2','#dc3545','#14b8a6'];
      this.charts.push(new Chart(this.paymentChartRef.nativeElement, {
        type: 'pie',
        data: {
          labels: this.revenueAnalytics.paymentMethodDistribution.map(p => p.method),
          datasets: [{
            data: this.revenueAnalytics.paymentMethodDistribution.map(p => p.amount),
            backgroundColor: colors,
            borderWidth: 0
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: 'right', labels: { padding: 16, usePointStyle: true } } }
        }
      }));
    }

    if (this.patientAnalytics) {
      const labels: string[] = [];
      const values: number[] = [];
      if (this.patientAnalytics.bloodGroupDistribution) {
        for (const [k, v] of Object.entries(this.patientAnalytics.bloodGroupDistribution)) {
          labels.push(k);
          values.push(v);
        }
      }
      if (labels.length) {
        this.charts.push(new Chart(this.demographicsChartRef.nativeElement, {
          type: 'doughnut',
          data: {
            labels,
            datasets: [{
              data: values,
              backgroundColor: ['#0d6efd','#198754','#fd7e14','#6610f2','#dc3545','#14b8a6','#0dcaf0','#ffc107'],
              borderWidth: 0
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '65%',
            plugins: { legend: { position: 'right', labels: { padding: 12, usePointStyle: true } } }
          }
        }));
      }
    }
  }

  getActivityColor(type: string): string {
    switch (type?.toLowerCase()) {
      case 'payment': return 'green';
      case 'refund': return 'orange';
      case 'appointment': return 'blue';
      case 'admission': return 'purple';
      case 'emergency': return 'red';
      default: return 'blue';
    }
  }

  getActivityIcon(type: string): string {
    switch (type?.toLowerCase()) {
      case 'payment': return 'M12 1v22M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6';
      case 'refund': return 'M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8';
      case 'appointment': return 'M8 2v4M16 2v4M3 10h18M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z';
      case 'admission': return 'M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z';
      case 'emergency': return 'M22 12h-4l-3 9L9 3l-3 9H2';
      default: return 'M12 8v4l3 3';
    }
  }

  formatCurrency(val: number): string {
    if (val >= 1000000) return '৳' + (val / 1000000).toFixed(1) + 'M';
    if (val >= 1000) return '৳' + (val / 1000).toFixed(1) + 'K';
    return '৳' + val;
  }

  applyFilters(): void {
    this.loadAllData();
  }
}
