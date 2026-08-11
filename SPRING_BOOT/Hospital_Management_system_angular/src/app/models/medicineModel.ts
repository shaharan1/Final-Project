export interface MedicineModel {
  id?: number;

  medicineName: string;

  genericId?: number;
  genericName?: string;

  dosage: string;

  // prescriptionId?: number;
}