import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/test_order.dart';
import 'package:flutter_hospital_management/models/lab_report.dart';
import 'package:flutter_hospital_management/services/lab_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final labServiceProvider = Provider<LabService>((ref) {
  return LabService(ref.watch(dioProvider));
});

class TestOrderListState {
  final bool isLoading;
  final List<TestOrder> orders;
  final String? error;

  const TestOrderListState(
      {this.isLoading = false, this.orders = const [], this.error});

  TestOrderListState copyWith({
    bool? isLoading,
    List<TestOrder>? orders,
    String? error,
  }) =>
      TestOrderListState(
        isLoading: isLoading ?? this.isLoading,
        orders: orders ?? this.orders,
        error: error ?? this.error,
      );
}

final testOrderNotifierProvider =
    StateNotifierProvider<TestOrderNotifier, TestOrderListState>((ref) {
  return TestOrderNotifier(ref.watch(labServiceProvider));
});

class TestOrderNotifier extends StateNotifier<TestOrderListState> {
  final LabService service;

  TestOrderNotifier(this.service) : super(const TestOrderListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getTestOrders();
      state = state.copyWith(isLoading: false, orders: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> advance(int id, String action, Map<String, String> body) async {
    try {
      await service.advanceStatus(id, action, body);
      await load();
    } catch (e) {
      state = state.copyWith(error: e.toString());
    }
  }
}

class LabReportListState {
  final bool isLoading;
  final List<LabReport> reports;
  final String? error;

  const LabReportListState(
      {this.isLoading = false, this.reports = const [], this.error});

  LabReportListState copyWith({
    bool? isLoading,
    List<LabReport>? reports,
    String? error,
  }) =>
      LabReportListState(
        isLoading: isLoading ?? this.isLoading,
        reports: reports ?? this.reports,
        error: error ?? this.error,
      );
}

final labReportNotifierProvider =
    StateNotifierProvider<LabReportNotifier, LabReportListState>((ref) {
  return LabReportNotifier(ref.watch(labServiceProvider));
});

class LabReportNotifier extends StateNotifier<LabReportListState> {
  final LabService service;

  LabReportNotifier(this.service) : super(const LabReportListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getReports();
      state = state.copyWith(isLoading: false, reports: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}

class LabDashboardState {
  final bool isLoading;
  final LabDashboard? dashboard;
  final String? error;

  const LabDashboardState({this.isLoading = false, this.dashboard, this.error});

  LabDashboardState copyWith({
    bool? isLoading,
    LabDashboard? dashboard,
    String? error,
  }) =>
      LabDashboardState(
        isLoading: isLoading ?? this.isLoading,
        dashboard: dashboard ?? this.dashboard,
        error: error ?? this.error,
      );
}

final labDashboardNotifierProvider =
    StateNotifierProvider<LabDashboardNotifier, LabDashboardState>((ref) {
  return LabDashboardNotifier(ref.watch(labServiceProvider));
});

class LabDashboardNotifier extends StateNotifier<LabDashboardState> {
  final LabService service;

  LabDashboardNotifier(this.service) : super(const LabDashboardState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final d = await service.getDashboard();
      state = state.copyWith(isLoading: false, dashboard: d);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
