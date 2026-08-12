export interface EmergencyBed {
  id?: number;
  bedNumber?: string;
  wardName?: string;
  bedType?: string;
  status?: string;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  assignedAt?: string;
  releasedAt?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
