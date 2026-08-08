export interface SurgeryMaster {
  id?: number;
  surgeryCode: string;
  surgeryName: string;
  categoryId?: number;
  categoryName?: string;
  standardRate: number;
  otCharge?: number;
  surgeonFee?: number;
  assistantSurgeonFee?: number;
  anesthesiaCharge?: number;
  nursingCharge?: number;
  equipmentCharge?: number;
  consumableCharge?: number;
  icuCharge?: number;
  wardCabinCharge?: number;
  medicineCharge?: number;
  laboratoryCharge?: number;
  radiologyCharge?: number;
  packageRate?: number;
  active?: boolean;
  estimatedDurationMin?: number;
  notes?: string;
}

export interface SurgeryMasterRequest {
  surgeryCode: string;
  surgeryName: string;
  categoryId: number;
  standardRate: number;
  otCharge?: number;
  surgeonFee?: number;
  assistantSurgeonFee?: number;
  anesthesiaCharge?: number;
  nursingCharge?: number;
  equipmentCharge?: number;
  consumableCharge?: number;
  icuCharge?: number;
  wardCabinCharge?: number;
  medicineCharge?: number;
  laboratoryCharge?: number;
  radiologyCharge?: number;
  packageRate?: number;
  active?: boolean;
  estimatedDurationMin?: number;
  notes?: string;
}
