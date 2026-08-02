import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { PatientService } from '../../../../services/patient.service';
import { PaymentService } from '../../../../services/billing/payment.service';
import { BillingDashboardService } from '../../../../services/billing/billing-dashboard.service';
import { InvoiceGeneratorService } from '../../../../services/billing/invoice-generator.service';
import { InvoiceService } from '../../../../services/billing/invoice.service';
import { DoctorChargeService } from '../../../../services/billing/doctor-charge.service';
import { AdmissionService } from '../../../../services/admission.service';
import { PharmacySaleService } from '../../../../services/pharmacy-sale.service';
import { InfrastructureService } from '../../../../services/infrastructure.service';
import { TestOrderService } from '../../../../services/test-order.service';
import { DietAssignmentService } from '../../../../services/dietary/diet-assignment.service';
import { PatientModel } from '../../../../models/patientModel';

interface BillItem {
  id: number;
  category: string;
  description: string;
  qty: number;
  unitPrice: number;
  discount: number;
  amount: number;
}

interface RecentBill {
  billNumber: string;
  patientName: string;
  date: string;
  total: number;
  status: string;
}

@Component({
  selector: 'app-patient-billing',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient-billing.component.html',
  styleUrl: './patient-billing.component.css'
})
export class PatientBillingComponent implements OnInit {
  patients: PatientModel[] = [];
  filteredPatients: PatientModel[] = [];
  searchTerm = '';
  selectedPatient: PatientModel | null = null;
  showPatientSearch = false;
  searching = false;

  billItems: BillItem[] = [];
  nextItemId = 1;
  categories = ['Consultation', 'Doctor Visit', 'Room Charge', 'Meal', 'Medicine', 'Lab Test', 'Diagnostic', 'Therapy', 'Procedure', 'Surgery', 'Imaging', 'Other'];

  billForm: any = {
    billNumber: '', patientName: '', phone: '', address: '',
    age: null, gender: '', notes: ''
  };

  recentBills: RecentBill[] = [];
  showRecentBills = false;
  msg = '';
  msgType = '';
  today = new Date();
  loading = false;
  taxRate = 0.18;
  discountPercent = 0;

  dashboardStats: any = null;

  unpaidBills: any[] = [];
  showUnpaidBills = false;
  loadingUnpaid = false;

  patientBill: any = null;
  activeAdmission: any = null;
  activeWard: any = null;
  patientAdmissions: any[] = [];
  patientDoctorCharges: any[] = [];
  patientPharmacySales: any[] = [];
  patientTestOrders: any[] = [];
  patientDietAssignments: any[] = [];
  loadingPatientBill = false;
  wardDays = 0;
  computedWardCost = 0;
  cachedWards: any[] = [];

  constructor(
    private cdr: ChangeDetectorRef,
    private invoiceGen: InvoiceGeneratorService,
    private patientService: PatientService,
    private paymentService: PaymentService,
    private dashboardService: BillingDashboardService,
    private invoiceService: InvoiceService,
    private doctorChargeService: DoctorChargeService,
    private admissionService: AdmissionService,
    private pharmacySaleService: PharmacySaleService,
    private infrastructureService: InfrastructureService,
    private testOrderService: TestOrderService,
    private dietAssignmentService: DietAssignmentService
  ) {}

  ngOnInit(): void {
    this.generateBillNumber();
    this.loadAllPatients();
    this.loadRecentBills();
    this.loadDashboardStats();
  }

  loadAllPatients(): void {
    this.patientService.getAll().subscribe({
      next: (res) => {
        this.patients = res;
        this.cdr.detectChanges();
      },
      error: (err) => console.log('Patient load error:', err)
    });
  }

  loadRecentBills(): void {
    this.paymentService.getAll().subscribe({
      next: (res) => {
        this.recentBills = res.map((p: any) => ({
          billNumber: p.paymentReference || p.invoiceNumber || '-',
          patientName: p.patientName || '-',
          date: p.paymentDate ? p.paymentDate.substring(0, 10) : '-',
          total: p.amount || 0,
          status: p.paymentStatus || 'PENDING'
        }));
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Payment load error:', err);
        this.recentBills = [];
      }
    });
  }

