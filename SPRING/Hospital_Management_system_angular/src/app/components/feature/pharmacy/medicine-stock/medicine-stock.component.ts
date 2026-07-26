import { Component, OnInit } from '@angular/core';
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
  error = '';

  showAddModal: boolean = false;
  showAdjustModal: boolean = false;
  showHistoryModal: boolean = false;
  selectedMedicine: MedicineStockModel | null = null;
  stockHistory: StockHistoryModel[] = [];
  loadingHistory = false;

  addFormModel: Partial<MedicineStockModel> = this.getEmptyAddForm();
  adjustFormModel: { adjustmentType: string; quantityChange: number; reason: string } = { adjustmentType: 'ADD', quantityChange: 0, reason: '' };

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
      barcode: '', supplierId: 0, supplierName: ''
    };
  }

  constructor(
    private stockService: StockService,
    private supplierService: SupplierService
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
        this.applyFilters();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load stock data. Please try again.';
        this.loading = false;
      }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAll().subscribe({
      next: (data: SupplierModel[]) => { this.suppliers = data; },
      error: () => {}
    });
  }

  setFilter(filter: string): void {
    this.activeFilter = filter;
    this.applyFilters();
  }

  applyFilters(): void {
    const now = new Date();
    const thirtyDays = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    let result = [...this.medicines];

    switch (this.activeFilter) {
      case 'Available':
        result = result.filter(m => (m.availableQuantity ?? m.stockQuantity) > (m.minimumStockLevel ?? 0));
        break;
      case 'Low Stock':
        result = result.filter(m => {
          const qty = m.availableQuantity ?? m.stockQuantity;
          return qty > 0 && qty <= (m.minimumStockLevel ?? 0);
        });
        break;
      case 'Expired':
        result = result.filter(m => new Date(m.expiryDate) < now);
        break;
      case 'Expiring Soon':
        result = result.filter(m => {
          const exp = new Date(m.expiryDate);
          return exp >= now && exp <= thirtyDays;
        });
        break;
    }

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(m =>
        m.medicineName.toLowerCase().includes(term) ||
        m.genericName.toLowerCase().includes(term) ||
        m.batchNumber.toLowerCase().includes(term)
      );
    }

    if (this.sortColumn) {
      result.sort((a: any, b: any) => {
        const valA = a[this.sortColumn];
        const valB = b[this.sortColumn];
        if (typeof valA === 'string') {
          return this.sortDirection === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA);
        }
        return this.sortDirection === 'asc' ? valA - valB : valB - valA;
      });
    }

    this.filteredMedicines = result;
  }

  sort(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.applyFilters();
  }

  filterMedicines(): void {
    this.applyFilters();
  }

  getMedicineStatus(medicine: MedicineStockModel): string {
    if (medicine.inventoryStatus) return medicine.inventoryStatus;
    const now = new Date();
    const expiry = new Date(medicine.expiryDate);
    if (expiry < now) return 'Expired';
    const qty = medicine.availableQuantity ?? medicine.stockQuantity;
    if (qty === 0) return 'Out of Stock';
    if (qty <= (medicine.minimumStockLevel ?? 0)) return 'Low Stock';
    return 'Available';
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'Available': return 'badge-green';
      case 'Low Stock': return 'badge-yellow';
      case 'Expired': return 'badge-red';
      case 'Out of Stock': return 'badge-red';
      default: return 'badge-gray';
    }
  }

  getExpiryClass(medicine: MedicineStockModel): string {
    const now = new Date();
    const expiry = new Date(medicine.expiryDate);
    if (expiry < now) return 'expiry-expired';
    const thirtyDays = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    if (expiry <= thirtyDays) return 'expiry-warning';
    return '';
  }

  openAddModal(): void {
    this.addFormModel = this.getEmptyAddForm();
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
  }

  saveMedicine(): void {
    const supplier = this.suppliers.find(s => s.id === this.addFormModel.supplierId);
    if (supplier) {
      this.addFormModel.supplierName = supplier.name;
    }
    this.stockService.addStock(this.addFormModel as MedicineStockModel).subscribe({
      next: () => {
        this.loadStock();
        this.closeAddModal();
      },
      error: () => {
        this.error = 'Failed to add stock. Please try again.';
      }
    });
  }

  openAdjustModal(medicine: MedicineStockModel): void {
    this.selectedMedicine = medicine;
    this.adjustFormModel = { adjustmentType: 'ADD', quantityChange: 0, reason: '' };
    this.showAdjustModal = true;
  }

  closeAdjustModal(): void {
    this.showAdjustModal = false;
    this.selectedMedicine = null;
  }

  saveAdjustment(): void {
    if (!this.selectedMedicine || this.adjustFormModel.quantityChange <= 0) return;
    const adjustment: StockAdjustmentModel = {
      medicineStockId: this.selectedMedicine.id!,
      adjustmentType: this.adjustFormModel.adjustmentType,
      quantityChange: this.adjustFormModel.quantityChange,
      reason: this.adjustFormModel.reason,
      performedBy: 'Admin'
    };
    this.stockService.adjustStock(adjustment).subscribe({
      next: () => {
        this.loadStock();
        this.closeAdjustModal();
      },
      error: () => {
        this.error = 'Failed to adjust stock. Please try again.';
      }
    });
  }

  openHistoryModal(medicine: MedicineStockModel): void {
    this.selectedMedicine = medicine;
    this.stockHistory = [];
    this.loadingHistory = true;
    this.showHistoryModal = true;
    this.stockService.getStockHistory(medicine.id!).subscribe({
      next: (data: StockHistoryModel[]) => {
        this.stockHistory = data;
        this.loadingHistory = false;
      },
      error: () => {
        this.stockHistory = [];
        this.loadingHistory = false;
      }
    });
  }

  closeHistoryModal(): void {
    this.showHistoryModal = false;
    this.selectedMedicine = null;
  }
}
