import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TestOrderModel } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';
import { StorageService } from '../../../../services/storage.service';

@Component({
  selector: 'app-lab-verification',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lab-verification.component.html',
  styleUrl: './lab-verification.component.css',
})
export class LabVerificationComponent implements OnInit {

  orders: TestOrderModel[] = [];
  searchKeyword = '';
  loading = true;

  showVerifyModal = false;
  showPdfModal = false;
  selectedOrder: TestOrderModel | null = null;
  verifiedBy = '';
  verificationNotes = '';
  userName = '';
  pdfUrl: string | null = null;

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
    this.testOrderService.getByStatus('RESULT_ENTERED').subscribe({
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
      o.testName?.toLowerCase().includes(kw) ||
      o.resultValue?.toLowerCase().includes(kw)
    );
  }

  openVerifyModal(order: TestOrderModel): void {
    this.selectedOrder = order;
    this.verifiedBy = this.userName;
    this.verificationNotes = '';
    this.showVerifyModal = true;
  }

  closeVerifyModal(): void {
    this.showVerifyModal = false;
    this.selectedOrder = null;
  }

  verifyResult(): void {
    if (!this.selectedOrder?.id || !this.verifiedBy) {
      alert('Please enter verifier name');
      return;
    }
    this.testOrderService.verifyResult(this.selectedOrder.id, this.verifiedBy, this.verificationNotes).subscribe({
      next: () => {
        alert('Result Verified Successfully');
        this.closeVerifyModal();
        this.loadOrders();
      },
      error: (err) => { console.log(err); alert('Failed'); }
    });
  }

  viewPdf(order: TestOrderModel): void {
    if (!order.id) return;
    this.testOrderService.downloadReportPdf(order.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        this.pdfUrl = url;
        this.selectedOrder = order;
        this.showPdfModal = true;
      },
      error: (err) => { console.log(err); alert('PDF not available yet. Verify the result first.'); }
    });
  }

  downloadPdf(): void {
    if (this.pdfUrl) {
      const a = document.createElement('a');
      a.href = this.pdfUrl;
      a.download = 'Lab_Report_' + (this.selectedOrder?.patientCode || 'report') + '.pdf';
      a.click();
    }
  }

  closePdfModal(): void {
    this.showPdfModal = false;
    this.pdfUrl = null;
    this.selectedOrder = null;
  }
}
