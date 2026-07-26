import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TestOrderModel } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';

@Component({
  selector: 'app-lab-reception',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lab-reception.component.html',
  styleUrl: './lab-reception.component.css',
})
export class LabReceptionComponent implements OnInit {

  orders: TestOrderModel[] = [];
  searchKeyword = '';
  loading = true;

  showCollectModal = false;
  selectedOrder: TestOrderModel | null = null;
  collectorName = '';
  sampleType = '';

  sampleTypes = ['Blood', 'Urine', 'Stool', 'Sputum', 'CSF', 'Tissue', 'Swab', 'Other'];

  constructor(
    private testOrderService: TestOrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.testOrderService.getByStatus('PENDING').subscribe({
      next: (res) => {
        this.orders = res;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.loading = false; }
    });
  }

  get filteredOrders(): TestOrderModel[] {
    if (!this.searchKeyword) return this.orders;
    const kw = this.searchKeyword.toLowerCase();
    return this.orders.filter(o =>
      o.patientName?.toLowerCase().includes(kw) ||
      o.patientCode?.toLowerCase().includes(kw) ||
      o.testName?.toLowerCase().includes(kw) ||
      o.testCode?.toLowerCase().includes(kw)
    );
  }

  openCollectModal(order: TestOrderModel): void {
    this.selectedOrder = order;
    this.collectorName = '';
    this.sampleType = '';
    this.showCollectModal = true;
  }

  closeCollectModal(): void {
    this.showCollectModal = false;
    this.selectedOrder = null;
  }

  collectSample(): void {
    if (!this.selectedOrder?.id || !this.collectorName || !this.sampleType) {
      alert('Please fill all fields');
      return;
    }
    this.testOrderService.collectSample(this.selectedOrder.id, this.collectorName, this.sampleType).subscribe({
      next: () => {
        alert('Sample Collected Successfully');
        this.closeCollectModal();
        this.loadOrders();
      },
      error: (err) => { console.log(err); alert('Failed'); }
    });
  }
}
