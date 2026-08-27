import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ModuleKeys {
  static const dashboard = 'dashboard';
  static const patients = 'patients';
  static const appointments = 'appointments';
  static const admissions = 'admissions';
  static const beds = 'beds';
  static const billing = 'billing';
  static const pharmacy = 'pharmacy';
  static const pharmacySales = 'pharmacy_sales';
  static const labTests = 'lab_tests';
  static const labReports = 'lab_reports';
  static const labDashboard = 'lab_dashboard';
  static const doctors = 'doctors';
  static const ambulances = 'ambulances';
  static const ambulanceTrips = 'ambulance_trips';
  static const dietPlans = 'diet_plans';
  static const surgeryCatalog = 'surgery_catalog';
  static const surgeries = 'surgeries';
  static const insurance = 'insurance';
  static const insuranceClaims = 'insurance_claims';
  static const prescriptions = 'prescriptions';
}

/// Ordered list of all module keys (used by the admin UI).
const List<String> allModuleOrder = [
  ModuleKeys.dashboard,
  ModuleKeys.patients,
  ModuleKeys.appointments,
  ModuleKeys.admissions,
  ModuleKeys.beds,
  ModuleKeys.billing,
  ModuleKeys.pharmacy,
  ModuleKeys.pharmacySales,
  ModuleKeys.labTests,
  ModuleKeys.labReports,
  ModuleKeys.labDashboard,
  ModuleKeys.doctors,
  ModuleKeys.ambulances,
  ModuleKeys.ambulanceTrips,
  ModuleKeys.dietPlans,
  ModuleKeys.surgeryCatalog,
  ModuleKeys.surgeries,
  ModuleKeys.insurance,
  ModuleKeys.insuranceClaims,
  ModuleKeys.prescriptions,
];

const Map<String, String> moduleLabels = {
  ModuleKeys.dashboard: 'Dashboard',
  ModuleKeys.patients: 'Patients',
  ModuleKeys.appointments: 'Appointments',
  ModuleKeys.admissions: 'Admissions',
  ModuleKeys.beds: 'Beds',
  ModuleKeys.billing: 'Billing',
  ModuleKeys.pharmacy: 'Pharmacy',
  ModuleKeys.pharmacySales: 'Pharmacy Sales',
  ModuleKeys.labTests: 'Lab Tests',
  ModuleKeys.labReports: 'Lab Reports',
  ModuleKeys.labDashboard: 'Lab Dashboard',
  ModuleKeys.doctors: 'Doctors',
  ModuleKeys.ambulances: 'Ambulances',
  ModuleKeys.ambulanceTrips: 'Ambulance Trips',
  ModuleKeys.dietPlans: 'Diet Plans',
  ModuleKeys.surgeryCatalog: 'Surgery Catalog',
  ModuleKeys.surgeries: 'Surgeries',
  ModuleKeys.insurance: 'Insurance',
  ModuleKeys.insuranceClaims: 'Insurance Claims',
  ModuleKeys.prescriptions: 'Prescriptions',
};

const Map<String, IconData> moduleIcons = {
  ModuleKeys.dashboard: Icons.dashboard,
  ModuleKeys.patients: Icons.people,
  ModuleKeys.appointments: Icons.calendar_today,
  ModuleKeys.admissions: Icons.assignment_ind,
  ModuleKeys.beds: Icons.bed,
  ModuleKeys.billing: Icons.receipt_long,
  ModuleKeys.pharmacy: Icons.medication,
  ModuleKeys.pharmacySales: Icons.shopping_cart,
  ModuleKeys.labTests: Icons.science,
  ModuleKeys.labReports: Icons.biotech,
  ModuleKeys.labDashboard: Icons.analytics,
  ModuleKeys.doctors: Icons.medical_services,
  ModuleKeys.ambulances: Icons.emergency,
  ModuleKeys.ambulanceTrips: Icons.route,
  ModuleKeys.dietPlans: Icons.restaurant_menu,
  ModuleKeys.surgeryCatalog: Icons.category,
  ModuleKeys.surgeries: Icons.medical_information,
  ModuleKeys.insurance: Icons.health_and_safety,
  ModuleKeys.insuranceClaims: Icons.receipt_long,
  ModuleKeys.prescriptions: Icons.receipt_long,
};

