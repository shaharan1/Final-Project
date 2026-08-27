import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/medicine.dart';
import 'package:flutter_hospital_management/models/pharmacy_sale.dart';
import 'package:flutter_hospital_management/services/pharmacy_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final pharmacyServiceProvider = Provider<PharmacyService>((ref) {
  return PharmacyService(ref.watch(dioProvider));
});

class MedicineListState {
  final bool isLoading;
  final List<Medicine> medicines;
  final String? error;

  const MedicineListState(
      {this.isLoading = false, this.medicines = const [], this.error});

  MedicineListState copyWith({
    bool? isLoading,
    List<Medicine>? medicines,
    String? error,
  }) =>
      MedicineListState(
        isLoading: isLoading ?? this.isLoading,
        medicines: medicines ?? this.medicines,
        error: error ?? this.error,
      );
}

final medicineNotifierProvider =
    StateNotifierProvider<MedicineNotifier, MedicineListState>((ref) {
  return MedicineNotifier(ref.watch(pharmacyServiceProvider));
});

class MedicineNotifier extends StateNotifier<MedicineListState> {
  final PharmacyService service;

  MedicineNotifier(this.service) : super(const MedicineListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getMedicines();
      state = state.copyWith(isLoading: false, medicines: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> search(String keyword) async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = keyword.isEmpty
          ? await service.getMedicines()
          : await service.searchMedicines(keyword);
      state = state.copyWith(isLoading: false, medicines: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> create(Medicine m) async {
    try {
      await service.createMedicine(m);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}

class SaleListState {
  final bool isLoading;
  final List<PharmacySale> sales;
  final String? error;

  const SaleListState(
      {this.isLoading = false, this.sales = const [], this.error});

  SaleListState copyWith({
    bool? isLoading,
    List<PharmacySale>? sales,
    String? error,
  }) =>
      SaleListState(
        isLoading: isLoading ?? this.isLoading,
        sales: sales ?? this.sales,
        error: error ?? this.error,
      );
}

final saleNotifierProvider =
    StateNotifierProvider<SaleNotifier, SaleListState>((ref) {
  return SaleNotifier(ref.watch(pharmacyServiceProvider));
});

class SaleNotifier extends StateNotifier<SaleListState> {
  final PharmacyService service;

  SaleNotifier(this.service) : super(const SaleListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getSales();
      state = state.copyWith(isLoading: false, sales: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
