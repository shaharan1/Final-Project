import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/dashboard.dart';
import 'package:flutter_hospital_management/services/dashboard_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final dashboardServiceProvider = Provider<DashboardService>((ref) {
  return DashboardService(ref.watch(dioProvider));
});

class DashboardState {
  final bool isLoading;
  final BillingDashboard? billing;
  final Map<String, dynamic>? payments;
  final String? error;

  const DashboardState({
    this.isLoading = false,
    this.billing,
    this.payments,
    this.error,
  });

  DashboardState copyWith({
    bool? isLoading,
    BillingDashboard? billing,
    Map<String, dynamic>? payments,
    String? error,
  }) {
    return DashboardState(
      isLoading: isLoading ?? this.isLoading,
      billing: billing ?? this.billing,
      payments: payments ?? this.payments,
      error: error ?? this.error,
    );
  }
}

final dashboardNotifierProvider =
    StateNotifierProvider<DashboardNotifier, DashboardState>((ref) {
  return DashboardNotifier(ref.watch(dashboardServiceProvider));
});

class DashboardNotifier extends StateNotifier<DashboardState> {
  final DashboardService service;

  DashboardNotifier(this.service) : super(const DashboardState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final billing = await service.getBillingSummary();
      final payments = await service.getPaymentStats();
      state = state.copyWith(
          isLoading: false, billing: billing, payments: payments);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
