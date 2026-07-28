import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmergencyBedService } from '../../../../services/emergency/emergency-bed.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyBed } from '../../../../models/emergency/emergency-bed.model';
import { EmergencyPatient } from '../../../../models/emergency/emergency-patient.model';

@Component({
  selector: 'app-emergency-bed',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-bed.component.html',
  styleUrls: ['./emergency-bed.component.css']
})
export class EmergencyBedComponent implements OnInit {
  private bedService = inject(EmergencyBedService);
  private patientService = inject(EmergencyPatientService);

  beds: EmergencyBed[] = [];
  patients: EmergencyPatient[] = [];
  filteredBeds: EmergencyBed[] = [];
  selectedBed: EmergencyBed | null = null;
  loading = true;
  msg = '';
  msgType: 'success' | 'error' = 'success';
  wardFilter = 'ALL';
  statusFilter = 'ALL';
  wardSummary: any[] = [];
  showModal = false;
  showCreateForm = false;
  showAssignModal = false;
  assignPatientId: number | null = null;

  bedForm: Partial<EmergencyBed> = this.getEmptyBedForm();

  wards = ['EMERGENCY', 'TRAUMA', 'RESUSCITATION', 'OBSERVATION', 'ICU'];
  bedStatuses = ['AVAILABLE', 'OCCUPIED', 'CLEANING', 'RESERVED', 'MAINTENANCE'];
  bedTypes = ['STANDARD', 'MONITORED', 'VENTILATOR', 'ISOLATION', 'BARIATRIC'];

  ngOnInit(): void {
    this.loadBeds();
    this.loadPatients();
  }

  loadBeds(): void {
    this.loading = true;
    this.bedService.getAll().subscribe({
      next: (data) => {
        this.beds = data;
        this.applyFilters();
        this.getWardSummary();
        this.loading = false;
      },
      error: () => {
        this.showMessage('Failed to load beds', 'error');
        this.loading = false;
      }
    });
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data) => this.patients = data,
      error: () => {}
    });
  }

  createBed(): void {
    this.bedService.create(this.bedForm).subscribe({
      next: () => {
        this.showMessage('Bed created successfully', 'success');
        this.showCreateForm = false;
        this.bedForm = this.getEmptyBedForm();
        this.loadBeds();
      },
      error: () => this.showMessage('Failed to create bed', 'error')
    });
  }

  assignBed(bedId: number, patientId: number): void {
    this.bedService.assignBed(bedId, patientId).subscribe({
      next: () => {
        this.showMessage('Patient assigned to bed successfully', 'success');
        this.showAssignModal = false;
        this.assignPatientId = null;
        this.selectedBed = null;
        this.loadBeds();
      },
      error: () => this.showMessage('Failed to assign bed', 'error')
    });
  }

  releaseBed(bedId: number): void {
    this.bedService.releaseBed(bedId).subscribe({
      next: () => {
        this.showMessage('Bed released successfully', 'success');
        this.selectedBed = null;
        this.loadBeds();
      },
      error: () => this.showMessage('Failed to release bed', 'error')
    });
  }

  updateStatus(bedId: number, status: string): void {
    this.bedService.updateStatus(bedId, status).subscribe({
      next: () => {
        this.showMessage(`Bed status updated to ${status}`, 'success');
        this.selectedBed = null;
        this.loadBeds();
      },
      error: () => this.showMessage('Failed to update bed status', 'error')
    });
  }

  filterByWard(ward: string): void {
    this.wardFilter = ward;
    this.applyFilters();
  }

  filterByStatus(status: string): void {
    this.statusFilter = status;
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredBeds = this.beds.filter(b => {
      const matchWard = this.wardFilter === 'ALL' || b.wardName === this.wardFilter;
      const matchStatus = this.statusFilter === 'ALL' || b.status === this.statusFilter;
      return matchWard && matchStatus;
    });
  }

  getWardSummary(): void {
    this.wardSummary = this.wards.map(ward => {
      const wardBeds = this.beds.filter(b => b.wardName === ward);
      const total = wardBeds.length;
      const available = wardBeds.filter(b => b.status === 'AVAILABLE').length;
      const occupied = wardBeds.filter(b => b.status === 'OCCUPIED').length;
      const cleaning = wardBeds.filter(b => b.status === 'CLEANING').length;
      const reserved = wardBeds.filter(b => b.status === 'RESERVED').length;
      const maintenance = wardBeds.filter(b => b.status === 'MAINTENANCE').length;
      const utilization = total > 0 ? Math.round((occupied / total) * 100) : 0;
      return { ward, total, available, occupied, cleaning, reserved, maintenance, utilization };
    });
  }

  getBedsForWard(ward: string): EmergencyBed[] {
    return this.filteredBeds.filter(b => b.wardName === ward);
  }

  getBedStatusColor(status: string | undefined): string {
    const colors: Record<string, string> = {
      'AVAILABLE': '#198754',
      'OCCUPIED': '#dc3545',
      'CLEANING': '#ffc107',
      'RESERVED': '#0d6efd',
      'MAINTENANCE': '#6c757d'
    };
    return colors[status || ''] || '#6c757d';
  }

  getBedIcon(status: string | undefined): string {
    const icons: Record<string, string> = {
      'AVAILABLE': 'bi-bed',
      'OCCUPIED': 'bi-person-fill',
      'CLEANING': 'bi-stars',
      'RESERVED': 'bi-bookmark-fill',
      'MAINTENANCE': 'bi-tools'
    };
    return icons[status || ''] || 'bi-bed';
  }

  getPatientName(patientId?: number): string {
    if (!patientId) return '';
    const patient = this.patients.find(p => p.id === patientId);
    return patient ? patient.patientName : 'Unknown';
  }

  getPatientNameById(id: number | null): string {
    if (!id) return '';
    const patient = this.patients.find(p => p.id === id);
    return patient ? patient.patientName : 'Unknown';
  }

  getTotalBeds(): number {
    return this.beds.length;
  }

  getAvailableBeds(): number {
    return this.beds.filter(b => b.status === 'AVAILABLE').length;
  }

  getOccupiedBeds(): number {
    return this.beds.filter(b => b.status === 'OCCUPIED').length;
  }

  openBedModal(bed: EmergencyBed): void {
    this.selectedBed = bed;
    this.showModal = true;
  }

  closeBedModal(): void {
    this.selectedBed = null;
    this.showModal = false;
    this.showAssignModal = false;
    this.assignPatientId = null;
  }

  openAssignModal(bed: EmergencyBed): void {
    this.selectedBed = bed;
    this.showAssignModal = true;
  }

  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
    if (!this.showCreateForm) {
      this.bedForm = this.getEmptyBedForm();
    }
  }

  private getEmptyBedForm(): Partial<EmergencyBed> {
    return {
      bedNumber: '',
      wardName: 'EMERGENCY',
      bedType: 'STANDARD',
      status: 'AVAILABLE'
    };
  }

  private showMessage(text: string, type: 'success' | 'error'): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => this.msg = '', 4000);
  }
}
