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
  calorieTarget = 0;
  chartType = 'bar';

  private allOrders: any[] = [];
  private allPlans: any[] = [];
  private allAssignments: any[] = [];
  private typeAvgCalories: Record<string, number> = {};
  private overallAvgCalories = 2000;

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
        this.buildTypeCalorieMap();
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

  /* Average totalCalories per diet type (from real plan data) so we can derive
     calories for orders even when order.dietPlan is not populated. */
  private buildTypeCalorieMap(): void {
    const byType: Record<string, number[]> = {};
    let sum = 0, cnt = 0;
    for (const p of this.allPlans) {
      const c = p.totalCalories || 0;
      (byType[p.dietType] = byType[p.dietType] || []).push(c);
      sum += c; cnt += 1;
    }
    this.typeAvgCalories = {};
    for (const t of Object.keys(byType)) {
      const arr = byType[t];
      this.typeAvgCalories[t] = arr.reduce((s, x) => s + x, 0) / arr.length;
    }
    this.overallAvgCalories = cnt > 0 ? Math.round(sum / cnt) : 2000;
  }

  private caloriesForOrder(o: any): number {
    const t = o.dietType;
    if (t && this.typeAvgCalories[t] != null) return this.typeAvgCalories[t];
    if (o.dietPlan && o.dietPlan.totalCalories) return o.dietPlan.totalCalories;
    return this.overallAvgCalories;
  }

  private buildDietTypeData(): void {
    const typeColors: Record<string, string> = {
      'Regular': '#059669', 'Diabetic': '#dc3545', 'LowSalt': '#fd7e14',
      'Cardiac': '#0d6efd', 'HighProtein': '#10b981', 'Liquid': '#0891b2',
      'Soft': '#ca8a04', 'Renal': '#0d9488', 'Pediatric': '#e83e8c',
      'LowFat': '#7c3aed', 'Pregnancy': '#db2777', 'PostSurgery': '#dc2626', 'Special': '#6b7280'
    };
    const counts: Record<string, number> = {};
    for (const p of this.allPlans) {
      const t = p.dietType || 'Other';
      counts[t] = 0;
    }
    for (const o of this.allOrders) {
      const t = o.dietType || 'Other';
      counts[t] = (counts[t] || 0) + 1;
    }
    this.dietTypeData = Object.entries(counts).map(([type, count]) => ({
      type, count, color: typeColors[type] || '#6b7280'
    })).sort((a, b) => b.count - a.count);
  }

  private buildWardData(): void {
    const wardColors = ['#059669', '#0891b2', '#10b981', '#0d9488', '#0284c7', '#65a30d', '#ca8a04', '#dc2626'];
    const wardCounts: Record<string, number> = {};
    for (const o of this.allOrders) {
      const wardObj = o.ward || (o.admittedPatient && o.admittedPatient.ward);
      const ward = (wardObj && (wardObj.name || wardObj.wardName)) || o.bedNumber || 'Unassigned';
      wardCounts[ward] = (wardCounts[ward] || 0) + 1;
    }
    this.wardData = Object.entries(wardCounts).map(([ward, meals], i) => ({
      ward, meals, color: wardColors[i % wardColors.length]
    })).sort((a, b) => b.meals - a.meals);
  }

  private dayBucket(d: Date): { label: string; start: Date; end: Date } {
    const start = new Date(d.getFullYear(), d.getMonth(), d.getDate());
    const end = new Date(start.getTime() + 24 * 60 * 60 * 1000);
    return { label: `${start.getDate()}/${start.getMonth() + 1}`, start, end };
  }

  private monthBucket(d: Date): { label: string; start: Date; end: Date } {
    const start = new Date(d.getFullYear(), d.getMonth(), 1);
    const end = new Date(d.getFullYear(), d.getMonth() + 1, 1);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return { label: months[start.getMonth()], start, end };
  }

  private buildCalorieData(): void {
    const now = new Date();
    const buckets: { label: string; start: Date; end: Date }[] = [];
    if (this.period === 'week') {
      for (let i = 6; i >= 0; i--) { const d = new Date(now); d.setDate(d.getDate() - i); buckets.push(this.dayBucket(d)); }
    } else if (this.period === 'year') {
      for (let i = 11; i >= 0; i--) { const d = new Date(now); d.setMonth(d.getMonth() - i); buckets.push(this.monthBucket(d)); }
    } else {
      for (let i = 29; i >= 0; i--) { const d = new Date(now); d.setDate(d.getDate() - i); buckets.push(this.dayBucket(d)); }
    }

    this.dailyCalories = buckets.map(b => {
      const inBucket = this.allOrders.filter(o => {
        const t = o.createdAt || o.updatedAt ? new Date(o.createdAt || o.updatedAt) : null;
        return t && t >= b.start && t < b.end;
      });
      const calories = inBucket.reduce((s, o) => s + this.caloriesForOrder(o), 0);
      return { day: b.label, calories: Math.round(calories), orders: inBucket.length };
    });

    const total = this.dailyCalories.reduce((s, d) => s + d.calories, 0);
    this.calorieTarget = this.dailyCalories.length ? Math.round(total / this.dailyCalories.length) : 0;
  }

  private buildCompletionData(): void {
    const mealTimes = ['BREAKFAST', 'LUNCH', 'DINNER', 'MORNING_SNACKS', 'EVENING_SNACKS'];
    const labels = ['Breakfast', 'Lunch', 'Dinner', 'Morning Snacks', 'Evening Snacks'];
    const preparedStatuses = ['PREPARING', 'COOKING', 'READY', 'DELIVERED'];
    this.completionData = mealTimes.map((mt, i) => {
      const mealOrders = this.allOrders.filter(o => o.mealTime === mt);
      const prepared = mealOrders.filter(o => preparedStatuses.includes(o.status)).length;
      const delivered = mealOrders.filter(o => o.status === 'DELIVERED').length;
      const percentage = prepared > 0 ? Math.round((delivered / prepared) * 100) : 0;
      return { label: labels[i], delivered, prepared, percentage };
    });
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

  private calorieMinMax(): { min: number; max: number } {
    const vals = this.dailyCalories.map((d: any) => d.calories);
    let min = Math.min(...vals, 0);
    let max = Math.max(...vals, 1);
    if (max === min) max = min + 1;
    const pad = (max - min) * 0.15;
    min = Math.max(0, min - pad);
    max = max + pad;
    return { min, max };
  }

  getX(index: number): number {
    const n = Math.max(this.dailyCalories.length - 1, 1);
    return (index / n) * 400;
  }

  getY(value: number): number {
    const { min, max } = this.calorieMinMax();
    const y = 140 - ((value - min) / (max - min)) * 130;
    return Math.max(8, Math.min(142, y));
  }

  getCalorieAreaPoints(): string {
    if (!this.dailyCalories.length) return '';
    const pts = this.dailyCalories.map((d: any, i: number) => `${this.getX(i)},${this.getY(d.calories)}`);
    return `0,150 ${pts.join(' ')} 400,150`;
  }

  getCalorieLinePoints(): string {
    return this.dailyCalories.map((d: any, i: number) => `${this.getX(i)},${this.getY(d.calories)}`).join(' ');
  }

  getTargetY(): number {
    return this.getY(this.calorieTarget);
  }

  getDonutGradient(): string {
    const pct = this.getAverageCompletion();
    return `conic-gradient(#10b981 0% ${pct}%, #e9ecef ${pct}% 100%)`;
  }
}
