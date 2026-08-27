class TestOrder {
  final int id;
  final int? testMasterId;
  final String? testCode;
  final String? testName;
  final double? standardPrice;
  final String? normalRange;
  final String? orderStatus;
  final String? orderedDate;
  final int? patientId;
  final String? patientName;
  final String? patientCode;
  final String? patientPhone;
  final String? patientGender;
  final int? doctorId;
  final String? doctorName;
  final String? doctorSpecialization;
  final String? sampleType;
  final String? resultValue;
  final String? resultNotes;

  const TestOrder({
    required this.id,
    this.testMasterId,
    this.testCode,
    this.testName,
    this.standardPrice,
    this.normalRange,
    this.orderStatus,
    this.orderedDate,
    this.patientId,
    this.patientName,
    this.patientCode,
    this.patientPhone,
    this.patientGender,
    this.doctorId,
    this.doctorName,
    this.doctorSpecialization,
    this.sampleType,
    this.resultValue,
    this.resultNotes,
  });

  factory TestOrder.fromJson(Map<String, dynamic> j) => TestOrder(
        id: j['id'],
        testMasterId: j['testMasterId'],
        testCode: j['testCode'],
        testName: j['testName'],
        standardPrice: (j['standardPrice'] as num?)?.toDouble(),
        normalRange: j['normalRange'],
        orderStatus: j['orderStatus'],
        orderedDate: j['orderedDate']?.toString(),
        patientId: j['patientId'],
        patientName: j['patientName'],
        patientCode: j['patientCode'],
        patientPhone: j['patientPhone'],
        patientGender: j['patientGender'],
        doctorId: j['doctorId'],
        doctorName: j['doctorName'],
        doctorSpecialization: j['doctorSpecialization'],
        sampleType: j['sampleType'],
        resultValue: j['resultValue'],
        resultNotes: j['resultNotes'],
      );
}

class TestOrderStatusHelper {
  static const Map<String, Map<String, String>> _actions = {
    'ORDERED': {'label': 'Collect Sample', 'action': 'collect-sample'},
    'SAMPLE_COLLECTED': {'label': 'Receive Sample', 'action': 'receive-sample'},
    'SAMPLE_RECEIVED': {'label': 'Start Testing', 'action': 'start-testing'},
    'TESTING': {'label': 'Enter Result', 'action': 'enter-result'},
    'RESULT_ENTERED': {'label': 'Verify', 'action': 'verify'},
  };

  static Map<String, String>? nextAction(String? status) {
    if (status == null) return null;
    for (final entry in _actions.entries) {
      if (status.toUpperCase().contains(entry.key)) return entry.value;
    }
    return null;
  }
}
