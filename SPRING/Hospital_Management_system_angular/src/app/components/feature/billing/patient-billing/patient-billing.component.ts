import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PaymentService } from '../../../../services/billing/payment.service';
import { PatientService } from '../../../../services/patient.service';

export interface BillItem {
  id: number;
  category: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discount: number;
  amount: number;
}

export interface Bill {
  id?: number;
  invoiceNumber: string;
  patientId: string;
  patientName: string;
  patientPhone: string;
  patientAddress: string;
  patientAge: string;
  patientGender: string;
  items: BillItem[];
  subtotal: number;
  discount: number;
  vat: number;
  serviceCharge: number;
  insuranceCoverage: number;
  advancePayment: number;
  netPayable: number;
  dueAmount: number;
  status: string;
  billDate: string;
  paymentMethod: string;
  notes: string;
}

@Component({
  selector: 'app-patient-billing',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient-billing.component.html',
  styleUrls: ['./patient-billing.component.css']
})
export class PatientBillingComponent implements OnInit {
  activeTab: 'create' | 'history' = 'create';
  loading = false;
  loadingBills = false;
  processingBill = false;

  searchQuery = '';
  searchType: 'name' | 'phone' | 'id' = 'name';
  searchResults: any[] = [];
  showSearchResults = false;
  selectedPatient: any = null;

  bill = this.getEmptyBill();

  categories: string[] = [
    'Registration', 'Consultation', 'Admission', 'Bed',
    'Doctor Visit', 'Nursing', 'Operation', 'ICU',
    'Oxygen', 'Laboratory', 'Radiology', 'Pharmacy',
    'Ambulance', 'Food', 'Other'
  ];

  recentBills: Bill[] = [];
  filteredBills: Bill[] = [];
  billSearchTerm = '';
  billFilterStatus = 'all';

  showBillDetailModal = false;
  selectedBill: Bill | null = null;
  showConfirmModal = false;
  confirmAction = '';

  successMessage = '';
  errorMessage = '';

  private searchDebounce: any;

  constructor(
    private paymentService: PaymentService,
    private patientService: PatientService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadRecentBills();
  }

  getEmptyBill(): Bill {
    return {
      invoiceNumber: '',
      patientId: '',
      patientName: '',
      patientPhone: '',
      patientAddress: '',
      patientAge: '',
      patientGender: '',
      items: [],
      subtotal: 0,
      discount: 0,
      vat: 0,
      serviceCharge: 0,
      insuranceCoverage: 0,
      advancePayment: 0,
      netPayable: 0,
      dueAmount: 0,
      status: 'Pending',
      billDate: new Date().toISOString().split('T')[0],
      paymentMethod: 'Cash',
      notes: ''
    };
  }

  generateInvoiceNumber(): string {
    const now = new Date();
    const y = now.getFullYear();
    const m = String(now.getMonth() + 1).padStart(2, '0');
    const d = String(now.getDate()).padStart(2, '0');
    const r = String(Math.floor(Math.random() * 9000) + 1000);
    return 'INV-' + y + m + d + '-' + r;
  }

  loadRecentBills(): void {
    this.loadingBills = true;
    this.paymentService.getAll().subscribe({
      next: (data: any) => {
        const list = Array.isArray(data) ? data : [];
        this.recentBills = list.map((p: any) => this.mapToBill(p));
        this.filteredBills = [...this.recentBills];
        this.loadingBills = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.recentBills = [];
        this.filteredBills = [];
        this.loadingBills = false;
        this.cdr.detectChanges();
      }
    });
  }

  private mapToBill(p: any): Bill {
    return {
      id: p.id,
      invoiceNumber: p.paymentReference || p.invoiceNumber || ('PAY-' + p.id),
      patientId: p.patientId || '',
      patientName: p.patientName || 'Unknown',
      patientPhone: '',
      patientAddress: '',
      patientAge: '',
      patientGender: '',
      items: [],
      subtotal: p.amount || 0,
      discount: p.discount || 0,
      vat: p.vat || 0,
      serviceCharge: 0,
      insuranceCoverage: p.insuranceCoverage || 0,
      advancePayment: 0,
      netPayable: p.netAmount || p.amount || 0,
      dueAmount: p.dueAmount || 0,
      status: p.paymentStatus || 'Pending',
      billDate: p.paymentDate || p.createdDate || '',
      paymentMethod: p.paymentMethod || 'Cash',
      notes: p.notes || ''
    };
  }

