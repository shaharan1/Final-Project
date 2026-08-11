export interface LabReportResult {
  id?: number;
  parameterId: number;
  parameterName: string;
  parameterCode: string;
  unit?: string;
  resultValue?: string;
  status: string;
  statusLabel: string;
  interpretation?: string;
  abnormal?: boolean;
  critical?: boolean;
  displayOrder?: number;
  referenceRangeDisplay?: string;
}

export interface LabReport {
  id: number;
  reportNumber?: string;
  testOrderId: number;
  testMasterId?: number;
  testCode?: string;
  testName?: string;
  orderStatus?: string;

  patientId: number;
  patientCode?: string;
  patientName?: string;
  patientGender?: string;
  patientAge?: string;
  patientPhone?: string;

  doctorId?: number;
  doctorName?: string;
  doctorSpecialization?: string;

  reportStatus: string;
  statusLabel: string;
  finalImpression?: string;
  recommendation?: string;

  specialistName?: string;
  specialistDesignation?: string;
  specialistSignature?: string;

  sampleType?: string;
  sampleCollectedDate?: string;
  sampleReceivedDate?: string;

  createdBy?: string;
  createdDate?: string;
  reportedDate?: string;

  results: LabReportResult[];
}

export interface LabDashboard {
  totalReports: number;
  normalReports: number;
  abnormalReports: number;
  criticalReports: number;
  denguePositive: number;
  pendingVerification: number;
  readyReports: number;
  recentReports: LabReport[];
  criticalAlerts: string[];
}
