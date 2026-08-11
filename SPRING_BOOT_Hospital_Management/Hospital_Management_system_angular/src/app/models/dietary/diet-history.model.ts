export interface DietHistory {
  id?: number;
  patient?: any;
  patientId?: number;
  dietAssignment?: any;
  dietPlan?: any;
  actionType: string;
  description?: string;
  previousValue?: string;
  newValue?: string;
  performedBy?: string;
  userRole?: string;
  weightKg?: number;
  bmi?: number;
  createdAt?: string;
}
