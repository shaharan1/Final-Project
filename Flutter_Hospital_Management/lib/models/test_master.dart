class TestMaster {
  final int? id;
  final String? testCode;
  final String? testName;
  final double? standardPrice;
  final String? normalRange;
  final bool? active;

  const TestMaster({
    this.id,
    this.testCode,
    this.testName,
    this.standardPrice,
    this.normalRange,
    this.active,
  });

  factory TestMaster.fromJson(Map<String, dynamic> j) => TestMaster(
        id: j['id'],
        testCode: j['testCode'],
        testName: j['testName'],
        standardPrice: (j['standardPrice'] as num?)?.toDouble(),
        normalRange: j['normalRange'],
        active: j['active'],
      );

  String get display => testName ?? testCode ?? 'Test #$id';
}
