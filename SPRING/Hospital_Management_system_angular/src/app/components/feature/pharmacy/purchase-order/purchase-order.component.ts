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
  medicineSearchTerm = '';

  searchTerm = '';
  showCreateModal = false;
  showDetailModal = false;
  showDeleteModal = false;
  showMedicineDropdown = false;
  currentStep = 1;
  creating = false;
  loading = false;
  error = '';

  purchaseForm: PurchaseModel = this.getEmptyForm();
  newItem: PurchaseItemModel = this.getEmptyItem();
  selectedPurchase: PurchaseModel | null = null;
  purchaseToDelete: PurchaseModel | null = null;
  processingId: number | null = null;

  getEmptyForm(): PurchaseModel {
    return {
      supplierId: 0, supplierName: '', items: [],
      totalAmount: 0, vat: 0, discount: 0, netAmount: 0,
      paidAmount: 0, dueAmount: 0, paymentMethod: 'Cash', notes: ''
    };
  }

  getEmptyItem(): PurchaseItemModel {
    return { stockId: 0, medicineName: '', batchNumber: '', quantity: 1, unitPrice: 0, discount: 0, subtotal: 0 };
  }

  get totalPurchases(): number { return this.purchases.length; }
  get totalAmount(): number { return this.purchases.reduce((sum: number, p: PurchaseModel) => sum + (p.totalAmount ?? 0), 0); }
  get pendingDues(): number { return this.purchases.reduce((sum: number, p: PurchaseModel) => sum + (p.dueAmount ?? 0), 0); }

  constructor(
    private supplierService: SupplierService,
    private stockService: StockService,
    private purchaseService: PurchasePharmacyService
  ) {}

  ngOnInit(): void {
    this.loadPurchases();
    this.loadSuppliers();
    this.loadStock();
  }

  loadPurchases(): void {
    this.loading = true;
    this.purchaseService.getAll().subscribe({
      next: (data: PurchaseModel[]) => {
        this.purchases = data;
        this.filterPurchases();
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAll().subscribe({
      next: (data: SupplierModel[]) => { this.suppliers = data; },
      error: () => {}
    });
  }

  loadStock(): void {
    this.stockService.getAll().subscribe({
      next: (data: MedicineStockModel[]) => { this.stockItems = data; },
      error: () => {}
    });
  }

  filterPurchases(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredPurchases = this.purchases.filter(p =>
      (p.invoiceNo ?? '').toLowerCase().includes(term) ||
      (p.supplierName ?? '').toLowerCase().includes(term)
    );
  }

  searchMedicines(): void {
    this.showMedicineDropdown = this.medicineSearchTerm.length >= 2;
    if (this.medicineSearchTerm.length < 2) { this.searchedMedicines = []; return; }
    this.stockService.search(this.medicineSearchTerm).subscribe({
      next: (data: MedicineStockModel[]) => {
        this.searchedMedicines = data;
        this.showMedicineDropdown = data.length > 0;
      },
      error: () => {
        this.searchedMedicines = this.stockItems.filter((s: MedicineStockModel) =>
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
    this.updateItemSubtotal();
    this.searchedMedicines = [];
    this.showMedicineDropdown = false;
    this.medicineSearchTerm = medicine.medicineName;
  }

  updateItemSubtotal(): void {
    this.newItem.subtotal = (this.newItem.quantity * this.newItem.unitPrice) - (this.newItem.discount ?? 0);
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
    this.purchaseForm.totalAmount = items.reduce((sum: number, i: PurchaseItemModel) => sum + (i.quantity * i.unitPrice) - (i.discount ?? 0), 0);
    const discount = this.purchaseForm.discount ?? 0;
    const vat = (this.purchaseForm.totalAmount ?? 0) * 0.18;
    this.purchaseForm.vat = Math.round(vat * 100) / 100;
    this.purchaseForm.netAmount = (this.purchaseForm.totalAmount ?? 0) + vat - discount;
    this.purchaseForm.dueAmount = Math.max(0, (this.purchaseForm.netAmount ?? 0) - (this.purchaseForm.paidAmount ?? 0));
  }

  goToStep(step: number): void {
    if (step === 2 && this.purchaseForm.supplierId === 0) return;
    if (step === 3 && (!this.purchaseForm.items || this.purchaseForm.items.length === 0)) return;
    this.currentStep = step;
    if (step === 3) this.calculateTotals();
  }

  selectSupplier(supplier: SupplierModel): void {
    this.purchaseForm.supplierId = supplier.id!;
    this.purchaseForm.supplierName = supplier.name;
  }

  openCreateModal(): void {
    this.purchaseForm = this.getEmptyForm();
    this.newItem = this.getEmptyItem();
    this.currentStep = 1;
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  confirmPurchase(): void {
    if (!this.purchaseForm.items || this.purchaseForm.items.length === 0) return;
    this.creating = true;
    const paid = this.purchaseForm.paidAmount ?? 0;
    const net = this.purchaseForm.netAmount ?? 0;
    const purchase: PurchaseModel = {
      supplierId: this.purchaseForm.supplierId!,
      supplierName: this.purchaseForm.supplierName,
      totalAmount: this.purchaseForm.totalAmount,
      vat: this.purchaseForm.vat,
      discount: this.purchaseForm.discount,
      netAmount: this.purchaseForm.netAmount,
      paidAmount: paid,
      dueAmount: this.purchaseForm.dueAmount,
      status: 'PENDING',
      paymentStatus: net > 0 && paid >= net ? 'PAID' : paid > 0 ? 'PARTIAL' : 'PENDING',
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
        this.creating = false;
        this.error = 'Failed to create purchase order.';
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
    if (this.purchaseToDelete && this.purchaseToDelete.id) {
      this.purchaseService.delete(this.purchaseToDelete.id).subscribe({
        next: () => {
          this.purchases = this.purchases.filter((p: PurchaseModel) => p.id !== this.purchaseToDelete!.id);
          this.filterPurchases();
          this.showDeleteModal = false;
          this.purchaseToDelete = null;
        },
        error: () => { this.error = 'Failed to delete purchase.'; }
      });
    } else {
      this.showDeleteModal = false;
      this.purchaseToDelete = null;
    }
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.purchaseToDelete = null;
  }

  trackById(index: number, item: PurchaseModel): number {
    return item.id ?? index;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Paid': return 'badge-success';
      case 'Partial': return 'badge-warning';
      case 'Pending': return 'badge-danger';
      default: return 'badge-secondary';
    }
  }
}
