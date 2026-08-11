import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmergencyTimelineService } from '../../../../services/emergency/emergency-timeline.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';

@Component({
  selector: 'app-emergency-timeline',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-timeline.component.html',
  styleUrls: ['./emergency-timeline.component.css']
})
export class EmergencyTimelineComponent implements OnInit {
  patients: any[] = [];
  timeline: any[] = [];
  selectedPatient: any = null;
  loading = false;

  eventTypes = [
    { type: 'ARRIVAL', label: 'Arrival', color: '#3b82f6', icon: '🚑' },
    { type: 'REGISTRATION', label: 'Registration', color: '#6366f1', icon: '📝' },
    { type: 'TRIAGE', label: 'Triage', color: '#eab308', icon: '⚠️' },
    { type: 'DOCTOR_ASSIGNED', label: 'Doctor Assigned', color: '#22c55e', icon: '👨‍⚕️' },
    { type: 'MEDICINE_ORDERED', label: 'Medicine Ordered', color: '#f97316', icon: '💊' },
    { type: 'LAB_ORDERED', label: 'Lab Ordered', color: '#a855f7', icon: '🧪' },
    { type: 'RADIOLOGY_ORDERED', label: 'Radiology Ordered', color: '#8b5cf6', icon: '📡' },
    { type: 'OBSERVATION', label: 'Observation', color: '#64748b', icon: '👁️' },
    { type: 'BED_ASSIGNED', label: 'Bed Assigned', color: '#06b6d4', icon: '🛏️' },
    { type: 'ADMITTED', label: 'Admitted', color: '#22c55e', icon: '🏥' },
    { type: 'ICU_TRANSFER', label: 'ICU Transfer', color: '#ef4444', icon: '🚨' },
    { type: 'SURGERY', label: 'Surgery', color: '#dc2626', icon: '🔪' },
    { type: 'DISCHARGED', label: 'Discharged', color: '#6b7280', icon: '✅' },
    { type: 'TRANSFERRED', label: 'Transferred', color: '#6b7280', icon: '🔄' },
    { type: 'BILLING_GENERATED', label: 'Billing Generated', color: '#f97316', icon: '💰' },
    { type: 'BILLING_PAID', label: 'Billing Paid', color: '#22c55e', icon: '💳' }
  ];

  constructor(
    private timelineService: EmergencyTimelineService,
    private patientService: EmergencyPatientService
  ) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data: any[]) => this.patients = data,
      error: () => {}
    });
  }

  selectPatient(patient: any): void {
    this.selectedPatient = patient;
    this.loadTimeline(patient.id);
  }

  loadTimeline(patientId: number): void {
    this.loading = true;
    this.timelineService.getByEmergencyPatientId(patientId).subscribe({
      next: (data: any[]) => {
        this.timeline = data.sort((a: any, b: any) => new Date(b.eventTime).getTime() - new Date(a.eventTime).getTime());
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  getEventIcon(type: string): string {
    return this.eventTypes.find(e => e.type === type)?.icon || '📌';
  }

  getEventColor(type: string): string {
    return this.eventTypes.find(e => e.type === type)?.color || '#6b7280';
  }

  getEventLabel(type: string): string {
    return this.eventTypes.find(e => e.type === type)?.label || type;
  }

  formatTime(timestamp: string): string {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleString('en-US', {
      month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
    });
  }
}
