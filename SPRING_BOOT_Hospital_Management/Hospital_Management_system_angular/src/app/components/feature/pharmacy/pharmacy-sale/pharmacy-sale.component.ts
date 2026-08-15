import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PharmacySaleService } from '../../../../services/pharmacy-sale.service';
import { StockService } from '../../../../services/stock.service';
import { PrescriptionService } from '../../../../services/prescription.service';
import { PharmacySaleModel, PharmacySaleItemModel } from '../../../../models/pharmacy-sale.model';
import { MedicineStockModel } from '../../../../models/medicine-stock.model';
import { PrescriptionModel, PrescriptionItemModel } from '../../../../models/prescriptionModel';

interface DispenseLine {
  medicineName: string;
  dosage: string;
  duration: string;
  instruction: string;
  stockId: number;
  stockName: string;
  batchNumber: string;
  quantity: number;
  unitPrice: number;
  options: MedicineStockModel[];
}

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

  showPrescriptionModal: boolean = false;
  pendingPrescriptions: PrescriptionModel[] = [];
  loadingPrescriptions: boolean = false;
  selectedPrescription: PrescriptionModel | null = null;
  dispenseLines: DispenseLine[] = [];
  activePrescriptionId: number | null = null;
  activePrescriptionNo: string = '';

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
    private stockService: StockService,
    private prescriptionService: PrescriptionService,
    private cdr: ChangeDetectorRef
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
    this.subtotal = this.cartItems.reduce((sum: number, i: PharmacySaleItemModel) => sum + (i.quantity * (i.unitPrice || 0)), 0);
    this.vat = Math.round(this.subtotal * 0.5 * 100) / 100;
    this.netPayable = this.subtotal + this.vat - this.discount;
    this.changeAmount = this.paidAmount - this.netPayable;
    if (this.changeAmount < 0) this.changeAmount = 0;
  }

  openPrescriptionModal(): void {
    this.showPrescriptionModal = true;
    this.loadingPrescriptions = true;
    this.pendingPrescriptions = [];
    this.prescriptionService.getPending().subscribe({
      next: (data: PrescriptionModel[]) => {
        this.pendingPrescriptions = data;
        this.loadingPrescriptions = false;
      },
      error: () => {
        this.loadingPrescriptions = false;
        this.error = 'Failed to load pending prescriptions.';
      }
    });
  }

  closePrescriptionModal(): void {
    this.showPrescriptionModal = false;
    this.selectedPrescription = null;
    this.dispenseLines = [];
  }

  selectPrescription(rx: PrescriptionModel): void {
    this.selectedPrescription = rx;
    this.dispenseLines = (rx.prescriptionItems || []).map((item: PrescriptionItemModel) => ({
      medicineName: item.medicineName || '',
      dosage: item.dosage || '',
      duration: item.duration || '',
      instruction: item.instruction || '',
      stockId: 0,
      stockName: '',
      batchNumber: '',
      quantity: 1,
      unitPrice: 0,
      options: []
    } as DispenseLine));
    this.autoMatchDispenseLines();
  }

  private autoMatchDispenseLines(): void {
    this.stockService.getAll().subscribe({
      next: (allStock: MedicineStockModel[]) => {
        const available = allStock.filter(s => (s.availableQuantity ?? s.stockQuantity ?? 0) > 0);
        for (const line of this.dispenseLines) {
          const match = available.find(s => s.medicineName.toLowerCase() === line.medicineName.toLowerCase());
          if (match) {
            line.stockId = match.id!;
            line.stockName = match.medicineName;
            line.batchNumber = match.batchNumber;
            line.unitPrice = match.salePrice;
          }
        }
      },
      error: () => {}
    });
  }

  searchStockForLine(line: DispenseLine, term: string): void {
    if (term.length < 2) { line.options = []; return; }
    this.stockService.search(term).subscribe({
      next: (data: MedicineStockModel[]) => {
        line.options = data.filter(s => (s.availableQuantity ?? s.stockQuantity ?? 0) > 0);
      },
      error: () => { line.options = []; }
    });
  }

  chooseStockForLine(line: DispenseLine, stock: MedicineStockModel): void {
    line.stockId = stock.id!;
    line.stockName = stock.medicineName;
    line.batchNumber = stock.batchNumber;
    line.unitPrice = stock.salePrice;
    line.options = [];
  }

  get dispenseReady(): boolean {
    return this.dispenseLines.length > 0 && this.dispenseLines.every(l => l.stockId > 0 && l.quantity > 0);
  }

  addDispenseToCart(): void {
    if (!this.dispenseReady) return;
    if (this.selectedPrescription) {
      this.patientName = this.selectedPrescription.patientName || '';
      this.patientPhone = this.selectedPrescription.patientPhone || '';
      this.patientType = 'OUTPATIENT';
      this.doctorName = this.selectedPrescription.doctorName || '';
    }
    for (const line of this.dispenseLines) {
      const existing = this.cartItems.find(c => c.medicineStockId === line.stockId);
      if (existing) {
        existing.quantity += line.quantity;
        existing.subtotal = existing.quantity * (existing.unitPrice || 0) - (existing.discount || 0);
      } else {
        this.cartItems.push({
          medicineStockId: line.stockId,
          medicineName: line.stockName,
          batchNumber: line.batchNumber,
          quantity: line.quantity,
          unitPrice: line.unitPrice,
          discount: 0,
          subtotal: line.quantity * line.unitPrice
        });
      }
    }
    if (this.selectedPrescription?.id) {
      this.activePrescriptionId = this.selectedPrescription.id;
      this.activePrescriptionNo = this.selectedPrescription.prescriptionNumber || '';
    }
    this.closePrescriptionModal();
    this.calculateTotals();
  }

  clearActivePrescription(): void {
    this.activePrescriptionId = null;
    this.activePrescriptionNo = '';
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
      prescriptionId: this.activePrescriptionId ?? undefined,
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
    this.activePrescriptionId = null;
    this.activePrescriptionNo = '';
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
    const s = (status || '').toUpperCase();
    return s === 'PAID' ? 'badge-paid' : s === 'PENDING_BILLING' || s === 'PENDING' ? 'badge-pending' : 'badge-partial';
  }
}
