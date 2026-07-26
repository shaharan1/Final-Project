export interface TestOrderModel {
  id?: number;
  testCode: string;
  testName: string;
  standardPrice: number;
  normalRange: string;
  orderStatus: string;
  orderedDate?: string;
  lastUpdated?: string;

  patientId: number;
  patientName: string;
  patientCode: string;
  patientPhone?: string;
  patientGender?: string;

  doctorId: number;
  doctorName: string;
  doctorSpecialization?: string;

  prescriptionId?: number;

  sampleCollectorName?: string;
  sampleType?: string;
  sampleCollectedDate?: string;

  sampleReceivedDate?: string;
  sampleReceivedBy?: string;

  testingStartDate?: string;

  resultValue?: string;
  resultNotes?: string;
  resultEnteredDate?: string;
  resultEnteredBy?: string;

  verifiedBy?: string;
  verifiedDate?: string;
  verificationNotes?: string;
}

export interface LabStats {
  pending: number;
  sampleCollected: number;
  sampleReceived: number;
  testing: number;
  resultEntered: number;
  verified: number;
  completed: number;
  total: number;
}
