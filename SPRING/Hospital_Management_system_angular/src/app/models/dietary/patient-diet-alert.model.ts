export interface PatientDietAlert {
  id?: number;
  patient?: any;
  patientId?: number;
  admittedPatient?: any;
  alertType: string;
  description: string;
  severity: string;
  status: string;
  allergenName?: string;
  specialInstructions?: string;
  createdBy?: string;
  createdAt?: string;
  resolvedAt?: string;
}
