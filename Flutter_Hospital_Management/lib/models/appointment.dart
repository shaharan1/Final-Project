class Appointment {
  final int? id;
  final String? appointmentNumber;
  final String? status;
  final int? serialNo;
  final int? tokenNumber;
  final String? patientName;
  final String? mobileNumber;
  final String? specialization;
  final String? problemDescription;
  final String? appointmentDate;
  final String? appointmentTime;
  final double? feeCharged;
  final String? paymentMethod;
  final String? transactionId;
  final int? registeredPatientId;
  final int? doctorId;
  final String? doctorName;
  final String? doctorChamber;
  final int? scheduleSlotId;
  final bool? slotIsBooked;
  final String? doctorSpecialization;

  const Appointment({
    this.id,
    this.appointmentNumber,
    this.status,
    this.serialNo,
    this.tokenNumber,
    this.patientName,
    this.mobileNumber,
    this.specialization,
    this.problemDescription,
    this.appointmentDate,
    this.appointmentTime,
    this.feeCharged,
    this.paymentMethod,
    this.transactionId,
    this.registeredPatientId,
    this.doctorId,
    this.doctorName,
    this.doctorChamber,
    this.scheduleSlotId,
    this.slotIsBooked,
    this.doctorSpecialization,
  });

  factory Appointment.fromJson(Map<String, dynamic> j) => Appointment(
        id: j['id'],
        appointmentNumber: j['appointmentNumber'],
        status: j['status'],
        serialNo: j['serialNo'],
        tokenNumber: j['tokenNumber'],
        patientName: j['patientName'],
        mobileNumber: j['mobileNumber'],
        specialization: j['specialization'],
        problemDescription: j['problemDescription'],
        appointmentDate: j['appointmentDate']?.toString(),
        appointmentTime: j['appointmentTime']?.toString(),
        feeCharged: (j['feeCharged'] as num?)?.toDouble(),
        paymentMethod: j['paymentMethod'],
        transactionId: j['transactionId'],
        registeredPatientId: j['registeredPatientId'],
        doctorId: j['doctorId'],
        doctorName: j['doctorName'],
        doctorChamber: j['doctorChamber'],
        scheduleSlotId: j['scheduleSlotId'],
        slotIsBooked: j['slotIsBooked'],
        doctorSpecialization: j['doctorSpecialization'],
      );
}

class AppointmentRequest {
  final int? patientId;
  final String? patientName;
  final String? mobileNumber;
  final String? specialization;
  final String? problemDescription;
  final int? doctorId;
  final String appointmentDate;
  final String appointmentTime;
  final String? paymentMethod;
  final String? transactionId;

  AppointmentRequest({
    this.patientId,
    this.patientName,
    this.mobileNumber,
    this.specialization,
    this.problemDescription,
    this.doctorId,
    required this.appointmentDate,
    required this.appointmentTime,
    this.paymentMethod,
    this.transactionId,
  });

  Map<String, dynamic> toJson() {
    final m = <String, dynamic>{
      'appointmentDate': appointmentDate,
      'appointmentTime': appointmentTime,
    };
    if (patientId != null) m['patientId'] = patientId;
    if (patientName != null) m['patientName'] = patientName;
    if (mobileNumber != null) m['mobileNumber'] = mobileNumber;
    if (specialization != null) m['specialization'] = specialization;
    if (problemDescription != null) {
      m['problemDescription'] = problemDescription;
    }
    if (doctorId != null) m['doctorId'] = doctorId;
    if (paymentMethod != null) m['paymentMethod'] = paymentMethod;
    if (transactionId != null) m['transactionId'] = transactionId;
    return m;
  }
}
