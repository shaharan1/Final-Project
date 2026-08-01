export interface PurchaseModel {
  id?: number;
  supplierId: number;
  supplierName?: string;
  invoiceNo?: string;
  purchaseDate?: string;
  totalAmount?: number;
  vat?: number;
  discount?: number;
  netAmount?: number;
  paidAmount?: number;
  dueAmount?: number;
  status?: string;
  paymentStatus?: string;
  paymentMethod?: string;
  notes?: string;
  items?: PurchaseItemModel[];
}

export interface PurchaseItemModel {
  id?: number;
  purchaseId?: number;
  stockId: number;
  medicineName?: string;
  batchNumber?: string;
  quantity: number;
  unitPrice: number;
  discount?: number;
  vat?: number;
  subtotal?: number;
  manufacturingDate?: string;
  expiryDate?: string;
}
