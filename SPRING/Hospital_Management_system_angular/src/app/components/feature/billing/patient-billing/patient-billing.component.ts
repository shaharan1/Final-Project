import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PaymentService } from '../../../../services/billing/payment.service';
import { BillingDashboardService } from '../../../../services/billing/billing-dashboard.service';

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
  billFilterDate = '';

  showBillDetailModal = false;
  selectedBill: Bill | null = null;
  showConfirmModal = false;
  confirmAction = '';

  successMessage = '';
  errorMessage = '';

  constructor(
    private paymentService: PaymentService,
    private billingDashboardService: BillingDashboardService,
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
        this.recentBills = this.getMockBills();
        this.filteredBills = [...this.recentBills];
        this.loadingBills = false;
        this.cdr.detectChanges();
      }
    });
  }

  private mapToBill(p: any): Bill {
    return {
      id: p.id,
      invoiceNumber: p.invoiceNumber || p.id,
      patientId: p.patientId || '',
      patientName: p.patientName || p.patient?.name || 'Unknown',
      patientPhone: p.patientPhone || p.patient?.phone || '',
      patientAddress: p.patientAddress || '',
      patientAge: p.patientAge || '',
      patientGender: p.patientGender || '',
      items: p.items || [],
      subtotal: p.subtotal || p.totalAmount || 0,
      discount: p.discount || 0,
      vat: p.vat || 0,
      serviceCharge: p.serviceCharge || 0,
      insuranceCoverage: p.insuranceCoverage || 0,
      advancePayment: p.advancePayment || 0,
      netPayable: p.netPayable || p.totalAmount || 0,
      dueAmount: p.dueAmount || 0,
      status: p.paymentStatus || p.status || 'Pending',
      billDate: p.billDate || p.paymentDate || '',
      paymentMethod: p.paymentMethod || 'Cash',
      notes: p.notes || ''
    };
  }

  private getMockBills(): Bill[] {
    return [
      {
        id: 1, invoiceNumber: 'INV-20260720-0001', patientId: 'P-1001',
        patientName: 'Rahim Uddin', patientPhone: '+8801712345678',
        patientAddress: 'Dhaka', patientAge: '35', patientGender: 'Male',
        items: [
          { id: 1, category: 'Registration', description: 'OPD Registration', quantity: 1, unitPrice: 500, discount: 0, amount: 500 },
          { id: 2, category: 'Consultation', description: 'General Checkup', quantity: 1, unitPrice: 1500, discount: 100, amount: 1400 }
        ],
        subtotal: 1900, discount: 100, vat: 324, serviceCharge: 100,
        insuranceCoverage: 500, advancePayment: 500,
        netPayable: 1724, dueAmount: 724, status: 'Partial',
        billDate: '2026-07-20', paymentMethod: 'Cash', notes: ''
      },
      {
        id: 2, invoiceNumber: 'INV-20260721-0002', patientId: 'P-1002',
        patientName: 'Fatima Khan', patientPhone: '+8801812345679',
        patientAddress: 'Chittagong', patientAge: '28', patientGender: 'Female',
        items: [
          { id: 1, category: 'Admission', description: 'General Ward 3 Days', quantity: 3, unitPrice: 2000, discount: 0, amount: 6000 },
          { id: 2, category: 'Doctor Visit', description: 'Daily Visit', quantity: 3, unitPrice: 800, discount: 0, amount: 2400 }
        ],
        subtotal: 8400, discount: 0, vat: 1512, serviceCharge: 200,
        insuranceCoverage: 0, advancePayment: 5000,
        netPayable: 10112, dueAmount: 5112, status: 'Pending',
        billDate: '2026-07-21', paymentMethod: 'Card', notes: ''
      },
      {
        id: 3, invoiceNumber: 'INV-20260722-0003', patientId: 'P-1003',
        patientName: 'Karim Hassan', patientPhone: '+8801912345670',
        patientAddress: 'Sylhet', patientAge: '52', patientGender: 'Male',
        items: [
          { id: 1, category: 'Operation', description: 'Appendectomy', quantity: 1, unitPrice: 25000, discount: 2000, amount: 23000 },
          { id: 2, category: 'Bed', description: 'ICU Bed 2 Days', quantity: 2, unitPrice: 5000, discount: 0, amount: 10000 }
        ],
        subtotal: 33000, discount: 2000, vat: 5580, serviceCharge: 500,
        insuranceCoverage: 10000, advancePayment: 15000,
        netPayable: 27080, dueAmount: 2080, status: 'Paid',
        billDate: '2026-07-22', paymentMethod: 'Mobile', notes: ''
      },
      {
        id: 4, invoiceNumber: 'INV-20260723-0004', patientId: 'P-1004',
        patientName: 'Sumaiya Akter', patientPhone: '+8801612345671',
        patientAddress: 'Rajshahi', patientAge: '41', patientGender: 'Female',
        items: [
          { id: 1, category: 'Laboratory', description: 'Blood Test Panel', quantity: 1, unitPrice: 2500, discount: 0, amount: 2500 },
          { id: 2, category: 'Radiology', description: 'Chest X-Ray', quantity: 1, unitPrice: 1200, discount: 0, amount: 1200 }
        ],
        subtotal: 3700, discount: 200, vat: 630, serviceCharge: 100,
        insuranceCoverage: 1000, advancePayment: 2000,
        netPayable: 3430, dueAmount: 430, status: 'Pending',
        billDate: '2026-07-23', paymentMethod: 'Cash', notes: ''
      }
    ];
  }

  searchPatient(): void {
    if (this.searchQuery.length < 2) {
      this.searchResults = [];
      this.showSearchResults = false;
      return;
    }
    this.paymentService.getAll().subscribe({
      next: (data: any) => {
        const list = Array.isArray(data) ? data : [];
        const q = this.searchQuery.toLowerCase();
        this.searchResults = list.filter((p: any) => {
          if (this.searchType === 'name') return (p.patientName || '').toLowerCase().includes(q);
          if (this.searchType === 'phone') return (p.patientPhone || '').toLowerCase().includes(q);
          return String(p.patientId || '').toLowerCase().includes(q);
        }).slice(0, 8);
        this.showSearchResults = this.searchResults.length > 0;
        this.cdr.detectChanges();
      },
      error: () => { this.showSearchResults = false; }
    });
  }

  selectPatient(patient: any): void {
    this.selectedPatient = patient;
    this.bill.patientId = patient.patientId || patient.id || '';
    this.bill.patientName = patient.patientName || patient.patient?.name || '';
    this.bill.patientPhone = patient.patientPhone || patient.patient?.phone || '';
    this.bill.patientAddress = patient.patientAddress || '';
    this.bill.patientAge = patient.patientAge || '';
    this.bill.patientGender = patient.patientGender || '';
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

  getSubtotal(): number {
    return this.bill.items.reduce((sum, i) => sum + (i.quantity * i.unitPrice), 0);
  }

  getItemsDiscountTotal(): number {
    return this.bill.items.reduce((sum, i) => sum + i.discount, 0);
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
  }

  saveDraft(): void {
    if (!this.bill.patientName.trim() || this.bill.items.length === 0) {
      this.errorMessage = 'Please add patient info and at least one bill item.';
      setTimeout(() => this.errorMessage = '', 3000);
      return;
    }
    this.bill.invoiceNumber = this.generateInvoiceNumber();
    this.confirmAction = 'draft';
    this.showConfirmModal = true;
  }

  executeConfirmAction(): void {
    this.showConfirmModal = false;
    this.processingBill = true;

    const payload = {
      invoiceNumber: this.bill.invoiceNumber,
      patientId: this.bill.patientId,
      patientName: this.bill.patientName,
      patientPhone: this.bill.patientPhone,
      patientAddress: this.bill.patientAddress,
      patientAge: this.bill.patientAge,
      patientGender: this.bill.patientGender,
      items: this.bill.items,
      subtotal: this.bill.subtotal,
      discount: this.bill.discount,
      vat: this.bill.vat,
      serviceCharge: this.bill.serviceCharge,
      insuranceCoverage: this.bill.insuranceCoverage,
      advancePayment: this.bill.advancePayment,
      netPayable: this.bill.netPayable,
      dueAmount: this.bill.dueAmount,
      status: this.confirmAction === 'draft' ? 'Draft' : 'Pending',
      paymentMethod: this.bill.paymentMethod,
      notes: this.bill.notes,
      billDate: this.bill.billDate,
      paymentDate: new Date().toISOString()
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
        setTimeout(() => this.successMessage = '', 3000);
        this.activeTab = 'history';
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to save bill. Please try again.';
        this.processingBill = false;
        setTimeout(() => this.errorMessage = '', 3000);
        this.cdr.detectChanges();
      }
    });
  }

  closeConfirmModal(): void {
    this.showConfirmModal = false;
    this.confirmAction = '';
  }

  printBill(): void {
    window.print();
  }

  resetBillForm(): void {
    this.bill = this.getEmptyBill();
    this.bill.billDate = new Date().toISOString().split('T')[0];
    this.selectedPatient = null;
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
  }

  onFilterStatusChange(): void {
    this.filterBills();
  }

  viewBillDetail(bill: Bill): void {
    this.selectedBill = bill;
    this.showBillDetailModal = true;
  }

  closeBillDetailModal(): void {
    this.showBillDetailModal = false;
    this.selectedBill = null;
  }

  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'paid': return 'badge-paid';
      case 'pending': return 'badge-pending';
      case 'partial': return 'badge-partial';
      case 'draft': return 'badge-draft';
      case 'overdue': return 'badge-overdue';
      default: return 'badge-pending';
    }
  }
}
