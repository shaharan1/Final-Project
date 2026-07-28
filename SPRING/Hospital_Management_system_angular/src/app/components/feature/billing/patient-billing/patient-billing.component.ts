import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PatientService } from '../../../../services/patient.service';
import { PaymentService } from '../../../../services/billing/payment.service';
import { BillingDashboardService } from '../../../../services/billing/billing-dashboard.service';
import { InvoiceGeneratorService } from '../../../../services/billing/invoice-generator.service';
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
  categories = ['Consultation', 'Diagnostic', 'Therapy', 'Medicine', 'Procedure', 'Room Charge', 'Surgery', 'Lab Test', 'Imaging', 'Other'];

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

  constructor(
    private cdr: ChangeDetectorRef,
    private invoiceGen: InvoiceGeneratorService,
    private patientService: PatientService,
    private paymentService: PaymentService,
    private dashboardService: BillingDashboardService
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
    const invoiceData = {
      patientName: this.billForm.patientName,
      patientContact: this.billForm.phone,
      amount: this.calculateSubtotal(),
      discount: this.calculateDiscount(),
      totalAmount: this.calculateTotal(),
      totalDiscount: this.calculateDiscount(),
      payable: this.calculateTotal(),
      received: 0,
      due: this.calculateTotal(),
      preparedBy: this.billForm.notes || '',
      doctorId: null
    };
    this.invoiceGen.generatePdf(this.billForm, this.billItems, this.discountPercent, this.taxRate);
    this.msg = 'Bill saved as draft!';
    this.msgType = 'success';
    this.loading = false;
    this.cdr.detectChanges();
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
