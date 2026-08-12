export interface MedicineStockModel {
  id?: number;
  medicineName: string;
  genericName: string;
  strength: string;
  dosageForm: string;
  batchNumber: string;
  stockQuantity: number;
  reservedQuantity?: number;
  damagedQuantity?: number;
  availableQuantity?: number;
  purchasePrice: number;
  salePrice: number;
  vat?: number;
  minimumStockLevel?: number;
  reorderLevel?: number;
  manufacturingDate: string;
  expiryDate: string;
  barcode?: string;
  supplierId: number;
  supplierName?: string;
  active?: boolean;
  expired?: boolean;
  expiringSoon?: boolean;
  lowStock?: boolean;
  inventoryStatus?: string;
  createdDate?: string;
}
