import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/ambulance.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class AmbulanceDetailScreen extends ConsumerStatefulWidget {
  final Ambulance item;
  const AmbulanceDetailScreen({super.key, required this.item});

  @override
  ConsumerState<AmbulanceDetailScreen> createState() =>
      _AmbulanceDetailScreenState();
}

class _AmbulanceDetailScreenState extends ConsumerState<AmbulanceDetailScreen> {
  late Ambulance _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.ambulanceNumber ?? 'Ambulance')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.emergency,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.ambulanceNumber ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 4),
          if (m.vehicleType != null)
            Center(
              child: Text(m.vehicleType!,
                  style: const TextStyle(color: Colors.grey)),
            ),
          const SizedBox(height: 20),
          DetailSection('Vehicle Information', [
            DetailRow('Ambulance Number', m.ambulanceNumber,
                icon: Icons.confirmation_number),
            DetailRow('Vehicle Type', m.vehicleType, icon: Icons.car_repair),
            DetailRow('Vehicle Plate', m.vehiclePlate, icon: Icons.badge),
            DetailRow('Status', m.status, icon: Icons.info),
            DetailRow('Current Location', m.currentLocation,
                icon: Icons.location_on),
            DetailRow('Fuel Status', m.fuelStatus?.toString(),
                icon: Icons.local_gas_station),
            DetailRow('Active', m.isActive?.toString(), icon: Icons.power),
          ], icon: Icons.emergency),
          DetailSection('Crew', [
            DetailRow('Driver Name', m.driverName, icon: Icons.person),
            DetailRow('Driver Phone', m.driverPhone, icon: Icons.phone),
            DetailRow('Paramedic Name', m.paramedicName, icon: Icons.medical_services),
            DetailRow('Paramedic Phone', m.paramedicPhone,
                icon: Icons.phone_android),
          ], icon: Icons.people),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
