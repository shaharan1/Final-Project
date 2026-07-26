import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SupplierService } from '../../../../services/supplier.service';
import { StockService } from '../../../../services/stock.service';
import { PurchasePharmacyService } from '../../../../services/purchase-pharmacy.service';
import { SupplierModel } from '../../../../models/supplier.model';
import { MedicineStockModel } from '../../../../models/medicine-stock.model';
import { PurchaseModel, PurchaseItemModel } from '../../../../models/purchase.model';

@Component({
  selector: 'app-purchase-order',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './purchase-order.component.html',
  styleUrls: ['./purchase-order.component.css']
})
export class PurchaseOrderComponent implements OnInit {
  purchases: PurchaseModel[] = [];
  filteredPurchases: PurchaseModel[] = [];
  suppliers: SupplierModel[] = [];
  stockItems: MedicineStockModel[] = [];
  searchedMedicines: MedicineStockModel[] = [];

  searchTerm: string = '';
  showCreateModal: boolean = false;
  showDetailModal: boolean = false;
  showDeleteModal: boolean = false;
  purchaseToDelete: PurchaseModel | null = null;
  selectedPurchase: PurchaseModel | null = null;

  currentStep: number = 1;
  medicineSearchTerm: string = '';
  showMedicineDropdown: boolean = false;
  purchaseForm: Partial<PurchaseModel> = this.getEmptyForm();
  newItem: PurchaseItemModel = this.getEmptyItem();

  getEmptyForm(): Partial<PurchaseModel> {
    return {
      supplierId: 0, supplierName: '', invoiceNo: '', purchaseDate: '',
      totalAmount: 0, vat: 0, discount: 0, netAmount: 0,
      paidAmount: 0, dueAmount: 0, status: 'Pending',
      paymentMethod: 'Cash', notes: '', items: []
    };
  }

  getEmptyItem(): PurchaseItemModel {
    return { stockId: 0, medicineName: '', batchNumber: '', quantity: 1, unitPrice: 0, discount: 0, subtotal: 0 };
  }

  get totalPurchases(): number { return this.purchases.length; }
  get thisMonthPurchases(): number {
    const now = new Date();
    return this.purchases.filter(p =>
      p.purchaseDate && new Date(p.purchaseDate).getMonth() === now.getMonth() &&
      new Date(p.purchaseDate).getFullYear() === now.getFullYear()
    ).length;
  }
  get totalAmount(): number { return this.purchases.reduce((sum, p) => sum + (p.totalAmount || 0), 0); }
  get pendingDues(): number {
    return this.purchases
      .filter(p => p.status === 'Pending' || p.status === 'Partial')
      .reduce((sum, p) => sum + (p.dueAmount || 0), 0);
  }

  loading = false;
  error = '';
  creating = false;

  constructor(
    private purchaseService: PurchasePharmacyService,
    private supplierService: SupplierService,
    private stockService: StockService
  ) {}

  ngOnInit(): void {
    this.loadPurchases();
    this.loadSuppliers();
    this.loadStock();
  }

