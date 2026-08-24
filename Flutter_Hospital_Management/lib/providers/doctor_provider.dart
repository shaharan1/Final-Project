import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/doctor.dart';
import 'package:flutter_hospital_management/services/doctor_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final doctorServiceProvider = Provider<DoctorService>((ref) {
  return DoctorService(ref.watch(dioProvider));
});

class DoctorListState {
  final bool isLoading;
  final List<Doctor> doctors;
  final String? error;

  const DoctorListState({
    this.isLoading = false,
    this.doctors = const [],
    this.error,
  });

  DoctorListState copyWith({
    bool? isLoading,
    List<Doctor>? doctors,
    String? error,
  }) {
    return DoctorListState(
      isLoading: isLoading ?? this.isLoading,
      doctors: doctors ?? this.doctors,
      error: error ?? this.error,
    );
  }
}

final doctorNotifierProvider =
    StateNotifierProvider<DoctorNotifier, DoctorListState>((ref) {
  return DoctorNotifier(ref.watch(doctorServiceProvider));
});

class DoctorNotifier extends StateNotifier<DoctorListState> {
  final DoctorService service;

  DoctorNotifier(this.service) : super(const DoctorListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getAll();
      state = state.copyWith(isLoading: false, doctors: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
