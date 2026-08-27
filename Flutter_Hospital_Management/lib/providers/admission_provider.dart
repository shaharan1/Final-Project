import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/admission.dart';
import 'package:flutter_hospital_management/services/admission_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final admissionServiceProvider = Provider<AdmissionService>((ref) {
  return AdmissionService(ref.watch(dioProvider));
});

class AdmissionListState {
  final bool isLoading;
  final List<Admission> admissions;
  final String? error;

  const AdmissionListState({
    this.isLoading = false,
    this.admissions = const [],
    this.error,
  });

  AdmissionListState copyWith({
    bool? isLoading,
    List<Admission>? admissions,
    String? error,
  }) {
    return AdmissionListState(
      isLoading: isLoading ?? this.isLoading,
      admissions: admissions ?? this.admissions,
      error: error ?? this.error,
    );
  }
}

final admissionNotifierProvider =
    StateNotifierProvider<AdmissionNotifier, AdmissionListState>((ref) {
  return AdmissionNotifier(ref.watch(admissionServiceProvider));
});

class AdmissionNotifier extends StateNotifier<AdmissionListState> {
  final AdmissionService service;

  AdmissionNotifier(this.service) : super(const AdmissionListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getAll();
      state = state.copyWith(isLoading: false, admissions: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> admit(AdmissionRequest request) async {
    try {
      await service.admit(request);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }

  Future<bool> discharge(int id) async {
    try {
      await service.discharge(id);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}
