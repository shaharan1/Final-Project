export interface SurgeryRequest {
  patientId: number;
  admittedPatientId?: number | null;
  surgeonId: number;
  assistantSurgeonId?: number | null;
  anesthesiologistId?: number | null;
  departmentId?: number | null;
  categoryId?: number | null;
  surgeryMasterId?: number | null;
  operationTheatreId?: number | null;

  surgeryDate: string;
  startTime?: string | null;
  endTime?: string | null;
  estimatedDurationMin?: number;
  priority: string;
  anesthesiaType?: string;
  clinicalNotes?: string;
  preOperativeDiagnosis?: string;
  postOperativeDiagnosis?: string;
  status?: string;

  surgeryCharge?: number;
  otCharge?: number;
  surgeonFee?: number;
  assistantSurgeonFee?: number;
  anesthesiaFee?: number;
  nursingCharge?: number;
  equipmentCharge?: number;
  consumableCharge?: number;
  icuCharge?: number;
  wardCabinCharge?: number;
  medicineCharge?: number;
  laboratoryCharge?: number;
  radiologyCharge?: number;

  discountPercent?: number | null;
  vatRate?: number;
  insuranceCoverage?: number;
  advancePaid?: number;
  cancellationReason?: string;
}
