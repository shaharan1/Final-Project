class Patient {
  final int? id;
  final String? patientCode;
  final String name;
  final String? gender;
  final String? dateOfBirth;
  final String? bloodGroup;
  final String? maritalStatus;
  final String? phone;
  final String? alternatePhone;
  final String? email;
  final String? nationalId;
  final String? address;
  final String? city;
  final String? district;
  final String? postalCode;
  final String? emergencyContactName;
  final String? emergencyContactNumber;
  final String? relationship;

  const Patient({
    this.id,
    this.patientCode,
    required this.name,
    this.gender,
    this.dateOfBirth,
    this.bloodGroup,
    this.maritalStatus,
    this.phone,
    this.alternatePhone,
    this.email,
    this.nationalId,
    this.address,
    this.city,
    this.district,
    this.postalCode,
    this.emergencyContactName,
    this.emergencyContactNumber,
    this.relationship,
  });

  factory Patient.fromJson(Map<String, dynamic> j) => Patient(
        id: j['id'],
        patientCode: j['patientCode'],
        name: (j['name'] ?? '').toString(),
        gender: j['gender'],
        dateOfBirth: j['dateOfBirth']?.toString(),
        bloodGroup: j['bloodGroup'],
        maritalStatus: j['maritalStatus'],
        phone: j['phone'],
        alternatePhone: j['alternatePhone'],
        email: j['email'],
        nationalId: j['nationalId'],
        address: j['address'],
        city: j['city'],
        district: j['district'],
        postalCode: j['postalCode'],
        emergencyContactName: j['emergencyContactName'],
        emergencyContactNumber: j['emergencyContactNumber'],
        relationship: j['relationship'],
      );
}

class PatientRequest {
  final String name;
  final String? gender;
  final String? dateOfBirth;
  final String? bloodGroup;
  final String? maritalStatus;
  final String? phone;
  final String? alternatePhone;
  final String? email;
  final String? nationalId;
  final String? address;
  final String? city;
  final String? district;
  final String? postalCode;
  final String? emergencyContactName;
  final String? emergencyContactNumber;
  final String? relationship;

  PatientRequest({
    required this.name,
    this.gender,
    this.dateOfBirth,
    this.bloodGroup,
    this.maritalStatus,
    this.phone,
    this.alternatePhone,
    this.email,
    this.nationalId,
    this.address,
    this.city,
    this.district,
    this.postalCode,
    this.emergencyContactName,
    this.emergencyContactNumber,
    this.relationship,
  });

  Map<String, dynamic> toJson() {
    final m = <String, dynamic>{'name': name};
    if (gender != null) m['gender'] = gender;
    if (dateOfBirth != null) m['dateOfBirth'] = dateOfBirth;
    if (bloodGroup != null) m['bloodGroup'] = bloodGroup;
    if (maritalStatus != null) m['maritalStatus'] = maritalStatus;
    if (phone != null) m['phone'] = phone;
    if (alternatePhone != null) m['alternatePhone'] = alternatePhone;
    if (email != null) m['email'] = email;
    if (nationalId != null) m['nationalId'] = nationalId;
    if (address != null) m['address'] = address;
    if (city != null) m['city'] = city;
    if (district != null) m['district'] = district;
    if (postalCode != null) m['postalCode'] = postalCode;
    if (emergencyContactName != null) {
      m['emergencyContactName'] = emergencyContactName;
    }
    if (emergencyContactNumber != null) {
      m['emergencyContactNumber'] = emergencyContactNumber;
    }
    if (relationship != null) m['relationship'] = relationship;
    return m;
  }
}
