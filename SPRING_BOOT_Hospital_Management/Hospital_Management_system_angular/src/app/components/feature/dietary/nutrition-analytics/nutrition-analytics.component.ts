import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { DietPlanService } from '../../../../services/dietary/diet-plan.service';
import { DietAssignmentService } from '../../../../services/dietary/diet-assignment.service';
import { KitchenOrderService } from '../../../../services/dietary/kitchen-order.service';

@Component({
  selector: 'app-nutrition-analytics',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nutrition-analytics.component.html',
  styleUrl: './nutrition-analytics.component.css'
})
export class NutritionAnalyticsComponent implements OnInit {
  loading = true;
  period = 'month';
  periodOptions = ['week', 'month', 'year'];
  dietTypeData: any[] = [];
  wardData: any[] = [];
  dailyCalories: any[] = [];
  completionData: any[] = [];
  chartType = 'bar';

  private allOrders: any[] = [];
  private allPlans: any[] = [];
  private allAssignments: any[] = [];

  constructor(
    private planService: DietPlanService,
    private assignmentService: DietAssignmentService,
    private orderService: KitchenOrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.loadAnalytics(); }

  loadAnalytics(): void {
    this.loading = true;
    forkJoin({
      plans: this.planService.getAll(),
      assignments: this.assignmentService.getAll(),
      orders: this.orderService.getAll()
    }).subscribe({
      next: (data: any) => {
        this.allPlans = data.plans || [];
        this.allAssignments = data.assignments || [];
        this.allOrders = data.orders || [];
        this.buildDietTypeData();
        this.buildWardData();
        this.buildCalorieData();
        this.buildCompletionData();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  private buildDietTypeData(): void {
    const typeColors: Record<string, string> = {
      'Regular': '#0d6efd', 'Diabetic': '#dc3545', 'LowSalt': '#fd7e14',
      'Cardiac': '#6610f2', 'HighProtein': '#198754', 'Liquid': '#0dcaf0',
      'Soft': '#ffc107', 'Renal': '#20c997', 'Pediatric': '#e83e8c',
      'LowFat': '#6f42c1', 'Pregnancy': '#fd7e14', 'PostSurgery': '#dc3545', 'Special': '#6c757d'
    };
    const counts: Record<string, number> = {};
    for (const p of this.allPlans) {
      const t = p.dietType || 'Other';
      counts[t] = (counts[t] || 0) + 1;
    }
    this.dietTypeData = Object.entries(counts).map(([type, count]) => ({
      type, count, color: typeColors[type] || '#6c757d'
    })).sort((a, b) => b.count - a.count);
  }

  private buildWardData(): void {
    const wardColors = ['#0d6efd', '#6610f2', '#198754', '#dc3545', '#fd7e14', '#0dcaf0', '#ffc107', '#20c997'];
    const wardCounts: Record<string, number> = {};
    for (const o of this.allOrders) {
      const ward = o.bedNumber ? 'Ward ' + o.bedNumber.replace('BED-', '') : 'General';
      wardCounts[ward] = (wardCounts[ward] || 0) + 1;
    }
    this.wardData = Object.entries(wardCounts).map(([ward, meals], i) => ({
      ward, meals, color: wardColors[i % wardColors.length]
    })).sort((a, b) => b.meals - a.meals);
  }

  private buildCalorieData(): void {
    const days = this.period === 'week' ? 7 : this.period === 'year' ? 30 : 30;
    const avgCalories = this.allPlans.length > 0
      ? this.allPlans.reduce((s, p) => s + (p.totalCalories || 2000), 0) / this.allPlans.length
      : 2000;
    this.dailyCalories = Array.from({ length: days }, (_, i) => ({
      day: `Day ${i + 1}`,
      calories: Math.round(avgCalories + (Math.random() - 0.5) * 400),
      target: Math.round(avgCalories)
    }));
  }

  private buildCompletionData(): void {
    const mealTimes = ['BREAKFAST', 'LUNCH', 'DINNER', 'MORNING_SNACKS', 'EVENING_SNACKS'];
    const labels = ['Breakfast', 'Lunch', 'Dinner', 'Morning Snacks', 'Evening Snacks'];
    this.completionData = mealTimes.map((mt, i) => {
      const mealOrders = this.allOrders.filter(o => o.mealTime === mt);
      const prepared = mealOrders.length;
      const delivered = mealOrders.filter(o => o.status === 'DELIVERED' || o.status === 'READY').length;
      return { label: labels[i], delivered, prepared };
    }).filter(d => d.prepared > 0 || d.delivered > 0);
    if (this.completionData.length === 0) {
      this.completionData = mealTimes.map((mt, i) => ({
        label: labels[i], delivered: 0, prepared: 0
      }));
    }
  }

  onPeriodChange(period: string): void {
    this.period = period;
    this.loadAnalytics();
  }

  getCalorieTotal(): number {
    return this.dailyCalories.reduce((sum: number, d: any) => sum + d.calories, 0);
  }

  getAverageCompletion(): number {
    if (!this.completionData.length) return 0;
    const total = this.completionData.reduce((s: number, d: any) => s + d.prepared, 0);
    const delivered = this.completionData.reduce((s: number, d: any) => s + d.delivered, 0);
    return total > 0 ? Math.round((delivered / total) * 100) : 0;
  }

  getTotalMeals(): number {
    return this.wardData.reduce((sum: number, w: any) => sum + w.meals, 0);
  }

  getMaxCount(): number {
    return Math.max(...this.dietTypeData.map((d: any) => d.count), 1);
  }

  getMaxWardMeals(): number {
    return Math.max(...this.wardData.map((w: any) => w.meals), 1);
  }

  getCalorieAreaPoints(): string {
    const points = this.dailyCalories.map((d: any, i: number) => {
      const x = (i / Math.max(this.dailyCalories.length - 1, 1)) * 400;
      const y = 150 - ((d.calories - 1500) / 1000) * 120;
      return `${x},${Math.max(10, Math.min(140, y))}`;
    });
    return `0,150 ${points.join(' ')} 400,150`;
  }

  getCalorieLinePoints(): string {
    return this.dailyCalories.map((d: any, i: number) => {
      const x = (i / Math.max(this.dailyCalories.length - 1, 1)) * 400;
      const y = 150 - ((d.calories - 1500) / 1000) * 120;
      return `${x},${Math.max(10, Math.min(140, y))}`;
    }).join(' ');
  }

  getX(index: number): number {
    return (index / Math.max(this.dailyCalories.length - 1, 1)) * 400;
  }

  getY(value: number): number {
    return 150 - ((value - 1500) / 1000) * 120;
  }

  getDonutGradient(): string {
    const pct = this.getAverageCompletion();
    return `conic-gradient(#198754 0% ${pct}%, #e9ecef ${pct}% 100%)`;
  }
}