/// Default module set per role (keys are normalized lower-case role names).
final Map<String, Set<String>> _defaults = {
  'admin': {...allModuleOrder},
  'officestaff': {...allModuleOrder},
  'receptionist': {
    ModuleKeys.dashboard,
    ModuleKeys.patients,
    ModuleKeys.appointments,
    ModuleKeys.admissions,
    ModuleKeys.beds,
    ModuleKeys.doctors,
  },
   'doctor': {
    ModuleKeys.dashboard,
    ModuleKeys.patients,
    ModuleKeys.appointments,
    ModuleKeys.doctors,
    ModuleKeys.labTests,
    ModuleKeys.labReports,
    ModuleKeys.dietPlans,
    ModuleKeys.surgeries,
    ModuleKeys.surgeryCatalog,
    ModuleKeys.prescriptions,
  },
  'nurse': {
    ModuleKeys.dashboard,
    ModuleKeys.patients,
    ModuleKeys.appointments,
    ModuleKeys.beds,
    ModuleKeys.admissions,
    ModuleKeys.dietPlans,
  },
  'pharmacist': {
    ModuleKeys.dashboard,
    ModuleKeys.pharmacy,
    ModuleKeys.pharmacySales,
  },
  'labtechnician': {
    ModuleKeys.dashboard,
    ModuleKeys.labTests,
    ModuleKeys.labReports,
    ModuleKeys.labDashboard,
  },
  'billingclerk': {
    ModuleKeys.dashboard,
    ModuleKeys.billing,
    ModuleKeys.pharmacySales,
    ModuleKeys.insurance,
    ModuleKeys.insuranceClaims,
  },
  'inventorymanager': {
    ModuleKeys.dashboard,
    ModuleKeys.pharmacy,
  },
  'wardmanager': {
    ModuleKeys.dashboard,
    ModuleKeys.beds,
    ModuleKeys.admissions,
    ModuleKeys.patients,
  },
  'dietician': {
    ModuleKeys.dashboard,
    ModuleKeys.dietPlans,
    ModuleKeys.patients,
  },
};

/// Public copy of the default per-role module sets (for the admin UI).
final Map<String, Set<String>> defaultRoleAccess = _defaults;

/// Normalize a backend role string to a lower-case key that matches `_defaults`
/// (handles "Admin", "ADMIN", "ROLE_ADMIN", etc.).
String normalizeRole(String? role) {
  if (role == null) return '';
  var r = role.trim();
  if (r.toUpperCase().startsWith('ROLE_')) r = r.substring(5);
  return r.toLowerCase();
}

Set<String> allowedModulesFor(
        String? role, Map<String, Set<String>> state) =>
    state[normalizeRole(role)] ??
    _defaults[normalizeRole(role)] ??
    <String>{};

bool canAccess(String? role, String key, Map<String, Set<String>> state) =>
    allowedModulesFor(role, state).contains(key);

const String _prefsKey = 'role_access_overrides';
const FlutterSecureStorage _store = FlutterSecureStorage();

final roleAccessProvider =
    StateNotifierProvider<RoleAccessNotifier, Map<String, Set<String>>>(
  (ref) => RoleAccessNotifier()..load(),
);

class RoleAccessNotifier extends StateNotifier<Map<String, Set<String>>> {
  RoleAccessNotifier()
      : super(_defaults.map((k, v) => MapEntry(k, {...v})));

  Future<void> load() async {
    try {
      final raw = await _store.read(key: _prefsKey);
      if (raw == null || raw.isEmpty) return;
      final decoded = jsonDecode(raw) as Map<String, dynamic>;
      final map = <String, Set<String>>{};
      decoded.forEach((k, v) {
        map[k.toLowerCase()] = {
          ...(v as List).map((e) => e as String)
        };
      });
      state = map;
    } catch (_) {
      // Keep defaults if persistence is unavailable.
    }
  }

  void toggle(String role, String key) {
    final r = normalizeRole(role);
    final current = {
      ...(state[r] ?? _defaults[r] ?? <String>{})
    };
    if (current.contains(key)) {
      current.remove(key);
    } else {
      current.add(key);
    }
    state = {...state, r: current};
    _persist();
  }

  void setForRole(String role, Set<String> keys) {
    final r = normalizeRole(role);
    state = {...state, r: {...keys}};
    _persist();
  }

  void resetAll() {
    state = _defaults.map((k, v) => MapEntry(k, {...v}));
    _persist();
  }

  Future<void> _persist() async {
    try {
      final enc = state.map((k, v) => MapEntry(k, v.toList()));
      await _store.write(key: _prefsKey, value: jsonEncode(enc));
    } catch (_) {
      // Ignore persistence failures.
    }
  }
}
