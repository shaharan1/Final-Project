import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/insurance.dart';
import 'package:flutter_hospital_management/services/insurance_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final insuranceServiceProvider = Provider<InsuranceService>((ref) {
  return InsuranceService(ref.watch(dioProvider));
});

class InsuranceListState {
  final bool isLoading;
  final List<Insurance> insurances;
  final List<InsuranceClaim> claims;
  final String? error;

  const InsuranceListState(
      {this.isLoading = false,
      this.insurances = const [],
      this.claims = const [],
      this.error});

  InsuranceListState copyWith({
    bool? isLoading,
    List<Insurance>? insurances,
    List<InsuranceClaim>? claims,
    String? error,
  }) =>
      InsuranceListState(
        isLoading: isLoading ?? this.isLoading,
        insurances: insurances ?? this.insurances,
        claims: claims ?? this.claims,
        error: error ?? this.error,
      );
}

final insuranceNotifierProvider =
    StateNotifierProvider<InsuranceNotifier, InsuranceListState>((ref) {
  return InsuranceNotifier(ref.watch(insuranceServiceProvider));
});

class InsuranceNotifier extends StateNotifier<InsuranceListState> {
  final InsuranceService service;

  InsuranceNotifier(this.service) : super(const InsuranceListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final insurances = await service.getInsurances();
      final claims = await service.getClaims();
      state = state.copyWith(
          isLoading: false, insurances: insurances, claims: claims);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
