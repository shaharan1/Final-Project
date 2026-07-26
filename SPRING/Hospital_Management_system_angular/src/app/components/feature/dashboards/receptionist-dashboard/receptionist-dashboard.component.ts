import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats, RecentAppointment } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-receptionist-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent, ProfileCardComponent],
  templateUrl: './receptionist-dashboard.component.html',
  styleUrl: './receptionist-dashboard.component.css',
})
export class ReceptionistDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  todayAppointments: RecentAppointment[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getReceptionistStats().subscribe(stats => {
      this.stats = stats;
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
      case 'CONFIRMED': return 'stable';
      case 'PENDING': return 'observation';
      case 'URGENT': return 'critical';
      default: return 'observation';
    }
  }

  formatStatus(status: string): string {
    switch (status?.toUpperCase()) {
      case 'CONFIRMED': return 'Checked In';
      case 'PENDING': return 'Waiting';
      case 'URGENT': return 'Urgent';
      default: return status;
    }
  }
}
