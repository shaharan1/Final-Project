export interface Refund {
  id?: number;
  refundReference?: string;
  paymentId: number;
  invoiceNumber: string;
  patientId: number;
  patientName: string;
  refundAmount: number;
  refundReason: string;
  refundType: string;
  refundStatus: string;
  approvedBy?: string;
  approvedDate?: string;
  processedBy?: string;
  processedDate?: string;
  notes?: string;
  createdDate?: string;
}
