import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

interface Medicine {
  id: number;
  medicineName: string;
  genericName: string;
  strength: string;
  dosageForm: string;
  batchNumber: string;
  stockQuantity: number;
  purchasePrice: number;
  salePrice: number;
  vat: number;
  minimumStockLevel: number;
  reorderLevel: number;
  manufacturingDate: string;
  expiryDate: string;
  barcode: string;
  supplierId: number;
  supplierName: string;
}

interface StockAdjustment {
  id: number;
  medicineId: number;
  medicineName: string;
  type: 'ADD' | 'DAMAGE' | 'RETURN';
  quantity: number;
  reason: string;
  date: string;
  performedBy: string;
}

interface Supplier {
  id: number;
  name: string;
}

@Component({
  selector: 'app-medicine-stock',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './medicine-stock.component.html',
  styleUrls: ['./medicine-stock.component.css']
})
export class MedicineStockComponent implements OnInit {
  medicines: Medicine[] = [
    { id: 1, medicineName: 'Paracetamol 500mg', genericName: 'Acetaminophen', strength: '500mg', dosageForm: 'Tablet', batchNumber: 'BAT-2024-001', stockQuantity: 500, purchasePrice: 2.5, salePrice: 4.0, vat: 5, minimumStockLevel: 100, reorderLevel: 200, manufacturingDate: '2024-01-15', expiryDate: '2026-06-30', barcode: 'BAR001', supplierId: 1, supplierName: 'MediPharm Ltd' },
    { id: 2, medicineName: 'Amoxicillin 250mg', genericName: 'Amoxicillin', strength: '250mg', dosageForm: 'Capsule', batchNumber: 'BAT-2024-002', stockQuantity: 30, purchasePrice: 8.0, salePrice: 12.0, vat: 5, minimumStockLevel: 50, reorderLevel: 100, manufacturingDate: '2024-03-10', expiryDate: '2025-09-15', barcode: 'BAR002', supplierId: 2, supplierName: 'HealthLine Supply' },
    { id: 3, medicineName: 'Omeprazole 20mg', genericName: 'Omeprazole', strength: '20mg', dosageForm: 'Capsule', batchNumber: 'BAT-2024-003', stockQuantity: 0, purchasePrice: 15.0, salePrice: 22.0, vat: 5, minimumStockLevel: 30, reorderLevel: 60, manufacturingDate: '2023-06-01', expiryDate: '2024-12-31', barcode: 'BAR003', supplierId: 3, supplierName: 'BioCare Pharma' },
    { id: 4, medicineName: 'Metformin 500mg', genericName: 'Metformin HCl', strength: '500mg', dosageForm: 'Tablet', batchNumber: 'BAT-2024-004', stockQuantity: 15, purchasePrice: 5.0, salePrice: 8.0, vat: 5, minimumStockLevel: 40, reorderLevel: 80, manufacturingDate: '2024-02-20', expiryDate: '2026-02-20', barcode: 'BAR004', supplierId: 4, supplierName: 'GlobalMed Inc' },
    { id: 5, medicineName: 'Cetirizine 10mg', genericName: 'Cetirizine HCl', strength: '10mg', dosageForm: 'Tablet', batchNumber: 'BAT-2024-005', stockQuantity: 200, purchasePrice: 3.0, salePrice: 5.0, vat: 5, minimumStockLevel: 50, reorderLevel: 100, manufacturingDate: '2024-04-05', expiryDate: '2026-04-05', barcode: 'BAR005', supplierId: 5, supplierName: 'PharmaTech BD' },
    { id: 6, medicineName: 'Ibuprofen 400mg', genericName: 'Ibuprofen', strength: '400mg', dosageForm: 'Tablet', batchNumber: 'BAT-2024-006', stockQuantity: 8, purchasePrice: 4.0, salePrice: 6.5, vat: 5, minimumStockLevel: 30, reorderLevel: 60, manufacturingDate: '2024-01-20', expiryDate: '2025-07-20', barcode: 'BAR006', supplierId: 1, supplierName: 'MediPharm Ltd' },
    { id: 7, medicineName: 'Azithromycin 500mg', genericName: 'Azithromycin', strength: '500mg', dosageForm: 'Tablet', batchNumber: 'BAT-2024-007', stockQuantity: 45, purchasePrice: 25.0, salePrice: 38.0, vat: 5, minimumStockLevel: 20, reorderLevel: 40, manufacturingDate: '2024-05-01', expiryDate: '2026-05-01', barcode: 'BAR007', supplierId: 2, supplierName: 'HealthLine Supply' },
    { id: 8, medicineName: 'Pantoprazole 40mg', genericName: 'Pantoprazole', strength: '40mg', dosageForm: 'Injection', batchNumber: 'BAT-2024-008', stockQuantity: 0, purchasePrice: 35.0, salePrice: 50.0, vat: 5, minimumStockLevel: 10, reorderLevel: 25, manufacturingDate: '2023-09-15', expiryDate: '2024-09-15', barcode: 'BAR008', supplierId: 3, supplierName: 'BioCare Pharma' },
  ];

