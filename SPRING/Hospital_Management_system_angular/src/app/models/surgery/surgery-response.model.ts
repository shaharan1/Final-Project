export interface SurgeryResponse {
  id: number;
  surgeryNumber: string;

  patientId: number;
  patientName: string;
  patientCode?: string;
  patientPhone?: string;

  admittedPatientId?: number;
  admissionStatus?: string;
  bedId?: number;
  wardId?: number;
  wardName?: string;
  bedNumber?: string;

  surgeonId?: number;
  surgeonName?: string;
  assistantSurgeonId?: number;
  assistantSurgeonName?: string;
  anesthesiologistId?: number;
  anesthesiologistName?: string;

  departmentId?: number;
  departmentName?: string;
  categoryId?: number;
  categoryName?: string;
  surgeryMasterId?: number;
  surgeryName?: string;
  surgeryCode?: string;
  operationTheatreId?: number;
  operationTheatreName?: string;
  otCode?: string;

  surgeryDate: string;
  startTime?: string;
  endTime?: string;
  estimatedDurationMin?: number;
  priority: string;
  anesthesiaType?: string;
  clinicalNotes?: string;
  preOperativeDiagnosis?: string;
  postOperativeDiagnosis?: string;
  status: string;
  cancellationReason?: string;

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

  subtotal?: number;
  discountPercent?: number;
  discountAmount?: number;
  vatRate?: number;
  vatAmount?: number;
  insuranceCoverage?: number;
  advancePaid?: number;
  totalAmount?: number;
  finalPayable?: number;

  billingInvoiceId?: number;
  billingInvoiceNumber?: string;
  billingStatus?: string;

  createdDate?: string;
  lastUpdated?: string;
}
