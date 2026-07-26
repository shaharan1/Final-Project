export interface PharmacySaleModel {
  id?: number;
  saleInvoiceNo?: string;
  patientType: string;
  patientName?: string;
  patientPhone?: string;
  patientId?: number;
  doctorId?: number;
  doctorName?: string;
  prescriptionId?: number;
  billingId?: number;
  totalAmount?: number;
  discount?: number;
  vat?: number;
  netPayable?: number;
  paidAmount?: number;
  changeAmount?: number;
  paymentMethod?: string;
  paymentStatus?: string;
  saleType?: string;
  items?: PharmacySaleItemModel[];
  saleDate?: string;
}

export interface PharmacySaleItemModel {
  id?: number;
  medicineStockId: number;
  medicineName?: string;
  batchNumber?: string;
  quantity: number;
  unitPrice?: number;
  discount?: number;
  subtotal?: number;
}
