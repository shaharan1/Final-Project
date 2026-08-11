


export interface DoctorModel {


    id?: number;

    name: string;
    email: string;
    phone: string;
    password?: string;

    gender: string;
    status: string;
    study: string;
    specialization: string;
    designation: string;

    registrationNumber: string;

    experienceYears: number;

    consultationFee: number;

    followUpFee: number;

    availableDays: string;

    dutyHours: string;

    chamber: string;

    joinDate: string;

    photo: string;

    doctorDepatrmentId: number;

    departmentName?: string;

    description?: string;


}


export interface DoctorResponseModel {
  id?: number;

  name: string;
  email: string;
  phone: string;
  gender: string;
  status: string;

  study: string;
  specialization: string;
  designation: string;
  registrationNumber: string;

  experienceYears: number;

  consultationFee: number;
  followUpFee: number;

  availableDays: string;
  dutyHours: string;
  chamber: string;

  joinDate: string; // LocalDate from Spring Boot

  photo: string;

  departmentId: number;
  departmentName: string;

  description: string;
}