  loadDashboardStats(): void {
    this.paymentService.getDashboardStats().subscribe({
      next: (res) => {
        this.dashboardStats = res;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Dashboard stats error:', err);
      }
    });
  }

  generateBillNumber(): void {
    const date = new Date();
    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const yy = String(date.getFullYear()).slice(-2);
    const seq = String(Math.floor(Math.random() * 9000) + 1000);
    this.billForm.billNumber = `BIL-${dd}${mm}${yy}-${seq}`;
  }

  searchPatients(): void {
    if (!this.searchTerm) {
      this.filteredPatients = [];
      this.showPatientSearch = false;
      return;
    }
    this.searching = true;
    const term = this.searchTerm.toLowerCase();

    this.patientService.search(term).subscribe({
      next: (res) => {
        this.filteredPatients = res;
        this.showPatientSearch = true;
        this.searching = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Search error:', err);
        this.filteredPatients = this.patients.filter(p =>
          (p.name && p.name.toLowerCase().includes(term)) ||
          (p.patientCode && p.patientCode.toLowerCase().includes(term)) ||
          (p.phone && p.phone.includes(term))
        );
        this.showPatientSearch = true;
        this.searching = false;
        this.cdr.detectChanges();
      }
    });
  }

  selectPatient(patient: PatientModel): void {
    this.selectedPatient = patient;
    this.billForm.patientName = patient.name;
    this.billForm.phone = patient.phone;
    this.billForm.address = patient.address;
    this.billForm.patientCode = patient.patientCode;
    this.billForm.gender = patient.gender;

    if (patient.dateOfBirth) {
      const dob = new Date(patient.dateOfBirth);
      const today = new Date();
      let age = today.getFullYear() - dob.getFullYear();
      const m = today.getMonth() - dob.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) {
        age--;
      }
      this.billForm.age = age;
    }

