export interface DietAssignment {
  id?: number;
  patient?: any;
  patientId?: number;
  admittedPatient?: any;
  admittedPatientId?: number;
  dietPlan?: any;
  dietPlanId?: number;
  assignedByDoctor?: any;
  dietician?: any;
  dieticianId?: number;
  startDate: string;
  endDate?: string;
  status: string;
  reason?: string;
  specialInstructions?: string;
  targetCalories?: number;
  targetWeight?: number;
  createdAt?: string;
  updatedAt?: string;
}
