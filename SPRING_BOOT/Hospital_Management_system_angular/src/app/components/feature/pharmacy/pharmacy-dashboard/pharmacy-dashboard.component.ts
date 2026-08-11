import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PharmacyDashboardService } from '../../../../services/pharmacy-dashboard.service';
import { PharmacyDashboardModel } from '../../../../models/pharmacy-dashboard.model';
import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';

@Component({
  selector: 'app-pharmacy-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ProfileCardComponent],
  templateUrl: './pharmacy-dashboard.component.html',
  styleUrls: ['./pharmacy-dashboard.component.css']
})
export class PharmacyDashboardComponent implements OnInit, OnDestroy {

  dashboard: PharmacyDashboardModel | null = null;
  loading = true;
  error = '';

  animatedValues: Record<string, number> = {};
  private animationFrames: number[] = [];

  constructor(
    private dashboardService: PharmacyDashboardService,
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
    this.dashboardService.getDashboard().subscribe({
      next: (data: any) => {
        this.dashboard = data;
        this.loading = false;
        this.animateCounters();
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to load dashboard data';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  private animateCounters(): void {
    if (!this.dashboard) return;
    const targets: Record<string, number> = {
      todaySales: this.dashboard.todaySales,
      todayPurchases: this.dashboard.todayPurchases,
      totalMedicines: this.dashboard.totalMedicines,
      availableStock: this.dashboard.totalAvailableStock,
      lowStock: this.dashboard.lowStockCount,
      expired: this.dashboard.expiredCount,
      expiringSoon: this.dashboard.expiringSoonCount,
      monthlyRevenue: this.dashboard.monthlyRevenue,
      monthlyProfit: this.dashboard.monthlyProfit
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

  getExpiryColor(daysLeft: number): string {
    if (daysLeft <= 7) return '#dc3545';
    if (daysLeft <= 30) return '#fd7e14';
    return '#ffc107';
  }

  getExpiryLabel(daysLeft: number): string {
    if (daysLeft <= 0) return 'Expired';
    if (daysLeft === 1) return '1 day left';
    return `${daysLeft} days left`;
  }
}
