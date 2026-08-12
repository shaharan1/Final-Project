import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmergencyBillingService } from '../../../../services/emergency/emergency-billing.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyBilling } from '../../../../models/emergency/emergency-billing.model';
import { EmergencyPatient } from '../../../../models/emergency/emergency-patient.model';

@Component({
  selector: 'app-emergency-billing',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-billing.component.html',
  styleUrls: ['./emergency-billing.component.css']
})
export class EmergencyBillingComponent implements OnInit {
  patients: EmergencyPatient[] = [];
  bills: EmergencyBilling[] = [];
  selectedPatient: EmergencyPatient | null = null;
  loading = false;
  msg = '';
  msgType = '';

  billForm = {
    registrationFee: 0,
    consultationFee: 0,
    bedCharge: 0,
    medicineCharge: 0,
    labCharge: 0,
    radiologyCharge: 0,
    procedureCharge: 0,
    operationCharge: 0,
    ambulanceCharge: 0,
    consumablesCharge: 0,
    doctorFee: 0,
    nursingCharge: 0,
    otherCharges: 0,
    discountPercent: 0,
    vatPercent: 18,
    insuranceCoverage: 0,
    advancePaid: 0,
    insuranceProvider: '',
    insurancePolicyNumber: '',
    isInsuranceClaimed: false,
    notes: ''
  };

  constructor(
    private billingService: EmergencyBillingService,
    private patientService: EmergencyPatientService
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    this.loadBills();
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data: EmergencyPatient[]) => this.patients = data,
      error: () => this.showMessage('Failed to load patients', 'error')
    });
  }

  loadBills(): void {
    this.billingService.getAll().subscribe({
      next: (data: EmergencyBilling[]) => this.bills = data,
      error: () => this.showMessage('Failed to load bills', 'error')
    });
  }

  selectPatient(patient: EmergencyPatient): void {
    this.selectedPatient = patient;
    this.loadBills();
  }

  generateBill(): void {
    if (!this.selectedPatient) {
      this.showMessage('Please select a patient first', 'error');
      return;
    }
    this.loading = true;
    const payload = {
      emergencyPatientId: this.selectedPatient.id,
      registrationFee: this.billForm.registrationFee,
      consultationFee: this.billForm.consultationFee,
      bedCharge: this.billForm.bedCharge,
      medicineCharge: this.billForm.medicineCharge,
      labCharge: this.billForm.labCharge,
      radiologyCharge: this.billForm.radiologyCharge,
      procedureCharge: this.billForm.procedureCharge,
      operationCharge: this.billForm.operationCharge,
      ambulanceCharge: this.billForm.ambulanceCharge,
      consumablesCharge: this.billForm.consumablesCharge,
      doctorFee: this.billForm.doctorFee,
      nursingCharge: this.billForm.nursingCharge,
      otherCharges: this.billForm.otherCharges,
      discountPercent: this.billForm.discountPercent,
      vatPercent: this.billForm.vatPercent,
      insuranceCoverage: this.billForm.insuranceCoverage,
      advancePaid: this.billForm.advancePaid,
      insuranceProvider: this.billForm.insuranceProvider,
      insurancePolicyNumber: this.billForm.insurancePolicyNumber,
      isInsuranceClaimed: this.billForm.isInsuranceClaimed,
      notes: this.billForm.notes
    };
    this.billingService.create(payload as any).subscribe({
      next: () => {
        this.showMessage('Bill generated successfully', 'success');
        this.resetForm();
        this.loadBills();
        this.loading = false;
      },
      error: () => {
        this.showMessage('Failed to generate bill', 'error');
        this.loading = false;
      }
    });
  }

  updatePaymentStatus(id: number, status: string): void {
    this.billingService.updatePaymentStatus(id, status).subscribe({
      next: () => {
        this.showMessage('Payment status updated', 'success');
        this.loadBills();
      },
      error: () => this.showMessage('Failed to update status', 'error')
    });
  }

  processInsuranceClaim(id: number): void {
    this.billingService.updatePaymentStatus(id, 'PAID').subscribe({
      next: () => {
        this.showMessage('Insurance claim processed', 'success');
        this.loadBills();
      },
      error: () => this.showMessage('Failed to process claim', 'error')
    });
  }

  getSubtotal(): number {
    return (
      this.billForm.registrationFee +
      this.billForm.consultationFee +
      this.billForm.bedCharge +
      this.billForm.medicineCharge +
      this.billForm.labCharge +
      this.billForm.radiologyCharge +
      this.billForm.procedureCharge +
      this.billForm.operationCharge +
      this.billForm.ambulanceCharge +
      this.billForm.consumablesCharge +
      this.billForm.doctorFee +
      this.billForm.nursingCharge +
      this.billForm.otherCharges
    );
  }

  getDiscountAmount(): number {
    return this.getSubtotal() * (this.billForm.discountPercent / 100);
  }

  getVatAmount(): number {
    return (this.getSubtotal() - this.getDiscountAmount()) * (this.billForm.vatPercent / 100);
  }

  getGrandTotal(): number {
    return this.getSubtotal() - this.getDiscountAmount() + this.getVatAmount() - this.billForm.insuranceCoverage - this.billForm.advancePaid;
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
  }

  getTodayRevenue(): number {
    const today = new Date().toDateString();
    return this.bills
      .filter(b => new Date(b.createdAt || '').toDateString() === today && b.paymentStatus === 'PAID')
      .reduce((sum, b) => sum + (b.grandTotal || 0), 0);
  }

  resetForm(): void {
    this.billForm = {
      registrationFee: 0, consultationFee: 0, bedCharge: 0,
      medicineCharge: 0, labCharge: 0, radiologyCharge: 0,
      procedureCharge: 0, operationCharge: 0, ambulanceCharge: 0,
      consumablesCharge: 0, doctorFee: 0, nursingCharge: 0,
      otherCharges: 0, discountPercent: 0, vatPercent: 18,
      insuranceCoverage: 0, advancePaid: 0, insuranceProvider: '',
      insurancePolicyNumber: '', isInsuranceClaimed: false, notes: ''
    };
  }

  showMessage(msg: string, type: string): void {
    this.msg = msg;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; }, 4000);
  }

  getPatientBills(): EmergencyBilling[] {
    if (!this.selectedPatient) return [];
    return this.bills.filter(b => b.emergencyPatientId === this.selectedPatient?.id);
  }
}
