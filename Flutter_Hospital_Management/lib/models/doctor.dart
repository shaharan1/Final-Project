class Doctor {
  final int? id;
  final String? name;
  final String? email;
  final String? phone;
  final String? gender;
  final String? status;
  final String? study;
  final String? specialization;
  final String? designation;
  final String? registrationNumber;
  final int? experienceYears;
  final double? consultationFee;
  final double? followUpFee;
  final String? availableDays;
  final String? dutyHours;
  final String? chamber;
  final String? departmentName;
  final String? description;

  const Doctor({
    this.id,
    this.name,
    this.email,
    this.phone,
    this.gender,
    this.status,
    this.study,
    this.specialization,
    this.designation,
    this.registrationNumber,
    this.experienceYears,
    this.consultationFee,
    this.followUpFee,
    this.availableDays,
    this.dutyHours,
    this.chamber,
    this.departmentName,
    this.description,
  });

  factory Doctor.fromJson(Map<String, dynamic> j) => Doctor(
        id: j['id'],
        name: j['name'],
        email: j['email'],
        phone: j['phone'],
        gender: j['gender'],
        status: j['status'],
        study: j['study'],
        specialization: j['specialization'],
        designation: j['designation'],
        registrationNumber: j['registrationNumber'],
        experienceYears: j['experienceYears'],
        consultationFee: (j['consultationFee'] as num?)?.toDouble(),
        followUpFee: (j['followUpFee'] as num?)?.toDouble(),
        availableDays: j['availableDays'],
        dutyHours: j['dutyHours'],
        chamber: j['chamber'],
        departmentName: j['departmentName'],
        description: j['description'],
      );

  String get displayName =>
      name != null && name!.isNotEmpty ? name! : 'Doctor';
}
