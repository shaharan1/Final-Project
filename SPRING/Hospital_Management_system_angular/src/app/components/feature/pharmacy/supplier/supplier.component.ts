import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { catchError, of, timeout } from 'rxjs';
import { SupplierService } from '../../../../services/supplier.service';
import { SupplierModel } from '../../../../models/supplier.model';

@Component({
  selector: 'app-supplier',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './supplier.component.html',
  styleUrls: ['./supplier.component.css']
})
export class SupplierComponent implements OnInit {

  suppliers: SupplierModel[] = [];
  filteredSuppliers: SupplierModel[] = [];
  searchTerm: string = '';
  showModal: boolean = false;
  showDeleteModal: boolean = false;
  editingSupplier: SupplierModel | null = null;
  supplierToDelete: SupplierModel | null = null;
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';
  loading = false;
  error = '';

  formModel: Partial<SupplierModel> = this.getEmptyForm();

  getEmptyForm(): Partial<SupplierModel> {
    return { name: '', contactPerson: '', phone: '', email: '', address: '', companyName: '', tradeLicense: '', drugLicense: '', website: '', notes: '', active: true };
  }

  get totalSuppliers(): number { return this.suppliers.length; }
  get activeSuppliers(): number { return this.suppliers.filter(s => s.active !== false).length; }
  get dueAmountSuppliers(): number { return this.suppliers.filter(s => (s.totalDue ?? 0) > 0).length; }

  constructor(private supplierService: SupplierService) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.loading = true;
    this.error = '';
    this.supplierService.getAll().pipe(
      timeout(15000),
      catchError(err => {
        console.error('Failed to load suppliers', err);
        this.error = 'Failed to load suppliers. Please try again.';
        this.loading = false;
        return of([]);
      })
    ).subscribe({
      next: (data: SupplierModel[]) => {
        this.suppliers = data ?? [];
        this.filterSuppliers();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load suppliers. Please try again.';
        this.loading = false;
      }
    });
  }

  filterSuppliers(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredSuppliers = this.suppliers.filter(s =>
      (s.name ?? '').toLowerCase().includes(term) ||
      (s.contactPerson ?? '').toLowerCase().includes(term) ||
      (s.phone ?? '').includes(term) ||
      (s.email ?? '').toLowerCase().includes(term) ||
      (s.companyName ?? '').toLowerCase().includes(term)
    );
  }

  sort(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.filteredSuppliers.sort((a: any, b: any) => {
      const valA = a[column];
      const valB = b[column];
      if (typeof valA === 'string') {
        return this.sortDirection === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA);
      }
      return this.sortDirection === 'asc' ? (valA ?? 0) - (valB ?? 0) : (valB ?? 0) - (valA ?? 0);
    });
  }

  openAddModal(): void {
    this.editingSupplier = null;
    this.formModel = this.getEmptyForm();
    this.showModal = true;
  }

  openEditModal(supplier: SupplierModel): void {
    this.editingSupplier = supplier;
    this.formModel = { ...supplier };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingSupplier = null;
  }

  saveSupplier(): void {
    if (this.editingSupplier && this.editingSupplier.id) {
      this.supplierService.update(this.editingSupplier.id, this.formModel as SupplierModel).pipe(
        catchError(err => {
          console.error('Failed to update supplier', err);
          this.error = 'Failed to update supplier.';
          return of(null);
        })
      ).subscribe({
        next: (result) => {
          if (result) {
            this.loadSuppliers();
            this.closeModal();
          }
        },
        error: () => { this.error = 'Failed to update supplier.'; }
      });
    } else {
      this.supplierService.create(this.formModel as SupplierModel).pipe(
        catchError(err => {
          console.error('Failed to create supplier', err);
          this.error = 'Failed to create supplier.';
          return of(null);
        })
      ).subscribe({
        next: (result) => {
          if (result) {
            this.loadSuppliers();
            this.closeModal();
          }
        },
        error: () => { this.error = 'Failed to create supplier.'; }
      });
    }
  }

  confirmDelete(supplier: SupplierModel): void {
    this.supplierToDelete = supplier;
    this.showDeleteModal = true;
  }

  deleteSupplier(): void {
    if (this.supplierToDelete && this.supplierToDelete.id) {
      this.supplierService.delete(this.supplierToDelete.id).pipe(
        catchError(err => {
          console.error('Failed to delete supplier', err);
          this.error = 'Failed to delete supplier.';
          return of(null);
        })
      ).subscribe({
        next: () => {
          this.loadSuppliers();
          this.showDeleteModal = false;
          this.supplierToDelete = null;
        },
        error: () => { this.error = 'Failed to delete supplier.'; }
      });
    } else {
      this.showDeleteModal = false;
      this.supplierToDelete = null;
    }
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.supplierToDelete = null;
  }

  getStatusClass(supplier: SupplierModel): string {
    if (supplier.active === false) return 'badge-inactive';
    if ((supplier.totalDue ?? 0) > 0) return 'badge-suspended';
    return 'badge-active';
  }

  getStatusLabel(supplier: SupplierModel): string {
    if (supplier.active === false) return 'Inactive';
    if ((supplier.totalDue ?? 0) > 0) return 'Active (Due)';
    return 'Active';
  }
}
