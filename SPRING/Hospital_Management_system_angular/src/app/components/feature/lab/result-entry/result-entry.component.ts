import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TestOrderModel } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';
import { StorageService } from '../../../../services/storage.service';

@Component({
  selector: 'app-result-entry',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './result-entry.component.html',
  styleUrl: './result-entry.component.css',
})
export class ResultEntryComponent implements OnInit {

  orders: TestOrderModel[] = [];
  searchKeyword = '';
  loading = true;

  showResultModal = false;
  showStartModal = false;
  selectedOrder: TestOrderModel | null = null;
  resultValue = '';
  resultNotes = '';
  enteredBy = '';
  userName = '';

  activeTab = 'READY';

  constructor(
    private testOrderService: TestOrderService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const user = this.storage.getUser();
    this.userName = user?.name || '';
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.testOrderService.getAll().subscribe({
      next: (res) => {
        this.orders = res.filter(o =>
          ['SAMPLE_RECEIVED', 'TESTING', 'RESULT_ENTERED'].includes(o.orderStatus)
        );
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.loading = false; }
    });
  }

  get filteredOrders(): TestOrderModel[] {
    let list = this.orders.filter(o => {
      if (this.activeTab === 'READY') return o.orderStatus === 'SAMPLE_RECEIVED';
      if (this.activeTab === 'TESTING') return o.orderStatus === 'TESTING';
      if (this.activeTab === 'DONE') return o.orderStatus === 'RESULT_ENTERED';
      return true;
    });
    if (this.searchKeyword) {
      const kw = this.searchKeyword.toLowerCase();
      list = list.filter(o =>
        o.patientName?.toLowerCase().includes(kw) ||
        o.testName?.toLowerCase().includes(kw)
      );
    }
    return list;
  }

  get readyCount(): number {
    return this.orders.filter(o => o.orderStatus === 'SAMPLE_RECEIVED').length;
  }

  get testingCount(): number {
    return this.orders.filter(o => o.orderStatus === 'TESTING').length;
  }

  get doneCount(): number {
    return this.orders.filter(o => o.orderStatus === 'RESULT_ENTERED').length;
  }

  openStartModal(order: TestOrderModel): void {
    this.selectedOrder = order;
    this.showStartModal = true;
  }

  closeStartModal(): void {
    this.showStartModal = false;
    this.selectedOrder = null;
  }

  startTesting(): void {
    if (!this.selectedOrder?.id) return;
    this.testOrderService.startTesting(this.selectedOrder.id).subscribe({
      next: () => {
        alert('Testing Started');
        this.closeStartModal();
        this.loadOrders();
      },
      error: (err) => { console.log(err); alert('Failed'); }
    });
  }

  openResultModal(order: TestOrderModel): void {
    this.selectedOrder = order;
    this.resultValue = '';
    this.resultNotes = '';
    this.enteredBy = this.userName;
    this.showResultModal = true;
  }

  closeResultModal(): void {
    this.showResultModal = false;
    this.selectedOrder = null;
  }

  submitResult(): void {
    if (!this.selectedOrder?.id || !this.resultValue || !this.enteredBy) {
      alert('Please fill result value and your name');
      return;
    }
    this.testOrderService.enterResult(this.selectedOrder.id, this.resultValue, this.resultNotes, this.enteredBy).subscribe({
      next: () => {
        alert('Result Entered Successfully');
        this.closeResultModal();
        this.loadOrders();
      },
      error: (err) => { console.log(err); alert('Failed'); }
    });
  }
}
