class PharmacySaleItem {
  final int? id;
  final int? medicineStockId;
  final String? medicineName;
  final String? batchNumber;
  final int? quantity;
  final double? unitPrice;
  final double? discount;
  final double? subtotal;

  const PharmacySaleItem({
    this.id,
    this.medicineStockId,
    this.medicineName,
    this.batchNumber,
    this.quantity,
    this.unitPrice,
    this.discount,
    this.subtotal,
  });

  factory PharmacySaleItem.fromJson(Map<String, dynamic> j) => PharmacySaleItem(
        id: j['id'],
        medicineStockId: j['medicineStockId'],
        medicineName: j['medicineName'],
        batchNumber: j['batchNumber'],
        quantity: j['quantity'],
        unitPrice: (j['unitPrice'] as num?)?.toDouble(),
        discount: (j['discount'] as num?)?.toDouble(),
        subtotal: (j['subtotal'] as num?)?.toDouble(),
      );
}

class PharmacySale {
  final int id;
  final String? saleInvoiceNo;
  final String? patientType;
  final String? patientName;
  final String? patientPhone;
  final int? patientId;
  final String? doctorName;
  final double? totalAmount;
  final double? discount;
  final double? vat;
  final double? netPayable;
  final double? paidAmount;
  final String? paymentMethod;
  final String? paymentStatus;
  final String? saleType;
  final List<PharmacySaleItem> items;
  final String? saleDate;

  const PharmacySale({
    required this.id,
    this.saleInvoiceNo,
    this.patientType,
    this.patientName,
    this.patientPhone,
    this.patientId,
    this.doctorName,
    this.totalAmount,
    this.discount,
    this.vat,
    this.netPayable,
    this.paidAmount,
    this.paymentMethod,
    this.paymentStatus,
    this.saleType,
    this.items = const [],
    this.saleDate,
  });

  factory PharmacySale.fromJson(Map<String, dynamic> j) => PharmacySale(
        id: j['id'],
        saleInvoiceNo: j['saleInvoiceNo'],
        patientType: j['patientType'],
        patientName: j['patientName'],
        patientPhone: j['patientPhone'],
        patientId: j['patientId'],
        doctorName: j['doctorName'],
        totalAmount: (j['totalAmount'] as num?)?.toDouble(),
        discount: (j['discount'] as num?)?.toDouble(),
        vat: (j['vat'] as num?)?.toDouble(),
        netPayable: (j['netPayable'] as num?)?.toDouble(),
        paidAmount: (j['paidAmount'] as num?)?.toDouble(),
        paymentMethod: j['paymentMethod'],
        paymentStatus: j['paymentStatus'],
        saleType: j['saleType'],
        items: (j['items'] as List?)
                ?.map((e) =>
                    PharmacySaleItem.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
        saleDate: j['saleDate']?.toString(),
      );
}
