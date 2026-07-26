import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PurchasePharmacyService } from '../../../../services/purchase-pharmacy.service';
import { SupplierService } from '../../../../services/supplier.service';
import { StockService } from '../../../../services/stock.service';
import { PurchaseModel, PurchaseItemModel } from '../../../../models/purchase.model';
import { SupplierModel } from '../../../../models/supplier.model';
import { MedicineStockModel } from '../../../../models/medicine-stock.model';

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
  purchaseForm: Partial<PurchaseModel> = this.getEmptyForm();
  newItem: PurchaseItemModel = this.getEmptyItem();

  getEmptyForm(): Partial<PurchaseModel> {
    return { supplierId: 0, supplierName: '', invoiceNo: '', purchaseDate: '', totalAmount: 0, vat: 0, discount: 0, netAmount: 0, paidAmount: 0, dueAmount: 0, status: 'Pending', paymentMethod: 'Cash', notes: '', items: [] };
  }

  getEmptyItem(): PurchaseItemModel {
    return { stockId: 0, medicineName: '', batchNumber: '', quantity: 1, unitPrice: 0, discount: 0, subtotal: 0 };
  }

  get totalPurchases(): number { return this.purchases.length; }
  get thisMonthPurchases(): number { const now = new Date(); return this.purchases.filter(p => p.purchaseDate && new Date(p.purchaseDate).getMonth() === now.getMonth()).length; }
  get totalAmount(): number { return this.purchases.reduce((sum, p) => sum + (p.totalAmount || 0), 0); }
  get pendingDues(): number { return this.purchases.filter(p => p.status === 'Pending' || p.status === 'Partial').reduce((sum, p) => sum + (p.dueAmount || 0), 0); }

  constructor(
    private purchaseService: PurchasePharmacyService,
    private supplierService: SupplierService,
    private stockService: StockService
  ) {}

  ngOnInit(): void {
    this.loadPurchases();
    this.loadSuppliers();
  }

  loadPurchases(): void {
    this.purchaseService.getAll().subscribe({
      next: (data) => { this.purchases = data; this.filteredPurchases = [...data]; },
      error: () => {
        this.purchases = [
          { id: 1, supplierId: 1, supplierName: 'MediPharm Ltd', invoiceNo: 'INV-2024-001', purchaseDate: '2024-12-15', totalAmount: 45000, vat: 8100, discount: 2000, netAmount: 51100, paidAmount: 30000, dueAmount: 21100, status: 'Partial', paymentMethod: 'Cash', items: [] },
          { id: 2, supplierId: 2, supplierName: 'HealthLine Supply', invoiceNo: 'INV-2024-002', purchaseDate: '2024-12-18', totalAmount: 32000, vat: 5760, discount: 1500, netAmount: 36260, paidAmount: 36260, dueAmount: 0, status: 'Paid', paymentMethod: 'Card', items: [] },
          { id: 3, supplierId: 3, supplierName: 'BioCare Pharma', invoiceNo: 'INV-2024-003', purchaseDate: '2024-12-20', totalAmount: 68000, vat: 12240, discount: 5000, netAmount: 75240, paidAmount: 0, dueAmount: 75240, status: 'Pending', paymentMethod: 'Cash', items: [] },
        ];
        this.filteredPurchases = [...this.purchases];
      }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAll().subscribe({
      next: (data) => this.suppliers = data,
      error: () => {
        this.suppliers = [
          { id: 1, name: 'MediPharm Ltd', contactPerson: 'Dr. Rahman', phone: '+880-1711-234567', email: 'rahman@medipharm.com', address: '45 Pharma Tower, Dhaka', companyName: 'MediPharm Bangladesh Ltd', tradeLicense: 'TL-2024-1234', drugLicense: 'DL-2024-5678', website: '', notes: '', active: true },
          { id: 2, name: 'HealthLine Supply', contactPerson: 'Fatima Khan', phone: '+880-1812-345678', email: 'fatima@healthline.com', address: '78 Health Ave, Chittagong', companyName: 'HealthLine Supply Co.', tradeLicense: 'TL-2024-2345', drugLicense: 'DL-2024-6789', website: '', notes: '', active: true },
          { id: 3, name: 'BioCare Pharma', contactPerson: 'Md. Hassan', phone: '+880-1913-456789', email: 'hassan@biocare.com', address: '12 Bio Street, Sylhet', companyName: 'BioCare Pharmaceuticals', tradeLicense: 'TL-2024-3456', drugLicense: 'DL-2024-7890', website: '', notes: '', active: true },
        ];
      }
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
      default: return '';
    }
  }

  openCreateModal(): void {
    this.purchaseForm = this.getEmptyForm();
    this.currentStep = 1;
    this.medicineSearchTerm = '';
    this.searchedMedicines = [];
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
    if (this.medicineSearchTerm.length < 2) { this.searchedMedicines = []; return; }
    this.stockService.search(this.medicineSearchTerm).subscribe({
      next: (data) => this.searchedMedicines = data.filter(s => (s.availableQuantity || 0) > 0),
      error: () => {
        this.searchedMedicines = [
          { id: 1, medicineName: 'Paracetamol 500mg', genericName: 'Paracetamol', strength: '500mg', dosageForm: 'Tablet', batchNumber: 'BAT-001', stockQuantity: 500, availableQuantity: 500, purchasePrice: 8, salePrice: 12, manufacturingDate: '2024-01-01', expiryDate: '2026-01-01', supplierId: 1, supplierName: 'MediPharm Ltd' },
          { id: 2, medicineName: 'Amoxicillin 250mg', genericName: 'Amoxicillin', strength: '250mg', dosageForm: 'Capsule', batchNumber: 'BAT-002', stockQuantity: 200, availableQuantity: 200, purchasePrice: 15, salePrice: 22, manufacturingDate: '2024-03-01', expiryDate: '2025-12-01', supplierId: 1, supplierName: 'MediPharm Ltd' },
          { id: 3, medicineName: 'Omeprazole 20mg', genericName: 'Omeprazole', strength: '20mg', dosageForm: 'Capsule', batchNumber: 'BAT-003', stockQuantity: 300, availableQuantity: 300, purchasePrice: 12, salePrice: 18, manufacturingDate: '2024-02-15', expiryDate: '2026-02-15', supplierId: 2, supplierName: 'HealthLine Supply' },
          { id: 4, medicineName: 'Cetirizine 10mg', genericName: 'Cetirizine', strength: '10mg', dosageForm: 'Tablet', batchNumber: 'BAT-004', stockQuantity: 150, availableQuantity: 150, purchasePrice: 6, salePrice: 10, manufacturingDate: '2024-04-01', expiryDate: '2025-11-01', supplierId: 3, supplierName: 'BioCare Pharma' },
        ].filter(s => s.medicineName.toLowerCase().includes(this.medicineSearchTerm.toLowerCase()));
      }
    });
  }

  addMedicineToItem(medicine: MedicineStockModel): void {
    this.newItem.stockId = medicine.id!;
    this.newItem.medicineName = medicine.medicineName;
    this.newItem.batchNumber = medicine.batchNumber;
    this.newItem.unitPrice = medicine.purchasePrice;
    this.newItem.subtotal = this.newItem.quantity * this.newItem.unitPrice - this.newItem.discount;
    this.searchedMedicines = [];
    this.medicineSearchTerm = medicine.medicineName;
  }

  updateItemSubtotal(): void {
    this.newItem.subtotal = this.newItem.quantity * this.newItem.unitPrice - this.newItem.discount;
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
    this.purchaseForm.dueAmount = (this.purchaseForm.netAmount || 0) - (this.purchaseForm.paidAmount || 0);
  }

  goToStep(step: number): void {
    if (step === 2 && this.purchaseForm.supplierId === 0) return;
    if (step === 3 && (!this.purchaseForm.items || this.purchaseForm.items.length === 0)) return;
    this.currentStep = step;
    if (step === 3) this.calculateTotals();
  }

  confirmPurchase(): void {
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
      status: (this.purchaseForm.paidAmount || 0) >= (this.purchaseForm.netAmount || 0) ? 'Paid' : (this.purchaseForm.paidAmount || 0) > 0 ? 'Partial' : 'Pending',
      paymentMethod: this.purchaseForm.paymentMethod,
      notes: this.purchaseForm.notes,
      items: this.purchaseForm.items
    };
    this.purchaseService.create(purchase).subscribe({
      next: (saved) => {
        this.purchases.unshift(saved);
        this.filterPurchases();
        this.closeCreateModal();
      },
      error: () => {
        purchase.id = this.purchases.length + 1;
        purchase.supplierName = this.purchaseForm.supplierName;
        this.purchases.unshift(purchase);
        this.filterPurchases();
        this.closeCreateModal();
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
        next: () => { this.purchases = this.purchases.filter(p => p.id !== this.purchaseToDelete!.id); this.filterPurchases(); },
        error: () => { this.purchases = this.purchases.filter(p => p.id !== this.purchaseToDelete!.id); this.filterPurchases(); }
      });
    }
    this.showDeleteModal = false;
    this.purchaseToDelete = null;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.purchaseToDelete = null;
  }
}
