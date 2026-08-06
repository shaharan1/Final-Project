export interface SurgeryCategory {
  id?: number;
  code: string;
  name: string;
  description?: string;
  active?: boolean;
  sortOrder?: number;
}

export interface SurgeryCategoryRequest {
  code: string;
  name: string;
  description?: string;
  active?: boolean;
  sortOrder?: number;
}
