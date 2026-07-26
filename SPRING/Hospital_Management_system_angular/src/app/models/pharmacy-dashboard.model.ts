export interface PharmacyDashboardModel {
  todaySales: number;
  todaySalesCount: number;
  todayPurchases: number;
  todayPurchasesCount: number;
  totalMedicines: number;
  totalAvailableStock: number;
  lowStockCount: number;
  expiredCount: number;
  expiringSoonCount: number;
  monthlyRevenue: number;
  monthlyProfit: number;
  recentSales: any[];
  topSellingMedicines: any[];
  salesChart: any[];
  expiryAlerts: any[];
}
