import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { KitchenOrderService } from '../../../../services/dietary/kitchen-order.service';
import { MealScheduleService } from '../../../../services/dietary/meal-schedule.service';

@Component({
  selector: 'app-kitchen-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './kitchen-dashboard.component.html',
  styleUrl: './kitchen-dashboard.component.css'
})
export class KitchenDashboardComponent implements OnInit {
  loading = true;
  todayOrders: any[] = [];
  orderCounts: Record<string, number> = {};
  mealSchedule: any[] = [];
  currentMeal: any = null;

  constructor(
    private orderService: KitchenOrderService,
    private scheduleService: MealScheduleService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.orderService.getTodayOrders().subscribe({
      next: (orders) => {
        this.todayOrders = orders;
        this.orderCounts = {
          PENDING: orders.filter(o => o.status === 'PENDING').length,
          PREPARING: orders.filter(o => o.status === 'PREPARING').length,
          COOKING: orders.filter(o => o.status === 'COOKING').length,
          READY: orders.filter(o => o.status === 'READY').length,
          DELIVERED: orders.filter(o => o.status === 'DELIVERED').length,
          CANCELLED: orders.filter(o => o.status === 'CANCELLED').length
        };
        this.scheduleService.getActive().subscribe({
          next: (schedules) => {
            this.mealSchedule = schedules;
            this.currentMeal = schedules.find(s => s.currentMeal) || schedules[0] || null;
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: () => { this.loading = false; this.cdr.detectChanges(); }
        });
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  updateOrderStatus(orderId: number, newStatus: string): void {
    this.orderService.updateStatus(orderId, newStatus).subscribe({
      next: () => this.loadData(),
      error: () => {}
    });
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'PENDING': 'badge-warning', 'PREPARING': 'badge-info',
      'COOKING': 'badge-primary', 'READY': 'badge-success',
      'DELIVERED': 'badge-success', 'CANCELLED': 'badge-danger'
    };
    return map[status] || 'badge-secondary';
  }

  getPriorityClass(priority: string): string {
    const map: Record<string, string> = {
      'URGENT': 'badge-danger', 'HIGH': 'badge-warning', 'NORMAL': 'badge-info'
    };
    return map[priority] || 'badge-secondary';
  }

  getFlowWidth(status: string): number {
    const steps = ['PENDING', 'PREPARING', 'COOKING', 'READY', 'DELIVERED', 'CANCELLED'];
    const idx = steps.indexOf(status);
    return idx >= 0 ? ((idx + 1) / steps.length) * 100 : 0;
  }
}