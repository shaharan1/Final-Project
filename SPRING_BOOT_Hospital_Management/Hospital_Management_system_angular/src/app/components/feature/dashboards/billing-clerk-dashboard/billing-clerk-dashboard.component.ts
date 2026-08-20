import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats, RecentAdmission } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-billing-clerk-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileCardComponent],
  templateUrl: './billing-clerk-dashboard.component.html',
  styleUrl: './billing-clerk-dashboard.component.css',
})
export class BillingClerkDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  recentAdmissions: RecentAdmission[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getAdminStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
      }
    });
    this.dashboardService.getRecentAdmissions().subscribe({
      next: (admissions) => {
        this.recentAdmissions = admissions;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
      }
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
      case 'ACTIVE': return 'Paid';
      case 'PENDING': return 'Pending';
      case 'DISCHARGED': return 'Overdue';
      default: return status;
    }
  }
}
