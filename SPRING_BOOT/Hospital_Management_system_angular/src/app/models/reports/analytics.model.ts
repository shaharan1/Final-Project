export interface DashboardSummary {
  totalPatients: number;
  todayAppointments: number;
  todayRevenue: number;
  monthlyRevenue: number;
  pharmacySales: number;
  labIncome: number;
  bedOccupancy: number;
  pendingPayments: number;
  dischargedPatients: number;
  emergencyCases: number;
}

export interface PatientAnalytics {
  total: number;
  male: number;
  female: number;
  bloodGroupDistribution: Record<string, number>;
  ageGroupDistribution: Record<string, number>;
  departmentWisePatients: { department: string; count: number }[];
  monthlyRegistrationTrend: { month: string; count: number }[];
}

export interface AppointmentAnalytics {
  total: number;
  completed: number;
  cancelled: number;
  pending: number;
  completionRate: number;
  cancellationRate: number;
  dailyTrend: { date: string; count: number }[];
  departmentWiseDistribution: { department: string; count: number }[];
  peakHours: { hour: number; count: number }[];
  statusDistribution: { status: string; count: number }[];
}

export interface DoctorAnalytics {
  topDoctors: { name: string; department: string; patientCount: number; consultationCount: number; revenue: number }[];
  departmentPerformance: { department: string; doctorCount: number; patientCount: number; revenue: number }[];
  monthlyPerformance: { month: string; consultations: number; revenue: number }[];
}

export interface LabAnalytics {
  totalTests: number;
  completedTests: number;
  pendingTests: number;
  criticalTests: number;
  dailyTestTrend: { date: string; count: number }[];
  testCategoryDistribution: { category: string; count: number }[];
}

export interface PharmacyAnalytics {
  totalSales: number;
  dailySales: number;
  monthlySales: number;
  topSellingMedicines: { name: string; quantity: number; revenue: number }[];
  lowStockMedicines: number;
  expiredMedicines: number;
  salesTrend: { date: string; amount: number }[];
}

export interface RevenueAnalytics {
  totalRevenue: number;
  billingRevenue: number;
  pharmacyRevenue: number;
  labRevenue: number;
  admissionRevenue: number;
  dailyTrend: { date: string; amount: number }[];
  monthlyTrend: { month: string; amount: number }[];
  paymentMethodDistribution: { method: string; amount: number; count: number }[];
  revenueByDepartment: { department: string; revenue: number }[];
}

export interface BedOccupancy {
  totalBeds: number;
  occupiedBeds: number;
  availableBeds: number;
  reservedBeds: number;
  occupancyRate: number;
  wardWiseOccupancy: { ward: string; total: number; occupied: number; available: number; rate: number }[];
  icuStats: { total: number; occupied: number; available: number };
}

export interface EmergencyAnalytics {
  totalCases: number;
  todayCases: number;
  criticalCases: number;
  severityDistribution: { level: string; count: number }[];
  statusDistribution: { status: string; count: number }[];
}

export interface FinancialAnalytics {
  totalIncome: number;
  totalExpenses: number;
  netProfit: number;
  outstandingDue: number;
  insuranceClaimsTotal: number;
  approvedClaims: number;
  pendingClaims: number;
  refundsTotal: number;
  monthlyTrend: { month: string; income: number; expenses: number; profit: number }[];
}

export interface ReportFilter {
  dateRange: 'today' | 'yesterday' | 'week' | 'month' | '6months' | 'year' | 'custom';
  startDate?: string;
  endDate?: string;
  department?: string;
  doctor?: string;
  status?: string;
  gender?: string;
  ageGroup?: string;
  paymentMethod?: string;
}

export interface ActivityItem {
  type: string;
  description: string;
  amount?: number;
  date: string;
  status: string;
}
