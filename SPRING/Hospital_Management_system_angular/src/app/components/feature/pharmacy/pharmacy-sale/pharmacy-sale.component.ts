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
  loading = false;
  loadingSales = false;
  processingSale = false;
  successMessage = '';
  error = '';

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
    this.loadingSales = true;
    this.saleService.getAll().subscribe({
      next: (data: any) => {
        this.saleHistory = data;
        this.filteredSales = [...data];
        this.loadingSales = false;
      },
      error: () => {
        this.error = 'Failed to load sales history.';
        this.loadingSales = false;
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
      next: (data: any) => {
        this.searchedMedicines = data.filter((s: any) => (s.availableQuantity || s.stockQuantity || 0) > 0);
      },
      error: () => {
        this.searchedMedicines = [];
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
    this.processingSale = true;
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
      next: (saved: any) => {
        this.saleHistory.unshift(saved);
        this.filteredSales = [...this.saleHistory];
        this.resetForm();
        this.processingSale = false;
        this.successMessage = 'Sale completed successfully!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: () => {
        this.error = 'Failed to process sale. Please try again.';
        this.processingSale = false;
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
