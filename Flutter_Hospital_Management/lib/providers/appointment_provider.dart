import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/services/appointment_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final appointmentServiceProvider = Provider<AppointmentService>((ref) {
  return AppointmentService(ref.watch(dioProvider));
});

class AppointmentListState {
  final bool isLoading;
  final List<Appointment> appointments;
  final String? error;

  const AppointmentListState({
    this.isLoading = false,
    this.appointments = const [],
    this.error,
  });

  AppointmentListState copyWith({
    bool? isLoading,
    List<Appointment>? appointments,
    String? error,
  }) {
    return AppointmentListState(
      isLoading: isLoading ?? this.isLoading,
      appointments: appointments ?? this.appointments,
      error: error ?? this.error,
    );
  }
}

final appointmentNotifierProvider =
    StateNotifierProvider<AppointmentNotifier, AppointmentListState>((ref) {
  return AppointmentNotifier(ref.watch(appointmentServiceProvider));
});

class AppointmentNotifier extends StateNotifier<AppointmentListState> {
  final AppointmentService service;

  AppointmentNotifier(this.service) : super(const AppointmentListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getAll();
      state = state.copyWith(isLoading: false, appointments: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> search(String query) async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list =
          query.isEmpty ? await service.getAll() : await service.search(query);
      state = state.copyWith(isLoading: false, appointments: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<bool> book(AppointmentRequest request) async {
    try {
      await service.book(request);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }

  Future<bool> cancel(int id) async {
    try {
      await service.cancel(id);
      await load();
      return true;
    } catch (e) {
      state = state.copyWith(error: e.toString());
      return false;
    }
  }
}
