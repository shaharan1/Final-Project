import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PaymentService } from '../../../../services/billing/payment.service';

@Component({
  selector: 'app-payment-module',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './payment-module.component.html',
  styleUrls: ['./payment-module.component.css']
})
export class PaymentModuleComponent implements OnInit {
  activeTab: 'process' | 'history' = 'process';
  loading = false;
  processingPayment = false;
  successMessage = '';
  error = '';
  loadingPayments = false;

  invoiceSearchTerm = '';
  invoiceData: any = null;
  invoiceNotFound = false;

  selectedPaymentMethod: string = 'Cash';
  cardNumber = '';
  cardExpiry = '';
  cardCVV = '';
  bankName = '';
  bankAccountNumber = '';
  bankRoutingNumber = '';
  mobileProvider: string = 'bKash';
  mobileNumber = '';
  insuranceCompany = '';
  insurancePolicy = '';
  insuranceCoverage = 0;

  discount = 0;
  vat = 0;
  amountPaid = 0;
  changeAmount = 0;

  splitPayments: { method: string; amount: number }[] = [];
  showSplitPayment = false;

  payments: any[] = [];
  filteredPayments: any[] = [];
  searchTerm = '';
  filterStatus = '';
  filterMethod = '';
  filterDateFrom = '';
  filterDateTo = '';

  showDetailModal = false;
  selectedPayment: any = null;

