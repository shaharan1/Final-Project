import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/ambulance.dart';
import 'package:flutter_hospital_management/services/ambulance_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final ambulanceServiceProvider = Provider<AmbulanceService>((ref) {
  return AmbulanceService(ref.watch(dioProvider));
});

class AmbulanceListState {
  final bool isLoading;
  final List<Ambulance> ambulances;
  final List<AmbulanceTrip> trips;
  final String? error;

  const AmbulanceListState(
      {this.isLoading = false,
      this.ambulances = const [],
      this.trips = const [],
      this.error});

  AmbulanceListState copyWith({
    bool? isLoading,
    List<Ambulance>? ambulances,
    List<AmbulanceTrip>? trips,
    String? error,
  }) =>
      AmbulanceListState(
        isLoading: isLoading ?? this.isLoading,
        ambulances: ambulances ?? this.ambulances,
        trips: trips ?? this.trips,
        error: error ?? this.error,
      );
}

final ambulanceNotifierProvider =
    StateNotifierProvider<AmbulanceNotifier, AmbulanceListState>((ref) {
  return AmbulanceNotifier(ref.watch(ambulanceServiceProvider));
});

class AmbulanceNotifier extends StateNotifier<AmbulanceListState> {
  final AmbulanceService service;

  AmbulanceNotifier(this.service) : super(const AmbulanceListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final amb = await service.getAmbulances();
      final trips = await service.getTrips();
      state = state.copyWith(
          isLoading: false, ambulances: amb, trips: trips);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
