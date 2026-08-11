export interface DietaryDashboardStats {
  todayMealsServed: number;
  activePatients: number;
  dietPlansAssigned: number;
  kitchenOrders: number;
  pendingMealDelivery: number;
  dieticiansOnDuty: number;
  caloriesServedToday: number;
  mealCompletionRate: number;
  
  recentOrders: any[];
  activeAlerts: any[];
  mealSchedule: any[];
  wardDistribution: any[];
  dietTypeDistribution: any[];
  recentActivity: any[];
}
