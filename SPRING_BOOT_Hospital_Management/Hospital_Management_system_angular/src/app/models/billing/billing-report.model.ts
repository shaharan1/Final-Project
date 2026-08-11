export interface DailyCollectionReport {
  date: string;
  totalCollection: number;
  cashCollection: number;
  cardCollection: number;
  mobileCollection: number;
  insuranceCollection: number;
  billCount: number;
}

export interface DepartmentRevenueReport {
  department: string;
  revenue: number;
  patientCount: number;
  averageBill: number;
}

export interface ProfitLossReport {
  month: string;
  revenue: number;
  expenses: number;
  profit: number;
  margin: number;
}

export interface PendingDueReport {
  patientId: number;
  patientName: string;
  invoiceNumber: string;
  totalAmount: number;
  paidAmount: number;
  dueAmount: number;
  dueDate: string;
  daysOverdue: number;
}
