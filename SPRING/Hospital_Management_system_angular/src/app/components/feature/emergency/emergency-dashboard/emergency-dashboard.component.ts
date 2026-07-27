import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { AmbulanceService } from '../../../../services/emergency/ambulance.service';
import { EmergencyBedService } from '../../../../services/emergency/emergency-bed.service';
import { EmergencyDashboard } from '../../../../models/emergency';

@Component({
  selector: 'app-emergency-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './emergency-dashboard.component.html',
  styleUrls: ['./emergency-dashboard.component.css']
})
export class EmergencyDashboardComponent implements OnInit, OnDestroy {
  private patientService = inject(EmergencyPatientService);
  private ambulanceService = inject(AmbulanceService);
  private bedService = inject(EmergencyBedService);
  private router = inject(Router);

  dashboard: EmergencyDashboard = {};
  loading = true;
  error = '';
  patients: any[] = [];
  recentActivity: any[] = [];
  refreshInterval: any;

  dashboardCards = [
    { label: 'Patients Today', value: 0, icon: 'bi-people-fill', color: '#dc3545', key: 'emergencyPatientsToday', route: '/emergency/registration' },
    { label: 'Critical', value: 0, icon: 'bi-heart-pulse-fill', color: '#ff6b35', key: 'criticalPatients', route: '/emergency/triage' },
    { label: 'Waiting', value: 0, icon: 'bi-clock-fill', color: '#ffc107', key: 'waitingPatients', route: '/emergency/triage' },
    { label: 'Under Treatment', value: 0, icon: 'bi-clipboard2-pulse-fill', color: '#0d6efd', key: 'patientsUnderTreatment', route: '/emergency/status-board' },
    { label: 'Admitted from ED', value: 0, icon: 'bi-hospital', color: '#6610f2', key: 'admittedFromEmergency', route: '/emergency/status-board' },
    { label: 'Ambulances Active', value: 0, icon: 'bi-truck', color: '#198754', key: 'ambulancesActive', route: '/emergency/ambulances' },
    { label: 'Doctors On Duty', value: 0, icon: 'bi-person-workspace', color: '#20c997', key: 'doctorsOnDuty', route: '/emergency/assignments' },
    { label: 'Nurses On Duty', value: 0, icon: 'bi-person-hearts', color: '#0dcaf0', key: 'nursesOnDuty', route: '/emergency/assignments' },
    { label: 'Available Beds', value: 0, icon: 'bi-bed', color: '#198754', key: 'availableEmergencyBeds', route: '/emergency/beds' },
    { label: 'ICU Beds Available', value: 0, icon: 'bi-heart-pulse', color: '#dc3545', key: 'icuBedsAvailable', route: '/emergency/beds' },
    { label: 'Today Revenue', value: 0, icon: 'bi-currency-dollar', color: '#ffc107', key: 'todaysEmergencyRevenue', route: '/emergency/billing', isCurrency: true },
    { label: 'Avg Wait Time', value: 0, icon: 'bi-stopwatch', color: '#fd7e14', key: 'averageWaitingTime', isTime: true, suffix: ' min' }
  ];

  triageDistribution: { level: number; label: string; color: string; count: number }[] = [];

  statusFilters = ['All', 'REGISTERED', 'TRIAGE_PENDING', 'TREATING', 'ADMITTED', 'DISCHARGED', 'CRITICAL'];
  selectedStatus = 'All';
  filteredPatients: any[] = [];

  ngOnInit(): void {
    this.loadDashboard();
    this.loadPatients();
    this.refreshInterval = setInterval(() => this.loadDashboard(), 30000);
  }

  ngOnDestroy(): void {
    if (this.refreshInterval) clearInterval(this.refreshInterval);
  }

  loadDashboard(): void {
    this.patientService.getDashboard().subscribe({
      next: (data) => {
        this.dashboard = data;
        this.updateCards();
        this.buildTriageDistribution();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load dashboard';
        this.loading = false;
      }
    });
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data) => {
        this.patients = data;
        this.filterByStatus(this.selectedStatus);
      },
      error: () => {}
    });
  }

  updateCards(): void {
    this.dashboardCards.forEach(card => {
      card.value = (this.dashboard as any)[card.key] || 0;
    });
  }

  buildTriageDistribution(): void {
    const levels = [1, 2, 3, 4, 5];
    this.triageDistribution = levels.map(level => ({
      level,
      label: this.getTriageLabel(level),
      color: this.getTriageColor(level),
      count: this.patients.filter(p => p.triageLevel === level).length
    }));
  }

  filterByStatus(status: string): void {
    this.selectedStatus = status;
    this.filteredPatients = status === 'All'
      ? this.patients.slice(0, 10)
      : this.patients.filter(p => p.status === status).slice(0, 10);
  }

  navigateTo(route: string | undefined): void {
    if (!route) return;
    this.router.navigate([route]);
  }

  formatCurrency(val: number): string {
    return '\u09F3' + (val || 0).toLocaleString('en-BD');
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'REGISTERED': 'badge-info',
      'TRIAGE_PENDING': 'badge-warning',
      'TREATING': 'badge-primary',
      'ADMITTED': 'badge-success',
      'DISCHARGED': 'badge-secondary',
      'CRITICAL': 'badge-danger',
      'TRANSFERRED': 'badge-purple'
    };
    return map[status] || 'badge-secondary';
  }

  getTriageColor(level: number): string {
    const colors: Record<number, string> = {
      1: '#dc3545',
      2: '#fd7e14',
      3: '#ffc107',
      4: '#198754',
      5: '#0d6efd'
    };
    return colors[level] || '#6c757d';
  }

  getTriageLabel(level: number): string {
    const labels: Record<number, string> = {
      1: 'Resuscitation',
      2: 'Emergency',
      3: 'Urgent',
      4: 'Semi-Urgent',
      5: 'Non-Urgent'
    };
    return labels[level] || 'Unknown';
  }

  getStatusLabel(status: string): string {
    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  refresh(): void {
    this.loading = true;
    this.loadDashboard();
    this.loadPatients();
  }

  getCurrentDateTime(): string {
    return new Date().toLocaleString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
