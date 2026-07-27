export interface EmergencyDoctorAssignment {
  id?: number;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  doctorId?: number;
  doctorName?: string;
  nurseId?: number;
  nurseName?: string;
  assignmentType?: string;
  assignedAt?: string;
  unassignedAt?: string;
  isActive?: boolean;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}
