export interface Payment {
  id?: number;
  paymentReference?: string;
  invoiceNumber: string;
  patientId: number;
  patientName: string;
  amount: number;
  paymentMethod: string;
  paymentStatus: string;
  transactionId?: string;
  cardLast4?: string;
  bankName?: string;
  mobileProvider?: string;
  insuranceCompanyId?: number;
  insuranceCoverage?: number;
  selfPayAmount?: number;
  discount?: number;
  vat?: number;
  netAmount: number;
  refundAmount?: number;
  notes?: string;
  processedBy?: string;
  paymentDate?: string;
  createdDate?: string;
}

export interface PaymentMethod {
  name: string;
  icon: string;
  color: string;
}

export interface DashboardSummary {
  todayRevenue: number;
  monthlyRevenue: number;
  pendingPayments: number;
  paidBills: number;
  unpaidBills: number;
  insuranceClaims: number;
  refundAmount: number;
  totalPatientsBilled: number;
  todayRevenueChange: number;
  monthlyRevenueChange: number;
}

export interface RevenueChart {
  date: string;
  revenue: number;
  count: number;
}

export interface PaymentMethodBreakdown {
  method: string;
  amount: number;
  count: number;
  percentage: number;
}

export interface DepartmentRevenue {
  department: string;
  revenue: number;
  percentage: number;
}

export interface RecentActivity {
  type: string;
  description: string;
  amount: number;
  time: string;
  status: string;
}
