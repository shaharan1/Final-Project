import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { PatientService } from '../../../../services/patient.service';
import { BillingInvoiceService } from '../../../../services/billing/billing-invoice.service';
import { InvoiceGeneratorService } from '../../../../services/billing/invoice-generator.service';
import { AdmissionService } from '../../../../services/admission.service';
import { PatientModel } from '../../../../models/patientModel';

interface BillItem {
  id: number;
  chargeCategoryId: number;
  categoryCode: string;
  categoryName: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discountPercent: number;
  discountAmount: number;
  amount: number;
  sourceModule: string;
  sourceId: number;
  itemStatus: string;
  isNew: boolean;
}

interface InvoiceRecord {
  id: number;
  invoiceNumber: string;
  patientName: string;
  patientCode: string;
  netAmount: number;
  totalPaid: number;
  dueAmount: number;
  paymentStatus: string;
  invoiceStatus: string;
  taxRate: number;
  discountPercent: number;
  createdDate: string;
  items: any[];
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

  chargeCategories: any[] = [];
  billItems: BillItem[] = [];
  nextItemId = 1;

  billForm: any = {
    invoiceNumber: '',
    patientName: '',
    phone: '',
    address: '',
    age: null,
    gender: '',
    notes: '',
    admittedPatientId: null,
    referringDoctorId: null,
    invoiceType: 'INPATIENT'
  };

  activeInvoice: InvoiceRecord | null = null;
  invoices: InvoiceRecord[] = [];
  showInvoiceList = false;
  loadingInvoices = false;

  msg = '';
  msgType = '';
  today = new Date();
  loading = false;
  taxRate = 0;
  discountPercent = 0;

  showPaymentModal = false;
  paymentForm: any = {
    amount: 0,
    paymentMethod: 'CASH',
    transactionId: '',
    notes: '',
    processedBy: 'Admin'
  };

  dashboardSummary: any = null;
  activeAdmission: any = null;
  loadingSync = false;

  categoriesByGroup: { group: string; items: any[] }[] = [];

  constructor(
    private cdr: ChangeDetectorRef,
    private invoiceGen: InvoiceGeneratorService,
    private patientService: PatientService,
    private billingService: BillingInvoiceService,
    private admissionService: AdmissionService
  ) {}

  ngOnInit(): void {
    this.generateInvoiceNumber();
    this.loadAllPatients();
    this.loadChargeCategories();
    this.loadDashboardSummary();
  }

  loadAllPatients(): void {
    this.patientService.getAll().subscribe({
      next: (res) => { this.patients = res; this.cdr.detectChanges(); },
      error: (err) => console.log('Patient load error:', err)
    });
  }

  loadChargeCategories(): void {
    this.billingService.getChargeCategories().subscribe({
      next: (res) => {
        this.chargeCategories = res;
        this.groupCategories();
        this.cdr.detectChanges();
      },
      error: () => {
        this.chargeCategories = [];
        this.cdr.detectChanges();
      }
    });
  }

  groupCategories(): void {
    const groups: Record<string, any[]> = {
      'Ward & Room': [],
      'Medical Services': [],
      'Diagnostics': [],
      'Pharmacy': [],
      'Diet & Nutrition': [],
      'Administrative': [],
      'Other': []
    };

    for (const cat of this.chargeCategories) {
      const code = cat.code;
      if (code === 'WARD_BED' || code === 'ICU_CCU' || code === 'ROOM_SERVICE') {
        groups['Ward & Room'].push(cat);
      } else if (code === 'DOCTOR_CONSULTATION' || code === 'SURGERY' || code === 'NURSING' || code === 'PHYSIOTHERAPY' || code === 'OXYGEN' || code === 'PROCEDURE') {
        groups['Medical Services'].push(cat);
      } else if (code === 'LAB_TEST' || code === 'IMAGING' || code === 'BLOOD_BANK') {
        groups['Diagnostics'].push(cat);
      } else if (code === 'MEDICINE' || code === 'EMERGENCY') {
        groups['Pharmacy'].push(cat);
      } else if (code === 'DIET_MEALS') {
        groups['Diet & Nutrition'].push(cat);
      } else if (code === 'ADMINISTRATIVE' || code === 'ADMISSION' || code === 'DISCHARGE') {
        groups['Administrative'].push(cat);
      } else {
        groups['Other'].push(cat);
      }
    }

    this.categoriesByGroup = Object.entries(groups)
      .filter(([_, items]) => items.length > 0)
      .map(([group, items]) => ({ group, items }));
  }

