import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/clinical.dart';
import 'package:flutter_hospital_management/services/clinical_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final dietPlanServiceProvider = Provider<DietPlanService>((ref) {
  return DietPlanService(ref.watch(dioProvider));
});

class DietPlanListState {
  final bool isLoading;
  final List<DietPlan> plans;
  final String? error;

  const DietPlanListState(
      {this.isLoading = false, this.plans = const [], this.error});

  DietPlanListState copyWith({
    bool? isLoading,
    List<DietPlan>? plans,
    String? error,
  }) =>
      DietPlanListState(
        isLoading: isLoading ?? this.isLoading,
        plans: plans ?? this.plans,
        error: error ?? this.error,
      );
}

final dietPlanNotifierProvider =
    StateNotifierProvider<DietPlanNotifier, DietPlanListState>((ref) {
  return DietPlanNotifier(ref.watch(dietPlanServiceProvider));
});

class DietPlanNotifier extends StateNotifier<DietPlanListState> {
  final DietPlanService service;

  DietPlanNotifier(this.service) : super(const DietPlanListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getDietPlans();
      state = state.copyWith(isLoading: false, plans: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}

final surgeryServiceProvider = Provider<SurgeryService>((ref) {
  return SurgeryService(ref.watch(dioProvider));
});

class ClinicalListState {
  final bool isLoading;
  final List<Surgery> surgeries;
  final List<SurgeryMaster> masters;
  final String? error;

  const ClinicalListState(
      {this.isLoading = false,
      this.surgeries = const [],
      this.masters = const [],
      this.error});

  ClinicalListState copyWith({
    bool? isLoading,
    List<Surgery>? surgeries,
    List<SurgeryMaster>? masters,
    String? error,
  }) =>
      ClinicalListState(
        isLoading: isLoading ?? this.isLoading,
        surgeries: surgeries ?? this.surgeries,
        masters: masters ?? this.masters,
        error: error ?? this.error,
      );
}

final clinicalNotifierProvider =
    StateNotifierProvider<ClinicalNotifier, ClinicalListState>((ref) {
  return ClinicalNotifier(ref.watch(surgeryServiceProvider));
});

class ClinicalNotifier extends StateNotifier<ClinicalListState> {
  final SurgeryService service;

  ClinicalNotifier(this.service) : super(const ClinicalListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final surgeries = await service.getSurgeries();
      final masters = await service.getSurgeryMasters();
      state = state.copyWith(
          isLoading: false, surgeries: surgeries, masters: masters);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
