class LabReportResult {
  final int? id;
  final String? parameterName;
  final String? parameterCode;
  final String? unit;
  final String? resultValue;
  final String? status;
  final String? statusLabel;
  final String? interpretation;
  final bool? abnormal;
  final bool? critical;
  final String? referenceRangeDisplay;

  const LabReportResult({
    this.id,
    this.parameterName,
    this.parameterCode,
    this.unit,
    this.resultValue,
    this.status,
    this.statusLabel,
    this.interpretation,
    this.abnormal,
    this.critical,
    this.referenceRangeDisplay,
  });

  factory LabReportResult.fromJson(Map<String, dynamic> j) => LabReportResult(
        id: j['id'],
        parameterName: j['parameterName'],
        parameterCode: j['parameterCode'],
        unit: j['unit'],
        resultValue: j['resultValue'],
        status: j['status'],
        statusLabel: j['statusLabel'],
        interpretation: j['interpretation'],
        abnormal: j['abnormal'],
        critical: j['critical'],
        referenceRangeDisplay: j['referenceRangeDisplay'],
      );
}

class LabReport {
  final int id;
  final String? reportNumber;
  final String? testName;
  final String? testCode;
  final String? orderStatus;
  final int? patientId;
  final String? patientCode;
  final String? patientName;
  final String? patientGender;
  final String? patientAge;
  final String? patientPhone;
  final String? doctorName;
  final String? reportStatus;
  final String? statusLabel;
  final String? finalImpression;
  final String? recommendation;
  final String? specialistName;
  final String? sampleType;
  final String? createdDate;
  final String? reportedDate;
  final List<LabReportResult> results;

  const LabReport({
    required this.id,
    this.reportNumber,
    this.testName,
    this.testCode,
    this.orderStatus,
    this.patientId,
    this.patientCode,
    this.patientName,
    this.patientGender,
    this.patientAge,
    this.patientPhone,
    this.doctorName,
    this.reportStatus,
    this.statusLabel,
    this.finalImpression,
    this.recommendation,
    this.specialistName,
    this.sampleType,
    this.createdDate,
    this.reportedDate,
    this.results = const [],
  });

  factory LabReport.fromJson(Map<String, dynamic> j) => LabReport(
        id: j['id'],
        reportNumber: j['reportNumber'],
        testName: j['testName'],
        testCode: j['testCode'],
        orderStatus: j['orderStatus'],
        patientId: j['patientId'],
        patientCode: j['patientCode'],
        patientName: j['patientName'],
        patientGender: j['patientGender'],
        patientAge: j['patientAge'],
        patientPhone: j['patientPhone'],
        doctorName: j['doctorName'],
        reportStatus: j['reportStatus'],
        statusLabel: j['statusLabel'],
        finalImpression: j['finalImpression'],
        recommendation: j['recommendation'],
        specialistName: j['specialistName'],
        sampleType: j['sampleType'],
        createdDate: j['createdDate']?.toString(),
        reportedDate: j['reportedDate']?.toString(),
        results: (j['results'] as List?)
                ?.map((e) =>
                    LabReportResult.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
      );
}

class LabDashboard {
  final int totalReports;
  final int normalReports;
  final int abnormalReports;
  final int criticalReports;
  final int denguePositive;
  final int pendingVerification;
  final int readyReports;
  final List<LabReport> recentReports;
  final List<String> criticalAlerts;

  const LabDashboard({
    this.totalReports = 0,
    this.normalReports = 0,
    this.abnormalReports = 0,
    this.criticalReports = 0,
    this.denguePositive = 0,
    this.pendingVerification = 0,
    this.readyReports = 0,
    this.recentReports = const [],
    this.criticalAlerts = const [],
  });

  factory LabDashboard.fromJson(Map<String, dynamic> j) => LabDashboard(
        totalReports: j['totalReports'] ?? 0,
        normalReports: j['normalReports'] ?? 0,
        abnormalReports: j['abnormalReports'] ?? 0,
        criticalReports: j['criticalReports'] ?? 0,
        denguePositive: j['denguePositive'] ?? 0,
        pendingVerification: j['pendingVerification'] ?? 0,
        readyReports: j['readyReports'] ?? 0,
        recentReports: (j['recentReports'] as List?)
                ?.map((e) => LabReport.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
        criticalAlerts: (j['criticalAlerts'] as List?)
                ?.map((e) => e.toString())
                .toList() ??
            [],
      );
}
