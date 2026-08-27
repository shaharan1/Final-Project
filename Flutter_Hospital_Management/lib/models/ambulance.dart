class Ambulance {
  final int? id;
  final String? ambulanceNumber;
  final String? vehicleType;
  final String? vehiclePlate;
  final String? driverName;
  final String? driverPhone;
  final String? paramedicName;
  final String? paramedicPhone;
  final String? status;
  final String? currentLocation;
  final int? fuelStatus;
  final bool? isActive;

  const Ambulance({
    this.id,
    this.ambulanceNumber,
    this.vehicleType,
    this.vehiclePlate,
    this.driverName,
    this.driverPhone,
    this.paramedicName,
    this.paramedicPhone,
    this.status,
    this.currentLocation,
    this.fuelStatus,
    this.isActive,
  });

  factory Ambulance.fromJson(Map<String, dynamic> j) => Ambulance(
        id: j['id'],
        ambulanceNumber: j['ambulanceNumber'],
        vehicleType: j['vehicleType'],
        vehiclePlate: j['vehiclePlate'],
        driverName: j['driverName'],
        driverPhone: j['driverPhone'],
        paramedicName: j['paramedicName'],
        paramedicPhone: j['paramedicPhone'],
        status: j['status'],
        currentLocation: j['currentLocation'],
        fuelStatus: j['fuelStatus'],
        isActive: j['isActive'],
      );
}

class AmbulanceTrip {
  final int? id;
  final String? ambulanceNumber;
  final String? tripType;
  final String? pickupLocation;
  final String? dropoffLocation;
  final String? status;
  final String? emergencyNumber;
  final double? distanceTravelled;

  const AmbulanceTrip({
    this.id,
    this.ambulanceNumber,
    this.tripType,
    this.pickupLocation,
    this.dropoffLocation,
    this.status,
    this.emergencyNumber,
    this.distanceTravelled,
  });

  factory AmbulanceTrip.fromJson(Map<String, dynamic> j) => AmbulanceTrip(
        id: j['id'],
        ambulanceNumber: j['ambulanceNumber'],
        tripType: j['tripType'],
        pickupLocation: j['pickupLocation'],
        dropoffLocation: j['dropoffLocation'],
        status: j['status'],
        emergencyNumber: j['emergencyNumber'],
        distanceTravelled: (j['distanceTravelled'] as num?)?.toDouble(),
      );
}
