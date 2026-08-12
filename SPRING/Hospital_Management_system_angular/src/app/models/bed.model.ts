export interface FacilityModel {
  id: number;
  name: string;
  standardCharge: number;
  active: boolean;
}

export interface BedModel {
  id: number;
  bedNumber: string;
  wardId: number;
  wardName: string;
  roomType: string;
  status: string;
  totalDailyCost: number;
  facilities?: FacilityModel[];
}
