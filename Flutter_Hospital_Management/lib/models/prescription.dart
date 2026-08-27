class PrescriptionItemRequest {
  final int? medicineId;
  final String? dosage;
  final String? duration;
  final String? instruction;

  PrescriptionItemRequest({
    this.medicineId,
    this.dosage,
    this.duration,
    this.instruction,
  });

  Map<String, dynamic> toJson() => {
        'medicineId': medicineId,
        'dosage': dosage,
        'duration': duration,
        'instruction': instruction,
      };
}

class PrescriptionRequest {
  final int? appointmentId;
  final int? doctorId;
  final int? patientId;
  final String? diagnosis;
  final String? chiefComplaints;
  final String? symptoms;
  final String? bloodPressure;
  final String? pulseRate;
  final String? bodyTemperature;
  final String? weight;
  final String? notes;
  final String? nextFollowUpDate;
  final List<PrescriptionItemRequest> prescriptionItems;
  final List<int>? testIds;

  PrescriptionRequest({
    this.appointmentId,
    this.doctorId,
    this.patientId,
    this.diagnosis,
    this.chiefComplaints,
    this.symptoms,
    this.bloodPressure,
    this.pulseRate,
    this.bodyTemperature,
    this.weight,
    this.notes,
    this.nextFollowUpDate,
    this.prescriptionItems = const [],
    this.testIds,
  });

  Map<String, dynamic> toJson() => {
        'appointmentId': appointmentId,
        'doctorId': doctorId,
        'patientId': patientId,
        'diagnosis': diagnosis,
        'chiefComplaints': chiefComplaints,
        'symptoms': symptoms,
        'bloodPressure': bloodPressure,
        'pulseRate': pulseRate,
        'bodyTemperature': bodyTemperature,
        'weight': weight,
        'notes': notes,
        'nextFollowUpDate': nextFollowUpDate,
        'prescriptionItems': prescriptionItems.map((e) => e.toJson()).toList(),
        if (testIds != null) 'testIds': testIds,
      };
}

class PrescriptionItemResponse {
  final int? id;
  final String? medicineName;
  final String? dosage;
  final String? duration;
  final String? instruction;

  const PrescriptionItemResponse({
    this.id,
    this.medicineName,
    this.dosage,
    this.duration,
    this.instruction,
  });

  factory PrescriptionItemResponse.fromJson(Map<String, dynamic> j) =>
      PrescriptionItemResponse(
        id: j['id'],
        medicineName: j['medicineName'],
        dosage: j['dosage'],
        duration: j['duration'],
        instruction: j['instruction'],
      );
}

class PrescriptionResponse {
  final int? id;
  final String? prescriptionNumber;
  final int? appointmentId;
  final String? doctorName;
  final String? patientName;
  final String? diagnosis;
  final String? chiefComplaints;
  final String? symptoms;
  final String? notes;
  final String? nextFollowUpDate;
  final String? createdDate;
  final bool? dispensed;
  final List<PrescriptionItemResponse> prescriptionItems;

  const PrescriptionResponse({
    this.id,
    this.prescriptionNumber,
    this.appointmentId,
    this.doctorName,
    this.patientName,
    this.diagnosis,
    this.chiefComplaints,
    this.symptoms,
    this.notes,
    this.nextFollowUpDate,
    this.createdDate,
    this.dispensed,
    this.prescriptionItems = const [],
  });

  factory PrescriptionResponse.fromJson(Map<String, dynamic> j) =>
      PrescriptionResponse(
        id: j['id'],
        prescriptionNumber: j['prescriptionNumber'],
        appointmentId: j['appointmentId'],
        doctorName: j['doctorName'],
        patientName: j['patientName'],
        diagnosis: j['diagnosis'],
        chiefComplaints: j['chiefComplaints'],
        symptoms: j['symptoms'],
        notes: j['notes'],
        nextFollowUpDate: j['nextFollowUpDate']?.toString(),
        createdDate: j['createdDate']?.toString(),
        dispensed: j['dispensed'],
        prescriptionItems: (j['prescriptionItems'] as List? ?? [])
            .map((e) =>
                PrescriptionItemResponse.fromJson(e as Map<String, dynamic>))
            .toList(),
      );
}
