import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DietaryDashboardService } from '../../../../services/dietary/dietary-dashboard.service';

@Component({
  selector: 'app-dietary-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dietary-dashboard.component.html',
  styleUrl: './dietary-dashboard.component.css'
})
export class DietaryDashboardComponent implements OnInit {
  loading = true;
  stats: any = {};
  animatedValues: Record<string, number> = {};
  todayOrders: any[] = [];
  activeAlerts: any[] = [];
  mealSchedule: any[] = [];
  currentTime = new Date();

  kpiCards = [
    { key: 'todayMealsServed', label: "Today's Meals Served", icon: 'plate', color: '#0d6efd', target: 0 },
    { key: 'activePatients', label: 'Active Patients', icon: 'patient', color: '#6610f2', target: 0 },
    { key: 'dietPlansAssigned', label: 'Diet Plans Assigned', icon: 'clipboard', color: '#198754', target: 0 },
    { key: 'kitchenOrders', label: 'Kitchen Orders', icon: 'kitchen', color: '#fd7e14', target: 0 },
    { key: 'pendingMealDelivery', label: 'Pending Delivery', icon: 'truck', color: '#dc3545', target: 0 },
    { key: 'dieticiansOnDuty', label: 'Dieticians On Duty', icon: 'user', color: '#0dcaf0', target: 0 },
    { key: 'caloriesServedToday', label: 'Calories Served', icon: 'fire', color: '#ffc107', target: 0 },
    { key: 'mealCompletionRate', label: 'Completion Rate', icon: 'chart', color: '#198754', target: 0 }
  ];

  subModules = [
    { label: 'Patient Diet', icon: 'patient', route: '/dietary/patient-diet', color: '#0d6efd', desc: 'Manage patient diets' },
    { label: 'Diet Plans', icon: 'clipboard', route: '/dietary/diet-plans', color: '#6610f2', desc: 'Create & manage plans' },
    { label: 'Dieticians', icon: 'user', route: '/dietary/dieticians', color: '#198754', desc: 'Dietician management' },
    { label: 'Meal Schedule', icon: 'clock', route: '/dietary/meal-schedule', color: '#fd7e14', desc: 'View meal timeline' },
    { label: 'Kitchen Dashboard', icon: 'kitchen', route: '/dietary/kitchen-dashboard', color: '#dc3545', desc: 'Kitchen operations' },
    { label: 'Kitchen Orders', icon: 'order', route: '/dietary/kitchen-orders', color: '#0dcaf0', desc: 'Manage orders' },
    { label: 'Nutrition Analytics', icon: 'chart', route: '/dietary/nutrition-analytics', color: '#ffc107', desc: 'Analytics & insights' },
    { label: 'Diet Reports', icon: 'report', route: '/dietary/diet-reports', color: '#20c997', desc: 'Reports & export' },
    { label: 'Diet Alerts', icon: 'alert', route: '/dietary/diet-alerts', color: '#dc3545', desc: 'Special alerts' }
  ];

  constructor(
    private dashboardService: DietaryDashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
this.dashboardService.getDashboardStats().subscribe({
      next: (data: any) => {
        this.stats = data;
        this.todayOrders = (data.orders || []).slice(0, 8);
        this.activeAlerts = (data.alerts || []).slice(0, 5);
        this.mealSchedule = data.schedules || [];
        
        this.kpiCards[0].target = data.todayMealsServed || 0;
        this.kpiCards[1].target = (data.activePatients || []).length || 0;
        this.kpiCards[2].target = data.dietPlansAssigned || 0;
        this.kpiCards[3].target = data.kitchenOrders || 0;
        this.kpiCards[4].target = data.pendingMealDelivery || 0;
        this.kpiCards[5].target = data.dieticiansOnDuty || 0;
        this.kpiCards[6].target = data.caloriesServedToday || 0;
        this.kpiCards[7].target = data.mealCompletionRate || 0;

        this.kpiCards.forEach(card => this.animateValue(card.key, card.target));
        
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  animateValue(key: string, target: number): void {
    if (target === 0) { this.animatedValues[key] = 0; return; }
    const duration = 1200;
    const startTime = performance.now();
    const step = (now: number) => {
      const progress = Math.min((now - startTime) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      this.animatedValues[key] = Math.round(eased * target);
      this.cdr.markForCheck();
      if (progress < 1) requestAnimationFrame(step);
    };
    requestAnimationFrame(step);
  }

  getAnimated(key: string): number {
    return this.animatedValues[key] ?? 0;
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'PENDING': 'badge-warning', 'PREPARING': 'badge-info',
      'COOKING': 'badge-primary', 'READY': 'badge-success',
      'DELIVERED': 'badge-success', 'CANCELLED': 'badge-danger',
      'ACTIVE': 'badge-danger', 'HIGH': 'badge-warning',
      'CRITICAL': 'badge-danger', 'MEDIUM': 'badge-warning',
      'LOW': 'badge-info', 'ACKNOWLEDGED': 'badge-info',
      'RESOLVED': 'badge-success'
    };
    return map[status] || 'badge-secondary';
  }

  getAlertIcon(type: string): string {
    const map: Record<string, string> = {
      'DIABETIC': 'dYc,', 'LOW_SODIUM': 'dY,', 'ALLERGY': '�s��,?',
      'NPO': 'dYs�', 'FASTING': '�?�', 'CRITICAL': 'dYs"',
      'FOOD_ALLERGY': 'dY�o', 'KITCHEN_ALERT': 'dY?3', 'LATE_DELIVERY': 'dY"�'
    };
    return map[type] || '�s��,?';
  }

  getProgressPercent(value: number): number {
    return Math.min(value, 100);
  }

  getAlertSeverityColor(severity: string): string {
    const map: Record<string, string> = { 'CRITICAL': '#dc3545', 'HIGH': '#fd7e14', 'MEDIUM': '#ffc107', 'LOW': '#0dcaf0' };
    return map[severity] || '#0dcaf0';
  }

  goToOrder(id: number): void {
    /* Navigate to order detail */
  }
}