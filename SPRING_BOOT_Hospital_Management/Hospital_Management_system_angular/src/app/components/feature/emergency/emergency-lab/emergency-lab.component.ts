import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmergencyLabOrderService } from '../../../../services/emergency/emergency-lab-order.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyPatient } from '../../../../models/emergency/emergency-patient.model';
import { EmergencyLabOrder } from '../../../../models/emergency/emergency-lab-order.model';

@Component({
  selector: 'app-emergency-lab',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-lab.component.html',
  styleUrls: ['./emergency-lab.component.css']
})
export class EmergencyLabComponent implements OnInit {
  patients: EmergencyPatient[] = [];
  orders: EmergencyLabOrder[] = [];
  criticalOrders: EmergencyLabOrder[] = [];
  selectedPatient: EmergencyPatient | null = null;
  activeTab = 'lab';
  loading = false;
  msg = '';
  msgType = '';

  orderForm = {
    testName: '',
    orderType: 'LAB',
    priority: 'ROUTINE',
    orderedBy: '',
    notes: ''
  };

  labTests = ['CBC', 'BLOOD_GROUP', 'CROSS_MATCH', 'BLOOD_SUGAR', 'TROPONIN', 'ELECTROLYTES', 'URINALYSIS'];
  radiologyTests = ['ECG', 'CT_SCAN', 'MRI', 'X_RAY', 'ULTRASOUND'];
  priorities = ['STAT', 'URGENT', 'ROUTINE'];

  quickTests = [
    { name: 'CBC', type: 'LAB', icon: '🩸', priority: 'URGENT' },
    { name: 'BLOOD_SUGAR', type: 'LAB', icon: '🔬', priority: 'STAT' },
    { name: 'ECG', type: 'RADIOLOGY', icon: '💓', priority: 'URGENT' },
    { name: 'X_RAY', type: 'RADIOLOGY', icon: '🦴', priority: 'ROUTINE' },
    { name: 'CT_SCAN', type: 'RADIOLOGY', icon: '🧠', priority: 'URGENT' },
    { name: 'TROPONIN', type: 'LAB', icon: '❤️', priority: 'STAT' }
  ];

  constructor(
    private labService: EmergencyLabOrderService,
    private patientService: EmergencyPatientService
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    this.loadOrders();
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data: EmergencyPatient[]) => this.patients = data,
      error: () => this.showMessage('Failed to load patients', 'error')
    });
  }

  loadOrders(): void {
    this.labService.getAll().subscribe({
      next: (data: EmergencyLabOrder[]) => {
        this.orders = data;
        this.getCriticalOrders();
      },
      error: () => this.showMessage('Failed to load orders', 'error')
    });
  }

  selectPatient(patient: EmergencyPatient): void {
    this.selectedPatient = patient;
    this.loadOrders();
  }

  orderTest(): void {
    if (!this.selectedPatient) {
      this.showMessage('Please select a patient first', 'error');
      return;
    }
    this.loading = true;
    const availableTests = this.orderForm.orderType === 'LAB' ? this.labTests : this.radiologyTests;
    if (!availableTests.includes(this.orderForm.testName)) {
      this.showMessage('Invalid test for selected order type', 'error');
      this.loading = false;
      return;
    }
    this.labService.create({ ...this.orderForm, emergencyPatientId: this.selectedPatient.id } as EmergencyLabOrder).subscribe({
      next: () => {
        this.showMessage('Test ordered successfully', 'success');
        this.resetForm();
        this.loadOrders();
        this.loading = false;
      },
      error: () => {
        this.showMessage('Failed to order test', 'error');
        this.loading = false;
      }
    });
  }

  updateResult(id: number, result: string): void {
    this.labService.updateResult(id, result).subscribe({
      next: () => {
        this.showMessage('Result updated', 'success');
        this.loadOrders();
      },
      error: () => this.showMessage('Failed to update result', 'error')
    });
  }

  updateStatus(id: number, status: string): void {
    this.labService.updateStatus(id, status).subscribe({
      next: () => {
        this.showMessage('Status updated', 'success');
        this.loadOrders();
      },
      error: () => this.showMessage('Failed to update status', 'error')
    });
  }

  getCriticalOrders(): void {
    this.criticalOrders = this.orders.filter(o => o.status === 'CRITICAL');
  }

  prefillTest(test: { name: string; type: string; icon: string; priority: string }): void {
    this.orderForm.testName = test.name;
    this.orderForm.orderType = test.type;
    this.orderForm.priority = test.priority;
  }

  resetForm(): void {
    this.orderForm = {
      testName: '',
      orderType: 'LAB',
      priority: 'ROUTINE',
      orderedBy: '',
      notes: ''
    };
  }

  showMessage(msg: string, type: string): void {
    this.msg = msg;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; }, 4000);
  }

  getPatientOrders(): EmergencyLabOrder[] {
    if (!this.selectedPatient) return [];
    return this.orders.filter(o => o.emergencyPatientId === this.selectedPatient?.id);
  }

  getLabOrders(): EmergencyLabOrder[] {
    return this.getPatientOrders().filter(o => o.orderType === 'LAB');
  }

  getRadiologyOrders(): EmergencyLabOrder[] {
    return this.getPatientOrders().filter(o => o.orderType === 'RADIOLOGY');
  }

  getStatusSteps(): string[] {
    return ['ORDERED', 'SAMPLE_COLLECTED', 'PROCESSING', 'COMPLETED'];
  }

  isStepCompleted(order: EmergencyLabOrder, step: string): boolean {
    const steps = this.getStatusSteps();
    const currentIdx = steps.indexOf(order.status || '');
    const stepIdx = steps.indexOf(step);
    if (order.status === 'CRITICAL') return stepIdx < steps.length;
    return stepIdx <= currentIdx;
  }

  isCurrentStep(order: EmergencyLabOrder, step: string): boolean {
    return order.status === step;
  }

  formatTime(t: string): string {
    if (!t) return '';
    return new Date(t).toLocaleString('en-US', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
  }
}
