export interface DietPlan {
  id?: number;
  name: string;
  dietType: string;
  description?: string;
  createdByDietician?: any;
  approvedByDoctor?: any;

  breakfast?: string;
  breakfastTime?: string;
  morningSnacks?: string;
  morningSnacksTime?: string;
  lunch?: string;
  lunchTime?: string;
  eveningSnacks?: string;
  eveningSnacksTime?: string;
  dinner?: string;
  dinnerTime?: string;
  nightDiet?: string;
  nightDietTime?: string;

  totalCalories?: number;
  protein?: number;
  carbohydrate?: number;
  fat?: number;
  fiber?: number;
  sodium?: number;
  potassium?: number;
  waterIntakeMl?: number;
  vitaminRecommendation?: string;
  doctorRecommendation?: string;
  dieticianNotes?: string;
  pricePerDay?: number;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
