


export interface ScheduleSlotModel {

  id?: number;

  doctorId: number;

  doctorName?: string;

  doctorSpecialization?: string;

  date: string;

  startTime: string;

  endTime: string;

  isBooked?: boolean;

}