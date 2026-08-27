class CategoryRevenue {
  final String? categoryCode;
  final String? categoryName;
  final double? totalAmount;
  final int? itemCount;

  const CategoryRevenue({
    this.categoryCode,
    this.categoryName,
    this.totalAmount,
    this.itemCount,
  });

  factory CategoryRevenue.fromJson(Map<String, dynamic> j) => CategoryRevenue(
        categoryCode: j['categoryCode'],
        categoryName: j['categoryName'],
        totalAmount: (j['totalAmount'] as num?)?.toDouble(),
        itemCount: j['itemCount'],
      );
}

class PaymentMethodSummary {
  final String? method;
  final int? count;
  final double? totalAmount;

  const PaymentMethodSummary({this.method, this.count, this.totalAmount});

  factory PaymentMethodSummary.fromJson(Map<String, dynamic> j) =>
      PaymentMethodSummary(
        method: j['method'],
        count: j['count'],
        totalAmount: (j['totalAmount'] as num?)?.toDouble(),
      );
}

class DailyRevenue {
  final String? date;
  final double? revenue;
  final double? collected;

  const DailyRevenue({this.date, this.revenue, this.collected});

  factory DailyRevenue.fromJson(Map<String, dynamic> j) => DailyRevenue(
        date: j['date'],
        revenue: (j['revenue'] as num?)?.toDouble(),
        collected: (j['collected'] as num?)?.toDouble(),
      );
}

class BillingDashboard {
  final int totalInvoices;
  final int todayInvoices;
  final double? totalRevenue;
  final double? todayRevenue;
  final double? totalCollected;
  final double? totalDue;
  final int unpaidCount;
  final int partialCount;
  final int paidCount;
  final List<CategoryRevenue> revenueByCategory;
  final List<PaymentMethodSummary> paymentMethodBreakdown;
  final List<DailyRevenue> dailyRevenueChart;

  const BillingDashboard({
    this.totalInvoices = 0,
    this.todayInvoices = 0,
    this.totalRevenue,
    this.todayRevenue,
    this.totalCollected,
    this.totalDue,
    this.unpaidCount = 0,
    this.partialCount = 0,
    this.paidCount = 0,
    this.revenueByCategory = const [],
    this.paymentMethodBreakdown = const [],
    this.dailyRevenueChart = const [],
  });

  factory BillingDashboard.fromJson(Map<String, dynamic> j) => BillingDashboard(
        totalInvoices: j['totalInvoices'] ?? 0,
        todayInvoices: j['todayInvoices'] ?? 0,
        totalRevenue: (j['totalRevenue'] as num?)?.toDouble(),
        todayRevenue: (j['todayRevenue'] as num?)?.toDouble(),
        totalCollected: (j['totalCollected'] as num?)?.toDouble(),
        totalDue: (j['totalDue'] as num?)?.toDouble(),
        unpaidCount: j['unpaidCount'] ?? 0,
        partialCount: j['partialCount'] ?? 0,
        paidCount: j['paidCount'] ?? 0,
        revenueByCategory: (j['revenueByCategory'] as List?)
                ?.map((e) => CategoryRevenue.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
        paymentMethodBreakdown: (j['paymentMethodBreakdown'] as List?)
                ?.map((e) =>
                    PaymentMethodSummary.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
        dailyRevenueChart: (j['dailyRevenueChart'] as List?)
                ?.map((e) => DailyRevenue.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
      );
}
