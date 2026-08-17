import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MealScheduleService } from '../../../../services/dietary/meal-schedule.service';
import { KitchenOrderService } from '../../../../services/dietary/kitchen-order.service';

@Component({
  selector: 'app-meal-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './meal-schedule.component.html',
  styleUrl: './meal-schedule.component.css'
})
export class MealScheduleComponent implements OnInit {
  schedules: any[] = [];
  todayOrders: any[] = [];
  loading = true;
  showModal = false;
  formModel: any = {};
  editingItem: any = null;
  orderCounts: Record<string, number> = {};

  constructor(
    private scheduleService: MealScheduleService,
    private orderService: KitchenOrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.scheduleService.getAll().subscribe({
      next: (schedules) => {
        this.schedules = schedules;
        this.orderService.getTodayOrders().subscribe({
          next: (orders) => {
            this.todayOrders = orders;
            this.schedules.forEach(s => {
              const key = s.mealName.toUpperCase().replace(/\s/g, '_').replace(/SNACKS/, 'SNACKS').replace(/NIGHT DIET/, 'NIGHT_DIET');
              this.orderCounts[s.mealName] = orders.filter(o => o.mealTime === key || o.mealTime === s.mealName.toUpperCase()).length;
            });
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: () => { this.loading = false; this.cdr.detectChanges(); }
        });
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  openAddModal(): void { this.formModel = { mealName: '', servingTime: '', preparationStartTime: '', preparationEndTime: '', status: 'ACTIVE', totalOrdersToday: 0 }; this.editingItem = null; this.showModal = true; }

  openEditModal(item: any): void { this.formModel = { ...item }; this.editingItem = item; this.showModal = true; }

  saveItem(): void {
    if (!this.formModel.mealName || !this.formModel.servingTime) return;
    if (this.editingItem) {
      this.scheduleService.update(this.editingItem.id, this.formModel).subscribe({ next: () => { this.showModal = false; this.loadData(); } });
    } else {
      this.scheduleService.create(this.formModel).subscribe({ next: () => { this.showModal = false; this.loadData(); } });
    }
  }

  getMealProgress(schedule: any): number {
    if (!schedule.totalOrdersToday) return 0;
    return Math.round(((schedule.completedOrders || 0) / schedule.totalOrdersToday) * 100);
  }

  getStatusBadge(status: string): string {
    return status === 'ACTIVE' ? 'badge-success' : 'badge-secondary';
  }

  getMealIcon(mealName: string): string {
    const map: Record<string, string> = {
      'Breakfast': '🌅', 'Morning Snacks': '🍎', 'Lunch': '☀️',
      'Evening Snacks': '🍪', 'Dinner': '🌙', 'Night Diet': '💫'
    };
    return map[mealName] || '🍽️';
  }

  totalOrdersToday(): number {
    return this.schedules.reduce((sum, s) => sum + (this.orderCounts[s.mealName] || 0), 0);
  }

  totalPlanned(): number {
    return this.schedules.reduce((sum, s) => sum + (s.totalOrdersToday || 0), 0);
  }
}