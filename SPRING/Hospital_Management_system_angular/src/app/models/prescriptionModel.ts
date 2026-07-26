import { MedicineModel } from "./medicineModel";

export interface PrescriptionItemModel {

  id?: number;

  medicineId: number;

  medicineName?: string;

  dosage: string;

  duration: string;

  instruction: string;

  suggestions?: MedicineModel[];

}
export interface PrescriptionModel {
  id?: number;

  appointmentId: number | null;
  doctorId: number | null;
  patientId: number | null;

  doctorName?: string;
  doctorSpecialization?: string;
  doctorDesignation?: string;
  doctorDepartment?: string;
  patientName?: string;
  patientAge?: string;
  patientGender?: string;
  patientPhone?: string;
  patientBloodGroup?: string;
  prescriptionNumber?: string;
  createdDate?: string;

  diagnosis: string;
  chiefComplaints: string;
  symptoms: string;

  bloodPressure: string;
  pulseRate: string;
  bodyTemperature: string;
  weight: string;

  notes: string;

  nextFollowUpDate: string;

  prescriptionItems: PrescriptionItemModel[];

  testIds: number[];

  tests?: { id?: number; testCode?: string; testName?: string; standardPrice?: number }[];
}