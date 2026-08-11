export interface MealSchedule {
  id?: number;
  mealName: string;
  servingTime: string;
  preparationStartTime: string;
  preparationEndTime?: string;
  status: string;
  totalOrdersToday?: number;
  completedOrders?: number;
  pendingOrders?: number;
  cancelledOrders?: number;
  notes?: string;
  currentMeal?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
