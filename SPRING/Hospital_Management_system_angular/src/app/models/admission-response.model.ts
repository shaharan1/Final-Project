export interface AdmissionResponse {

    admissionId: number;

    patientId: number;

    bedId: number;

    patientName: string;

    patientCode: string;

    doctorName: string;

    assignedBedNumber: string;

    wardName: string;

    initialDiagnosis: string;

    admissionDate: string;

    status: string;

}