  searchPatient(): void {
    clearTimeout(this.searchDebounce);
    if (this.searchQuery.length < 2) {
      this.searchResults = [];
      this.showSearchResults = false;
      return;
    }
    this.searchDebounce = setTimeout(() => {
      this.patientService.search(this.searchQuery).subscribe({
        next: (patients: any[]) => {
          this.searchResults = (patients || []).slice(0, 8);
          this.showSearchResults = this.searchResults.length > 0;
          this.cdr.detectChanges();
        },
        error: () => {
          this.searchResults = [];
          this.showSearchResults = false;
          this.cdr.detectChanges();
        }
      });
    }, 300);
  }

  selectPatient(patient: any): void {
    this.selectedPatient = patient;
    this.bill.patientId = patient.id || patient.patientCode || '';
    this.bill.patientName = patient.name || patient.patientName || '';
    this.bill.patientPhone = patient.phone || patient.phoneNumber || '';
    this.bill.patientAddress = patient.address || '';
    this.bill.patientAge = patient.age || '';
    this.bill.patientGender = patient.gender || '';
    this.showSearchResults = false;
    this.cdr.detectChanges();
  }

  clearPatientSelection(): void {
    this.selectedPatient = null;
    this.bill.patientId = '';
    this.bill.patientName = '';
    this.bill.patientPhone = '';
    this.bill.patientAddress = '';
    this.bill.patientAge = '';
    this.bill.patientGender = '';
    this.cdr.detectChanges();
  }

  addBillItem(): void {
    const nextId = this.bill.items.length > 0
      ? Math.max(...this.bill.items.map(i => i.id)) + 1
      : 1;
    this.bill.items.push({
      id: nextId,
      category: 'Registration',
      description: '',
      quantity: 1,
      unitPrice: 0,
      discount: 0,
      amount: 0
    });
    this.recalculate();
  }

  removeBillItem(index: number): void {
    this.bill.items.splice(index, 1);
    this.recalculate();
  }

  updateItemAmount(item: BillItem): void {
    item.amount = (item.quantity * item.unitPrice) - item.discount;
    if (item.amount < 0) item.amount = 0;
    this.recalculate();
  }

  recalculate(): void {
    const itemsSubtotal = this.bill.items.reduce((sum, i) => sum + (i.quantity * i.unitPrice), 0);
    const itemsDiscount = this.bill.items.reduce((sum, i) => sum + i.discount, 0);
    const totalDiscount = itemsDiscount + this.bill.discount;
    const afterDiscount = Math.max(0, itemsSubtotal - totalDiscount);
    this.bill.subtotal = itemsSubtotal;
    this.bill.vat = Math.round(afterDiscount * 0.18 * 100) / 100;
    if (this.bill.vat < 0) this.bill.vat = 0;
    this.bill.netPayable = afterDiscount + this.bill.vat + this.bill.serviceCharge - this.bill.insuranceCoverage - this.bill.advancePayment;
    this.bill.dueAmount = Math.max(0, this.bill.netPayable);
    this.cdr.detectChanges();
  }

  onSummaryChange(): void {
    this.recalculate();
  }

  canGenerate(): boolean {
    return this.bill.patientName.trim().length > 0 &&
           this.bill.items.length > 0 &&
           this.bill.items.every(i => i.description.trim().length > 0 && i.quantity > 0) &&
           !this.processingBill;
  }

  generateInvoice(): void {
    if (!this.canGenerate()) return;
    this.bill.invoiceNumber = this.generateInvoiceNumber();
    this.confirmAction = 'generate';
    this.showConfirmModal = true;
    this.cdr.detectChanges();
  }

