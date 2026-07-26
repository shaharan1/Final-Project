import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, RecentAppointment } from '../../../../services/dashboard.service';
import { StorageService } from '../../../../services/storage.service';
import { PrescriptionModel } from '../../../../models/prescriptionModel';
import { PrescriptionService } from '../../../../services/prescription.service';
import { DoctorModelService } from '../../../../services/doctor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-doctor-dashboard-home',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent, ProfileCardComponent],
  templateUrl: './doctor-dashboard-home.component.html',
  styleUrl: './doctor-dashboard-home.component.css',
})
export class DoctorDashboardHomeComponent implements OnInit {
  totalPatients = 0;
  todayAppointments = 0;
  pendingReports = 0;
  appointments: RecentAppointment[] = [];
  prescriptions: PrescriptionModel[] = [];
  loading = true;
  doctorName = 'Doctor';

  constructor(
    private dashboardService: DashboardService,
    private storageService: StorageService,
    private prescriptionService: PrescriptionService,
    private doctorService: DoctorModelService,
    private cdr: ChangeDetectorRef,
    private router: Router
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
      this.doctorService.findByUserId(user.userId).subscribe({
        next: (doctor) => {
          if (doctor?.id) {
            this.loadPrescriptions(doctor.id);
          }
        },
        error: () => {}
      });
    } else {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  loadPrescriptions(doctorId: number): void {
    this.prescriptionService.getByDoctorId(doctorId).subscribe({
      next: (res: PrescriptionModel[]) => {
        this.prescriptions = res;
        this.cdr.markForCheck();
      },
      error: () => {}
    });
  }

  hasPrescription(appointmentId: number): boolean {
    return this.prescriptions.some(p => p.appointmentId === appointmentId);
  }

  editPrescription(appointmentId: number): void {
    const prescription = this.prescriptions.find(p => p.appointmentId === appointmentId);
    if (prescription?.id) {
      this.router.navigate(['/prescriptions/edit', prescription.id]);
    } else {
      this.router.navigate(['/prescriptions/create', appointmentId]);
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
