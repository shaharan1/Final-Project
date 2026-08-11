export interface EmergencyLabOrder {
  id?: number;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  testName?: string;
  orderType?: string;
  orderedBy?: string;
  orderedAt?: string;
  sampleCollectionTime?: string;
  resultTime?: string;
  resultValue?: string;
  status?: string;
  isCritical?: boolean;
  priority?: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}