  loadDashboardSummary(): void {
    this.billingService.getDashboardSummary().subscribe({
      next: (res) => { this.dashboardSummary = res; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  generateInvoiceNumber(): void {
    const date = new Date();
    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const yy = String(date.getFullYear()).slice(-2);
    const seq = String(Math.floor(Math.random() * 9000) + 1000);
    this.billForm.invoiceNumber = `INV-${dd}${mm}${yy}-${seq}`;
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
      error: () => {
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
    this.billForm.patientId = patient.id;

    if (patient.dateOfBirth) {
      const dob = new Date(patient.dateOfBirth);
      const today = new Date();
      let age = today.getFullYear() - dob.getFullYear();
      const m = today.getMonth() - dob.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) age--;
      this.billForm.age = age;
    }

    this.showPatientSearch = false;
    this.searchTerm = '';
    this.loadPatientInvoices(patient.id!);
    this.loadActiveAdmission(patient.id!);
    this.cdr.detectChanges();
  }

  loadPatientInvoices(patientId: number): void {
    this.loadingInvoices = true;
    this.billingService.getByPatientId(patientId).subscribe({
      next: (res) => {
        this.invoices = res;
        this.loadingInvoices = false;

        const draft = res.find((inv: any) => inv.invoiceStatus === 'DRAFT' || inv.invoiceStatus === 'FINALIZED');
        if (draft && !this.activeInvoice) {
          this.loadInvoice(draft);
        }

        this.cdr.detectChanges();
      },
      error: () => {
        this.invoices = [];
        this.loadingInvoices = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadActiveAdmission(patientId: number): void {
    this.admissionService.getAll().subscribe({
      next: (admissions) => {
        const admission = admissions.find((a: any) => a.patientId === patientId && a.status === 'ADMITTED');
        this.activeAdmission = admission || null;
        if (admission) {
          this.billForm.admittedPatientId = admission.admissionId;
        }
        this.cdr.detectChanges();
      },
      error: () => { this.activeAdmission = null; }
    });
  }

  syncFromModules(): void {
    if (!this.billForm.admittedPatientId) {
      this.msg = 'No active admission found for this patient';
      this.msgType = 'error';
      return;
    }
    this.loadingSync = true;
    this.billingService.syncFromModules(this.billForm.admittedPatientId).subscribe({
      next: (invoice) => {
        this.activeInvoice = invoice;
        this.billItems = (invoice.items || []).map((item: any) => ({
          id: item.id,
          chargeCategoryId: item.chargeCategoryId,
          categoryCode: item.categoryCode,
          categoryName: item.categoryName || this.getCategoryName(item.categoryCode),
          description: item.description,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
          discountPercent: item.discountPercent || 0,
          discountAmount: item.discountAmount || 0,
          amount: item.amount,
          sourceModule: item.sourceModule,
          sourceId: item.sourceId,
          itemStatus: item.itemStatus || 'ACTIVE',
          isNew: false
        }));
        this.nextItemId = this.billItems.length + 1;
        this.taxRate = invoice.taxRate || 0;
        this.discountPercent = invoice.discountPercent || 0;
        this.billForm.invoiceNumber = invoice.invoiceNumber;
        this.loadingSync = false;
        this.msg = `Synced ${this.billItems.length} items from modules`;
        this.msgType = 'success';
        if (this.selectedPatient) this.loadPatientInvoices(this.selectedPatient.id!);
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingSync = false;
        this.msg = 'Failed to sync from modules';
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  getCategoryName(code: string): string {
    const cat = this.chargeCategories.find(c => c.code === code);
    return cat ? cat.name : code;
  }

  addItem(): void {
    const defaultCat = this.chargeCategories.length > 0 ? this.chargeCategories[0] : null;
    this.billItems.push({
      id: this.nextItemId++,
      chargeCategoryId: defaultCat?.id || 0,
      categoryCode: defaultCat?.code || '',
      categoryName: defaultCat?.name || '',
      description: '',
      quantity: 1,
      unitPrice: 0,
      discountPercent: 0,
      discountAmount: 0,
      amount: 0,
      sourceModule: 'MANUAL',
      sourceId: 0,
      itemStatus: 'ACTIVE',
      isNew: true
    });
    this.cdr.detectChanges();
  }

  removeItem(id: number): void {
    const item = this.billItems.find(i => i.id === id);
    if (item && item.sourceModule !== 'MANUAL' && item.sourceId > 0) {
      if (!confirm('This item was synced from a module. Remove anyway?')) return;
    }
    this.billItems = this.billItems.filter(i => i.id !== id);
    this.cdr.detectChanges();
  }

  onCategoryChange(item: BillItem): void {
    const cat = this.chargeCategories.find(c => c.code === item.categoryCode);
    if (cat) {
      item.chargeCategoryId = cat.id;
      item.categoryName = cat.name;
    }
  }

  updateItem(item: BillItem): void {
    const qty = item.quantity || 0;
    const price = item.unitPrice || 0;
    const disc = item.discountPercent || 0;
    const base = qty * price;
    item.discountAmount = base * disc / 100;
    item.amount = base - item.discountAmount;
  }

  calculateSubtotal(): number {
    return this.billItems.reduce((sum, item) => sum + (item.amount || 0), 0);
  }

  calculateDiscount(): number {
    return this.calculateSubtotal() * ((this.discountPercent || 0) / 100);
  }

  calculateTax(): number {
    return (this.calculateSubtotal() - this.calculateDiscount()) * (this.taxRate / 100);
  }

  calculateTotal(): number {
    return this.calculateSubtotal() - this.calculateDiscount() + this.calculateTax();
  }

  calculateDue(): number {
    const paid = this.activeInvoice?.totalPaid || 0;
    return this.calculateTotal() - paid;
  }

  createInvoice(): void {
    if (!this.billForm.patientName) {
      this.msg = 'Please select or enter a patient name';
      this.msgType = 'error';
      return;
    }
    if (this.billItems.length === 0) {
      this.msg = 'Add at least one bill item';
      this.msgType = 'error';
      return;
    }

    if (this.activeInvoice && this.activeInvoice.id) {
      this.updateExistingInvoice();
      return;
    }

    if (this.billForm.admittedPatientId) {
      this.billingService.getByAdmittedPatientId(this.billForm.admittedPatientId).subscribe({
        next: (existing) => {
          const draft = existing.find((inv: any) => inv.invoiceStatus === 'DRAFT');
          if (draft) {
            this.loadInvoice(draft);
            this.msg = 'Existing draft invoice loaded: ' + draft.invoiceNumber;
            this.msgType = 'success';
            this.updateExistingInvoice();
          } else {
            this.createNewInvoice();
          }
        },
        error: () => this.createNewInvoice()
      });
    } else {
      this.createNewInvoice();
    }
  }

  private createNewInvoice(): void {
    this.loading = true;
    const payload = this.buildInvoicePayload();

    this.billingService.createInvoice(payload).subscribe({
      next: (res) => { this.onInvoiceCreated(res); },
      error: () => { this.loading = false; this.msg = 'Failed to create invoice'; this.msgType = 'error'; this.cdr.detectChanges(); }
    });
  }

  private updateExistingInvoice(): void {
    this.loading = true;
    const payload = this.buildInvoicePayload();

    this.billingService.updateInvoice(this.activeInvoice!.id, payload).subscribe({
      next: (res) => { this.onInvoiceCreated(res); },
      error: () => { this.loading = false; this.msg = 'Failed to update invoice'; this.msgType = 'error'; this.cdr.detectChanges(); }
    });
  }

  private buildInvoicePayload(): any {
    return {
      patientId: this.billForm.patientId || 0,
      admittedPatientId: this.billForm.admittedPatientId,
      referringDoctorId: this.billForm.referringDoctorId,
      invoiceType: this.billForm.invoiceType,
      taxRate: this.taxRate,
      discountPercent: this.discountPercent,
      notes: this.billForm.notes,
      preparedBy: 'Admin',
      items: this.billItems.filter(i => i.itemStatus === 'ACTIVE').map(i => ({
        chargeCategoryId: i.chargeCategoryId,
        categoryCode: i.categoryCode,
        description: i.description,
        quantity: i.quantity,
        unitPrice: i.unitPrice,
        discountPercent: i.discountPercent,
        sourceModule: i.sourceModule,
        sourceId: i.sourceId
      }))
    };
  }

  private onInvoiceCreated(invoice: any): void {
    this.activeInvoice = invoice;
    this.billForm.invoiceNumber = invoice.invoiceNumber;
    this.loading = false;
    this.msg = 'Invoice created: ' + invoice.invoiceNumber;
    this.msgType = 'success';
    if (this.selectedPatient) this.loadPatientInvoices(this.selectedPatient.id!);
    this.cdr.detectChanges();
  }

  finalizeInvoice(): void {
    if (!this.activeInvoice) return;
    this.loading = true;
    this.billingService.finalizeInvoice(this.activeInvoice.id, 'Admin').subscribe({
      next: (res) => {
        this.activeInvoice = res;
        this.loading = false;
        this.msg = 'Invoice finalized: ' + res.invoiceNumber;
        this.msgType = 'success';
        if (this.selectedPatient) this.loadPatientInvoices(this.selectedPatient.id!);
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.msg = 'Failed to finalize';
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  openPaymentModal(): void {
    if (!this.activeInvoice) {
      this.msg = 'Create or load an invoice first';
      this.msgType = 'error';
      return;
    }
    this.paymentForm = {
      amount: this.calculateDue() > 0 ? this.calculateDue() : this.calculateTotal(),
      paymentMethod: 'CASH',
      transactionId: '',
      notes: '',
      processedBy: 'Admin'
    };
    this.showPaymentModal = true;
    this.cdr.detectChanges();
  }

  onPaymentAmountChange(): void {
    this.cdr.detectChanges();
  }

  processPayment(): void {
    if (!this.activeInvoice || !this.paymentForm.amount) return;
    this.loading = true;
    const payload = {
      invoiceId: this.activeInvoice.id,
      amount: this.paymentForm.amount,
      paymentMethod: this.paymentForm.paymentMethod,
      transactionId: this.paymentForm.transactionId,
      notes: this.paymentForm.notes,
      processedBy: this.paymentForm.processedBy
    };

    this.billingService.processPayment(payload).subscribe({
      next: (res) => {
        this.showPaymentModal = false;
        this.loading = false;
        this.msg = 'Payment processed! Amount: ' + this.formatCurrency(this.paymentForm.amount);
        this.msgType = 'success';
        this.billingService.getById(this.activeInvoice!.id).subscribe({
          next: (updated) => { this.activeInvoice = updated; this.cdr.detectChanges(); }
        });
        if (this.selectedPatient) this.loadPatientInvoices(this.selectedPatient.id!);
        this.loadDashboardSummary();
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.msg = 'Payment failed';
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  loadInvoice(invoice: InvoiceRecord): void {
    this.activeInvoice = invoice;
    this.billForm.invoiceNumber = invoice.invoiceNumber;
    this.billItems = (invoice.items || []).map((item: any) => ({
      id: item.id || this.nextItemId++,
      chargeCategoryId: item.chargeCategoryId,
      categoryCode: item.categoryCode,
      categoryName: item.categoryName || this.getCategoryName(item.categoryCode),
      description: item.description,
      quantity: item.quantity,
      unitPrice: item.unitPrice,
      discountPercent: item.discountPercent || 0,
      discountAmount: item.discountAmount || 0,
      amount: item.amount,
      sourceModule: item.sourceModule || 'MANUAL',
      sourceId: item.sourceId || 0,
      itemStatus: item.itemStatus || 'ACTIVE',
      isNew: false
    }));
    this.nextItemId = this.billItems.length + 1;
    this.taxRate = invoice.taxRate || 0;
    this.discountPercent = invoice.discountPercent || 0;
    this.showInvoiceList = false;
    this.msg = 'Loaded invoice: ' + invoice.invoiceNumber;
    this.msgType = 'success';
    this.cdr.detectChanges();
  }

  generatePdf(): void {
    if (this.billItems.length === 0) {
      this.msg = 'No items to generate PDF';
      this.msgType = 'error';
      return;
    }
    this.invoiceGen.generatePdf(this.billForm, this.billItems, this.discountPercent, this.taxRate);
    this.msg = 'PDF downloaded';
    this.msgType = 'success';
    this.cdr.detectChanges();
  }

  printBill(): void {
    window.print();
  }

  resetForm(): void {
    this.billItems = [];
    this.nextItemId = 1;
    this.billForm = {
      invoiceNumber: '', patientName: '', phone: '', address: '',
      age: null, gender: '', notes: '', admittedPatientId: null,
      referringDoctorId: null, invoiceType: 'INPATIENT'
    };
    this.selectedPatient = null;
    this.activeInvoice = null;
    this.activeAdmission = null;
    this.taxRate = 0;
    this.discountPercent = 0;
    this.invoices = [];
    this.generateInvoiceNumber();
    this.msg = '';
    this.cdr.detectChanges();
  }

  toggleInvoiceList(): void {
    this.showInvoiceList = !this.showInvoiceList;
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'PAID': 'badge-success', 'COMPLETED': 'badge-success',
      'PARTIAL': 'badge-warning', 'PARTIALLY_PAID': 'badge-warning',
      'UNPAID': 'badge-danger', 'DRAFT': 'badge-secondary',
      'FINALIZED': 'badge-info', 'CANCELLED': 'badge-danger',
      'REFUNDED': 'badge-info'
    };
    return map[status] || 'badge-secondary';
  }

  formatCurrency(amount: number): string {
    return '৳' + (amount || 0).toLocaleString('en-BD', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }
}
