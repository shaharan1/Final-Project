import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BillingDashboardService } from '../../../../services/billing/billing-dashboard.service';
import {
  DashboardSummary,
  RevenueChart,
  PaymentMethodBreakdown,
  DepartmentRevenue,
  RecentActivity
} from '../../../../models/billing/payment.model';

@Component({
  selector: 'app-billing-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './billing-dashboard.component.html',
  styleUrls: ['./billing-dashboard.component.css']
})
export class BillingDashboardComponent implements OnInit, OnDestroy {

  summary: DashboardSummary | null = null;
  dailyRevenue: RevenueChart[] = [];
  monthlyRevenue: RevenueChart[] = [];
  departmentRevenue: DepartmentRevenue[] = [];
  paymentMethods: PaymentMethodBreakdown[] = [];
  recentActivity: RecentActivity[] = [];

  loading = true;
  error = '';

  animatedValues: Record<string, number> = {};
  private animationFrames: number[] = [];

  private maxDailyRevenue = 0;
  private maxMonthlyRevenue = 0;

  constructor(
    private dashboardService: BillingDashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  ngOnDestroy(): void {
    this.animationFrames.forEach(id => cancelAnimationFrame(id));
  }

  loadDashboard(): void {
    this.loading = true;
    this.error = '';

    this.dashboardService.getSummary().subscribe({
      next: (data: DashboardSummary) => {
        this.summary = data;
        this.loading = false;
        this.animateCounters();
        this.loadChartData();
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to load dashboard data';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadChartData(): void {
    this.dashboardService.getDailyRevenueChart().subscribe({
      next: (data: RevenueChart[]) => {
        this.dailyRevenue = data;
        this.maxDailyRevenue = Math.max(...data.map(d => d.revenue), 1);
        this.cdr.markForCheck();
      }
    });

    this.dashboardService.getMonthlyRevenueChart().subscribe({
      next: (data: RevenueChart[]) => {
        this.monthlyRevenue = data;
        this.maxMonthlyRevenue = Math.max(...data.map(d => d.revenue), 1);
        this.cdr.markForCheck();
      }
    });

    this.dashboardService.getDepartmentRevenue().subscribe({
      next: (data: DepartmentRevenue[]) => {
        this.departmentRevenue = data;
        this.cdr.markForCheck();
      }
    });

    this.dashboardService.getPaymentMethodDistribution().subscribe({
      next: (data: PaymentMethodBreakdown[]) => {
        this.paymentMethods = data;
        this.cdr.markForCheck();
      }
    });

    this.dashboardService.getRecentActivity().subscribe({
      next: (data: RecentActivity[]) => {
        this.recentActivity = data;
        this.cdr.markForCheck();
      }
    });
  }

  private animateCounters(): void {
    if (!this.summary) return;

    const targets: Record<string, number> = {
      todayRevenue: this.summary.todayRevenue,
      monthlyRevenue: this.summary.monthlyRevenue,
      pendingPayments: this.summary.pendingPayments,
      paidBills: this.summary.paidBills,
      unpaidBills: this.summary.unpaidBills,
      insuranceClaims: this.summary.insuranceClaims,
      refundAmount: this.summary.refundAmount,
      totalPatientsBilled: this.summary.totalPatientsBilled
    };

    for (const key of Object.keys(targets)) {
      this.animateValue(key, targets[key]);
    }
  }

  private animateValue(key: string, target: number): void {
    const duration = 1200;
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

  getDailyBarHeight(revenue: number): number {
    return this.maxDailyRevenue > 0 ? (revenue / this.maxDailyRevenue) * 100 : 0;
  }

  getMonthlyBarHeight(revenue: number): number {
    return this.maxMonthlyRevenue > 0 ? (revenue / this.maxMonthlyRevenue) * 100 : 0;
  }

  getPaymentPieGradient(): string {
    if (!this.paymentMethods.length) return '';
    let accumulated = 0;
    const stops: string[] = [];
    const colors = ['#0d6efd', '#198754', '#fd7e14', '#6610f2', '#dc3545', '#14b8a6'];

    for (let i = 0; i < this.paymentMethods.length; i++) {
      const start = accumulated;
      accumulated += this.paymentMethods[i].percentage;
      stops.push(`${colors[i % colors.length]} ${start}% ${accumulated}%`);
    }

    return `conic-gradient(${stops.join(', ')})`;
  }

  getActivityIcon(type: string): string {
    switch (type) {
      case 'PAYMENT': return 'M12 1v22M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6';
      case 'REFUND': return 'M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8';
      case 'INVOICE': return 'M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z M14 2v6h6 M16 13H8 M16 17H8 M10 9H8';
      case 'CLAIM': return 'M22 12h-4l-3 9L9 3l-3 9H2';
      default: return 'M12 8v4l3 3';
    }
  }

  getActivityColor(type: string): string {
    switch (type) {
      case 'PAYMENT': return 'green';
      case 'REFUND': return 'orange';
      case 'INVOICE': return 'blue';
      case 'CLAIM': return 'purple';
      default: return 'blue';
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'COMPLETED':
      case 'PAID':
      case 'APPROVED':
        return 'badge--success';
      case 'PENDING':
      case 'PROCESSING':
        return 'badge--warning';
      case 'FAILED':
      case 'REJECTED':
      case 'CANCELLED':
        return 'badge--danger';
      default:
        return 'badge--info';
    }
  }

  formatDayLabel(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { weekday: 'short' });
  }

  formatMonthLabel(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { month: 'short' });
  }
}
