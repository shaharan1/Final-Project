export interface OperationTheatre {
  id?: number;
  otCode: string;
  otName: string;
  location?: string;
  equipmentAvailable?: string;
  capacity?: number;
  status: string;
  active?: boolean;
}

export interface OperationTheatreRequest {
  otCode: string;
  otName: string;
  location?: string;
  equipmentAvailable?: string;
  capacity?: number;
  status?: string;
  active?: boolean;
}
