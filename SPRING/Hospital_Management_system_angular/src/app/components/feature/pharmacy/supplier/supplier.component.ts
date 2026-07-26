import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

interface Supplier {
  id: number;
  name: string;
  contactPerson: string;
  phone: string;
  email: string;
  address: string;
  companyName: string;
  tradeLicense: string;
  drugLicense: string;
  website: string;
  notes: string;
  status: 'Active' | 'Inactive' | 'Suspended';
  dueAmount: number;
}

@Component({
  selector: 'app-supplier',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './supplier.component.html',
  styleUrls: ['./supplier.component.css']
})
export class SupplierComponent implements OnInit {
  suppliers: Supplier[] = [
    { id: 1, name: 'MediPharm Ltd', contactPerson: 'Dr. Rahman Ahmed', phone: '+880-1711-234567', email: 'rahman@medipharm.com', address: '45 Pharma Tower, Dhaka', companyName: 'MediPharm Bangladesh Ltd', tradeLicense: 'TL-2024-1234', drugLicense: 'DL-2024-5678', website: 'www.medipharm.com', notes: 'Premium supplier', status: 'Active', dueAmount: 15000 },
    { id: 2, name: 'HealthLine Supply', contactPerson: 'Fatima Khan', phone: '+880-1812-345678', email: 'fatima@healthline.com', address: '78 Health Ave, Chittagong', companyName: 'HealthLine Supply Co.', tradeLicense: 'TL-2024-2345', drugLicense: 'DL-2024-6789', website: 'www.healthline.com', notes: 'Reliable partner', status: 'Active', dueAmount: 0 },
    { id: 3, name: 'BioCare Pharma', contactPerson: 'Md. Hassan', phone: '+880-1913-456789', email: 'hassan@biocare.com', address: '12 Bio Street, Sylhet', companyName: 'BioCare Pharmaceuticals', tradeLicense: 'TL-2024-3456', drugLicense: 'DL-2024-7890', website: 'www.biocare.com', notes: 'Specializes in generics', status: 'Active', dueAmount: 25000 },
    { id: 4, name: 'GlobalMed Inc', contactPerson: 'Sarah Islam', phone: '+880-1614-567890', email: 'sarah@globalmed.com', address: '90 Global Plaza, Rajshahi', companyName: 'GlobalMed Inc.', tradeLicense: 'TL-2024-4567', drugLicense: 'DL-2024-8901', website: 'www.globalmed.com', notes: 'International supplier', status: 'Inactive', dueAmount: 5000 },
    { id: 5, name: 'PharmaTech BD', contactPerson: 'Kamal Hossain', phone: '+880-1715-678901', email: 'kamal@pharmatech.com', address: '33 Tech Road, Khulna', companyName: 'PharmaTech Bangladesh', tradeLicense: 'TL-2024-5678', drugLicense: 'DL-2024-9012', website: 'www.pharmatech.com', notes: 'Fast delivery', status: 'Active', dueAmount: 8000 },
    { id: 6, name: 'LifeCare Pharma', contactPerson: 'Nadia Akter', phone: '+880-1816-789012', email: 'nadia@lifecare.com', address: '56 Life Blvd, Barisal', companyName: 'LifeCare Pharma Ltd', tradeLicense: 'TL-2024-6789', drugLicense: 'DL-2024-0123', website: 'www.lifecare.com', notes: 'Budget friendly', status: 'Suspended', dueAmount: 42000 },
    { id: 7, name: 'NovaChem Supply', contactPerson: 'Farhan Sheikh', phone: '+880-1917-890123', email: 'farhan@novachem.com', address: '81 Nova Tower, Comilla', companyName: 'NovaChem Supply Co.', tradeLicense: 'TL-2024-7890', drugLicense: 'DL-2024-1234', website: 'www.novachem.com', notes: 'Chemical specialist', status: 'Active', dueAmount: 0 },
    { id: 8, name: 'PrimeHealth Dist', contactPerson: 'Tariq Alam', phone: '+880-1618-901234', email: 'tariq@primehealth.com', address: '24 Prime Lane, Gazipur', companyName: 'PrimeHealth Distribution', tradeLicense: 'TL-2024-8901', drugLicense: 'DL-2024-2345', website: 'www.primehealth.com', notes: 'Wide coverage area', status: 'Active', dueAmount: 12000 },
  ];

  filteredSuppliers: Supplier[] = [];
  searchTerm: string = '';
  showModal: boolean = false;
  showDeleteModal: boolean = false;
  editingSupplier: Supplier | null = null;
  supplierToDelete: Supplier | null = null;
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  formModel: Partial<Supplier> = this.getEmptyForm();

  getEmptyForm(): Partial<Supplier> {
    return { name: '', contactPerson: '', phone: '', email: '', address: '', companyName: '', tradeLicense: '', drugLicense: '', website: '', notes: '', status: 'Active', dueAmount: 0 };
  }

  get totalSuppliers(): number { return this.suppliers.length; }
  get activeSuppliers(): number { return this.suppliers.filter(s => s.status === 'Active').length; }
  get dueAmountSuppliers(): number { return this.suppliers.filter(s => s.dueAmount > 0).length; }

  ngOnInit(): void {
    this.filteredSuppliers = [...this.suppliers];
  }

  filterSuppliers(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredSuppliers = this.suppliers.filter(s =>
      s.name.toLowerCase().includes(term) ||
      s.contactPerson.toLowerCase().includes(term) ||
      s.phone.includes(term) ||
      s.email.toLowerCase().includes(term) ||
      s.companyName.toLowerCase().includes(term)
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
      return this.sortDirection === 'asc' ? valA - valB : valB - valA;
    });
  }

  openAddModal(): void {
    this.editingSupplier = null;
    this.formModel = this.getEmptyForm();
    this.showModal = true;
  }

  openEditModal(supplier: Supplier): void {
    this.editingSupplier = supplier;
    this.formModel = { ...supplier };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingSupplier = null;
  }

  saveSupplier(): void {
    if (this.editingSupplier) {
      const idx = this.suppliers.findIndex(s => s.id === this.editingSupplier!.id);
      if (idx !== -1) {
        this.suppliers[idx] = { ...this.formModel } as Supplier;
      }
    } else {
      const newId = Math.max(...this.suppliers.map(s => s.id)) + 1;
      this.suppliers.push({ ...this.formModel, id: newId } as Supplier);
    }
    this.filterSuppliers();
    this.closeModal();
  }

  confirmDelete(supplier: Supplier): void {
    this.supplierToDelete = supplier;
    this.showDeleteModal = true;
  }

  deleteSupplier(): void {
    if (this.supplierToDelete) {
      this.suppliers = this.suppliers.filter(s => s.id !== this.supplierToDelete!.id);
      this.filterSuppliers();
    }
    this.showDeleteModal = false;
    this.supplierToDelete = null;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.supplierToDelete = null;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Active': return 'badge-active';
      case 'Inactive': return 'badge-inactive';
      case 'Suspended': return 'badge-suspended';
      default: return '';
    }
  }
}
