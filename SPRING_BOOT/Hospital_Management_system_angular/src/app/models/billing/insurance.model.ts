export interface InsuranceCompany {
  id?: number;
  companyName: string;
  contactPerson: string;
  phone: string;
  email: string;
  address: string;
  policyPrefix: string;
  coveragePercentage: number;
  maxCoverage: number;
  active: boolean;
  notes?: string;
  createdDate?: string;
}

export interface InsuranceClaim {
  id?: number;
  claimNumber?: string;
  insuranceId: number;
  insuranceCompanyName: string;
  policyNumber: string;
  patientId: number;
  patientName: string;
  invoiceNumber: string;
  claimAmount: number;
  approvedAmount?: number;
  claimStatus: string;
  submissionDate?: string;
  reviewDate?: string;
  settlementDate?: string;
  notes?: string;
  processedBy?: string;
  createdDate?: string;
}
