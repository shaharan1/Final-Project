export interface DoctorDiscount {
  id?: number;
  doctorId: number;
  doctorName?: string;
  fixedDiscount?: number;
  percentageDiscount?: number;
  departmentDiscount?: number;
  specialPromoDiscount?: number;
  effectiveDiscountPercent?: number;
  active?: boolean;
  notes?: string;
}

export interface DoctorDiscountRequest {
  doctorId: number;
  fixedDiscount?: number;
  percentageDiscount?: number;
  departmentDiscount?: number;
  specialPromoDiscount?: number;
  active?: boolean;
  notes?: string;
}
