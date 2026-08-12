export interface PatientModel {

  id?: number;

  appointmentId?: number | null;

  patientCode: string;

  name: string;
  gender: string;
  dateOfBirth: string;

  bloodGroup: string;
  maritalStatus: string;

  phone: string;
  alternatePhone: string;

  email: string;

  nationalId: string;

  address: string;
  city: string;
  district: string;
  postalCode: string;

  emergencyContactName: string;
  emergencyContactNumber: string;
  relationship: string;

}