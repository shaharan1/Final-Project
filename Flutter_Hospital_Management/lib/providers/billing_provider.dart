import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';
import 'package:flutter_hospital_management/services/billing_service.dart';
import 'package:flutter_hospital_management/services/refund_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final billingServiceProvider = Provider<BillingService>((ref) {
  return BillingService(ref.watch(dioProvider));
});

final refundServiceProvider = Provider<RefundService>((ref) {
  return RefundService(ref.watch(dioProvider));
});

class BillingListState {
  final bool isLoading;
  final List<BillingInvoice> invoices;
  final String? error;

  const BillingListState({
    this.isLoading = false,
    this.invoices = const [],
    this.error,
  });

  BillingListState copyWith({
    bool? isLoading,
    List<BillingInvoice>? invoices,
    String? error,
  }) {
    return BillingListState(
      isLoading: isLoading ?? this.isLoading,
      invoices: invoices ?? this.invoices,
      error: error ?? this.error,
    );
  }
}

final billingNotifierProvider =
    StateNotifierProvider<BillingNotifier, BillingListState>((ref) {
  return BillingNotifier(ref.watch(billingServiceProvider));
});

class BillingNotifier extends StateNotifier<BillingListState> {
  final BillingService service;

  BillingNotifier(this.service) : super(const BillingListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getAll();
      state = state.copyWith(isLoading: false, invoices: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> search(String q) async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list =
          q.isEmpty ? await service.getAll() : await service.search(q);
      state = state.copyWith(isLoading: false, invoices: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> create(BillingInvoiceRequest request) async {
    try {
      await service.create(request);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}

class BillingDetailState {
  final bool isLoading;
  final BillingInvoice? invoice;
  final List<BillingPayment> payments;
  final String? error;

  const BillingDetailState({
    this.isLoading = false,
    this.invoice,
    this.payments = const [],
    this.error,
  });

  BillingDetailState copyWith({
    bool? isLoading,
    BillingInvoice? invoice,
    List<BillingPayment>? payments,
    String? error,
  }) {
    return BillingDetailState(
      isLoading: isLoading ?? this.isLoading,
      invoice: invoice ?? this.invoice,
      payments: payments ?? this.payments,
      error: error ?? this.error,
    );
  }
}

final billingDetailProvider = StateNotifierProvider.family<
    BillingDetailNotifier, BillingDetailState, int>((ref, id) {
  return BillingDetailNotifier(ref.watch(billingServiceProvider), id);
});

class BillingDetailNotifier extends StateNotifier<BillingDetailState> {
  final BillingService service;
  final int id;

  BillingDetailNotifier(this.service, this.id)
      : super(const BillingDetailState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final invoice = await service.getById(id);
      final payments = await service.getPayments(id);
      state = state.copyWith(
          isLoading: false, invoice: invoice, payments: payments);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> addItem(BillingInvoiceItemRequest item) async {
    try {
      final invoice = await service.addItem(id, item);
      state = state.copyWith(invoice: invoice);
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }

  Future<bool> finalize(String finalizedBy) async {
    try {
      final invoice = await service.finalize(id, finalizedBy);
      state = state.copyWith(invoice: invoice);
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }

  Future<bool> recordPayment(BillingPaymentRequest request) async {
    try {
      await service.processPayment(request);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}

class CategoryState {
  final bool isLoading;
  final List<ChargeCategory> categories;
  final String? error;

  const CategoryState({
    this.isLoading = false,
    this.categories = const [],
    this.error,
  });

  CategoryState copyWith({
    bool? isLoading,
    List<ChargeCategory>? categories,
    String? error,
  }) {
    return CategoryState(
      isLoading: isLoading ?? this.isLoading,
      categories: categories ?? this.categories,
      error: error ?? this.error,
    );
  }
}

final categoryProvider =
    StateNotifierProvider<CategoryNotifier, CategoryState>((ref) {
  return CategoryNotifier(ref.watch(billingServiceProvider));
});

class CategoryNotifier extends StateNotifier<CategoryState> {
  final BillingService service;

  CategoryNotifier(this.service) : super(const CategoryState());

  Future<void> load() async {
    if (state.categories.isNotEmpty) return;
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getCategories();
      state = state.copyWith(isLoading: false, categories: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
