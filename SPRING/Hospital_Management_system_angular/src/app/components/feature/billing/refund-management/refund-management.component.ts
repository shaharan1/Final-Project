import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RefundService } from '../../../../services/billing/refund.service';
import { PaymentService } from '../../../../services/billing/payment.service';
import { Refund } from '../../../../models/billing/refund.model';

@Component({
  selector: 'app-refund-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './refund-management.component.html',
  styleUrls: ['./refund-management.component.css']
})
export class RefundManagementComponent implements OnInit {
  activeTab: 'new' | 'history' = 'new';
  loading = false;
  loadingHistory = false;
  error = '';
  successMessage = '';

  refunds: Refund[] = [];
  filteredRefunds: Refund[] = [];

  searchQuery = '';
  statusFilter = '';

  invoiceSearch = '';
  invoiceData: any = null;
  invoiceNotFound = false;

  newRefund: Partial<Refund> = {
    invoiceNumber: '',
    patientId: 0,
    patientName: '',
    refundAmount: 0,
    refundReason: '',
    refundType: '',
    refundStatus: 'PENDING',
    processedBy: ''
  };

  showApproveModal = false;
  showRejectModal = false;
  showProcessModal = false;
  showDetailModal = false;
  selectedRefund: Refund | null = null;
  approveBy = '';
  rejectReason = '';
  processingAction = false;

  totalRefunds = 0;
  pendingCount = 0;
  approvedCount = 0;
  processedCount = 0;
  totalRefundAmount = 0;

  constructor(
    private refundService: RefundService,
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadRefunds();
  }

  loadRefunds(): void {
    this.loadingHistory = true;
    this.error = '';
    this.refundService.getAll().subscribe({
      next: (data: Refund[]) => {
        this.refunds = data;
        this.filteredRefunds = [...this.refunds];
        this.computeStats();
        this.loadingHistory = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to load refund data';
        this.loadingHistory = false;
        this.cdr.markForCheck();
      }
    });
  }

  computeStats(): void {
    this.totalRefunds = this.refunds.length;
    this.pendingCount = this.refunds.filter(r => r.refundStatus === 'PENDING').length;
    this.approvedCount = this.refunds.filter(r => r.refundStatus === 'APPROVED').length;
    this.processedCount = this.refunds.filter(r => r.refundStatus === 'PROCESSED').length;
    this.totalRefundAmount = this.refunds.reduce((sum, r) => sum + (r.refundAmount || 0), 0);
  }

  lookupInvoice(): void {
    if (!this.invoiceSearch.trim()) return;
    this.loading = true;
    this.invoiceNotFound = false;
    this.invoiceData = null;
    this.paymentService.getByInvoice(this.invoiceSearch).subscribe({
      next: (data: any) => {
        if (data && data.length > 0) {
          this.invoiceData = data[0];
          this.newRefund.invoiceNumber = this.invoiceData.invoiceNumber || this.invoiceSearch;
          this.newRefund.patientId = this.invoiceData.patientId || 0;
          this.newRefund.patientName = this.invoiceData.patientName || '';
        } else {
          this.invoiceNotFound = true;
        }
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.invoiceNotFound = true;
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  submitRefund(): void {
    if (!this.newRefund.invoiceNumber || !this.newRefund.refundAmount || !this.newRefund.refundType) return;
    this.loading = true;
    this.error = '';
    this.refundService.create(this.newRefund).subscribe({
      next: (saved: Refund) => {
        this.refunds.unshift(saved);
        this.filteredRefunds = [...this.refunds];
        this.computeStats();
        this.resetRefundForm();
        this.loading = false;
        this.successMessage = 'Refund request submitted successfully!';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to submit refund request';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  resetRefundForm(): void {
    this.newRefund = {
      invoiceNumber: '',
      patientId: 0,
      patientName: '',
      refundAmount: 0,
      refundReason: '',
      refundType: '',
      refundStatus: 'PENDING',
      processedBy: ''
    };
    this.invoiceSearch = '';
    this.invoiceData = null;
    this.invoiceNotFound = false;
  }

  filterRefunds(): void {
    let result = [...this.refunds];
    if (this.searchQuery.trim()) {
      const q = this.searchQuery.toLowerCase();
      result = result.filter(r =>
        (r.refundReference || '').toLowerCase().includes(q) ||
        (r.invoiceNumber || '').toLowerCase().includes(q) ||
        (r.patientName || '').toLowerCase().includes(q)
      );
    }
    if (this.statusFilter) {
      result = result.filter(r => r.refundStatus === this.statusFilter);
    }
    this.filteredRefunds = result;
  }

  openApproveModal(refund: Refund): void {
    this.selectedRefund = refund;
    this.approveBy = '';
    this.showApproveModal = true;
  }

  approveRefund(): void {
    if (!this.selectedRefund || !this.approveBy.trim()) return;
    this.processingAction = true;
    this.refundService.approve(this.selectedRefund.id!, this.approveBy).subscribe({
      next: (updated: Refund) => {
        this.updateRefundInList(updated);
        this.showApproveModal = false;
        this.processingAction = false;
        this.successMessage = 'Refund approved successfully!';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to approve refund';
        this.processingAction = false;
        this.cdr.markForCheck();
      }
    });
  }

  openRejectModal(refund: Refund): void {
    this.selectedRefund = refund;
    this.rejectReason = '';
    this.showRejectModal = true;
  }

  rejectRefund(): void {
    if (!this.selectedRefund || !this.rejectReason.trim()) return;
    this.processingAction = true;
    this.refundService.reject(this.selectedRefund.id!, this.rejectReason).subscribe({
      next: (updated: Refund) => {
        this.updateRefundInList(updated);
        this.showRejectModal = false;
        this.processingAction = false;
        this.successMessage = 'Refund rejected.';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to reject refund';
        this.processingAction = false;
        this.cdr.markForCheck();
      }
    });
  }

  openProcessModal(refund: Refund): void {
    this.selectedRefund = refund;
    this.showProcessModal = true;
  }

  processRefund(): void {
    if (!this.selectedRefund) return;
    this.processingAction = true;
    this.refundService.process(this.selectedRefund.id!).subscribe({
      next: (updated: Refund) => {
        this.updateRefundInList(updated);
        this.showProcessModal = false;
        this.processingAction = false;
        this.successMessage = 'Refund processed successfully!';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to process refund';
        this.processingAction = false;
        this.cdr.markForCheck();
      }
    });
  }

  viewRefundDetail(refund: Refund): void {
    this.selectedRefund = refund;
    this.showDetailModal = true;
  }

  closeModals(): void {
    this.showApproveModal = false;
    this.showRejectModal = false;
    this.showProcessModal = false;
    this.showDetailModal = false;
    this.selectedRefund = null;
  }

  private updateRefundInList(updated: Refund): void {
    const idx = this.refunds.findIndex(r => r.id === updated.id);
    if (idx >= 0) {
      this.refunds[idx] = updated;
      this.filteredRefunds = [...this.refunds];
      this.computeStats();
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'PENDING': return 'badge-pending';
      case 'APPROVED': return 'badge-approved';
      case 'REJECTED': return 'badge-rejected';
      case 'PROCESSED': return 'badge-processed';
      default: return 'badge-pending';
    }
  }
}
