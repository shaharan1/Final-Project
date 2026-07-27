import { Component, OnInit, OnDestroy, inject, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { AmbulanceService } from '../../../../services/emergency/ambulance.service';
import { EmergencyBedService } from '../../../../services/emergency/emergency-bed.service';
import { EmergencyDashboard } from '../../../../models/emergency';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-emergency-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './emergency-dashboard.component.html',
  styleUrls: ['./emergency-dashboard.component.css']
})
export class EmergencyDashboardComponent implements OnInit, OnDestroy, AfterViewInit {
  private patientService = inject(EmergencyPatientService);
  private ambulanceService = inject(AmbulanceService);
  private bedService = inject(EmergencyBedService);
  private router = inject(Router);

  @ViewChild('triagePieChart') triagePieChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('statusBarChart') statusBarChartRef!: ElementRef<HTMLCanvasElement>;

  dashboard: EmergencyDashboard = {};
  loading = true;
  error = '';
  patients: any[] = [];
  recentActivity: any[] = [];
  refreshInterval: any;
  triageChart: Chart | null = null;
  statusChart: Chart | null = null;

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

  ngAfterViewInit(): void {
    this.buildCharts();
  }

  ngOnDestroy(): void {
    if (this.refreshInterval) clearInterval(this.refreshInterval);
    this.triageChart?.destroy();
    this.statusChart?.destroy();
  }

  loadDashboard(): void {
    this.patientService.getDashboard().subscribe({
      next: (data) => {
        this.dashboard = data;
        this.updateCards();
        this.buildTriageDistribution();
        this.loading = false;
        this.buildCharts();
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
        this.buildTriageDistribution();
        this.buildCharts();
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

  buildCharts(): void {
    this.buildTriagePieChart();
    this.buildStatusBarChart();
  }

  private buildTriagePieChart(): void {
    if (!this.triagePieChartRef?.nativeElement) return;
    if (this.triageChart) this.triageChart.destroy();

    const counts = [1, 2, 3, 4, 5].map(level =>
      this.patients.filter(p => p.triageLevel === level).length
    );

    this.triageChart = new Chart(this.triagePieChartRef.nativeElement, {
      type: 'doughnut',
      data: {
        labels: ['Resuscitation', 'Emergency', 'Urgent', 'Semi-Urgent', 'Non-Urgent'],
        datasets: [{
          data: counts,
          backgroundColor: ['#dc3545', '#fd7e14', '#ffc107', '#198754', '#0d6efd'],
          borderColor: '#0a0e27',
          borderWidth: 3,
          hoverOffset: 8
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { color: 'rgba(255,255,255,0.7)', padding: 16, font: { size: 12 } }
          }
        }
      }
    });
  }

  private buildStatusBarChart(): void {
    if (!this.statusBarChartRef?.nativeElement) return;
    if (this.statusChart) this.statusChart.destroy();

    const statuses = ['REGISTERED', 'TRIAGE_PENDING', 'TREATING', 'ADMITTED', 'DISCHARGED', 'CRITICAL'];
    const counts = statuses.map(s => this.patients.filter(p => p.status === s).length);

    this.statusChart = new Chart(this.statusBarChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Registered', 'Waiting Triage', 'Treating', 'Admitted', 'Discharged', 'Critical'],
        datasets: [{
          label: 'Patients',
          data: counts,
          backgroundColor: [
            'rgba(13,202,240,0.6)',
            'rgba(255,193,7,0.6)',
            'rgba(13,110,253,0.6)',
            'rgba(25,135,84,0.6)',
            'rgba(108,117,125,0.6)',
            'rgba(220,53,69,0.6)'
          ],
          borderColor: [
            '#0dcaf0', '#ffc107', '#0d6efd', '#198754', '#6c757d', '#dc3545'
          ],
          borderWidth: 1,
          borderRadius: 6
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            ticks: { color: 'rgba(255,255,255,0.5)', font: { size: 11 } },
            grid: { color: 'rgba(255,255,255,0.04)' }
          },
          y: {
            beginAtZero: true,
            ticks: { color: 'rgba(255,255,255,0.5)', stepSize: 1 },
            grid: { color: 'rgba(255,255,255,0.06)' }
          }
        },
        plugins: {
          legend: { display: false }
        }
      }
    });
  }
}
