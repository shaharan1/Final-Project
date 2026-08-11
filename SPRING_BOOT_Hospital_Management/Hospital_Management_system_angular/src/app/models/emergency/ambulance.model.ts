export interface Ambulance {
  id?: number;
  ambulanceNumber?: string;
  vehicleType?: string;
  vehiclePlate?: string;
  driverName?: string;
  driverPhone?: string;
  paramedicName?: string;
  paramedicPhone?: string;
  status?: string;
  currentLocation?: string;
  fuelStatus?: number;
  lastServiceDate?: string;
  nextServiceDate?: string;
  isActive?: boolean;
  equipmentChecklist?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface AmbulanceTrip {
  id?: number;
  ambulanceId?: number;
  ambulanceNumber?: string;
  emergencyPatientId?: number;
  emergencyNumber?: string;
  tripType?: string;
  pickupLocation?: string;
  dropoffLocation?: string;
  dispatchTime?: string;
  arrivalTime?: string;
  completionTime?: string;
  distanceTravelled?: number;
  responseTimeMinutes?: number;
  status?: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}