  saveDraft(): void {
    if (!this.bill.patientName.trim() || this.bill.items.length === 0) {
      this.errorMessage = 'Please add patient info and at least one bill item.';
      setTimeout(() => { this.errorMessage = ''; this.cdr.detectChanges(); }, 3000);
      this.cdr.detectChanges();
      return;
    }
    this.bill.invoiceNumber = this.generateInvoiceNumber();
    this.confirmAction = 'draft';
    this.showConfirmModal = true;
    this.cdr.detectChanges();
  }

  executeConfirmAction(): void {
    this.showConfirmModal = false;
    this.processingBill = true;
    this.cdr.detectChanges();

    const payload = {
      invoiceNumber: this.bill.invoiceNumber,
      patientId: this.bill.patientId ? Number(this.bill.patientId) : 0,
      patientName: this.bill.patientName,
      amount: this.bill.netPayable,
      paymentMethod: this.bill.paymentMethod.toUpperCase(),
      discount: this.bill.discount,
      VAT: this.bill.vat,
      notes: this.bill.notes || (this.confirmAction === 'draft' ? 'Draft bill' : ''),
      processedBy: 'BillingClerk'
    };

    this.paymentService.processPayment(payload).subscribe({
      next: (saved: any) => {
        const mapped = this.mapToBill(saved);
        this.recentBills.unshift(mapped);
        this.filteredBills = [...this.recentBills];
        this.processingBill = false;
        this.bill = this.getEmptyBill();
        this.bill.billDate = new Date().toISOString().split('T')[0];
        this.selectedPatient = null;
        this.successMessage = this.confirmAction === 'draft'
          ? 'Bill saved as draft successfully!'
          : 'Invoice generated successfully!';
        setTimeout(() => { this.successMessage = ''; this.cdr.detectChanges(); }, 3000);
        this.activeTab = 'history';
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to save bill. Please try again.';
        this.processingBill = false;
        setTimeout(() => { this.errorMessage = ''; this.cdr.detectChanges(); }, 3000);
        this.cdr.detectChanges();
      }
    });
  }

  closeConfirmModal(): void {
    this.showConfirmModal = false;
    this.confirmAction = '';
    this.cdr.detectChanges();
  }

  printBill(): void {
    window.print();
  }

  resetBillForm(): void {
    this.bill = this.getEmptyBill();
    this.bill.billDate = new Date().toISOString().split('T')[0];
    this.selectedPatient = null;
    this.cdr.detectChanges();
  }

  filterBills(): void {
    const term = this.billSearchTerm.toLowerCase();
    this.filteredBills = this.recentBills.filter(b => {
      const matchesSearch = (b.invoiceNumber || '').toLowerCase().includes(term) ||
        (b.patientName || '').toLowerCase().includes(term) ||
        (b.patientId || '').toLowerCase().includes(term);
      const matchesStatus = this.billFilterStatus === 'all' ||
        b.status.toLowerCase() === this.billFilterStatus.toLowerCase();
      return matchesSearch && matchesStatus;
    });
    this.cdr.detectChanges();
  }

  viewBillDetail(bill: Bill): void {
    this.selectedBill = bill;
    this.showBillDetailModal = true;
    this.cdr.detectChanges();
  }

  closeBillDetailModal(): void {
    this.showBillDetailModal = false;
    this.selectedBill = null;
    this.cdr.detectChanges();
  }

  deleteBill(bill: Bill): void {
    if (!bill.id) return;
    if (!confirm('Delete this bill?')) return;
    this.paymentService.deletePayment(bill.id).subscribe({
      next: () => {
        this.recentBills = this.recentBills.filter(b => b.id !== bill.id);
        this.filteredBills = [...this.recentBills];
        this.showBillDetailModal = false;
        this.selectedBill = null;
        this.successMessage = 'Bill deleted successfully!';
        setTimeout(() => { this.successMessage = ''; this.cdr.detectChanges(); }, 3000);
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to delete bill.';
        setTimeout(() => { this.errorMessage = ''; this.cdr.detectChanges(); }, 3000);
        this.cdr.detectChanges();
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'completed': return 'badge-paid';
      case 'paid': return 'badge-paid';
      case 'pending': return 'badge-pending';
      case 'partial': return 'badge-partial';
      case 'draft': return 'badge-draft';
      case 'failed': return 'badge-overdue';
      default: return 'badge-pending';
    }
  }
}
