import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TriageService } from '../../../../services/emergency/triage.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyPatient, Triage } from '../../../../models/emergency';

@Component({
  selector: 'app-emergency-triage',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-triage.component.html',
  styleUrls: ['./emergency-triage.component.css']
})
export class EmergencyTriageComponent implements OnInit {
  private triageService = inject(TriageService);
  private patientService = inject(EmergencyPatientService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  patients: EmergencyPatient[] = [];
  filteredPatients: EmergencyPatient[] = [];
  triages: Triage[] = [];
  selectedPatient: EmergencyPatient | null = null;
  loading = false;
  saving = false;
  msg = '';
  msgType = '';
  statusFilter = 'ALL';

  triageForm: Triage = this.getEmptyForm();

  ngOnInit(): void {
    this.loadPatients();
    this.loadTriages();
  }

  getEmptyForm(): Triage {
    return {
      triageLevel: null as any,
      bloodPressureSystolic: null as any,
      bloodPressureDiastolic: null as any,
      pulse: null as any,
      temperature: null as any,
      oxygenSaturation: null as any,
      respirationRate: null as any,
      painScore: null as any,
      glasgowComaScale: null as any,
      assessmentNotes: '',
      assessedBy: ''
    };
  }

  loadPatients(): void {
    this.loading = true;
    this.patientService.getAll().subscribe({
      next: (data) => {
        this.patients = data.filter(p =>
          p.status === 'REGISTERED' || p.status === 'TRIAGE_PENDING'
        );
        this.filterPatients();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadTriages(): void {
    this.triageService.getAll().subscribe({
      next: (data) => {
        this.triages = data.slice(-10).reverse();
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }

  filterPatients(): void {
    if (this.statusFilter === 'ALL') {
      this.filteredPatients = [...this.patients];
    } else {
      this.filteredPatients = this.patients.filter(p => p.status === this.statusFilter);
    }
  }

  onStatusFilterChange(): void {
    this.filterPatients();
  }

  selectPatient(patient: EmergencyPatient): void {
    this.selectedPatient = patient;
    this.triageForm = this.getEmptyForm();
    this.msg = '';
    this.msgType = '';
    this.cdr.detectChanges();
  }

  submitTriage(): void {
    if (!this.selectedPatient || !this.triageForm.triageLevel) {
      this.msg = 'Select a patient and triage level';
      this.msgType = 'error';
      return;
    }
    this.saving = true;
    this.triageForm.emergencyPatientId = this.selectedPatient.id;
    this.triageForm.emergencyNumber = this.selectedPatient.emergencyNumber;

    this.triageService.create(this.triageForm).subscribe({
      next: () => {
        this.patientService.updateStatus(this.selectedPatient!.id!, {
          status: 'TRIAGE_PENDING',
          notes: `Triage Level ${this.triageForm.triageLevel} assigned`
        } as any).subscribe({
          next: () => {
            this.saving = false;
            this.msg = `Triage assessment saved for ${this.selectedPatient!.patientName}`;
            this.msgType = 'success';
            this.selectedPatient = null;
            this.triageForm = this.getEmptyForm();
            this.loadPatients();
            this.loadTriages();
          },
          error: () => {
            this.saving = false;
            this.msg = 'Triage saved but status update failed';
            this.msgType = 'warning';
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        this.saving = false;
        this.msg = 'Failed: ' + (err.error?.message || 'Unknown error');
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  getTriageColor(level: number): string {
    const colors: Record<number, string> = {
      1: '#ef4444', 2: '#f97316', 3: '#eab308', 4: '#22c55e', 5: '#3b82f6'
    };
    return colors[level] || '#6b7280';
  }

  getTriageLabel(level: number): string {
    const labels: Record<number, string> = {
      1: 'Resuscitation', 2: 'Emergent', 3: 'Urgent', 4: 'Less Urgent', 5: 'Non-Urgent'
    };
    return labels[level] || 'Unknown';
  }

  getVitalStatus(field: string, value: any): string {
    if (value === null || value === undefined || value === '') return '';
    const v = Number(value);
    const ranges: Record<string, [number, number]> = {
      bloodPressureSystolic: [90, 140],
      bloodPressureDiastolic: [60, 90],
      pulse: [60, 100],
      temperature: [36.1, 37.5],
      oxygenSaturation: [95, 100],
      respirationRate: [12, 20],
      painScore: [0, 3],
      glasgowComaScale: [14, 15]
    };
    const [min, max] = ranges[field] || [0, 100];
    return v < min || v > max ? 'abnormal' : 'normal';
  }

  formatTime(t: string): string {
    if (!t) return '';
    return new Date(t).toLocaleString('en-BD');
  }
}
