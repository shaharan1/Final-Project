class DietPlan {
  final int? id;
  final String? name;
  final String? dietType;
  final String? description;
  final double? totalCalories;
  final double? protein;
  final double? pricePerDay;
  final bool? active;

  const DietPlan({
    this.id,
    this.name,
    this.dietType,
    this.description,
    this.totalCalories,
    this.protein,
    this.pricePerDay,
    this.active,
  });

  factory DietPlan.fromJson(Map<String, dynamic> j) => DietPlan(
        id: j['id'],
        name: j['name'],
        dietType: j['dietType'],
        description: j['description'],
        totalCalories: (j['totalCalories'] as num?)?.toDouble(),
        protein: (j['protein'] as num?)?.toDouble(),
        pricePerDay: (j['pricePerDay'] as num?)?.toDouble(),
        active: j['active'],
      );
}

class SurgeryMaster {
  final int? id;
  final String? surgeryCode;
  final String? surgeryName;
  final String? categoryName;
  final double? standardRate;
  final bool? active;
  final int? estimatedDurationMin;

  const SurgeryMaster({
    this.id,
    this.surgeryCode,
    this.surgeryName,
    this.categoryName,
    this.standardRate,
    this.active,
    this.estimatedDurationMin,
  });

  factory SurgeryMaster.fromJson(Map<String, dynamic> j) => SurgeryMaster(
        id: j['id'],
        surgeryCode: j['surgeryCode'],
        surgeryName: j['surgeryName'],
        categoryName: j['categoryName'],
        standardRate: (j['standardRate'] as num?)?.toDouble(),
        active: j['active'],
        estimatedDurationMin: j['estimatedDurationMin'],
      );
}

class Surgery {
  final int? id;
  final String? surgeryNumber;
  final String? patientName;
  final String? patientCode;
  final String? surgeonName;
  final String? surgeryName;
  final String? surgeryDate;
  final String? priority;
  final String? status;
  final String? wardName;
  final String? bedNumber;
  final double? totalAmount;
  final double? finalPayable;

  const Surgery({
    this.id,
    this.surgeryNumber,
    this.patientName,
    this.patientCode,
    this.surgeonName,
    this.surgeryName,
    this.surgeryDate,
    this.priority,
    this.status,
    this.wardName,
    this.bedNumber,
    this.totalAmount,
    this.finalPayable,
  });

  factory Surgery.fromJson(Map<String, dynamic> j) => Surgery(
        id: j['id'],
        surgeryNumber: j['surgeryNumber'],
        patientName: j['patientName'],
        patientCode: j['patientCode'],
        surgeonName: j['surgeonName'],
        surgeryName: j['surgeryName'],
        surgeryDate: j['surgeryDate']?.toString(),
        priority: j['priority'],
        status: j['status'],
        wardName: j['wardName'],
        bedNumber: j['bedNumber'],
        totalAmount: (j['totalAmount'] as num?)?.toDouble(),
        finalPayable: (j['finalPayable'] as num?)?.toDouble(),
      );
}
