import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SurgeryReferenceService } from '../../../../services/surgery/surgery-reference.service';
import { DoctorModelService } from '../../../../services/doctor.service';
import { SurgeryCategory, SurgeryCategoryRequest } from '../../../../models/surgery/surgery-category.model';
import { SurgeryMaster, SurgeryMasterRequest } from '../../../../models/surgery/surgery-master.model';
import { OperationTheatre, OperationTheatreRequest } from '../../../../models/surgery/operation-theatre.model';
import { DoctorDiscount, DoctorDiscountRequest } from '../../../../models/surgery/doctor-discount.model';
import { DoctorModel } from '../../../../models/doctorModel';

@Component({
  selector: 'app-surgery-rate-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './surgery-rate-management.component.html',
  styleUrl: './surgery-rate-management.component.css'
})
export class SurgeryRateManagementComponent implements OnInit {
  activeTab = 'masters';
  tabs = [
    { key: 'masters', label: 'Rate Card', icon: '💠' },
    { key: 'categories', label: 'Categories', icon: '🗂️' },
    { key: 'theatres', label: 'Operation Theatres', icon: '🏥' },
    { key: 'discounts', label: 'Doctor Discounts', icon: '💳' }
  ];

  categories: SurgeryCategory[] = [];
  masters: SurgeryMaster[] = [];
  theatres: OperationTheatre[] = [];
  discounts: DoctorDiscount[] = [];
  doctors: DoctorModel[] = [];

  searchMaster = '';
  searchCategory = '';
  searchTheatre = '';
  searchDiscount = '';

  // Modal state
  showModal = false;
  modalTitle = '';
  editingId: number | null = null;
  categoryForm: SurgeryCategoryRequest = { code: '', name: '', description: '', active: true, sortOrder: 0 };
  masterForm: SurgeryMasterRequest = {
    surgeryCode: '', surgeryName: '', categoryId: 0, standardRate: 0,
    otCharge: 0, anesthesiaCharge: 0, nursingCharge: 0, equipmentCharge: 0,
    consumableCharge: 0, icuCharge: 0, packageRate: 0, active: true, estimatedDurationMin: 60, notes: ''
  };
  theatreForm: OperationTheatreRequest = { otCode: '', otName: '', location: '', equipmentAvailable: '', capacity: 1, status: 'AVAILABLE', active: true };
  discountForm: DoctorDiscountRequest = { doctorId: 0, discountPercent: 0, departmentDiscount: 0, promoDiscount: 0, active: true };

  loading = false;
  msg = '';
  msgType = 'success';

