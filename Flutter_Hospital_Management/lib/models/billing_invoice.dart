class ChargeCategory {
  final int? id;
  final String? code;
  final String? name;
  final String? description;
  final double? defaultUnitPrice;

  const ChargeCategory({
    this.id,
    this.code,
    this.name,
    this.description,
    this.defaultUnitPrice,
  });

  factory ChargeCategory.fromJson(Map<String, dynamic> j) => ChargeCategory(
        id: j['id'],
        code: j['code'],
        name: j['name'],
        description: j['description'],
        defaultUnitPrice: (j['defaultUnitPrice'] as num?)?.toDouble(),
      );

  String get display => name ?? code ?? 'Category';
}

class BillingInvoiceItem {
  final int? id;
  final int? chargeCategoryId;
  final String? categoryCode;
  final String? categoryName;
  final String? description;
  final int? quantity;
  final double? unitPrice;
  final double? discountPercent;
  final double? discountAmount;
  final double? amount;
  final String? itemStatus;

  const BillingInvoiceItem({
    this.id,
    this.chargeCategoryId,
    this.categoryCode,
    this.categoryName,
    this.description,
    this.quantity,
    this.unitPrice,
    this.discountPercent,
    this.discountAmount,
    this.amount,
    this.itemStatus,
  });

  factory BillingInvoiceItem.fromJson(Map<String, dynamic> j) => BillingInvoiceItem(
        id: j['id'],
        chargeCategoryId: j['chargeCategoryId'],
        categoryCode: j['categoryCode'],
        categoryName: j['categoryName'],
        description: j['description'],
        quantity: j['quantity'],
        unitPrice: (j['unitPrice'] as num?)?.toDouble(),
        discountPercent: (j['discountPercent'] as num?)?.toDouble(),
        discountAmount: (j['discountAmount'] as num?)?.toDouble(),
        amount: (j['amount'] as num?)?.toDouble(),
        itemStatus: j['itemStatus'],
      );
}

class BillingInvoice {
  final int? id;
  final String? invoiceNumber;
  final int? patientId;
  final String? patientName;
  final String? patientCode;
  final String? patientPhone;
  final String? invoiceType;
  final List<BillingInvoiceItem> items;
  final double? subtotal;
  final double? taxRate;
  final double? taxAmount;
  final double? discountPercent;
  final double? discountAmount;
  final double? netAmount;
  final double? totalPaid;
  final double? dueAmount;
  final String? paymentStatus;
  final String? invoiceStatus;
  final String? notes;
  final String? preparedBy;
  final String? finalizedBy;

  const BillingInvoice({
    this.id,
    this.invoiceNumber,
    this.patientId,
    this.patientName,
    this.patientCode,
    this.patientPhone,
    this.invoiceType,
    this.items = const [],
    this.subtotal,
    this.taxRate,
    this.taxAmount,
    this.discountPercent,
    this.discountAmount,
    this.netAmount,
    this.totalPaid,
    this.dueAmount,
    this.paymentStatus,
    this.invoiceStatus,
    this.notes,
    this.preparedBy,
    this.finalizedBy,
  });

  factory BillingInvoice.fromJson(Map<String, dynamic> j) => BillingInvoice(
        id: j['id'],
        invoiceNumber: j['invoiceNumber'],
        patientId: j['patientId'],
        patientName: j['patientName'],
        patientCode: j['patientCode'],
        patientPhone: j['patientPhone'],
        invoiceType: j['invoiceType'],
        items: (j['items'] as List?)
                ?.map((e) => BillingInvoiceItem.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [],
        subtotal: (j['subtotal'] as num?)?.toDouble(),
        taxRate: (j['taxRate'] as num?)?.toDouble(),
        taxAmount: (j['taxAmount'] as num?)?.toDouble(),
        discountPercent: (j['discountPercent'] as num?)?.toDouble(),
        discountAmount: (j['discountAmount'] as num?)?.toDouble(),
        netAmount: (j['netAmount'] as num?)?.toDouble(),
        totalPaid: (j['totalPaid'] as num?)?.toDouble(),
        dueAmount: (j['dueAmount'] as num?)?.toDouble(),
        paymentStatus: j['paymentStatus'],
        invoiceStatus: j['invoiceStatus'],
        notes: j['notes'],
        preparedBy: j['preparedBy'],
        finalizedBy: j['finalizedBy'],
      );
}

class BillingInvoiceItemRequest {
  final int? chargeCategoryId;
  final String? categoryCode;
  final String? description;
  final int? quantity;
  final double? unitPrice;
  final double? discountPercent;
  final String? sourceModule;
  final int? sourceId;

