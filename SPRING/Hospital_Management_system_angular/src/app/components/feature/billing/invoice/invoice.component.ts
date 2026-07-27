import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PaymentService } from '../../../../services/billing/payment.service';

interface InvoiceItem {
  category: string;
  description: string;
  qty: number;
  unitPrice: number;
  discount: number;
  amount: number;
}

interface Invoice {
  id: number;
  invoiceNumber: string;
  invoiceDate: string;
  dueDate: string;
  status: string;
  patientName: string;
  patientId: number;
  patientPhone: string;
  patientEmail: string;
  patientAddress: string;
  doctorName: string;
  department: string;
  items: InvoiceItem[];
  subtotal: number;
  totalDiscount: number;
  vat: number;
  serviceCharge: number;
  insuranceCoverage: number;
  netPayable: number;
  paidAmount: number;
  dueAmount: number;
  paymentMethod: string;
  paymentDate: string;
}

@Component({
  selector: 'app-invoice',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {

  invoices: Invoice[] = [];
  filteredInvoices: Invoice[] = [];
  selectedInvoice: Invoice | null = null;
  loading = true;
  error = '';

  searchQuery = '';
  statusFilter = '';

  searchInvoiceNumber = '';

  constructor(
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    this.loading = true;
    this.error = '';
    this.paymentService.getAll().subscribe({
      next: (data: any[]) => {
        this.invoices = this.mapToInvoices(data);
        this.filteredInvoices = [...this.invoices];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to load invoices';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  private mapToInvoices(data: any[]): Invoice[] {
    return data.map((item: any, index: number) => ({
      id: item.id ?? index + 1,
      invoiceNumber: item.invoiceNumber ?? `INV-${String(index + 1).padStart(5, '0')}`,
      invoiceDate: item.invoiceDate ?? item.paymentDate ?? new Date().toISOString().split('T')[0],
      dueDate: item.dueDate ?? '',
      status: item.status ?? 'PENDING',
      patientName: item.patientName ?? item.patient?.name ?? 'N/A',
      patientId: item.patientId ?? item.patient?.id ?? 0,
      patientPhone: item.patientPhone ?? item.patient?.phone ?? '',
      patientEmail: item.patientEmail ?? item.patient?.email ?? '',
      patientAddress: item.patientAddress ?? item.patient?.address ?? '',
      doctorName: item.doctorName ?? item.doctor?.name ?? 'N/A',
      department: item.department ?? item.doctor?.department ?? 'General',
      items: item.items ?? this.generateDefaultItems(item),
      subtotal: item.subtotal ?? item.amount ?? 0,
      totalDiscount: item.totalDiscount ?? item.discount ?? 0,
      vat: item.vat ?? 0,
      serviceCharge: item.serviceCharge ?? 0,
      insuranceCoverage: item.insuranceCoverage ?? 0,
      netPayable: item.netPayable ?? item.amount ?? 0,
      paidAmount: item.paidAmount ?? item.amount ?? 0,
      dueAmount: item.dueAmount ?? 0,
      paymentMethod: item.paymentMethod ?? '',
      paymentDate: item.paymentDate ?? ''
    }));
  }

  private generateDefaultItems(item: any): InvoiceItem[] {
    return [
      { category: 'Consultation', description: 'Doctor Consultation', qty: 1, unitPrice: item.amount ?? 0, discount: 0, amount: item.amount ?? 0 }
    ];
  }

  filterInvoices(): void {
    let result = [...this.invoices];

    if (this.searchQuery.trim()) {
      const q = this.searchQuery.toLowerCase();
      result = result.filter(inv =>
        inv.invoiceNumber.toLowerCase().includes(q) ||
        inv.patientName.toLowerCase().includes(q)
      );
    }

    if (this.statusFilter) {
      result = result.filter(inv => inv.status === this.statusFilter);
    }

    this.filteredInvoices = result;
  }

  lookupInvoice(): void {
    if (!this.searchInvoiceNumber.trim()) return;
    const found = this.invoices.find(
      inv => inv.invoiceNumber.toLowerCase() === this.searchInvoiceNumber.trim().toLowerCase()
    );
    if (found) {
      this.selectedInvoice = found;
    } else {
      alert('Invoice not found');
    }
  }

  viewInvoice(invoice: Invoice): void {
    this.selectedInvoice = invoice;
  }

  backToList(): void {
    this.selectedInvoice = null;
  }

  printInvoice(): void {
    window.print();
  }

  downloadPdf(): void {
    if (!this.selectedInvoice) return;

    import('jspdf').then(({ default: jsPDF }) => {
      import('jspdf-autotable').then(({ default: autoTable }) => {
        const doc = new jsPDF('p', 'mm', 'a4');
        const inv = this.selectedInvoice!;

        doc.setFont('helvetica', 'bold');
        doc.setFontSize(18);
        doc.text('Elite Care Hospital', 105, 20, { align: 'center' });
        doc.setFontSize(10);
        doc.setFont('helvetica', 'normal');
        doc.text('123 Healthcare Avenue, Medical City', 105, 27, { align: 'center' });
        doc.text('Phone: +1 (555) 123-4567 | Email: info@elitecare.com', 105, 33, { align: 'center' });

        doc.setDrawColor(13, 110, 253);
        doc.setLineWidth(0.8);
        doc.line(10, 38, 200, 38);

        doc.setFont('helvetica', 'bold');
        doc.setFontSize(16);
        doc.text('INVOICE', 14, 50);
        doc.setFontSize(10);
        doc.setFont('helvetica', 'normal');
        doc.text(`Invoice #: ${inv.invoiceNumber}`, 14, 57);
        doc.text(`Date: ${inv.invoiceDate}`, 14, 63);
        if (inv.dueDate) doc.text(`Due Date: ${inv.dueDate}`, 14, 69);

        doc.text(`Status: ${inv.status}`, 140, 50);

        doc.setFont('helvetica', 'bold');
        doc.text('Patient Information', 14, 82);
        doc.setFont('helvetica', 'normal');
        doc.text(`Name: ${inv.patientName}`, 14, 89);
        doc.text(`ID: ${inv.patientId}`, 14, 95);
        doc.text(`Phone: ${inv.patientPhone}`, 14, 101);
        if (inv.patientEmail) doc.text(`Email: ${inv.patientEmail}`, 14, 107);

        doc.setFont('helvetica', 'bold');
        doc.text('Doctor Information', 120, 82);
        doc.setFont('helvetica', 'normal');
        doc.text(`Name: ${inv.doctorName}`, 120, 89);
        doc.text(`Department: ${inv.department}`, 120, 95);

        autoTable(doc, {
          startY: 115,
          head: [['#', 'Category', 'Description', 'Qty', 'Unit Price', 'Discount', 'Amount']],
          body: inv.items.map((item, i) => [
            i + 1,
            item.category,
            item.description,
            item.qty,
            item.unitPrice.toFixed(2),
            item.discount.toFixed(2),
            item.amount.toFixed(2)
          ]),
          theme: 'grid',
          headStyles: { fillColor: [13, 110, 253], textColor: 255, fontStyle: 'bold', halign: 'center' },
          bodyStyles: { halign: 'center' },
          alternateRowStyles: { fillColor: [245, 248, 255] }
        });

        let finalY = (doc as any).lastAutoTable.finalY + 10;

        doc.setFont('helvetica', 'normal');
        doc.text(`Subtotal:`, 130, finalY);
        doc.text(`$${inv.subtotal.toFixed(2)}`, 185, finalY, { align: 'right' });

        if (inv.totalDiscount > 0) {
          finalY += 7;
          doc.text(`Discount:`, 130, finalY);
          doc.text(`-$${inv.totalDiscount.toFixed(2)}`, 185, finalY, { align: 'right' });
        }

        finalY += 7;
        doc.text(`VAT (18%):`, 130, finalY);
        doc.text(`$${inv.vat.toFixed(2)}`, 185, finalY, { align: 'right' });

        if (inv.serviceCharge > 0) {
          finalY += 7;
          doc.text(`Service Charge:`, 130, finalY);
          doc.text(`$${inv.serviceCharge.toFixed(2)}`, 185, finalY, { align: 'right' });
        }

        if (inv.insuranceCoverage > 0) {
          finalY += 7;
          doc.text(`Insurance Coverage:`, 130, finalY);
          doc.text(`-$${inv.insuranceCoverage.toFixed(2)}`, 185, finalY, { align: 'right' });
        }

        finalY += 7;
        doc.setDrawColor(0);
        doc.setLineWidth(0.3);
        doc.line(130, finalY - 2, 198, finalY - 2);
        doc.setFont('helvetica', 'bold');
        doc.text(`Net Payable:`, 130, finalY);
        doc.text(`$${inv.netPayable.toFixed(2)}`, 185, finalY, { align: 'right' });

        finalY += 10;
        doc.setFont('helvetica', 'normal');
        doc.text(`Paid Amount:`, 130, finalY);
        doc.text(`$${inv.paidAmount.toFixed(2)}`, 185, finalY, { align: 'right' });

        finalY += 7;
        doc.text(`Due Amount:`, 130, finalY);
        doc.text(`$${inv.dueAmount.toFixed(2)}`, 185, finalY, { align: 'right' });

        finalY += 20;
        doc.setFontSize(8);
        doc.text('Terms & Conditions: Payment is due within 30 days. Late payments incur 1.5% monthly interest.', 14, finalY);

        finalY += 15;
        doc.line(14, finalY, 70, finalY);
        doc.text('Authorized Signature', 14, finalY + 5);

        doc.circle(160, finalY - 8, 12);
        doc.text('Hospital Seal', 160, finalY + 5, { align: 'center' });

        doc.save(`Invoice_${inv.invoiceNumber}.pdf`);
      });
    });
  }

  emailInvoice(): void {
    if (!this.selectedInvoice) return;
    alert(`Invoice ${this.selectedInvoice.invoiceNumber} would be emailed to ${this.selectedInvoice.patientEmail}`);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PAID': return 'status--paid';
      case 'PARTIAL': return 'status--partial';
      case 'PENDING': return 'status--pending';
      case 'OVERDUE': return 'status--overdue';
      case 'CANCELLED': return 'status--cancelled';
      default: return 'status--pending';
    }
  }

  getNetPayable(): number {
    if (!this.selectedInvoice) return 0;
    return this.selectedInvoice.subtotal - this.selectedInvoice.totalDiscount + this.selectedInvoice.vat + this.selectedInvoice.serviceCharge - this.selectedInvoice.insuranceCoverage;
  }
}
