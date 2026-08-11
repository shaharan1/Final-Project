export interface SurgeryDashboard {
  totalSurgeriesToday: number;
  scheduledSurgeries: number;
  completedSurgeries: number;
  cancelledSurgeries: number;
  inProgressSurgeries: number;
  otUtilizationPercent: number;
  totalSurgeryRevenue: number;
  pendingSurgeryBills: number;
  pendingBillAmount: number;

  topSurgeons: Array<[string, number, number]>;
  topCategories: Array<[string, number, number]>;
  topPerformedSurgeries: Array<[string, number, number]>;
  upcomingOtSchedule: any[];
  recentActivities: any[];
  monthlyStats: Array<[number, number, number]>;
  statusBreakdown: Array<[string, number]>;

  reportDate: string;
}
