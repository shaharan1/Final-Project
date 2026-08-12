export interface EmergencyDashboard {
  emergencyPatientsToday?: number;
  criticalPatients?: number;
  waitingPatients?: number;
  patientsUnderTreatment?: number;
  admittedFromEmergency?: number;
  ambulancesActive?: number;
  doctorsOnDuty?: number;
  nursesOnDuty?: number;
  availableEmergencyBeds?: number;
  icuBedsAvailable?: number;
  todaysEmergencyRevenue?: number;
  averageWaitingTime?: number;
}
