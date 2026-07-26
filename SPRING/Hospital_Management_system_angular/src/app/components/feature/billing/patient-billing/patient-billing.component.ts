import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

interface BillItem {
  id: number;
  category: string;
  description: string;
  qty: number;
  unitPrice: number;
  discount: number;
  amount: number;
}

interface Patient {
  id?: number;
  name: string;
  phone: string;
  address: string;
  age: number;
  gender: string;
  patientCode: string;
  bloodGroup: string;
  ward: string;
  bedNumber: string;
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
  patients: Patient[] = [];
  filteredPatients: Patient[] = [];
  searchTerm = '';
  selectedPatient: Patient | null = null;
  showPatientSearch = false;

  billItems: BillItem[] = [];
  nextItemId = 1;
  categories = ['Consultation', 'Diagnostic', 'Therapy', 'Medicine', 'Procedure', 'Room Charge', 'Surgery', 'Lab Test', 'Imaging', 'Other'];

  billForm = {
    billNumber: '',
    patientName: '',
    phone: '',
    address: '',
    age: null as number | null,
    gender: '',
    notes: ''
  };

  recentBills: RecentBill[] = [];
  showRecentBills = false;
  msg = '';
  msgType = '';
  today = new Date();
  loading = false;
  taxRate = 0.18;
  discountPercent = 0;

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.generateBillNumber();
    this.recentBills = this.getMockRecentBills();
  }

  generateBillNumber(): void {
    const date = new Date();
    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const yy = String(date.getFullYear()).slice(-2);
    const seq = String(Math.floor(Math.random() * 9000) + 1000);
    this.billForm.billNumber = `BIL-${dd}${mm}${yy}-${seq}`;
  }

  getMockRecentBills(): RecentBill[] {
    return [
      { billNumber: 'BIL-260727-1042', patientName: 'Ahmed Rahman', date: '2026-07-27', total: 12500, status: 'PAID' },
      { billNumber: 'BIL-260727-1041', patientName: 'Sara Islam', date: '2026-07-27', total: 8450, status: 'PENDING' },
      { billNumber: 'BIL-260726-2891', patientName: 'Mohammad Khan', date: '2026-07-26', total: 23100, status: 'PAID' },
      { billNumber: 'BIL-260726-2890', patientName: 'Fatima Begum', date: '2026-07-26', total: 5600, status: 'REFUNDED' },
      { billNumber: 'BIL-260725-1772', patientName: 'Hasan Talukder', date: '2026-07-25', total: 18900, status: 'PAID' },
    ];
  }

  searchPatients(): void {
    if (!this.searchTerm) { this.filteredPatients = []; return; }
    const term = this.searchTerm.toLowerCase();
    this.filteredPatients = this.patients.filter(p =>
      (p.name && p.name.toLowerCase().includes(term)) ||
      (p.patientCode && p.patientCode.toLowerCase().includes(term)) ||
      (p.phone && p.phone.includes(term))
    );
    this.showPatientSearch = true;
  }

  selectPatient(patient: Patient): void {
    this.selectedPatient = patient;
    this.billForm.patientName = patient.name;
    this.billForm.phone = patient.phone;
    this.billForm.address = patient.address;
    this.billForm.age = patient.age;
    this.billForm.gender = patient.gender;
    this.showPatientSearch = false;
    this.searchTerm = '';
    this.cdr.detectChanges();
  }

  addItem(): void {
    const newItem: BillItem = {
      id: this.nextItemId++,
      category: '',
      description: '',
      qty: 1,
      unitPrice: 0,
      discount: 0,
      amount: 0
    };
    this.billItems.push(newItem);
    this.cdr.detectChanges();
  }

  removeItem(id: number): void {
    this.billItems = this.billItems.filter(item => item.id !== id);
    this.calculateTotals();
  }

  updateItem(item: BillItem): void {
    item.amount = (item.qty * item.unitPrice) - ((item.qty * item.unitPrice * item.discount) / 100);
    this.calculateTotals();
  }

  calculateSubtotal(): number {
    return this.billItems.reduce((sum, item) => sum + item.amount, 0);
  }

  calculateDiscount(): number {
    return this.calculateSubtotal() * (this.discountPercent / 100);
  }

  calculateTax(): number {
    return (this.calculateSubtotal() - this.calculateDiscount()) * this.taxRate;
  }

  calculateTotal(): number {
    return this.calculateSubtotal() - this.calculateDiscount() + this.calculateTax();
  }

  calculateTotals(): void {
    this.billItems.forEach(item => {
      item.amount = (item.qty * item.unitPrice) - ((item.qty * item.unitPrice * item.discount) / 100);
    });
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

  saveDraft(): void {
    this.msg = 'Bill saved as draft'; this.msgType = 'success';
  }

  generateInvoice(): void {
    if (this.billItems.length === 0) {
      this.msg = 'Add at least one item to generate invoice'; this.msgType = 'error'; return;
    }
    if (!this.billForm.patientName) {
      this.msg = 'Please enter patient name'; this.msgType = 'error'; return;
    }
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
      this.msg = 'Invoice generated successfully!'; this.msgType = 'success';
    }, 500);
  }

  printBill(): void {
    window.print();
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'PAID': 'badge-success', 'PENDING': 'badge-warning',
      'REFUNDED': 'badge-info', 'CANCELLED': 'badge-danger', 'DRAFT': 'badge-secondary'
    };
    return map[status] || 'badge-secondary';
  }

  formatCurrency(amount: number): string {
    return '৳' + amount.toLocaleString('en-BD');
  }

  toggleRecentBills(): void {
    this.showRecentBills = !this.showRecentBills;
  }
}