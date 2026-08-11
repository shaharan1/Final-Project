export interface BillingInvoice {
  id?: number;
  invoiceNumber: string;
  patientId: number;
  patientName: string;
  patientPhone?: string;
  patientEmail?: string;
  patientAddress?: string;
  doctorName: string;
  department: string;
  admissionDate?: string;
  dischargeDate?: string;
  items: BillingInvoiceItem[];
  subtotal: number;
  discount: number;
  vat: number;
  serviceCharge: number;
  totalAmount: number;
  paidAmount: number;
  dueAmount: number;
  paymentStatus: string;
  paymentMethod?: string;
  insuranceCoverage?: number;
  notes?: string;
  generatedBy?: string;
  createdDate?: string;
}

export interface BillingInvoiceItem {
  id?: number;
  category: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discount: number;
  amount: number;
}

export interface BillItem {
  category: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discount: number;
  vat: number;
  amount: number;
}
