export interface ReportModel {
  id?: number;
  reportResult?: string;
  description?: string;
  sampleId?: string;
  interpretation?: string;
  preparedBy?: string;
  testDate?: string;
  createDate?: string;
  deliveryDate?: string;

  patientId: number;
  patientCode?: string;
  patientFullName?: string;
  patientPhone?: string;
  patientGender?: string;
  patientBloodGroup?: string;

  doctorId: number;
  doctorName?: string;
  doctorSpecialization?: string;

  testOrderId?: number;
}
