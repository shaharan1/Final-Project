export interface Dietician {
  id?: number;
  user?: {
    id: number;
    name: string;
    email: string;
    phone?: string;
    image?: string;
    role?: string;
  };
  userId?: number;
  specialization: string;
  qualification?: string;
  experienceYears?: number;
  licenseNumber?: string;
  phone?: string;
  availableDays?: string;
  dutyHours?: string;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