  constructor(
    private refService: SurgeryReferenceService,
    private doctorService: DoctorModelService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.refService.getMasters().subscribe(res => this.masters = res);
    this.refService.getCategories().subscribe(res => this.categories = res);
    this.refService.getTheatres().subscribe(res => this.theatres = res);
    this.refService.getDiscounts().subscribe(res => this.discounts = res);
    this.doctorService.getAll().subscribe(res => this.doctors = res);
    this.loading = false;
    this.cdr.detectChanges();
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  openAddByTab(): void {
    switch (this.activeTab) {
      case 'categories': this.openCategoryModal(); break;
      case 'theatres': this.openTheatreModal(); break;
      case 'discounts': this.openDiscountModal(); break;
      default: this.openMasterModal(); break;
    }
  }

  saveByTab(): void {
    switch (this.activeTab) {
      case 'categories': this.saveCategory(); break;
      case 'theatres': this.saveTheatre(); break;
      case 'discounts': this.saveDiscount(); break;
      default: this.saveMaster(); break;
    }
  }

  get categoryNameMap(): Record<number, string> {
    const map: Record<number, string> = {};
    this.categories.forEach(c => { if (c.id) map[c.id] = c.name; });
    return map;
  }

  get filteredMasters(): SurgeryMaster[] {
    const q = this.searchMaster.trim().toLowerCase();
    let list = this.masters;
    if (q) list = list.filter(m =>
      m.surgeryName?.toLowerCase().includes(q) ||
      m.surgeryCode?.toLowerCase().includes(q) ||
      m.categoryName?.toLowerCase().includes(q)
    );
    return list.sort((a, b) => (a.surgeryName || '').localeCompare(b.surgeryName || ''));
  }

  get filteredCategories(): SurgeryCategory[] {
    const q = this.searchCategory.trim().toLowerCase();
    let list = this.categories;
    if (q) list = list.filter(c => c.name?.toLowerCase().includes(q) || c.code?.toLowerCase().includes(q));
    return list;
  }

  get filteredTheatres(): OperationTheatre[] {
    const q = this.searchTheatre.trim().toLowerCase();
    let list = this.theatres;
    if (q) list = list.filter(t => t.otName?.toLowerCase().includes(q) || t.otCode?.toLowerCase().includes(q) || t.location?.toLowerCase().includes(q));
    return list;
  }

  get filteredDiscounts(): DoctorDiscount[] {
    const q = this.searchDiscount.trim().toLowerCase();
    let list = this.discounts;
    if (q) list = list.filter(d => (this.doctorName(d.doctorId) || '').toLowerCase().includes(q));
    return list;
  }

  doctorName(id?: number): string {
    if (!id) return '—';
    const d = this.doctors.find(x => x.id === id);
    return d ? d.name : '—';
  }

  // ===== CATEGORY =====
  openCategoryModal(cat?: SurgeryCategory): void {
    this.modalTitle = cat ? 'Edit Category' : 'New Category';
    this.editingId = cat?.id ?? null;
    this.categoryForm = cat ? {
      code: cat.code, name: cat.name, description: cat.description || '',
      active: cat.active ?? true, sortOrder: cat.sortOrder || 0
    } : { code: '', name: '', description: '', active: true, sortOrder: 0 };
    this.showModal = true;
  }

  saveCategory(): void {
    if (!this.categoryForm.name.trim() || !this.categoryForm.code.trim()) { this.showMsg('Code and name are required', 'error'); return; }
    const req = this.editingId
      ? this.refService.updateCategory(this.editingId, this.categoryForm)
      : this.refService.createCategory(this.categoryForm);
    req.subscribe({
      next: () => { this.closeModal(); this.loadAll(); this.showMsg('Category saved', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to save category', 'error')
    });
  }

  deleteCategory(cat: SurgeryCategory): void {
    if (!confirm(`Delete category "${cat.name}"?`)) return;
    this.refService.deleteCategory(cat.id!).subscribe({
      next: () => { this.loadAll(); this.showMsg('Category deleted', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to delete category', 'error')
    });
  }

  // ===== MASTER =====
  openMasterModal(m?: SurgeryMaster): void {
    this.modalTitle = m ? 'Edit Rate Card Entry' : 'New Rate Card Entry';
    this.editingId = m?.id ?? null;
    this.masterForm = m ? {
      surgeryCode: m.surgeryCode, surgeryName: m.surgeryName, categoryId: m.categoryId || 0,
      standardRate: m.standardRate, otCharge: m.otCharge || 0, anesthesiaCharge: m.anesthesiaCharge || 0,
      nursingCharge: m.nursingCharge || 0, equipmentCharge: m.equipmentCharge || 0,
      consumableCharge: m.consumableCharge || 0, icuCharge: m.icuCharge || 0, packageRate: m.packageRate || 0,
      active: m.active ?? true, estimatedDurationMin: m.estimatedDurationMin || 60, notes: m.notes || ''
    } : {
      surgeryCode: '', surgeryName: '', categoryId: this.categories[0]?.id || 0, standardRate: 0,
      otCharge: 0, anesthesiaCharge: 0, nursingCharge: 0, equipmentCharge: 0,
      consumableCharge: 0, icuCharge: 0, packageRate: 0, active: true, estimatedDurationMin: 60, notes: ''
    };
    this.showModal = true;
  }

  saveMaster(): void {
    if (!this.masterForm.surgeryName.trim() || !this.masterForm.surgeryCode.trim()) { this.showMsg('Code and name are required', 'error'); return; }
    if (!this.masterForm.categoryId) { this.showMsg('Select a category', 'error'); return; }
    const req = this.editingId
      ? this.refService.updateMaster(this.editingId, this.masterForm)
      : this.refService.createMaster(this.masterForm);
    req.subscribe({
      next: () => { this.closeModal(); this.loadAll(); this.showMsg('Rate card entry saved', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to save rate card entry', 'error')
    });
  }

  deleteMaster(m: SurgeryMaster): void {
    if (!confirm(`Delete "${m.surgeryName}" from rate card?`)) return;
    this.refService.deleteMaster(m.id!).subscribe({
      next: () => { this.loadAll(); this.showMsg('Rate card entry deleted', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to delete rate card entry', 'error')
    });
  }

  // ===== THEATRE =====
  openTheatreModal(t?: OperationTheatre): void {
    this.modalTitle = t ? 'Edit Operation Theatre' : 'New Operation Theatre';
    this.editingId = t?.id ?? null;
    this.theatreForm = t ? {
      otCode: t.otCode, otName: t.otName, location: t.location || '',
      equipmentAvailable: t.equipmentAvailable || '', capacity: t.capacity || 1,
      status: t.status || 'AVAILABLE', active: t.active ?? true
    } : { otCode: '', otName: '', location: '', equipmentAvailable: '', capacity: 1, status: 'AVAILABLE', active: true };
    this.showModal = true;
  }

  saveTheatre(): void {
    if (!this.theatreForm.otCode.trim() || !this.theatreForm.otName.trim()) { this.showMsg('OT code and name are required', 'error'); return; }
    const req = this.editingId
      ? this.refService.updateTheatre(this.editingId, this.theatreForm)
      : this.refService.createTheatre(this.theatreForm);
    req.subscribe({
      next: () => { this.closeModal(); this.loadAll(); this.showMsg('Operation theatre saved', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to save operation theatre', 'error')
    });
  }

  deleteTheatre(t: OperationTheatre): void {
    if (!confirm(`Delete operation theatre "${t.otName}"?`)) return;
    this.refService.deleteTheatre(t.id!).subscribe({
      next: () => { this.loadAll(); this.showMsg('Operation theatre deleted', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to delete operation theatre', 'error')
    });
  }

  // ===== DISCOUNT =====
  openDiscountModal(d?: DoctorDiscount): void {
    this.modalTitle = d ? 'Edit Doctor Discount' : 'New Doctor Discount';
    this.editingId = d?.id ?? null;
    this.discountForm = d ? {
      doctorId: d.doctorId, discountPercent: d.discountPercent || 0,
      departmentDiscount: d.departmentDiscount || 0, promoDiscount: d.promoDiscount || 0,
      active: d.active ?? true
    } : { doctorId: this.doctors[0]?.id || 0, discountPercent: 0, departmentDiscount: 0, promoDiscount: 0, active: true };
    this.showModal = true;
  }

  saveDiscount(): void {
    if (!this.discountForm.doctorId) { this.showMsg('Select a doctor', 'error'); return; }
    const req = this.editingId
      ? this.refService.updateDiscount(this.editingId, this.discountForm)
      : this.refService.createDiscount(this.discountForm);
    req.subscribe({
      next: () => { this.closeModal(); this.loadAll(); this.showMsg('Doctor discount saved', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to save doctor discount', 'error')
    });
  }

  deleteDiscount(d: DoctorDiscount): void {
    if (!confirm(`Delete discount for ${this.doctorName(d.doctorId)}?`)) return;
    this.refService.deleteDiscount(d.id!).subscribe({
      next: () => { this.loadAll(); this.showMsg('Doctor discount deleted', 'success'); },
      error: (err) => this.showMsg(err?.error?.message || 'Failed to delete doctor discount', 'error')
    });
  }

  totalDiscount(d: DoctorDiscount): number {
    return (d.discountPercent || 0) + (d.departmentDiscount || 0) + (d.promoDiscount || 0);
  }

  formatCurrency(amount: number | undefined): string {
    return '৳' + (amount || 0).toLocaleString('en-BD', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  closeModal(): void {
    this.showModal = false;
    this.editingId = null;
  }

  showMsg(text: string, type: string): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; this.cdr.detectChanges(); }, 4000);
  }
}
