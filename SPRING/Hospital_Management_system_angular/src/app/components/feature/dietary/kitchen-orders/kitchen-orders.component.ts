import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { KitchenOrderService } from '../../../../services/dietary/kitchen-order.service';
import { DietPlanService } from '../../../../services/dietary/diet-plan.service';

@Component({
  selector: 'app-kitchen-orders',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './kitchen-orders.component.html',
  styleUrl: './kitchen-orders.component.css'
})
export class KitchenOrdersComponent implements OnInit {
  orders: any[] = [];
  filteredOrders: any[] = [];
  loading = true;
  searchTerm = '';
  filterStatus = '';
  filterMealTime = '';
  filterPriority = '';
  showModal = false;
  formModel: any = {};
  msg = '';
  msgType = '';
  mealTimes = ['BREAKFAST', 'MORNING_SNACKS', 'LUNCH', 'EVENING_SNACKS', 'DINNER', 'NIGHT_DIET'];

  constructor(
    private orderService: KitchenOrderService,
    private dietPlanService: DietPlanService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.orderService.getAll().subscribe({
      next: (data) => { this.orders = data; this.filteredOrders = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  filterOrders(): void {
    let result = [...this.orders];
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(o => (o.orderNumber || '').toLowerCase().includes(term) || (o.patient?.name || '').toLowerCase().includes(term));
    }
    if (this.filterStatus) result = result.filter(o => o.status === this.filterStatus);
    if (this.filterMealTime) result = result.filter(o => o.mealTime === this.filterMealTime);
    if (this.filterPriority) result = result.filter(o => o.priority === this.filterPriority);
    this.filteredOrders = result;
  }

  openAddModal(): void {
    this.formModel = {
      patientId: null, mealTime: '', mealType: '', dietType: '',
      priority: 'NORMAL', status: 'PENDING', kitchenNotes: '', bedNumber: '', specialDiet: false
    };
    this.showModal = true;
  }

  saveOrder(): void {
    this.orderService.create(this.formModel).subscribe({
      next: () => { this.showModal = false; this.msg = 'Order created'; this.msgType = 'success'; this.loadData(); },
      error: () => { this.msg = 'Failed to create order'; this.msgType = 'error'; }
    });
  }

  updateOrderStatus(orderId: number, newStatus: string): void {
    this.orderService.updateStatus(orderId, newStatus).subscribe({
      next: () => { this.msg = 'Order status updated'; this.msgType = 'success'; this.loadData(); },
      error: () => { this.msg = 'Failed to update order'; this.msgType = 'error'; }
    });
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = { 'PENDING': 'badge-warning', 'PREPARING': 'badge-info', 'COOKING': 'badge-primary', 'READY': 'badge-success', 'DELIVERED': 'badge-success', 'CANCELLED': 'badge-danger' };
    return map[status] || 'badge-secondary';
  }

  getPriorityClass(priority: string): string {
    const map: Record<string, string> = { 'URGENT': 'badge-danger', 'HIGH': 'badge-warning', 'NORMAL': 'badge-info' };
    return map[priority] || 'badge-secondary';
  }

  printSlip(order: any): void {
    const printWindow = window.open('', '_blank', 'width=800,height=600');
    if (printWindow) {
      printWindow.document.write(`<html><head><title>Kitchen Slip ${order.orderNumber}</title><style>body{font-family:sans-serif;padding:24px;color:#333;}h1{color:#0d6efd;}table{border-collapse:collapse;width:100%;}th,td{border:1px solid #ddd;padding:8px;text-align:left;}th{background:#f0f0f0;}</style></head><body><h1>Kitchen Slip - ${order.orderNumber}</h1><p><strong>Patient:</strong> ${order.patient?.name}</p><p><strong>Ward:</strong> ${order.ward?.name || order.bedNumber}</p><p><strong>Meal:</strong> ${order.mealTime}</p><p><strong>Diet:</strong> ${order.dietType}</p><p><strong>Priority:</strong> ${order.priority}</p><p><strong>Status:</strong> ${order.status}</p><p><strong>Notes:</strong> ${order.kitchenNotes || '-'}</p><p><strong>Order Date:</strong> ${new Date().toLocaleString()}</p></body></html>`);
      printWindow.document.close();
      printWindow.print();
    }
  }
}