  loadPurchases(): void {
    this.loading = true;
    this.error = '';
    this.purchaseService.getAll().subscribe({
      next: (data: any) => {
        this.purchases = data;
        this.filteredPurchases = [...data];
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load purchases.';
        this.loading = false;
      }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAll().subscribe({
      next: (data: any) => this.suppliers = data,
      error: () => {}
    });
  }

  loadStock(): void {
    this.stockService.getAll().subscribe({
      next: (data: any) => this.stockItems = data,
      error: () => {}
    });
  }

  filterPurchases(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredPurchases = this.purchases.filter(p =>
      (p.invoiceNo || '').toLowerCase().includes(term) ||
      (p.supplierName || '').toLowerCase().includes(term) ||
      (p.status || '').toLowerCase().includes(term)
    );
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Paid': return 'badge-paid';
      case 'Partial': return 'badge-partial';
      case 'Pending': return 'badge-pending';
      default: return 'badge-pending';
    }
  }

  openCreateModal(): void {
    this.purchaseForm = this.getEmptyForm();
    this.currentStep = 1;
    this.medicineSearchTerm = '';
    this.searchedMedicines = [];
    this.showMedicineDropdown = false;
    this.newItem = this.getEmptyItem();
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  selectSupplier(supplier: SupplierModel): void {
    this.purchaseForm.supplierId = supplier.id!;
    this.purchaseForm.supplierName = supplier.name;
    this.currentStep = 2;
  }

  searchMedicines(): void {
    if (this.medicineSearchTerm.length < 2) {
      this.searchedMedicines = [];
      this.showMedicineDropdown = false;
      return;
    }
    this.stockService.search(this.medicineSearchTerm).subscribe({
      next: (data: any) => {
        this.searchedMedicines = data;
        this.showMedicineDropdown = data.length > 0;
      },
      error: () => {
        this.searchedMedicines = this.stockItems.filter(s =>
          s.medicineName.toLowerCase().includes(this.medicineSearchTerm.toLowerCase())
        );
        this.showMedicineDropdown = this.searchedMedicines.length > 0;
      }
    });
  }

  addMedicineToItem(medicine: MedicineStockModel): void {
    this.newItem.stockId = medicine.id!;
    this.newItem.medicineName = medicine.medicineName;
    this.newItem.batchNumber = medicine.batchNumber;
    this.newItem.unitPrice = medicine.purchasePrice;
    this.newItem.medicineName = medicine.medicineName;
    this.updateItemSubtotal();
    this.searchedMedicines = [];
    this.showMedicineDropdown = false;
    this.medicineSearchTerm = medicine.medicineName;
  }

  updateItemSubtotal(): void {
    this.newItem.subtotal = (this.newItem.quantity * this.newItem.unitPrice) - (this.newItem.discount || 0);
  }

  addItem(): void {
    if (this.newItem.stockId === 0 || this.newItem.quantity <= 0) return;
    if (!this.purchaseForm.items) this.purchaseForm.items = [];
    this.purchaseForm.items.push({ ...this.newItem });
    this.newItem = this.getEmptyItem();
    this.medicineSearchTerm = '';
    this.calculateTotals();
  }

  removeItem(index: number): void {
    this.purchaseForm.items!.splice(index, 1);
    this.calculateTotals();
  }

  calculateTotals(): void {
    const items = this.purchaseForm.items || [];
    this.purchaseForm.totalAmount = items.reduce((sum, i) => sum + (i.quantity * i.unitPrice), 0);
    const discount = this.purchaseForm.discount || 0;
    const vat = this.purchaseForm.totalAmount * 0.18;
    this.purchaseForm.vat = Math.round(vat * 100) / 100;
    this.purchaseForm.netAmount = this.purchaseForm.totalAmount + vat - discount;
    this.purchaseForm.dueAmount = Math.max(0, (this.purchaseForm.netAmount || 0) - (this.purchaseForm.paidAmount || 0));
  }

  goToStep(step: number): void {
    if (step === 2 && this.purchaseForm.supplierId === 0) return;
    if (step === 3 && (!this.purchaseForm.items || this.purchaseForm.items.length === 0)) return;
    this.currentStep = step;
    if (step === 3) this.calculateTotals();
  }

  confirmPurchase(): void {
    this.creating = true;
    const purchase: PurchaseModel = {
      supplierId: this.purchaseForm.supplierId!,
      supplierName: this.purchaseForm.supplierName,
      invoiceNo: this.purchaseForm.invoiceNo || 'INV-' + Date.now(),
      purchaseDate: new Date().toISOString().split('T')[0],
      totalAmount: this.purchaseForm.totalAmount,
      vat: this.purchaseForm.vat,
      discount: this.purchaseForm.discount,
      netAmount: this.purchaseForm.netAmount,
      paidAmount: this.purchaseForm.paidAmount,
      dueAmount: this.purchaseForm.dueAmount,
      status: (this.purchaseForm.paidAmount || 0) >= (this.purchaseForm.netAmount || 0) ? 'Paid'
        : (this.purchaseForm.paidAmount || 0) > 0 ? 'Partial' : 'Pending',
      paymentMethod: this.purchaseForm.paymentMethod,
      notes: this.purchaseForm.notes,
      items: this.purchaseForm.items
    };
    this.purchaseService.create(purchase).subscribe({
      next: (saved: PurchaseModel) => {
        this.purchases.unshift(saved);
        this.filterPurchases();
        this.closeCreateModal();
        this.creating = false;
      },
      error: () => {
        this.error = 'Failed to create purchase order.';
        this.creating = false;
      }
    });
  }

  viewPurchase(purchase: PurchaseModel): void {
    this.selectedPurchase = purchase;
    this.showDetailModal = true;
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedPurchase = null;
  }

  confirmDelete(purchase: PurchaseModel): void {
    this.purchaseToDelete = purchase;
    this.showDeleteModal = true;
  }

  deletePurchase(): void {
    if (this.purchaseToDelete) {
      this.purchaseService.delete(this.purchaseToDelete.id!).subscribe({
        next: () => {
          this.purchases = this.purchases.filter(p => p.id !== this.purchaseToDelete!.id);
          this.filterPurchases();
        },
        error: () => {
          this.purchases = this.purchases.filter(p => p.id !== this.purchaseToDelete!.id);
          this.filterPurchases();
        }
      });
    }
    this.showDeleteModal = false;
    this.purchaseToDelete = null;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.purchaseToDelete = null;
  }

  trackById(index: number, item: PurchaseModel): number {
    return item.id || index;
  }
}
