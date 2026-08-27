class Medicine {
  final int? id;
  final String medicineName;
  final String? genericName;
  final String? dosage;
  final int? genericId;
  final int? prescriptionId;

  Medicine({
    this.id,
    required this.medicineName,
    this.genericName,
    this.dosage,
    this.genericId,
    this.prescriptionId,
  });

  factory Medicine.fromJson(Map<String, dynamic> j) => Medicine(
        id: j['id'],
        medicineName: j['medicineName'] ?? '',
        genericName: j['genericName'],
        dosage: j['dosage'],
        genericId: j['genericId'],
        prescriptionId: j['prescriptionId'],
      );

  Map<String, dynamic> toJson() => {
        'medicineName': medicineName,
        'dosage': dosage,
        'genericId': genericId,
        'prescriptionId': prescriptionId,
      };
}
