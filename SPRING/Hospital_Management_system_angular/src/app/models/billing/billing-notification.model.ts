export interface BillingNotification {
  id?: number;
  title: string;
  message: string;
  type: string;
  relatedInvoiceNumber?: string;
  patientId?: number;
  isRead: boolean;
  createdDate?: string;
}
