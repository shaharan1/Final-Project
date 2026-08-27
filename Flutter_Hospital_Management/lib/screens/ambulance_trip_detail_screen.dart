import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/ambulance.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class AmbulanceTripDetailScreen extends ConsumerStatefulWidget {
  final AmbulanceTrip item;
  const AmbulanceTripDetailScreen({super.key, required this.item});

  @override
  ConsumerState<AmbulanceTripDetailScreen> createState() =>
      _AmbulanceTripDetailScreenState();
}

class _AmbulanceTripDetailScreenState
    extends ConsumerState<AmbulanceTripDetailScreen> {
  late AmbulanceTrip _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.ambulanceNumber ?? 'Trip')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.route,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.ambulanceNumber ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 20),
          DetailSection('Trip Information', [
            DetailRow('Ambulance Number', m.ambulanceNumber,
                icon: Icons.confirmation_number),
            DetailRow('Trip Type', m.tripType, icon: Icons.category),
            DetailRow('Status', m.status, icon: Icons.info),
            DetailRow('Distance Travelled',
                m.distanceTravelled != null ? '${m.distanceTravelled} km' : null,
                icon: Icons.straighten),
            DetailRow('Emergency Number', m.emergencyNumber,
                icon: Icons.emergency),
          ], icon: Icons.route),
          DetailSection('Route', [
            DetailRow('Pickup Location', m.pickupLocation,
                icon: Icons.location_on),
            DetailRow('Dropoff Location', m.dropoffLocation,
                icon: Icons.flag),
          ], icon: Icons.map),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
