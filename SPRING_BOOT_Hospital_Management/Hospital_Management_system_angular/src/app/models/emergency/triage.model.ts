export interface Triage {
  id?: number;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  triageLevel?: number;
  triageColor?: string;
  bloodPressureSystolic?: number;
  bloodPressureDiastolic?: number;
  pulse?: number;
  temperature?: number;
  oxygenSaturation?: number;
  respirationRate?: number;
  painScore?: number;
  glasgowComaScale?: number;
  assessmentNotes?: string;
  assessedBy?: string;
  assessedAt?: string;
  createdAt?: string;
  updatedAt?: string;
}
