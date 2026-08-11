import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { StockService } from '../../../../services/stock.service';
import { SupplierService } from '../../../../services/supplier.service';
import { MedicineStockModel } from '../../../../models/medicine-stock.model';
import { StockAdjustmentModel, StockHistoryModel } from '../../../../models/stock-adjustment.model';
import { SupplierModel } from '../../../../models/supplier.model';

@Component({
  selector: 'app-medicine-stock',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './medicine-stock.component.html',
  styleUrls: ['./medicine-stock.component.css']
})
export class MedicineStockComponent implements OnInit {

  medicines: MedicineStockModel[] = [];
  suppliers: SupplierModel[] = [];
  filteredMedicines: MedicineStockModel[] = [];

  searchTerm: string = '';
  activeFilter: string = 'All';
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';
  loading = false;
  saving = false;
  error = '';

  showAddModal = false;
  showAdjustModal = false;
  showHistoryModal = false;

  selectedMedicine: MedicineStockModel | null = null;
  stockHistory: StockHistoryModel[] = [];
  loadingHistory = false;

  addFormModel: Partial<MedicineStockModel> = this.getEmptyAddForm();
  adjustFormModel: StockAdjustmentModel = { medicineStockId: 0, adjustmentType: 'ADD', quantityChange: 0, reason: '', performedBy: 'Admin' };

  get totalMedicines(): number { return this.medicines.length; }
  get availableStock(): number { return this.medicines.filter(m => (m.availableQuantity ?? m.stockQuantity) > (m.minimumStockLevel ?? 0)).length; }
  get lowStock(): number { return this.medicines.filter(m => (m.availableQuantity ?? m.stockQuantity) > 0 && (m.availableQuantity ?? m.stockQuantity) <= (m.minimumStockLevel ?? 0)).length; }
  get expired(): number { return this.medicines.filter(m => new Date(m.expiryDate) < new Date()).length; }
  get expiringSoon(): number {
    const now = new Date();
    const thirtyDays = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    return this.medicines.filter(m => {
      const exp = new Date(m.expiryDate);
      return exp >= now && exp <= thirtyDays;
    }).length;
  }

  getEmptyAddForm(): Partial<MedicineStockModel> {
    return {
      medicineName: '', genericName: '', strength: '', dosageForm: '', batchNumber: '',
      stockQuantity: 0, purchasePrice: 0, salePrice: 0, vat: 5,
      minimumStockLevel: 0, reorderLevel: 0, manufacturingDate: '', expiryDate: '',
      barcode: '', supplierId: 0
    };
  }

  constructor(
    private stockService: StockService,
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStock();
    this.loadSuppliers();
  }

  loadStock(): void {
    this.loading = true;
    this.error = '';
    this.stockService.getAll().subscribe({
      next: (data: MedicineStockModel[]) => {
        this.medicines = data;
        this.filterMedicines();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load stock. Please try again.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAll().subscribe({
      next: (data: SupplierModel[]) => { this.suppliers = data; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  setFilter(filter: string): void {
    this.activeFilter = filter;
    this.filterMedicines();
  }

  filterMedicines(): void {
    const term = this.searchTerm.toLowerCase();
    let result = this.medicines.filter(m =>
      m.medicineName.toLowerCase().includes(term) ||
      m.genericName.toLowerCase().includes(term) ||
      m.batchNumber.toLowerCase().includes(term)
    );
    const now = new Date();
    const thirtyDays = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    switch (this.activeFilter) {
      case 'Available': result = result.filter(m => (m.availableQuantity ?? m.stockQuantity) > (m.minimumStockLevel ?? 0)); break;
      case 'Low Stock': result = result.filter(m => (m.availableQuantity ?? m.stockQuantity) > 0 && (m.availableQuantity ?? m.stockQuantity) <= (m.minimumStockLevel ?? 0)); break;
      case 'Expired': result = result.filter(m => new Date(m.expiryDate) < now); break;
      case 'Expiring Soon': result = result.filter(m => { const exp = new Date(m.expiryDate); return exp >= now && exp <= thirtyDays; }); break;
    }
    this.filteredMedicines = result;
    this.cdr.detectChanges();
  }

  sort(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.filteredMedicines.sort((a: any, b: any) => {
      const valA = a[column];
      const valB = b[column];
      if (typeof valA === 'string') {
        return this.sortDirection === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA);
      }
      return this.sortDirection === 'asc' ? (valA ?? 0) - (valB ?? 0) : (valB ?? 0) - (valA ?? 0);
    });
    this.cdr.detectChanges();
  }

  getMedicineStatus(medicine: MedicineStockModel): string {
    const qty = medicine.availableQuantity ?? medicine.stockQuantity;
    if (new Date(medicine.expiryDate) < new Date()) return 'Expired';
    if (qty === 0) return 'Out of Stock';
    if (qty <= (medicine.minimumStockLevel ?? 0)) return 'Low Stock';
    return 'Available';
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'Available': return 'badge-success';
      case 'Low Stock': return 'badge-warning';
      case 'Out of Stock': return 'badge-danger';
      case 'Expired': return 'badge-dark';
      default: return 'badge-secondary';
    }
  }

  getExpiryClass(medicine: MedicineStockModel): string {
    const now = new Date();
    const exp = new Date(medicine.expiryDate);
    if (exp < now) return 'text-danger';
    const thirtyDays = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    if (exp <= thirtyDays) return 'text-warning';
    return '';
  }

  openAddModal(): void {
    this.addFormModel = this.getEmptyAddForm();
    this.showAddModal = true;
    this.cdr.detectChanges();
  }

  closeAddModal(): void {
    this.showAddModal = false;
    this.cdr.detectChanges();
  }

  saveMedicine(): void {
    if (this.saving) return;
    this.saving = true;
    this.stockService.addStock(this.addFormModel as MedicineStockModel).subscribe({
      next: () => {
        this.saving = false;
        this.loadStock();
        this.closeAddModal();
        this.cdr.detectChanges();
      },
      error: () => { this.error = 'Failed to add stock.'; this.saving = false; this.cdr.detectChanges(); }
    });
  }

  openAdjustModal(medicine: MedicineStockModel): void {
    this.selectedMedicine = medicine;
    this.adjustFormModel = { medicineStockId: medicine.id!, adjustmentType: 'ADD', quantityChange: 0, reason: '', performedBy: 'Admin' };
    this.showAdjustModal = true;
    this.cdr.detectChanges();
  }

  closeAdjustModal(): void {
    this.showAdjustModal = false;
    this.selectedMedicine = null;
    this.cdr.detectChanges();
  }

  saveAdjustment(): void {
    if (this.saving) return;
    this.saving = true;
    this.stockService.adjustStock(this.adjustFormModel).subscribe({
      next: () => {
        this.saving = false;
        this.loadStock();
        this.closeAdjustModal();
        this.cdr.detectChanges();
      },
      error: () => { this.error = 'Failed to adjust stock.'; this.saving = false; this.cdr.detectChanges(); }
    });
  }

  openHistoryModal(medicine: MedicineStockModel): void {
    this.selectedMedicine = medicine;
    this.loadingHistory = true;
    this.stockHistory = [];
    this.showHistoryModal = true;
    this.stockService.getStockHistory(medicine.id!).subscribe({
      next: (data: StockHistoryModel[]) => {
        this.stockHistory = data;
        this.loadingHistory = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingHistory = false;
        this.cdr.detectChanges();
      }
    });
  }

  closeHistoryModal(): void {
    this.showHistoryModal = false;
    this.selectedMedicine = null;
    this.stockHistory = [];
    this.cdr.detectChanges();
  }
}
