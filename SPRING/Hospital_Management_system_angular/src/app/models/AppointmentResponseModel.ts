import { PatientModel } from "./patientModel";

export interface AppointmentResponseModel {

  id: number;

  appointmentNumber: string;

  status: string;

  patientName: string;

  mobileNumber: string;

  specialization: string;

  problemDescription: string;

  appointmentDate: string;

  appointmentTime: string;

  feeCharged: number;

  paymentMethod: string;

  transactionId: string;

  registeredPatientId: number;

  doctorId: number;

  doctorName: string;

  doctorChamber: string;

  scheduleSlotId: number;

  slotIsBooked: boolean;

  doctorSpecialization: string;

  pai:PatientModel;

}