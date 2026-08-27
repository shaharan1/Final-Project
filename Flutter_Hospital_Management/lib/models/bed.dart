class Bed {
  final int? id;
  final String? bedNumber;
  final int? wardId;
  final String? wardName;
  final String? roomType;
  final String? status;
  final double? totalDailyCost;

  const Bed({
    this.id,
    this.bedNumber,
    this.wardId,
    this.wardName,
    this.roomType,
    this.status,
    this.totalDailyCost,
  });

  factory Bed.fromJson(Map<String, dynamic> j) => Bed(
        id: j['id'],
        bedNumber: j['bedNumber'],
        wardId: j['wardId'],
        wardName: j['wardName'],
        roomType: j['roomType'],
        status: j['status']?.toString(),
        totalDailyCost: (j['totalDailyCost'] as num?)?.toDouble(),
      );
}
