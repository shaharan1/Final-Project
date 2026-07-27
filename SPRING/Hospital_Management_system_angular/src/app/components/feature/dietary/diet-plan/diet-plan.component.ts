import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DietPlanService } from '../../../../services/dietary/diet-plan.service';
import { DietPlan } from '../../../../models/dietary/diet-plan.model';

@Component({
  selector: 'app-diet-plan',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './diet-plan.component.html',
  styleUrl: './diet-plan.component.css'
})
export class DietPlanComponent implements OnInit {
  plans: DietPlan[] = [];
  filteredPlans: DietPlan[] = [];
  loading = true;
  searchTerm = '';
  filterDietType = '';
  showModal = false;
  showDeleteModal = false;
  editingPlan: DietPlan | null = null;
  planToDelete: DietPlan | null = null;
  expandedPlanId: number | null = null;
  formModel: Partial<DietPlan> = this.getEmptyForm();
  msg = '';
  msgType = '';

  dietTypes = ['Regular', 'Diabetic', 'LowSalt', 'LowFat', 'Cardiac', 'HighProtein', 'Liquid', 'Soft', 'Renal', 'Pediatric', 'Pregnancy', 'PostSurgery', 'Special'];

  constructor(
    private dietPlanService: DietPlanService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.loadPlans(); }

  loadPlans(): void {
    this.loading = true;
    this.dietPlanService.getAll().subscribe({
      next: (data) => { this.plans = data; this.filteredPlans = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  filterPlans(): void {
    let result = [...this.plans];
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p => 
        (p.name && p.name.toLowerCase().includes(term)) ||
        (p.dietType && p.dietType.toLowerCase().includes(term))
      );
    }
    if (this.filterDietType) {
      result = result.filter(p => p.dietType === this.filterDietType);
    }
    this.filteredPlans = result;
  }

  getEmptyForm(): Partial<DietPlan> {
    return {
      name: '', dietType: 'Regular', description: '',
      breakfast: '', breakfastTime: '07:30', morningSnacks: '', morningSnacksTime: '10:00',
      lunch: '', lunchTime: '12:30', eveningSnacks: '', eveningSnacksTime: '16:00',
      dinner: '', dinnerTime: '19:00', nightDiet: '', nightDietTime: '21:00',
      totalCalories: 2000, protein: 75, carbohydrate: 250, fat: 70, fiber: 25,
      sodium: 2000, potassium: 3500, waterIntakeMl: 2500,
      vitaminRecommendation: '', doctorRecommendation: '', dieticianNotes: '',
      active: true
    };
  }

  openAddModal(): void { this.formModel = this.getEmptyForm(); this.editingPlan = null; this.showModal = true; }

  openEditModal(plan: DietPlan): void { this.formModel = { ...plan }; this.editingPlan = plan; this.showModal = true; }

  savePlan(): void {
    if (!this.formModel.name || !this.formModel.dietType) { this.msg = 'Name and Diet Type are required'; this.msgType = 'error'; return; }
    if (this.editingPlan) {
      this.dietPlanService.update(this.editingPlan.id!, this.formModel as DietPlan).subscribe({ next: () => { this.showModal = false; this.msg = 'Diet plan updated'; this.msgType = 'success'; this.loadPlans(); }, error: () => { this.msg = 'Failed to update'; this.msgType = 'error'; } });
    } else {
      this.dietPlanService.create(this.formModel as DietPlan).subscribe({ next: () => { this.showModal = false; this.msg = 'Diet plan created'; this.msgType = 'success'; this.loadPlans(); }, error: () => { this.msg = 'Failed to create'; this.msgType = 'error'; } });
    }
  }

  confirmDelete(plan: DietPlan): void { this.planToDelete = plan; this.showDeleteModal = true; }
  cancelDelete(): void { this.showDeleteModal = false; this.planToDelete = null; }

  deletePlan(): void {
    if (this.planToDelete) {
      this.dietPlanService.delete(this.planToDelete.id!).subscribe({ next: () => { this.showDeleteModal = false; this.msg = 'Diet plan deleted'; this.msgType = 'success'; this.loadPlans(); }, error: () => { this.msg = 'Failed to delete'; this.msgType = 'error'; } });
    }
  }

  getDietTypeLabel(type: string): string { return type; }
  getDietTypeBadgeClass(type: string): string {
    const map: Record<string, string> = { 'Diabetic': 'badge-danger', 'LowSalt': 'badge-info', 'LowFat': 'badge-warning', 'Cardiac': 'badge-danger', 'HighProtein': 'badge-primary', 'Liquid': 'badge-info', 'Soft': 'badge-secondary', 'Renal': 'badge-warning', 'Pediatric': 'badge-success', 'Pregnancy': 'badge-info', 'PostSurgery': 'badge-warning', 'Special': 'badge-primary' };
    return map[type] || 'badge-secondary';
  }
  getDietTypeColor(type: string): string {
    const map: Record<string, string> = { 'Diabetic': '#dc3545', 'LowSalt': '#0dcaf0', 'LowFat': '#fd7e14', 'Cardiac': '#dc3545', 'HighProtein': '#0d6efd', 'Liquid': '#0dcaf0', 'Soft': '#6c757d', 'Renal': '#fd7e14', 'Pediatric': '#198754', 'Pregnancy': '#0dcaf0', 'PostSurgery': '#fd7e14', 'Special': '#0d6efd' };
    return map[type] || '#6c757d';
  }
  togglePlanDetail(plan: DietPlan): void { this.expandedPlanId = this.expandedPlanId === (plan.id ?? null) ? null : (plan.id ?? null); }
  closeModal(): void { this.showModal = false; this.showDeleteModal = false; }

  getMeals(plan: DietPlan): { time: string; name: string; content: string }[] {
    return [
      { time: plan.breakfastTime || '07:30', name: 'Breakfast', content: plan.breakfast || '-' },
      { time: plan.morningSnacksTime || '10:00', name: 'Morning Snacks', content: plan.morningSnacks || '-' },
      { time: plan.lunchTime || '12:30', name: 'Lunch', content: plan.lunch || '-' },
      { time: plan.eveningSnacksTime || '16:00', name: 'Evening Snacks', content: plan.eveningSnacks || '-' },
      { time: plan.dinnerTime || '19:00', name: 'Dinner', content: plan.dinner || '-' },
      { time: plan.nightDietTime || '21:00', name: 'Night Diet', content: plan.nightDiet || '-' }
    ];
  }
}