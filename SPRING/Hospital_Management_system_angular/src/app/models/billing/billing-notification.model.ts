export interface BillingNotification {
  id?: number;
  title: string;
  message: string;
  type: string;
  relatedInvoiceNumber?: string;
  patientId?: number;
  read: boolean;
  createdDate?: string;
}
