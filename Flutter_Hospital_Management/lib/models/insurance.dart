class Insurance {
  final int? id;
  final String? companyName;
  final String? contactPerson;
  final String? phone;
  final String? email;
  final double? coveragePercentage;
  final double? maxCoverage;
  final bool? active;

  const Insurance({
    this.id,
    this.companyName,
    this.contactPerson,
    this.phone,
    this.email,
    this.coveragePercentage,
    this.maxCoverage,
    this.active,
  });

  factory Insurance.fromJson(Map<String, dynamic> j) => Insurance(
        id: j['id'],
        companyName: j['companyName'],
        contactPerson: j['contactPerson'],
        phone: j['phone'],
        email: j['email'],
        coveragePercentage: (j['coveragePercentage'] as num?)?.toDouble(),
        maxCoverage: (j['maxCoverage'] as num?)?.toDouble(),
        active: j['active'],
      );
}

class InsuranceClaim {
  final int? id;
  final String? claimReference;
  final String? claimNumber;
  final String? patientName;
  final String? insuranceCompanyName;
  final String? policyNumber;
  final double? claimAmount;
  final double? approvedAmount;
  final String? claimStatus;
  final String? processedBy;

  const InsuranceClaim({
    this.id,
    this.claimReference,
    this.claimNumber,
    this.patientName,
    this.insuranceCompanyName,
    this.policyNumber,
    this.claimAmount,
    this.approvedAmount,
    this.claimStatus,
    this.processedBy,
  });

  factory InsuranceClaim.fromJson(Map<String, dynamic> j) => InsuranceClaim(
        id: j['id'],
        claimReference: j['claimReference'],
        claimNumber: j['claimNumber'],
        patientName: j['patientName'],
        insuranceCompanyName: j['insuranceCompanyName'],
        policyNumber: j['policyNumber'],
        claimAmount: (j['claimAmount'] as num?)?.toDouble(),
        approvedAmount: (j['approvedAmount'] as num?)?.toDouble(),
        claimStatus: j['claimStatus'],
        processedBy: j['processedBy'],
      );
}
