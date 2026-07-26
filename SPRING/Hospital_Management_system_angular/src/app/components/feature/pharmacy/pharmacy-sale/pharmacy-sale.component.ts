import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PharmacySaleService } from '../../../../services/pharmacy-sale.service';
import { StockService } from '../../../../services/stock.service';
import { PharmacySaleModel, PharmacySaleItemModel } from '../../../../models/pharmacy-sale.model';
import { MedicineStockModel } from '../../../../models/medicine-stock.model';

@Component({
  selector: 'app-pharmacy-sale',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './pharmacy-sale.component.html',
  styleUrls: ['./pharmacy-sale.component.css']
})
export class PharmacySaleComponent implements OnInit {
  saleHistory: PharmacySaleModel[] = [];
  filteredSales: PharmacySaleModel[] = [];
  searchedMedicines: MedicineStockModel[] = [];
  cartItems: PharmacySaleItemModel[] = [];

  searchTerm: string = '';
  medicineSearchTerm: string = '';
  activeTab: 'pos' | 'history' = 'pos';
  showSaleDetailModal: boolean = false;
  selectedSale: PharmacySaleModel | null = null;
  lowStockWarning: string = '';

  patientType: string = 'OUTPATIENT';
  patientName: string = '';
  patientPhone: string = '';
  doctorName: string = '';

  subtotal: number = 0;
  discount: number = 0;
  vat: number = 0;
  netPayable: number = 0;
  paidAmount: number = 0;
  changeAmount: number = 0;
  paymentMethod: string = 'Cash';

  constructor(
    private saleService: PharmacySaleService,
    private stockService: StockService
  ) {}

  ngOnInit(): void {
    this.loadSalesHistory();
  }

  loadSalesHistory(): void {
    this.saleService.getAll().subscribe({
      next: (data) => { this.saleHistory = data; this.filteredSales = [...data]; },
      error: () => {
        this.saleHistory = [
          { id: 1, saleInvoiceNo: 'SAL-2024-001', patientType: 'OUTPATIENT', patientName: 'Kamal Ahmed', patientPhone: '+880-1711-111111', doctorName: 'Dr. Rahman', totalAmount: 850, discount: 50, vat: 144, netPayable: 944, paidAmount: 1000, changeAmount: 56, paymentMethod: 'Cash', paymentStatus: 'Paid', saleDate: '2024-12-20' },
          { id: 2, saleInvoiceNo: 'SAL-2024-002', patientType: 'INPATIENT', patientName: 'Fatima Begum', patientPhone: '+880-1812-222222', doctorName: 'Dr. Khan', totalAmount: 1200, discount: 0, vat: 216, netPayable: 1416, paidAmount: 1416, changeAmount: 0, paymentMethod: 'Card', paymentStatus: 'Paid', saleDate: '2024-12-20' },
          { id: 3, saleInvoiceNo: 'SAL-2024-003', patientType: 'OUTPATIENT', patientName: 'Rahim Uddin', patientPhone: '+880-1913-333333', doctorName: 'Dr. Hassan', totalAmount: 650, discount: 100, vat: 100, netPayable: 650, paidAmount: 650, changeAmount: 0, paymentMethod: 'Mobile', paymentStatus: 'Paid', saleDate: '2024-12-19' },
        ];
        this.filteredSales = [...this.saleHistory];
      }
    });
  }

  filterSales(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredSales = this.saleHistory.filter(s =>
      (s.saleInvoiceNo || '').toLowerCase().includes(term) ||
      (s.patientName || '').toLowerCase().includes(term) ||
      (s.paymentMethod || '').toLowerCase().includes(term)
    );
  }

  searchMedicines(): void {
    this.lowStockWarning = '';
    if (this.medicineSearchTerm.length < 2) { this.searchedMedicines = []; return; }
    this.stockService.search(this.medicineSearchTerm).subscribe({
      next: (data) => {
        this.searchedMedicines = data.filter(s => (s.availableQuantity || 0) > 0);
      },
      error: () => {
        this.searchedMedicines = [
          { id: 1, medicineName: 'Paracetamol 500mg', genericName: 'Paracetamol', strength: '500mg', dosageForm: 'Tablet', batchNumber: 'BAT-001', stockQuantity: 500, availableQuantity: 500, purchasePrice: 8, salePrice: 12, manufacturingDate: '2024-01-01', expiryDate: '2026-01-01', supplierId: 1 },
          { id: 2, medicineName: 'Amoxicillin 250mg', genericName: 'Amoxicillin', strength: '250mg', dosageForm: 'Capsule', batchNumber: 'BAT-002', stockQuantity: 8, availableQuantity: 8, purchasePrice: 15, salePrice: 22, minimumStockLevel: 20, manufacturingDate: '2024-03-01', expiryDate: '2025-12-01', supplierId: 1, lowStock: true },
          { id: 3, medicineName: 'Omeprazole 20mg', genericName: 'Omeprazole', strength: '20mg', dosageForm: 'Capsule', batchNumber: 'BAT-003', stockQuantity: 300, availableQuantity: 300, purchasePrice: 12, salePrice: 18, manufacturingDate: '2024-02-15', expiryDate: '2026-02-15', supplierId: 2 },
          { id: 4, medicineName: 'Cetirizine 10mg', genericName: 'Cetirizine', strength: '10mg', dosageForm: 'Tablet', batchNumber: 'BAT-004', stockQuantity: 5, availableQuantity: 5, purchasePrice: 6, salePrice: 10, minimumStockLevel: 15, manufacturingDate: '2024-04-01', expiryDate: '2025-11-01', supplierId: 3, lowStock: true },
        ].filter(s => s.medicineName.toLowerCase().includes(this.medicineSearchTerm.toLowerCase()));
      }
    });
  }

