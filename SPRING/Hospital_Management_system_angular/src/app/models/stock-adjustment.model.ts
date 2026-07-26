export interface StockAdjustmentModel {
  medicineStockId: number;
  adjustmentType: string;
  quantityChange: number;
  reason: string;
  performedBy: string;
}

export interface StockHistoryModel {
  id: number;
  medicineName: string;
  batchNumber: string;
  adjustmentType: string;
  quantityChange: number;
  previousQuantity: number;
  newQuantity: number;
  reason: string;
  performedBy: string;
  adjustedAt: string;
}
