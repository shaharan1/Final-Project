import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { TestOrderModel, LabStats } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';
import { TestMasterService } from '../../../../services/test-master.service';
import { TestMasterModel } from '../../../../models/testMasterModel';

@Component({
  selector: 'app-lab-technician-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './lab-technician-dashboard.component.html',
  styleUrl: './lab-technician-dashboard.component.css',
})
export class LabTechnicianDashboardComponent implements OnInit {

  labStats: LabStats | null = null;
  recentOrders: TestOrderModel[] = [];
  testCatalog: TestMasterModel[] = [];
  loading = true;

  constructor(
    private testOrderService: TestOrderService,
    private testMasterService: TestMasterService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
    this.loadRecentOrders();
    this.loadTestCatalog();
  }

  loadStats(): void {
    this.testOrderService.getStats().subscribe({
      next: (res) => {
        this.labStats = res;
        this.cdr.markForCheck();
      }
    });
  }

  loadRecentOrders(): void {
    this.testOrderService.getAll().subscribe({
      next: (res) => {
        this.recentOrders = res.slice(0, 10);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.loading = false; }
    });
  }

  loadTestCatalog(): void {
    this.testMasterService.getAll().subscribe({
      next: (res) => {
        this.testCatalog = res;
        this.cdr.markForCheck();
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'status-pending';
      case 'SAMPLE_COLLECTED': return 'status-collected';
      case 'SAMPLE_RECEIVED': return 'status-received';
      case 'TESTING': return 'status-testing';
      case 'RESULT_ENTERED': return 'status-result';
      case 'VERIFIED': return 'status-verified';
      case 'COMPLETED': return 'status-completed';
      default: return 'status-pending';
    }
  }

  formatStatus(status: string): string {
    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  goTo(route: string): void {
    this.router.navigate([route]);
  }
}
