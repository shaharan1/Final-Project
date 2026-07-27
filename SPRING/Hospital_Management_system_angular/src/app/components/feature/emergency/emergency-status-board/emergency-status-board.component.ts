import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyBedService } from '../../../../services/emergency/emergency-bed.service';
import { AmbulanceService } from '../../../../services/emergency/ambulance.service';
import { EmergencyDoctorAssignmentService } from '../../../../services/emergency/emergency-doctor-assignment.service';
import { EmergencyPatient, EmergencyBed, Ambulance, EmergencyDoctorAssignment } from '../../../../models/emergency';

@Component({
  selector: 'app-emergency-status-board',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './emergency-status-board.component.html',
  styleUrls: ['./emergency-status-board.component.css']
})
export class EmergencyStatusBoardComponent implements OnInit, OnDestroy {
  private patientService = inject(EmergencyPatientService);
  private bedService = inject(EmergencyBedService);
  private ambulanceService = inject(AmbulanceService);
  private assignmentService = inject(EmergencyDoctorAssignmentService);
  private cdr = inject(ChangeDetectorRef);

  loading = true;
  refreshInterval: any;
  lastRefresh = new Date();
  currentTime = new Date();

  // Queue data
  waitingQueue: EmergencyPatient[] = [];
  treatmentQueue: EmergencyPatient[] = [];
  criticalQueue: EmergencyPatient[] = [];

  // Live data
  beds: EmergencyBed[] = [];
  ambulances: Ambulance[] = [];
  assignments: EmergencyDoctorAssignment[] = [];

  // Stats
  stats = {
    totalWaiting: 0,
    totalTreating: 0,
    totalCritical: 0,
    bedsAvailable: 0,
    bedsOccupied: 0,
    ambulancesAvailable: 0,
    ambulancesOnDuty: 0,
    doctorsActive: 0
  };

  ngOnInit(): void {
    this.loadAllData();
    this.refreshInterval = setInterval(() => this.loadAllData(), 15000);
    setInterval(() => { this.currentTime = new Date(); this.cdr.detectChanges(); }, 1000);
  }

  ngOnDestroy(): void {
    if (this.refreshInterval) clearInterval(this.refreshInterval);
  }

  loadAllData(): void {
    this.patientService.getByStatus('REGISTERED').subscribe({
      next: (data) => { this.waitingQueue = data; this.stats.totalWaiting = data.length; },
      error: () => {}
    });
    this.patientService.getByStatus('TREATING').subscribe({
      next: (data) => { this.treatmentQueue = data; this.stats.totalTreating = data.length; },
      error: () => {}
    });
    this.patientService.getByStatus('CRITICAL').subscribe({
      next: (data) => { this.criticalQueue = data; this.stats.totalCritical = data.length; },
      error: () => {}
    });
    this.bedService.getAll().subscribe({
      next: (data) => {
        this.beds = data;
        this.stats.bedsAvailable = data.filter(b => b.status === 'AVAILABLE').length;
        this.stats.bedsOccupied = data.filter(b => b.status === 'OCCUPIED').length;
      },
      error: () => {}
    });
    this.ambulanceService.getAll().subscribe({
      next: (data) => {
        this.ambulances = data;
        this.stats.ambulancesAvailable = data.filter(a => a.status === 'AVAILABLE').length;
        this.stats.ambulancesOnDuty = data.filter(a => a.status === 'ON_DUTY').length;
      },
      error: () => {}
    });
    this.assignmentService.getActiveAssignments().subscribe({
      next: (data) => { this.assignments = data; this.stats.doctorsActive = data.length; this.loading = false; this.lastRefresh = new Date(); },
      error: () => { this.loading = false; }
    });
  }

  getTriageColor(level: number | undefined): string {
    const colors: Record<number, string> = { 1: '#dc3545', 2: '#fd7e14', 3: '#ffc107', 4: '#198754', 5: '#0d6efd' };
    return colors[level || 0] || '#6c757d';
  }

  getTriageLabel(level: number | undefined): string {
    const labels: Record<number, string> = { 1: 'RESUS', 2: 'EMERG', 3: 'URGENT', 4: 'SEMI', 5: 'NON' };
    return labels[level || 0] || '';
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'AVAILABLE': 'badge-success', 'ON_DUTY': 'badge-warning', 'RETURNING': 'badge-info', 'MAINTENANCE': 'badge-secondary',
      'OCCUPIED': 'badge-danger', 'RESERVED': 'badge-primary', 'CLEANING': 'badge-warning'
    };
    return map[status] || 'badge-secondary';
  }

  getBedStatusColor(status: string | undefined): string {
    const map: Record<string, string> = {
      'AVAILABLE': '#198754', 'OCCUPIED': '#dc3545', 'CLEANING': '#ffc107', 'RESERVED': '#0d6efd', 'MAINTENANCE': '#6c757d'
    };
    return map[status || ''] || '#6c757d';
  }

  getAmbulanceStatusColor(status: string | undefined): string {
    const map: Record<string, string> = {
      'AVAILABLE': '#198754', 'ON_DUTY': '#fd7e14', 'RETURNING': '#0d6efd', 'MAINTENANCE': '#6c757d'
    };
    return map[status || ''] || '#6c757d';
  }

  getWaitingTime(arrivalTime: string | undefined): string {
    if (!arrivalTime) return '-';
    const diff = Date.now() - new Date(arrivalTime).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 60) return mins + 'm';
    return Math.floor(mins / 60) + 'h ' + (mins % 60) + 'm';
  }

  getWardBeds(ward: string): EmergencyBed[] {
    return this.beds.filter(b => b.wardName === ward);
  }

  getWards(): string[] {
    return [...new Set(this.beds.map(b => b.wardName).filter((w): w is string => !!w))];
  }

  formatTime(date: Date): string {
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  }

  formatDate(date: Date): string {
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
  }

  refresh(): void { this.loadAllData(); }
}
