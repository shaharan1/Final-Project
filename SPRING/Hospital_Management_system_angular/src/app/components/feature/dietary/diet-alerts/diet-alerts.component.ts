import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PatientDietAlertService } from '../../../../services/dietary/patient-diet-alert.service';

@Component({
  selector: 'app-diet-alerts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './diet-alerts.component.html',
  styleUrl: './diet-alerts.component.css'
})
export class DietAlertsComponent implements OnInit {
  alerts: any[] = [];
  filteredAlerts: any[] = [];
  loading = true;
  filterSeverity = '';
  filterType = '';
  filterStatus = '';
  showModal = false;
  formModel: any = {};
  msg = '';
  msgType = '';

  alertTypes = ['DIABETIC', 'LOW_SODIUM', 'ALLERGY', 'NPO', 'FASTING', 'CRITICAL', 'FOOD_ALLERGY', 'KITCHEN_ALERT', 'LATE_DELIVERY'];
  severities = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
  severityColors: Record<string, string> = { LOW: '#0dcaf0', MEDIUM: '#fd7e14', HIGH: '#dc3545', CRITICAL: '#dc3545' };

  constructor(private alertService: PatientDietAlertService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.alertService.getAll().subscribe({
      next: (data) => { this.alerts = data; this.filteredAlerts = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  filterAlerts(): void {
    let result = [...this.alerts];
    if (this.filterSeverity) result = result.filter(a => a.severity === this.filterSeverity);
    if (this.filterType) result = result.filter(a => a.alertType === this.filterType);
    if (this.filterStatus) result = result.filter(a => a.status === this.filterStatus);
    this.filteredAlerts = result;
  }

  acknowledgeAlert(id: number): void {
    this.alertService.updateStatus(id, 'ACKNOWLEDGED').subscribe({
      next: () => { this.msg = 'Alert acknowledged'; this.msgType = 'success'; this.loadData(); },
      error: () => {}
    });
  }

  resolveAlert(id: number): void {
    this.alertService.updateStatus(id, 'RESOLVED').subscribe({
      next: () => { this.msg = 'Alert resolved'; this.msgType = 'success'; this.loadData(); },
      error: () => {}
    });
  }

  openAddModal(): void {
    this.formModel = { patientId: null, alertType: 'FOOD_ALLERGY', description: '', severity: 'MEDIUM', allergenName: '', specialInstructions: '' };
    this.showModal = true;
  }

  saveAlert(): void {
    if (!this.formModel.patientId || !this.formModel.description) { this.msg = 'All required fields must be filled'; this.msgType = 'error'; return; }
    this.alertService.create(this.formModel).subscribe({
      next: () => { this.showModal = false; this.msg = 'Alert created'; this.msgType = 'success'; this.loadData(); },
      error: () => { this.msg = 'Failed to create alert'; this.msgType = 'error'; }
    });
  }

  getAlertIcon(type: string): string {
    const map: Record<string, string> = {
      'DIABETIC': '🩸', 'LOW_SODIUM': '🧂', 'ALLERGY': '⚠️', 'NPO': '🚫', 'FASTING': '⏰',
      'CRITICAL': '🚨', 'FOOD_ALLERGY': '🥜', 'KITCHEN_ALERT': '🍳', 'LATE_DELIVERY': '📦'
    };
    return map[type] || '⚠️';
  }

  getSeverityClass(severity: string): string {
    return 'alert-severity-' + severity.toLowerCase();
  }

  getAlertTypeBadge(type: string): string {
    const map: Record<string, string> = {
      'DIABETIC': 'badge-danger', 'LOW_SODIUM': 'badge-info', 'ALLERGY': 'badge-warning',
      'NPO': 'badge-secondary', 'CRITICAL': 'badge-danger', 'FOOD_ALLERGY': 'badge-warning',
      'KITCHEN_ALERT': 'badge-primary', 'LATE_DELIVERY': 'badge-info'
    };
    return map[type] || 'badge-secondary';
  }

  getStatusBadge(status: string): string {
    const map: Record<string, string> = {
      'ACTIVE': 'badge-danger', 'ACKNOWLEDGED': 'badge-warning', 'RESOLVED': 'badge-success'
    };
    return map[status] || 'badge-secondary';
  }
}