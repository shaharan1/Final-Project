class Admission {
  final int? admissionId;
  final int? patientId;
  final int? bedId;
  final String? patientName;
  final String? patientCode;
  final String? doctorName;
  final String? assignedBedNumber;
  final String? wardName;
  final String? initialDiagnosis;
  final String? admissionDate;
  final String? status;

  const Admission({
    this.admissionId,
    this.patientId,
    this.bedId,
    this.patientName,
    this.patientCode,
    this.doctorName,
    this.assignedBedNumber,
    this.wardName,
    this.initialDiagnosis,
    this.admissionDate,
    this.status,
  });

  factory Admission.fromJson(Map<String, dynamic> j) => Admission(
        admissionId: j['admissionId'],
        patientId: j['patientId'],
        bedId: j['bedId'],
        patientName: j['patientName'],
        patientCode: j['patientCode'],
        doctorName: j['doctorName'],
        assignedBedNumber: j['assignedBedNumber'],
        wardName: j['wardName'],
        initialDiagnosis: j['initialDiagnosis'],
        admissionDate: j['admissionDate']?.toString(),
        status: j['status']?.toString(),
      );
}

class AdmissionRequest {
  final int? patientId;
  final int? doctorId;
  final int? bedId;
  final String? initialDiagnosis;

  AdmissionRequest({
    this.patientId,
    this.doctorId,
    this.bedId,
    this.initialDiagnosis,
  });

  Map<String, dynamic> toJson() => {
        if (patientId != null) 'patientId': patientId,
        if (doctorId != null) 'doctorId': doctorId,
        if (bedId != null) 'bedId': bedId,
        if (initialDiagnosis != null) 'initialDiagnosis': initialDiagnosis,
      };
}
