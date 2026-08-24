import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/services/patient_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final patientServiceProvider = Provider<PatientService>((ref) {
  return PatientService(ref.watch(dioProvider));
});

class PatientListState {
  final bool isLoading;
  final List<Patient> patients;
  final String? error;

  const PatientListState({
    this.isLoading = false,
    this.patients = const [],
    this.error,
  });

  PatientListState copyWith({
    bool? isLoading,
    List<Patient>? patients,
    String? error,
  }) {
    return PatientListState(
      isLoading: isLoading ?? this.isLoading,
      patients: patients ?? this.patients,
      error: error ?? this.error,
    );
  }
}

final patientNotifierProvider =
    StateNotifierProvider<PatientNotifier, PatientListState>((ref) {
  return PatientNotifier(ref.watch(patientServiceProvider));
});

class PatientNotifier extends StateNotifier<PatientListState> {
  final PatientService service;

  PatientNotifier(this.service) : super(const PatientListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getAll();
      state = state.copyWith(isLoading: false, patients: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> search(String keyword) async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list =
          keyword.isEmpty ? await service.getAll() : await service.search(keyword);
      state = state.copyWith(isLoading: false, patients: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> save(PatientRequest request, {int? id}) async {
    try {
      if (id == null) {
        await service.create(request);
      } else {
        await service.update(id, request);
      }
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }

  Future<bool> delete(int id) async {
    try {
      await service.delete(id);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}
