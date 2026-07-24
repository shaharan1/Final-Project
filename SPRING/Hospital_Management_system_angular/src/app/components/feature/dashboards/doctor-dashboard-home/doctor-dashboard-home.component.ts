import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { DashboardService, RecentAppointment } from '../../../../services/dashboard.service';
import { StorageService } from '../../../../services/storage.service';

@Component({
  selector: 'app-doctor-dashboard-home',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './doctor-dashboard-home.component.html',
  styleUrl: './doctor-dashboard-home.component.css',
})
export class DoctorDashboardHomeComponent implements OnInit {
  totalPatients = 0;
  todayAppointments = 0;
  pendingReports = 0;
  appointments: RecentAppointment[] = [];
  loading = true;
  doctorName = 'Doctor';

  constructor(
    private dashboardService: DashboardService,
    private storageService: StorageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const user = this.storageService.getUser();
    if (user) {
      this.doctorName = user.name || 'Doctor';
      this.dashboardService.getDoctorStats(user.userId).subscribe(data => {
        this.totalPatients = data.totalPatients;
        this.todayAppointments = data.todayAppointments;
        this.pendingReports = data.pendingReports;
        this.appointments = data.appointments;
        this.loading = false;
        this.cdr.detectChanges();
      });
    } else {
      this.loading = false;
      this.cdr.detectChanges();
    }
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
      case 'CONFIRMED': return 'Confirmed';
      case 'PENDING': return 'Pending';
      case 'URGENT': return 'Urgent';
      default: return status;
    }
  }
}
