export interface EmergencyTimeline {
  id?: number;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  eventType?: string;
  eventTime?: string;
  description?: string;
  performedBy?: string;
  department?: string;
  status?: string;
  notes?: string;
  createdAt?: string;
}
