export interface KitchenOrder {
  id?: number;
  orderNumber?: string;
  patient?: any;
  patientId?: number;
  admittedPatient?: any;
  dietPlan?: any;
  dietAssignment?: any;
  ward?: any;
  bedNumber?: string;
  mealTime: string;
  mealType: string;
  dietType: string;
  priority: string;
  status: string;
  kitchenNotes?: string;
  preparedBy?: string;
  deliveredBy?: string;
  preparingAt?: string;
  cookingAt?: string;
  readyAt?: string;
  deliveredAt?: string;
  cancelledAt?: string;
  specialDiet?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
