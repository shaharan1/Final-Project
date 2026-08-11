export interface SurgerySchedule {
  surgeryId: number;
  surgeryNumber: string;
  surgeryName?: string;
  operationTheatreId?: number;
  operationTheatreName?: string;
  patientId?: number;
  patientName?: string;
  patientCode?: string;
  surgeonId?: number;
  surgeonName?: string;
  surgeryDate: string;
  startTime?: string;
  endTime?: string;
  priority: string;
  status: string;
}
