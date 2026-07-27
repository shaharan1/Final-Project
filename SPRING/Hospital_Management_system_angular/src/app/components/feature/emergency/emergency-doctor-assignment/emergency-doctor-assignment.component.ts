import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmergencyDoctorAssignmentService } from '../../../../services/emergency/emergency-doctor-assignment.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { DoctorModelService } from '../../../../services/doctor.service';
import { NurseService } from '../../../../services/nurse.service';
import { EmergencyPatient, EmergencyDoctorAssignment } from '../../../../models/emergency';
import { DoctorModel } from '../../../../models/doctorModel';
import { NurseModel } from '../../../../models/nurseModel';

@Component({
  selector: 'app-emergency-doctor-assignment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-doctor-assignment.component.html',
  styleUrls: ['./emergency-doctor-assignment.component.css']
})
export class EmergencyDoctorAssignmentComponent implements OnInit {
  private assignmentService = inject(EmergencyDoctorAssignmentService);
  private patientService = inject(EmergencyPatientService);
  private doctorService = inject(DoctorModelService);
  private nurseService = inject(NurseService);
  private cdr = inject(ChangeDetectorRef);

  patients: EmergencyPatient[] = [];
  filteredPatients: EmergencyPatient[] = [];
  assignments: EmergencyDoctorAssignment[] = [];
  activeAssignments: EmergencyDoctorAssignment[] = [];
  doctors: DoctorModel[] = [];
  nurses: NurseModel[] = [];
  selectedPatient: EmergencyPatient | null = null;
  loading = false;
  saving = false;
  msg = '';
  msgType = '';
  statusFilter = 'ALL';

  assignmentForm = this.getEmptyForm();

  statsTotal = 0;
  statsPrimary = 0;
  statsConsultant = 0;
  statsResident = 0;

  ngOnInit(): void {
    this.loadPatients();
    this.loadDoctors();
    this.loadNurses();
    this.loadActiveAssignments();
  }

  getEmptyForm() {
    return {
      doctorId: null as number | null,
      nurseId: null as number | null,
      assignmentType: 'PRIMARY',
      notes: ''
    };
  }

  loadPatients(): void {
    this.loading = true;
    this.patientService.getAll().subscribe({
      next: (data) => {
        this.patients = data.filter(p =>
          p.status === 'TRIAGE_PENDING' || p.status === 'TREATING' || p.status === 'REGISTERED'
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

  loadDoctors(): void {
    this.doctorService.getAll().subscribe({
      next: (data) => { this.doctors = data; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  loadNurses(): void {
    this.nurseService.getAllNurses().subscribe({
      next: (data) => { this.nurses = data; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  loadActiveAssignments(): void {
    this.assignmentService.getActiveAssignments().subscribe({
      next: (data) => {
        this.activeAssignments = data;
        this.computeStats();
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }

  loadAllAssignments(): void {
    this.assignmentService.getAll().subscribe({
      next: (data) => {
        this.assignments = data;
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }

  computeStats(): void {
    const active = this.activeAssignments;
    this.statsTotal = active.length;
    this.statsPrimary = active.filter(a => a.assignmentType === 'PRIMARY').length;
    this.statsConsultant = active.filter(a => a.assignmentType === 'CONSULTANT').length;
    this.statsResident = active.filter(a => a.assignmentType === 'RESIDENT').length;
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
    this.assignmentForm = this.getEmptyForm();
    this.msg = '';
    this.msgType = '';
    this.cdr.detectChanges();
  }

  assignDoctor(): void {
    if (!this.selectedPatient || !this.assignmentForm.doctorId) {
      this.msg = 'Select a patient and doctor';
      this.msgType = 'error';
      return;
    }
    this.saving = true;

    const doctor = this.doctors.find(d => d.id === this.assignmentForm.doctorId);
    const nurse = this.nurses.find(n => n.id === this.assignmentForm.nurseId);

    const assignment: EmergencyDoctorAssignment = {
      emergencyPatientId: this.selectedPatient.id,
      emergencyNumber: this.selectedPatient.emergencyNumber,
      doctorId: this.assignmentForm.doctorId,
      doctorName: doctor?.name || '',
      nurseId: this.assignmentForm.nurseId || undefined,
      nurseName: nurse?.name || '',
      assignmentType: this.assignmentForm.assignmentType,
      notes: this.assignmentForm.notes,
      isActive: true
    };

    this.assignmentService.create(assignment).subscribe({
      next: () => {
        this.patientService.updateStatus(this.selectedPatient!.id!, {
          status: 'TREATING',
          notes: `Doctor assigned: ${doctor?.name}`
        } as any).subscribe({
          next: () => {
            this.saving = false;
            this.msg = `Dr. ${doctor?.name} assigned to ${this.selectedPatient!.patientName}`;
            this.msgType = 'success';
            this.selectedPatient = null;
            this.assignmentForm = this.getEmptyForm();
            this.loadPatients();
            this.loadActiveAssignments();
          },
          error: () => {
            this.saving = false;
            this.msg = 'Assignment saved but status update failed';
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

  unassign(id: number | undefined): void {
    if (!id) return;
    this.assignmentService.unassign(id).subscribe({
      next: () => {
        this.msg = 'Assignment removed';
        this.msgType = 'success';
        this.loadActiveAssignments();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.msg = 'Failed to unassign: ' + (err.error?.message || 'Unknown error');
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  getAssignmentTypeColor(type: string): string {
    const colors: Record<string, string> = {
      'PRIMARY': '#3b82f6',
      'CONSULTANT': '#a855f7',
      'RESIDENT': '#22c55e',
      'SPECIALIST': '#f97316'
    };
    return colors[type] || '#6b7280';
  }

  getDoctorAssignmentCount(doctorId: number): number {
    return this.activeAssignments.filter(a => a.doctorId === doctorId).length;
  }

  getDoctorWorkloadPercent(doctorId: number): number {
    const count = this.getDoctorAssignmentCount(doctorId);
    const max = 5;
    return Math.min((count / max) * 100, 100);
  }

  formatTime(t: string): string {
    if (!t) return '';
    return new Date(t).toLocaleString('en-BD');
  }
}
