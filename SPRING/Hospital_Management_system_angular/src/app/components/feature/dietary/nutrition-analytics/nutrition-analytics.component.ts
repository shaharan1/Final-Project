import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DietaryDashboardService } from '../../../../services/dietary/dietary-dashboard.service';

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

  constructor(private dashboardService: DietaryDashboardService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.loadAnalytics(); }

  loadAnalytics(): void {
    this.loading = true;
    this.dashboardService.getDashboardStats().subscribe({
      next: (data: any) => {
        this.dietTypeData = this.generateDietTypeData(data);
        this.wardData = this.generateWardData(data);
        this.dailyCalories = this.generateCalorieData(data);
        this.completionData = this.generateCompletionData(data);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  generateDietTypeData(data: any): any[] {
    return [
      { type: 'Regular', count: 45, color: '#0d6efd' },
      { type: 'Diabetic', count: 28, color: '#dc3545' },
      { type: 'Low Salt', count: 22, color: '#fd7e14' },
      { type: 'Cardiac', count: 18, color: '#6610f2' },
      { type: 'High Protein', count: 15, color: '#198754' },
      { type: 'Liquid', count: 12, color: '#0dcaf0' },
      { type: 'Other', count: 10, color: '#6c757d' }
    ];
  }

  generateWardData(data: any): any[] {
    return [
      { ward: 'Ward A', meals: 120, color: '#0d6efd' },
      { ward: 'Ward B', meals: 95, color: '#6610f2' },
      { ward: 'Ward C', meals: 78, color: '#198754' },
      { ward: 'ICU', meals: 45, color: '#dc3545' },
      { ward: 'Pediatrics', meals: 38, color: '#fd7e14' },
      { ward: 'Maternity', meals: 32, color: '#0dcaf0' }
    ];
  }

  generateCalorieData(data: any): any[] {
    return Array.from({ length: 30 }, (_, i) => ({
      day: `Day ${i + 1}`,
      calories: Math.floor(1800 + Math.random() * 600),
      target: 2200
    }));
  }

  generateCompletionData(data: any): any[] {
    return [
      { label: 'Breakfast', delivered: 85, prepared: 100 },
      { label: 'Lunch', delivered: 78, prepared: 95 },
      { label: 'Dinner', delivered: 82, prepared: 90 },
      { label: 'Snacks', delivered: 90, prepared: 95 }
    ];
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
    const avg = this.completionData.reduce((s: number, d: any) => s + (d.delivered || 0), 0);
    return Math.round(avg / this.completionData.length);
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
    return `conic-gradient(#198754 0% ${pct}%, rgba(255,255,255,0.05) ${pct}% 100%)`;
  }
}