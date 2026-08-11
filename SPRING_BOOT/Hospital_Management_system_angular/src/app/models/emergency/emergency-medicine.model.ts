export interface EmergencyMedicine {
  id?: number;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  medicineName?: string;
  dose?: string;
  route?: string;
  frequency?: string;
  quantity?: number;
  stockAvailable?: boolean;
  administeredBy?: string;
  administeredAt?: string;
  pharmacyRequestSent?: boolean;
  status?: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}
