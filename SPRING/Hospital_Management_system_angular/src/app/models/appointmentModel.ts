


export interface AppointmentModel {

  id?: number;

  patientId?: number;

  patientName: string;

  mobileNumber: string;

  specialization: string;

  problemDescription: string;

  doctorId: number;

  appointmentDate: string;

  appointmentTime: string;

  paymentMethod: string;

  transactionId: string;

  appointmentNumber?: string;

  status?: string;

  feeCharged?: number;

  registeredPatientId?: number;

  doctorName?: string;

  doctorChamber?: string;

  scheduleSlotId?: number;

  slotIsBooked?: boolean;

  doctorSpecialization?: string;

}