    this.showPatientSearch = false;
    this.searchTerm = '';
    this.loadPatientBill(patient);
    this.loadUnpaidBills(patient);
    this.cdr.detectChanges();
  }

  loadPatientBill(patient: PatientModel): void {
    this.loadingPatientBill = true;
    this.patientBill = null;
    this.activeAdmission = null;
    this.activeWard = null;
    this.patientAdmissions = [];
    this.patientDoctorCharges = [];
    this.patientPharmacySales = [];
    this.patientTestOrders = [];
    this.patientDietAssignments = [];
    this.wardDays = 0;
    this.computedWardCost = 0;

    const wards$ = this.cachedWards.length > 0
      ? of(this.cachedWards)
      : this.infrastructureService.getAllWards();

    forkJoin({
      admissions: this.admissionService.getAll(),
      charges: this.doctorChargeService.getAll(),
      sales: this.pharmacySaleService.getAll(),
      wards: wards$,
      testOrders: this.testOrderService.getAll(),
      diets: this.dietAssignmentService.getAll()
    }).subscribe({
      next: ({ admissions, charges, sales, wards, testOrders, diets }) => {
        if (!this.cachedWards.length) this.cachedWards = wards;

        this.patientAdmissions = admissions.filter(a => a.patientId === patient.id);
        const admission = this.patientAdmissions.find(a => a.status === 'ADMITTED') || this.patientAdmissions[0];
        this.activeAdmission = admission || null;

        this.patientDoctorCharges = admission
          ? charges.filter(c => c.admittedPatientId === admission.admissionId)
          : [];
        this.patientPharmacySales = sales.filter(s => s.patientId === patient.id);
        this.patientTestOrders = testOrders.filter(t => t.patientId === patient.id);
        this.patientDietAssignments = diets.filter(d =>
          d.patientId === patient.id && d.status !== 'CANCELLED'
        );

        if (admission) {
          this.wardDays = this.computeWardDays(admission.admissionDate);
          this.activeWard = wards.find(w => w.name === admission.wardName) || null;
          this.computedWardCost = this.wardDays * (this.activeWard?.basePricePerDay || 0);

          this.invoiceService.getBillingSummary(admission.admissionId).subscribe({
            next: (summary) => {
              this.patientBill = summary;
              this.buildBillFromData();
              this.finishBillLoad();
            },
            error: () => {
              this.buildBillFromData();
              this.finishBillLoad();
            }
          });
        } else {
          this.buildBillFromData();
          this.finishBillLoad();
        }
      },
      error: () => {
        this.finishBillLoad();
      }
    });
  }

  private finishBillLoad(): void {
    this.loadingPatientBill = false;
    this.calculateTotals();
    this.cdr.detectChanges();
  }

  private computeWardDays(admissionDate: string): number {
    if (!admissionDate) return 1;
    const start = new Date(admissionDate);
    const now = new Date();
    const diff = now.getTime() - start.getTime();
    const days = Math.ceil(diff / (1000 * 60 * 60 * 24));
    return Math.max(1, days);
  }

  private computeDietDays(startDate: string, endDate: string): number {
    if (!startDate) return 1;
    const start = new Date(startDate);
    const end = endDate ? new Date(endDate) : new Date();
    const diff = end.getTime() - start.getTime();
    const days = Math.ceil(diff / (1000 * 60 * 60 * 24));
    return Math.max(1, days);
  }

  private buildBillFromData(): void {
    this.billItems = [];
    this.nextItemId = 1;

    const pushItem = (category: string, description: string, qty: number, unitPrice: number, discount = 0) => {
      if (!unitPrice || unitPrice <= 0) return;
      const amount = (qty * unitPrice) - ((qty * unitPrice * discount) / 100);
      this.billItems.push({
        id: this.nextItemId++,
        category,
        description,
        qty,
        unitPrice,
        discount,
        amount
      });
    };

    // 1. Ward / Bed charge: compute from ward basePricePerDay × days
    if (this.activeAdmission) {
      const wardCost = this.computedWardCost || this.patientBill?.wardCost || 0;
      const wardName = this.activeAdmission.wardName || 'Ward';
      const bedNum = this.activeAdmission.assignedBedNumber || '';
      if (wardCost > 0) {
        const unitPrice = this.wardDays > 0 ? Math.round(wardCost / this.wardDays) : wardCost;
        pushItem('Room Charge', `${wardName} - Bed ${bedNum} (${this.wardDays} days)`, this.wardDays, unitPrice);
      }
    }

    // 2. Doctor Visit charges
    for (const charge of this.patientDoctorCharges) {
      const visitDate = (charge.visitDate || '').substring(0, 10);
      pushItem(
        'Doctor Visit',
        `${charge.doctorName || 'Doctor'} - ${charge.description || 'Visit'} (${visitDate})`,
        1,
        charge.amount || 0
      );
    }

    // 3. Medicine (pharmacy sales items)
    for (const sale of this.patientPharmacySales) {
      const saleDate = (sale.saleDate || '').substring(0, 10);
      if (sale.items && sale.items.length > 0) {
        for (const it of sale.items) {
          pushItem(
            'Medicine',
            `${it.medicineName || 'Medicine'} (${sale.saleInvoiceNo || ''})`,
            it.quantity || 1,
            it.unitPrice || 0,
            it.discount || 0
          );
        }
      } else {
        pushItem('Medicine', `Pharmacy Sale - ${sale.saleInvoiceNo || ''} (${saleDate})`, 1, sale.netPayable || 0);
      }
    }

    // 4. Lab Tests
    for (const test of this.patientTestOrders) {
      pushItem(
        'Lab Test',
        `${test.testName || 'Test'} - ${test.testCode || ''}`,
        1,
        test.standardPrice || 0
      );
    }

    // 5. Meal / Diet assignments
    if (this.patientDietAssignments.length > 0) {
      for (const diet of this.patientDietAssignments) {
        const planName = diet.dietPlan?.name || diet.dietPlan?.dietType || 'Diet Plan';
        const start = (diet.startDate || '').substring(0, 10);
        const end = diet.endDate ? (diet.endDate || '').substring(0, 10) : 'Ongoing';
        const pricePerDay = diet.dietPlan?.pricePerDay || 0;
        const dietDays = this.computeDietDays(diet.startDate, diet.endDate);
        pushItem(
          'Meal',
          `${planName} (${start} to ${end})`,
          dietDays,
          pricePerDay
        );
      }
    } else if (this.patientBill && this.patientBill.mealCost > 0) {
      pushItem('Meal', 'Meal / Diet Charges', 1, this.patientBill.mealCost);
    }

    // 6. Other charges from sync-summary
    if (this.patientBill) {
      if (this.patientBill.testCost > 0 && this.patientTestOrders.length === 0) {
        pushItem('Lab Test', 'Diagnostic Tests (System)', 1, this.patientBill.testCost);
      }
      pushItem('Other', 'Miscellaneous Charges', 1, this.patientBill.otherCharge || 0);
    }
  }

  loadUnpaidBills(patient: PatientModel): void {
    this.loadingUnpaid = true;
    this.showUnpaidBills = true;
    this.paymentService.getUnpaidByPatientId(patient.id!).subscribe({
      next: (bills) => {
        this.unpaidBills = bills;
        this.loadingUnpaid = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.unpaidBills = [];
        this.loadingUnpaid = false;
        this.cdr.detectChanges();
      }
    });
  }

  searchUnpaidBills(): void {
    if (!this.searchTerm) return;
    this.loadingUnpaid = true;
    this.showUnpaidBills = true;
    this.paymentService.getUnpaidBySearch(this.searchTerm).subscribe({
      next: (bills) => {
        this.unpaidBills = bills;
        this.loadingUnpaid = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.unpaidBills = [];
        this.loadingUnpaid = false;
        this.cdr.detectChanges();
      }
    });
  }

  payUnpaidBill(bill: any): void {
    this.billForm.billNumber = bill.invoiceNumber;
    this.billForm.patientName = bill.patientName;
    this.billForm.phone = bill.patientContact || '';
    this.billForm.notes = bill.notes || '';
    this.discountPercent = bill.discount || 0;

    this.billItems = [{
      id: this.nextItemId++,
      category: 'Other',
      description: bill.invoiceNumber,
      qty: 1,
      unitPrice: bill.amount || 0,
      discount: bill.discount || 0,
      amount: bill.amount || 0
    }];

    this.showUnpaidBills = false;
    this.msg = 'Loaded unpaid bill: ' + bill.invoiceNumber;
    this.msgType = 'success';
    this.cdr.detectChanges();
  }

  dismissUnpaidBills(): void {
    this.showUnpaidBills = false;
    this.unpaidBills = [];
    this.cdr.detectChanges();
  }

  addItem(): void {
    this.billItems.push({
      id: this.nextItemId++,
      category: '',
      description: '',
      qty: 1,
      unitPrice: 0,
      discount: 0,
      amount: 0
    });
    this.cdr.detectChanges();
  }

  removeItem(id: number): void {
    this.billItems = this.billItems.filter(item => item.id !== id);
    this.calculateTotals();
  }

  updateItem(item: BillItem): void {
    if (item.qty == null) item.qty = 0;
    if (item.unitPrice == null) item.unitPrice = 0;
    if (item.discount == null) item.discount = 0;
    item.amount = (item.qty * item.unitPrice) - ((item.qty * item.unitPrice * item.discount) / 100);
    this.calculateTotals();
  }

  calculateSubtotal(): number {
    return this.billItems.reduce((sum, item) => sum + (item.amount || 0), 0);
  }

  calculateDiscount(): number {
    return this.calculateSubtotal() * ((this.discountPercent || 0) / 100);
  }

  calculateTax(): number {
    return (this.calculateSubtotal() - this.calculateDiscount()) * this.taxRate;
  }

  calculateTotal(): number {
    return this.calculateSubtotal() - this.calculateDiscount() + this.calculateTax();
  }

  calculateTotals(): void {
    this.billItems.forEach(item => this.updateItem(item));
    this.cdr.detectChanges();
  }

  resetForm(): void {
    this.billItems = [];
    this.nextItemId = 1;
    this.billForm = {
      billNumber: '', patientName: '', phone: '', address: '',
      age: null, gender: '', notes: ''
    };
    this.selectedPatient = null;
    this.discountPercent = 0;
    this.patientBill = null;
    this.activeAdmission = null;
    this.activeWard = null;
    this.patientAdmissions = [];
    this.patientDoctorCharges = [];
    this.patientPharmacySales = [];
    this.patientTestOrders = [];
    this.patientDietAssignments = [];
    this.wardDays = 0;
    this.computedWardCost = 0;
    this.generateBillNumber();
    this.msg = '';
    this.cdr.detectChanges();
  }

  loadRecentBill(bill: RecentBill): void {
    this.billForm.billNumber = bill.billNumber;
    this.billForm.patientName = bill.patientName;
    this.msg = 'Loaded bill: ' + bill.billNumber;
    this.msgType = 'success';
    this.generateBillNumber();
    this.cdr.detectChanges();
  }

  saveDraft(): void {
    if (!this.billForm.patientName) {
      this.msg = 'Please enter patient name';
      this.msgType = 'error';
      return;
    }
    this.loading = true;
    const paymentData = {
      invoiceNumber: this.billForm.billNumber,
      patientId: this.selectedPatient?.id || 0,
      patientName: this.billForm.patientName,
      amount: this.calculateTotal(),
      paymentMethod: 'CASH',
      transactionId: '',
      notes: this.billForm.notes || ''
    };

    this.paymentService.processPayment(paymentData).subscribe({
      next: (res) => {
        if (res.id) {
          this.paymentService.updateStatus(res.id, 'PENDING').subscribe();
        }
        this.invoiceGen.generatePdf(this.billForm, this.billItems, this.discountPercent, this.taxRate);
        this.msg = 'Bill saved as draft! ' + this.billForm.billNumber;
        this.msgType = 'success';
        this.loading = false;
        this.loadRecentBills();
        this.loadDashboardStats();
        if (this.selectedPatient) {
          this.loadUnpaidBills(this.selectedPatient);
        }
        this.resetForm();
        this.cdr.detectChanges();
      },
      error: () => {
        this.invoiceGen.generatePdf(this.billForm, this.billItems, this.discountPercent, this.taxRate);
        this.msg = 'Draft saved locally (API unavailable)';
        this.msgType = 'success';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  generateInvoice(): void {
    if (this.billItems.length === 0) {
      this.msg = 'Add at least one item to generate invoice';
      this.msgType = 'error';
      return;
    }
    if (!this.billForm.patientName) {
      this.msg = 'Please enter patient name';
      this.msgType = 'error';
      return;
    }
    this.loading = true;

    const paymentData = {
      invoiceNumber: this.billForm.billNumber,
      patientId: this.selectedPatient?.id || 0,
      patientName: this.billForm.patientName,
      amount: this.calculateTotal(),
      paymentMethod: 'CASH',
      transactionId: '',
      notes: this.billForm.notes || ''
    };

    this.paymentService.processPayment(paymentData).subscribe({
      next: (res) => {
        this.invoiceGen.generatePdf(this.billForm, this.billItems, this.discountPercent, this.taxRate);
        this.msg = 'Invoice generated & payment recorded! Bill #' + this.billForm.billNumber;
        this.msgType = 'success';
        this.loading = false;
        this.loadRecentBills();
        this.loadDashboardStats();
        this.resetForm();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Payment error:', err);
        this.invoiceGen.generatePdf(this.billForm, this.billItems, this.discountPercent, this.taxRate);
        this.msg = 'Invoice PDF downloaded (payment API unavailable)';
        this.msgType = 'success';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  printBill(): void {
    window.print();
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'PAID': 'badge-success', 'COMPLETED': 'badge-success',
      'PENDING': 'badge-warning', 'PENDING_VERIFICATION': 'badge-warning',
      'REFUNDED': 'badge-info', 'CANCELLED': 'badge-danger', 'DRAFT': 'badge-secondary'
    };
    return map[status] || 'badge-secondary';
  }

  formatCurrency(amount: number): string {
    return '৳' + (amount || 0).toLocaleString('en-BD');
  }

  toggleRecentBills(): void {
    this.showRecentBills = !this.showRecentBills;
  }
}
