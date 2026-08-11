export interface DoctorNotification {
  id?: number;
  doctorId: number;
  patientId?: number;
  patientName?: string;
  patientCode?: string;
  testOrderId?: number;
  title: string;
  message?: string;
  type?: string;
  severity?: string;
  isRead?: boolean;
  createdDate?: string;
}
