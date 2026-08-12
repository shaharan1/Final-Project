import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyPatient } from '../../../../models/emergency';

@Component({
  selector: 'app-emergency-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './emergency-registration.component.html',
  styleUrls: ['./emergency-registration.component.css']
})
export class EmergencyRegistrationComponent implements OnInit {
  private patientService = inject(EmergencyPatientService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  loading = false;
  saving = false;
  msg = '';
  msgType = '';

  form: EmergencyPatient = this.getEmptyForm();

  recentPatients: EmergencyPatient[] = [];
  showRecent = false;

  bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];
  genders = ['Male', 'Female', 'Other'];
  accidentTypes = ['VEHICLE', 'FALL', 'BURN', 'ASSAULT', 'OTHER'];
  relations = ['Father', 'Mother', 'Spouse', 'Sibling', 'Child', 'Friend', 'Other'];

  ngOnInit(): void {
    this.loadRecent();
  }

  getEmptyForm(): EmergencyPatient {
    return {
      patientName: '', age: undefined, gender: '', phone: '', nationalId: '',
      bloodGroup: '', address: '', isUnknownPatient: false, isPoliceCase: false,
      isReferral: false, referralHospital: '', ambulanceId: '',
      emergencyContactName: '', emergencyContactPhone: '', emergencyContactRelation: '',
      symptoms: '', chiefComplaint: '', injuryDetails: '', accidentType: '',
      emergencyNotes: '', severityLevel: 'MODERATE'
    };
  }

  loadRecent(): void {
    this.patientService.getAll().subscribe({
      next: (data) => { this.recentPatients = data.slice(0, 5); },
      error: () => {}
    });
  }

  register(): void {
    if (!this.form.patientName) {
      this.msg = 'Patient name is required';
      this.msgType = 'error';
      return;
    }
    this.saving = true;
    this.patientService.create(this.form).subscribe({
      next: (res) => {
        this.saving = false;
        this.msg = `Patient registered! Emergency #${res.emergencyNumber}`;
        this.msgType = 'success';
        this.form = this.getEmptyForm();
        this.loadRecent();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.saving = false;
        this.msg = 'Registration failed: ' + (err.error?.message || 'Unknown error');
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  saveDraft(): void {
    localStorage.setItem('emergencyDraft', JSON.stringify(this.form));
    this.msg = 'Draft saved!';
    this.msgType = 'success';
  }

  printSlip(): void {
    window.print();
  }

  toggleUnknown(): void {
    if (this.form.isUnknownPatient) {
      this.form.patientName = 'Unknown Patient';
      this.form.phone = '';
      this.form.address = '';
      this.form.age = undefined;
      this.form.gender = '';
    } else {
      this.form.patientName = '';
    }
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'REGISTERED': 'badge-info',
      'TRIAGE_PENDING': 'badge-warning',
      'TREATING': 'badge-primary',
      'ADMITTED': 'badge-success',
      'CRITICAL': 'badge-danger'
    };
    return map[status] || 'badge-secondary';
  }

  formatTime(t: string): string {
    if (!t) return '';
    return new Date(t).toLocaleString('en-BD');
  }
}