  addToCart(medicine: MedicineStockModel): void {
    this.lowStockWarning = '';
    const existing = this.cartItems.find(c => c.medicineStockId === medicine.id);
    if (existing) {
      existing.quantity++;
      existing.subtotal = existing.quantity * (existing.unitPrice || 0) - (existing.discount || 0);
    } else {
      this.cartItems.push({
        medicineStockId: medicine.id!,
        medicineName: medicine.medicineName,
        batchNumber: medicine.batchNumber,
        quantity: 1,
        unitPrice: medicine.salePrice,
        discount: 0,
        subtotal: medicine.salePrice
      });
    }
    if (medicine.lowStock || (medicine.minimumStockLevel && (medicine.availableQuantity || 0) <= medicine.minimumStockLevel)) {
      this.lowStockWarning = `Low stock warning: Only ${medicine.availableQuantity} units left for ${medicine.medicineName}`;
    }
    this.medicineSearchTerm = '';
    this.searchedMedicines = [];
    this.calculateTotals();
  }

  updateCartItemQty(item: PharmacySaleItemModel): void {
    item.subtotal = item.quantity * (item.unitPrice || 0) - (item.discount || 0);
    this.calculateTotals();
  }

  removeCartItem(index: number): void {
    this.cartItems.splice(index, 1);
    this.calculateTotals();
  }

  calculateTotals(): void {
    this.subtotal = this.cartItems.reduce((sum, i) => sum + (i.quantity * (i.unitPrice || 0)), 0);
    this.vat = Math.round(this.subtotal * 0.18 * 100) / 100;
    this.netPayable = this.subtotal + this.vat - this.discount;
    this.changeAmount = this.paidAmount - this.netPayable;
    if (this.changeAmount < 0) this.changeAmount = 0;
  }

  onDiscountChange(): void {
    this.calculateTotals();
  }

  onPaidChange(): void {
    this.changeAmount = Math.max(0, this.paidAmount - this.netPayable);
  }

  selectPayment(method: string): void {
    this.paymentMethod = method;
  }

  confirmSale(): void {
    if (this.cartItems.length === 0 || this.paidAmount < this.netPayable) return;
    const sale: PharmacySaleModel = {
      patientType: this.patientType,
      patientName: this.patientName,
      patientPhone: this.patientPhone,
      doctorName: this.doctorName,
      totalAmount: this.subtotal,
      discount: this.discount,
      vat: this.vat,
      netPayable: this.netPayable,
      paidAmount: this.paidAmount,
      changeAmount: this.changeAmount,
      paymentMethod: this.paymentMethod,
      paymentStatus: 'Paid',
      saleType: 'PHARMACY',
      saleDate: new Date().toISOString().split('T')[0],
      items: this.cartItems
    };
    this.saleService.processSale(sale).subscribe({
      next: (saved) => {
        this.saleHistory.unshift(saved);
        this.filteredSales = [...this.saleHistory];
        this.resetForm();
      },
      error: () => {
        sale.id = this.saleHistory.length + 1;
        sale.saleInvoiceNo = 'SAL-' + Date.now();
        this.saleHistory.unshift(sale);
        this.filteredSales = [...this.saleHistory];
        this.resetForm();
      }
    });
  }

  resetForm(): void {
    this.cartItems = [];
    this.patientType = 'OUTPATIENT';
    this.patientName = '';
    this.patientPhone = '';
    this.doctorName = '';
    this.subtotal = 0;
    this.discount = 0;
    this.vat = 0;
    this.netPayable = 0;
    this.paidAmount = 0;
    this.changeAmount = 0;
    this.paymentMethod = 'Cash';
    this.lowStockWarning = '';
  }

  viewSaleDetail(sale: PharmacySaleModel): void {
    this.selectedSale = sale;
    this.showSaleDetailModal = true;
  }

  closeSaleDetailModal(): void {
    this.showSaleDetailModal = false;
    this.selectedSale = null;
  }

  getStatusClass(status: string): string {
    return status === 'Paid' ? 'badge-paid' : status === 'Pending' ? 'badge-pending' : 'badge-partial';
  }
}
