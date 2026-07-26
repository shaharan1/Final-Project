import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TestOrderModel } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';

@Component({
  selector: 'app-sample-collection',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sample-collection.component.html',
  styleUrl: './sample-collection.component.css',
})
export class SampleCollectionComponent implements OnInit {

  orders: TestOrderModel[] = [];
  searchKeyword = '';
  loading = true;

  showReceiveModal = false;
  selectedOrder: TestOrderModel | null = null;
  receivedBy = '';

  constructor(
    private testOrderService: TestOrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.testOrderService.getByStatus('SAMPLE_COLLECTED').subscribe({
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
      o.testName?.toLowerCase().includes(kw)
    );
  }

  openReceiveModal(order: TestOrderModel): void {
    this.selectedOrder = order;
    this.receivedBy = '';
    this.showReceiveModal = true;
  }

  closeReceiveModal(): void {
    this.showReceiveModal = false;
    this.selectedOrder = null;
  }

  receiveSample(): void {
    if (!this.selectedOrder?.id || !this.receivedBy) {
      alert('Please enter receiver name');
      return;
    }
    this.testOrderService.receiveSample(this.selectedOrder.id, this.receivedBy).subscribe({
      next: () => {
        alert('Sample Received in Lab');
        this.closeReceiveModal();
        this.loadOrders();
      },
      error: (err) => { console.log(err); alert('Failed'); }
    });
  }
}
