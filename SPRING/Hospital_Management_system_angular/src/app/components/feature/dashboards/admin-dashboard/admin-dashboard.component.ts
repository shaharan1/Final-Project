import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats, WardOccupancy, RecentAdmission, RecentAppointment } from '../../../../services/dashboard.service';
import { TestOrderService } from '../../../../services/test-order.service';
import { LabStats } from '../../../../models/test-order.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent, ProfileCardComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  wards: WardOccupancy[] = [];
  recentAdmissions: RecentAdmission[] = [];
  todayAppointments: RecentAppointment[] = [];
  financialSummary: any = null;
  labStats: LabStats | null = null;
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private testOrderService: TestOrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getAdminStats().subscribe(stats => {
      this.stats = stats;
      this.cdr.detectChanges();
    });
    this.dashboardService.getWardOccupancy().subscribe(wards => {
      this.wards = wards;
      this.cdr.detectChanges();
    });
    this.dashboardService.getRecentAdmissions().subscribe(admissions => {
      this.recentAdmissions = admissions;
      this.loading = false;
      this.cdr.detectChanges();
    });
    this.dashboardService.getTodayAppointments().subscribe(appts => {
      this.todayAppointments = appts;
      this.cdr.detectChanges();
    });
    this.dashboardService.getFinancialSummary().subscribe(fin => {
      this.financialSummary = fin;
      this.cdr.detectChanges();
    });
    this.testOrderService.getStats().subscribe({
      next: (res) => { this.labStats = res; this.cdr.markForCheck(); },
      error: () => {}
    });
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'stable';
      case 'PENDING': return 'observation';
      case 'DISCHARGED': return 'stable';
      default: return 'observation';
    }
  }

  formatStatus(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'Stable';
      case 'PENDING': return 'Under Observation';
      case 'DISCHARGED': return 'Discharged';
      default: return status;
    }
  }

  getBarClass(percentage: number): string {
    if (percentage >= 90) return 'high';
    if (percentage <= 40) return 'low';
    return '';
  }

  formatCurrency(amount: number): string {
    return '৳' + (amount || 0).toLocaleString('en-BD');
  }

  getAppointmentBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'CONFIRMED': return 'badge--green';
      case 'PENDING_VERIFICATION': return 'badge--yellow';
      case 'CANCELLED': return 'badge--red';
      default: return 'badge--default';
    }
  }
}
