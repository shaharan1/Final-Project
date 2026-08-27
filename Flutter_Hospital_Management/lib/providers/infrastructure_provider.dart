import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/bed.dart';
import 'package:flutter_hospital_management/services/infrastructure_service.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';

final infrastructureServiceProvider = Provider<InfrastructureService>((ref) {
  return InfrastructureService(ref.watch(dioProvider));
});

class BedListState {
  final bool isLoading;
  final List<Bed> beds;
  final String? error;

  const BedListState({
    this.isLoading = false,
    this.beds = const [],
    this.error,
  });

  BedListState copyWith({
    bool? isLoading,
    List<Bed>? beds,
    String? error,
  }) {
    return BedListState(
      isLoading: isLoading ?? this.isLoading,
      beds: beds ?? this.beds,
      error: error ?? this.error,
    );
  }
}

final bedNotifierProvider =
    StateNotifierProvider<BedNotifier, BedListState>((ref) {
  return BedNotifier(ref.watch(infrastructureServiceProvider));
});

class BedNotifier extends StateNotifier<BedListState> {
  final InfrastructureService service;

  BedNotifier(this.service) : super(const BedListState());

  Future<void> load() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final list = await service.getBeds();
      state = state.copyWith(isLoading: false, beds: list);
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
