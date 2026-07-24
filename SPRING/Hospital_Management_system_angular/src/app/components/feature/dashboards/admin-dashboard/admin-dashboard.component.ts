import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { DashboardService, DashboardStats, WardOccupancy, RecentAdmission } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css',
})
export class AdminDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  wards: WardOccupancy[] = [];
  recentAdmissions: RecentAdmission[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
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
}
