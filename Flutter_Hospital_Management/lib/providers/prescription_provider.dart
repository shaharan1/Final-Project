import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/prescription.dart';
import 'package:flutter_hospital_management/services/prescription_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final prescriptionServiceProvider = Provider<PrescriptionService>((ref) {
  return PrescriptionService(ref.watch(dioProvider));
});

class PrescriptionListState {
  final bool isLoading;
  final List<PrescriptionResponse> prescriptions;
  final String? error;

  const PrescriptionListState(
      {this.isLoading = false,
      this.prescriptions = const [],
      this.error});

  PrescriptionListState copyWith({
    bool? isLoading,
    List<PrescriptionResponse>? prescriptions,
    String? error,
  }) =>
      PrescriptionListState(
        isLoading: isLoading ?? this.isLoading,
        prescriptions: prescriptions ?? this.prescriptions,
        error: error ?? this.error,
      );
}

final prescriptionNotifierProvider =
    StateNotifierProvider<PrescriptionNotifier, PrescriptionListState>((ref) {
  return PrescriptionNotifier(ref.watch(prescriptionServiceProvider));
});

class PrescriptionNotifier extends StateNotifier<PrescriptionListState> {
  final PrescriptionService service;

  PrescriptionNotifier(this.service)
      : super(const PrescriptionListState());

  Future<void> loadByDoctor(int doctorId) async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getByDoctor(doctorId);
      state = state.copyWith(isLoading: false, prescriptions: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> create(PrescriptionRequest request) async {
    try {
      await service.create(request);
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}
