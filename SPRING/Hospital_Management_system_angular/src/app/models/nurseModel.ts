

export interface NurseModel {

 id?: number;
  userId?: number;

  name: string;
  email: string;
  phone: string;
  password?: string;

  address: string;
  gender: string;
  joinDate: string;
  photo: string;

  nurseType: string;
  qualification: string;
  registrationNumber: string;
  experienceYears: number;

  shift: string;
  workingHours: string;

  onDuty: boolean;
  assignedWard: string;
  remarks: string;

  active?: boolean;

}