  suppliers: Supplier[] = [
    { id: 1, name: 'MediPharm Ltd' },
    { id: 2, name: 'HealthLine Supply' },
    { id: 3, name: 'BioCare Pharma' },
    { id: 4, name: 'GlobalMed Inc' },
    { id: 5, name: 'PharmaTech BD' },
  ];

  filteredMedicines: Medicine[] = [];
  searchTerm: string = '';
  activeFilter: string = 'All';
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  showAddModal: boolean = false;
  showAdjustModal: boolean = false;
  showHistoryModal: boolean = false;
  selectedMedicine: Medicine | null = null;
  stockHistory: StockAdjustment[] = [];

  addFormModel: Partial<Medicine> = this.getEmptyAddForm();
  adjustFormModel: { type: 'ADD' | 'DAMAGE' | 'RETURN'; quantity: number; reason: string } = { type: 'ADD', quantity: 0, reason: '' };

  get totalMedicines(): number { return this.medicines.length; }
  get availableStock(): number { return this.medicines.filter(m => m.stockQuantity > m.minimumStockLevel).length; }
  get lowStock(): number { return this.medicines.filter(m => m.stockQuantity > 0 && m.stockQuantity <= m.minimumStockLevel).length; }
  get expired(): number { return this.medicines.filter(m => new Date(m.expiryDate) < new Date()).length; }
  get expiringSoon(): number {
    const now = new Date();
    const thirtyDays = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    return this.medicines.filter(m => {
      const exp = new Date(m.expiryDate);
      return exp >= now && exp <= thirtyDays;
    }).length;
  }

  getEmptyAddForm(): Partial<Medicine> {
    return {
      medicineName: '', genericName: '', strength: '', dosageForm: '', batchNumber: '',
      stockQuantity: 0, purchasePrice: 0, salePrice: 0, vat: 5,
      minimumStockLevel: 0, reorderLevel: 0, manufacturingDate: '', expiryDate: '',
      barcode: '', supplierId: 0, supplierName: ''
    };
  }

  ngOnInit(): void {
    this.filteredMedicines = [...this.medicines];
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
        result = result.filter(m => m.stockQuantity > m.minimumStockLevel);
        break;
      case 'Low Stock':
        result = result.filter(m => m.stockQuantity > 0 && m.stockQuantity <= m.minimumStockLevel);
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

  getMedicineStatus(medicine: Medicine): string {
    const now = new Date();
    const expiry = new Date(medicine.expiryDate);
    if (expiry < now) return 'Expired';
    if (medicine.stockQuantity === 0) return 'Out of Stock';
    if (medicine.stockQuantity <= medicine.minimumStockLevel) return 'Low Stock';
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

  getExpiryClass(medicine: Medicine): string {
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
    const newId = Math.max(...this.medicines.map(m => m.id)) + 1;
    this.medicines.push({ ...this.addFormModel, id: newId } as Medicine);
    this.applyFilters();
    this.closeAddModal();
  }

  openAdjustModal(medicine: Medicine): void {
    this.selectedMedicine = medicine;
    this.adjustFormModel = { type: 'ADD', quantity: 0, reason: '' };
    this.showAdjustModal = true;
  }

  closeAdjustModal(): void {
    this.showAdjustModal = false;
    this.selectedMedicine = null;
  }

  saveAdjustment(): void {
    if (!this.selectedMedicine) return;
    const m = this.medicines.find(md => md.id === this.selectedMedicine!.id);
    if (m) {
      if (this.adjustFormModel.type === 'ADD') {
        m.stockQuantity += this.adjustFormModel.quantity;
      } else {
        m.stockQuantity = Math.max(0, m.stockQuantity - this.adjustFormModel.quantity);
      }
      const historyEntry: StockAdjustment = {
        id: this.stockHistory.length + 1,
        medicineId: m.id,
        medicineName: m.medicineName,
        type: this.adjustFormModel.type,
        quantity: this.adjustFormModel.quantity,
        reason: this.adjustFormModel.reason,
        date: new Date().toISOString().split('T')[0],
        performedBy: 'Admin'
      };
      this.stockHistory.push(historyEntry);
    }
    this.applyFilters();
    this.closeAdjustModal();
  }

  openHistoryModal(medicine: Medicine): void {
    this.selectedMedicine = medicine;
    this.showHistoryModal = true;
  }

  closeHistoryModal(): void {
    this.showHistoryModal = false;
    this.selectedMedicine = null;
  }

  getHistoryForMedicine(medicineId: number): StockAdjustment[] {
    return this.stockHistory.filter(h => h.medicineId === medicineId);
  }
}