  BillingInvoiceItemRequest({
    this.chargeCategoryId,
    this.categoryCode,
    this.description,
    this.quantity,
    this.unitPrice,
    this.discountPercent,
    this.sourceModule,
    this.sourceId,
  });

  Map<String, dynamic> toJson() {
    final m = <String, dynamic>{};
    if (chargeCategoryId != null) m['chargeCategoryId'] = chargeCategoryId;
    if (categoryCode != null) m['categoryCode'] = categoryCode;
    if (description != null) m['description'] = description;
    if (quantity != null) m['quantity'] = quantity;
    if (unitPrice != null) m['unitPrice'] = unitPrice;
    if (discountPercent != null) m['discountPercent'] = discountPercent;
    if (sourceModule != null) m['sourceModule'] = sourceModule;
    if (sourceId != null) m['sourceId'] = sourceId;
    return m;
  }
}

class BillingInvoiceRequest {
  final int? patientId;
  final int? admittedPatientId;
  final String? invoiceType;
  final double? taxRate;
  final double? discountPercent;
  final String? notes;
  final String? preparedBy;
  final List<BillingInvoiceItemRequest> items;

  BillingInvoiceRequest({
    this.patientId,
    this.admittedPatientId,
    this.invoiceType,
    this.taxRate,
    this.discountPercent,
    this.notes,
    this.preparedBy,
    this.items = const [],
  });

  Map<String, dynamic> toJson() => {
        if (patientId != null) 'patientId': patientId,
        if (admittedPatientId != null) 'admittedPatientId': admittedPatientId,
        if (invoiceType != null) 'invoiceType': invoiceType,
        if (taxRate != null) 'taxRate': taxRate,
        if (discountPercent != null) 'discountPercent': discountPercent,
        if (notes != null) 'notes': notes,
        if (preparedBy != null) 'preparedBy': preparedBy,
        'items': items.map((e) => e.toJson()).toList(),
      };
}

class BillingPayment {
  final int? id;
  final int? invoiceId;
  final String? invoiceNumber;
  final String? patientName;
  final double? amount;
  final String? paymentMethod;
  final String? transactionId;
  final String? paymentStatus;
  final String? notes;
  final String? processedBy;

  const BillingPayment({
    this.id,
    this.invoiceId,
    this.invoiceNumber,
    this.patientName,
    this.amount,
    this.paymentMethod,
    this.transactionId,
    this.paymentStatus,
    this.notes,
    this.processedBy,
  });

  factory BillingPayment.fromJson(Map<String, dynamic> j) => BillingPayment(
        id: j['id'],
        invoiceId: j['invoiceId'],
        invoiceNumber: j['invoiceNumber'],
        patientName: j['patientName'],
        amount: (j['amount'] as num?)?.toDouble(),
        paymentMethod: j['paymentMethod'],
        transactionId: j['transactionId'],
        paymentStatus: j['paymentStatus'],
        notes: j['notes'],
        processedBy: j['processedBy'],
      );
}

class BillingPaymentRequest {
  final int? invoiceId;
  final double? amount;
  final String? paymentMethod;
  final String? transactionId;
  final String? notes;
  final String? processedBy;

  BillingPaymentRequest({
    this.invoiceId,
    this.amount,
    this.paymentMethod,
    this.transactionId,
    this.notes,
    this.processedBy,
  });

  Map<String, dynamic> toJson() => {
        if (invoiceId != null) 'invoiceId': invoiceId,
        if (amount != null) 'amount': amount,
        if (paymentMethod != null) 'paymentMethod': paymentMethod,
        if (transactionId != null) 'transactionId': transactionId,
        if (notes != null) 'notes': notes,
        if (processedBy != null) 'processedBy': processedBy,
      };
}

class RefundRequestModel {
  final int? paymentId;
  final String? invoiceNumber;
  final int? patientId;
  final String? patientName;
  final double? refundAmount;
  final String? refundReason;
  final String? refundType;
  final String? processedBy;
  final String? notes;

  RefundRequestModel({
    this.paymentId,
    this.invoiceNumber,
    this.patientId,
    this.patientName,
    this.refundAmount,
    this.refundReason,
    this.refundType,
    this.processedBy,
    this.notes,
  });

  Map<String, dynamic> toJson() => {
        if (paymentId != null) 'paymentId': paymentId,
        if (invoiceNumber != null) 'invoiceNumber': invoiceNumber,
        if (patientId != null) 'patientId': patientId,
        if (patientName != null) 'patientName': patientName,
        if (refundAmount != null) 'refundAmount': refundAmount,
        if (refundReason != null) 'refundReason': refundReason,
        if (refundType != null) 'refundType': refundType,
        if (processedBy != null) 'processedBy': processedBy,
        if (notes != null) 'notes': notes,
      };
}