  constructor(private paymentService: PaymentService) {}

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    this.loadingPayments = true;
    this.paymentService.getAll().subscribe({
      next: (data: any) => {
        this.payments = data;
        this.filteredPayments = [...data];
        this.loadingPayments = false;
      },
      error: () => {
        this.error = 'Failed to load payment history.';
        this.loadingPayments = false;
      }
    });
  }

  searchInvoice(): void {
    if (!this.invoiceSearchTerm.trim()) return;
    this.loading = true;
    this.invoiceNotFound = false;
    this.invoiceData = null;
    this.paymentService.getByInvoice(this.invoiceSearchTerm).subscribe({
      next: (data: any) => {
        if (data && data.length > 0) {
          this.invoiceData = data[0];
          this.discount = this.invoiceData.discount || 0;
          this.vat = this.invoiceData.vat || 0;
          this.amountPaid = this.invoiceData.amountDue || 0;
          this.calculateChange();
        } else {
          this.invoiceNotFound = true;
        }
        this.loading = false;
      },
      error: () => {
        this.invoiceNotFound = true;
        this.loading = false;
      }
    });
  }

  calculateChange(): void {
    const total = (this.invoiceData?.amountDue || 0) - this.discount + this.vat;
    this.changeAmount = Math.max(0, this.amountPaid - total);
  }

  onDiscountChange(): void {
    this.calculateChange();
  }

  onVatChange(): void {
    this.calculateChange();
  }

  onAmountPaidChange(): void {
    this.calculateChange();
  }

  selectPaymentMethod(method: string): void {
    this.selectedPaymentMethod = method;
  }

  getNetTotal(): number {
    if (!this.invoiceData) return 0;
    return (this.invoiceData.amountDue || 0) - this.discount + this.vat;
  }

  addSplitPayment(): void {
    this.splitPayments.push({ method: 'Cash', amount: 0 });
  }

  removeSplitPayment(index: number): void {
    this.splitPayments.splice(index, 1);
  }

  getSplitTotal(): number {
    return this.splitPayments.reduce((sum, sp) => sum + sp.amount, 0);
  }

  processPayment(): void {
    if (!this.invoiceData) return;
    this.processingPayment = true;
    this.error = '';
    const paymentData = {
      invoiceId: this.invoiceData.id,
      invoiceNumber: this.invoiceData.invoiceNumber || this.invoiceSearchTerm,
      patientId: this.invoiceData.patientId,
      patientName: this.invoiceData.patientName,
      amountDue: this.invoiceData.amountDue,
      discount: this.discount,
      vat: this.vat,
      amountPaid: this.amountPaid,
      paymentMethod: this.selectedPaymentMethod,
      cardDetails: this.selectedPaymentMethod === 'Card' ? {
        cardNumber: this.cardNumber,
        expiry: this.cardExpiry,
        cvv: this.cardCVV
      } : null,
      bankDetails: this.selectedPaymentMethod === 'Bank Transfer' ? {
        bankName: this.bankName,
        accountNumber: this.bankAccountNumber,
        routingNumber: this.bankRoutingNumber
      } : null,
      mobileDetails: this.selectedPaymentMethod === 'Mobile Banking' ? {
        provider: this.mobileProvider,
        number: this.mobileNumber
      } : null,
      insuranceDetails: this.selectedPaymentMethod === 'Insurance' ? {
        company: this.insuranceCompany,
        policy: this.insurancePolicy,
        coveragePercent: this.insuranceCoverage
      } : null,
      splitPayments: this.selectedPaymentMethod === 'Split Payment' ? this.splitPayments : null,
      paymentDate: new Date().toISOString(),
      status: 'COMPLETED'
    };
    this.paymentService.processPayment(paymentData).subscribe({
      next: (saved: any) => {
        this.payments.unshift(saved);
        this.filteredPayments = [...this.payments];
        this.resetPaymentForm();
        this.processingPayment = false;
        this.successMessage = 'Payment processed successfully!';
        setTimeout(() => this.successMessage = '', 4000);
      },
      error: () => {
        this.error = 'Failed to process payment. Please try again.';
        this.processingPayment = false;
      }
    });
  }

  resetPaymentForm(): void {
    this.invoiceSearchTerm = '';
    this.invoiceData = null;
    this.invoiceNotFound = false;
    this.selectedPaymentMethod = 'Cash';
    this.cardNumber = '';
    this.cardExpiry = '';
    this.cardCVV = '';
    this.bankName = '';
    this.bankAccountNumber = '';
    this.bankRoutingNumber = '';
    this.mobileProvider = 'bKash';
    this.mobileNumber = '';
    this.insuranceCompany = '';
    this.insurancePolicy = '';
    this.insuranceCoverage = 0;
    this.discount = 0;
    this.vat = 0;
    this.amountPaid = 0;
    this.changeAmount = 0;
    this.splitPayments = [];
    this.showSplitPayment = false;
  }

  filterPayments(): void {
    let result = [...this.payments];
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p =>
        (p.refNumber || '').toLowerCase().includes(term) ||
        (p.invoiceNumber || '').toLowerCase().includes(term) ||
        (p.patientName || '').toLowerCase().includes(term)
      );
    }
    if (this.filterStatus) {
      result = result.filter(p => p.status === this.filterStatus);
    }
    if (this.filterMethod) {
      result = result.filter(p => p.paymentMethod === this.filterMethod);
    }
    if (this.filterDateFrom) {
      result = result.filter(p => new Date(p.paymentDate) >= new Date(this.filterDateFrom));
    }
    if (this.filterDateTo) {
      result = result.filter(p => new Date(p.paymentDate) <= new Date(this.filterDateTo));
    }
    this.filteredPayments = result;
  }

  viewPaymentDetail(payment: any): void {
    this.selectedPayment = payment;
    this.showDetailModal = true;
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedPayment = null;
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'COMPLETED': return 'badge-completed';
      case 'PENDING': return 'badge-pending';
      case 'FAILED': return 'badge-failed';
      case 'REFUNDED': return 'badge-refunded';
      default: return 'badge-pending';
    }
  }

  exportPDF(): void {
    this.successMessage = 'PDF export started...';
    setTimeout(() => this.successMessage = '', 3000);
  }

  exportExcel(): void {
    this.successMessage = 'Excel export started...';
    setTimeout(() => this.successMessage = '', 3000);
  }
}
