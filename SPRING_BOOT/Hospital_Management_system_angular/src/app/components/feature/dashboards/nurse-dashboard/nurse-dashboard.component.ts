import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats, RecentAdmission, WardOccupancy } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-nurse-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileCardComponent],
  templateUrl: './nurse-dashboard.component.html',
  styleUrl: './nurse-dashboard.component.css',
})
export class NurseDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  recentAdmissions: RecentAdmission[] = [];
  wards: WardOccupancy[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getNurseStats().subscribe(stats => {
      this.stats = stats;
      this.cdr.detectChanges();
    });
    this.dashboardService.getRecentAdmissions().subscribe(admissions => {
      this.recentAdmissions = admissions;
      this.cdr.detectChanges();
    });
    this.dashboardService.getWardOccupancy().subscribe(wards => {
      this.wards = wards;
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
      case 'CRITICAL': return 'critical';
      default: return 'observation';
    }
  }

  formatStatus(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'Stable';
      case 'PENDING': return 'Under Observation';
      case 'CRITICAL': return 'Critical';
      default: return status;
    }
  }

  getBarClass(percentage: number): string {
    if (percentage >= 90) return 'high';
    if (percentage <= 40) return 'low';
    return '';
  }
}
