import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats, WardOccupancy, RecentAdmission } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-ward-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileCardComponent],
  templateUrl: './ward-manager-dashboard.component.html',
  styleUrl: './ward-manager-dashboard.component.css',
})
export class WardManagerDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  wards: WardOccupancy[] = [];
  recentAdmissions: RecentAdmission[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getWardManagerStats().subscribe({
      next: (stats) => { this.stats = stats; this.cdr.detectChanges(); },
      error: () => { this.cdr.detectChanges(); }
    });
    this.dashboardService.getWardOccupancy().subscribe({
      next: (wards) => { this.wards = wards; this.cdr.detectChanges(); },
      error: () => { this.cdr.detectChanges(); }
    });
    this.dashboardService.getRecentAdmissions().subscribe({
      next: (admissions) => { this.recentAdmissions = admissions; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'stable';
      case 'PENDING': return 'observation';
      case 'CRITICAL': return 'critical';
      default: return 'observation';
    }
  }

  formatStatus(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'Stable';
      case 'PENDING': return 'Observation';
      case 'CRITICAL': return 'Critical';
      default: return status;
    }
  }

  getBarClass(percentage: number): string {
    if (percentage >= 90) return 'high';
    if (percentage <= 40) return 'low';
    return '';
  }

  get occupancyRate(): number {
    if (!this.stats || this.stats.totalBeds === 0) return 0;
    return Math.round((this.stats.admittedPatients / this.stats.totalBeds) * 100 * 10) / 10;
  }
}
