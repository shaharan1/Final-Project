import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DieticianService } from '../../../../services/dietary/dietician.service';

@Component({
  selector: 'app-dietician-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dietician-management.component.html',
  styleUrl: './dietician-management.component.css'
})
export class DieticianManagementComponent implements OnInit {
  dieticians: any[] = [];
  filteredDieticians: any[] = [];
  loading = true;
  searchTerm = '';
  showModal = false;
  showDeleteModal = false;
  editingItem: any = null;
  itemToDelete: any = null;
  formModel: any = {};
  msg = '';
  msgType = '';

  constructor(private dieticianService: DieticianService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.dieticianService.getAll().subscribe({
      next: (data) => { this.dieticians = data; this.filteredDieticians = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  filterDieticians(): void {
    if (!this.searchTerm) { this.filteredDieticians = [...this.dieticians]; return; }
    const term = this.searchTerm.toLowerCase();
    this.filteredDieticians = this.dieticians.filter(d =>
      (d.user?.name && d.user.name.toLowerCase().includes(term)) ||
      (d.specialization && d.specialization.toLowerCase().includes(term)) ||
      (d.licenseNumber && d.licenseNumber.toLowerCase().includes(term))
    );
  }

  openAddModal(): void { this.formModel = { specialization: '', qualification: '', experienceYears: 0, licenseNumber: '', phone: '', availableDays: '', dutyHours: '', active: true }; this.editingItem = null; this.showModal = true; }

  openEditModal(item: any): void { this.formModel = { ...item }; this.editingItem = item; this.showModal = true; }

  saveItem(): void {
    if (!this.formModel.specialization) { this.msg = 'Specialization is required'; this.msgType = 'error'; return; }
    if (this.editingItem) {
      this.dieticianService.update(this.editingItem.id, this.formModel).subscribe({ next: () => { this.showModal = false; this.loadData(); }, error: () => {} });
    } else {
      this.dieticianService.create(this.formModel).subscribe({ next: () => { this.showModal = false; this.loadData(); }, error: () => {} });
    }
  }

  confirmDelete(item: any): void { this.itemToDelete = item; this.showDeleteModal = true; }
  cancelDelete(): void { this.showDeleteModal = false; this.itemToDelete = null; }

  deleteItem(): void {
    if (this.itemToDelete) {
      this.dieticianService.delete(this.itemToDelete.id).subscribe({ next: () => { this.showDeleteModal = false; this.loadData(); }, error: () => {} });
    }
  }

  getInitials(name: string): string { return name ? name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2) : '?'; }
}