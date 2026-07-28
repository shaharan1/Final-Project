import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats, RecentAdmission, RecentAppointment } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-office-staff-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileCardComponent],
  templateUrl: './office-staff-dashboard.component.html',
  styleUrl: './office-staff-dashboard.component.css',
})
export class OfficeStaffDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  recentAdmissions: RecentAdmission[] = [];
  todayAppointments: RecentAppointment[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getOfficeStats().subscribe(stats => {
      this.stats = stats;
      this.cdr.detectChanges();
    });
    this.dashboardService.getRecentAdmissions().subscribe(admissions => {
      this.recentAdmissions = admissions;
      this.cdr.detectChanges();
    });
    this.dashboardService.getTodayAppointments().subscribe(appointments => {
      this.todayAppointments = appointments;
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': case 'CONFIRMED': return 'stable';
      case 'PENDING': return 'observation';
      case 'CANCELLED': return 'critical';
      default: return 'observation';
    }
  }

  formatStatus(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'Active';
      case 'PENDING': return 'Pending';
      case 'CONFIRMED': return 'Confirmed';
      case 'CANCELLED': return 'Cancelled';
      default: return status;
    }
  }
}
