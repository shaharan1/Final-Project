import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/screens/home_screen.dart';
import 'package:flutter_hospital_management/screens/dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/patient_list_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_list_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_list_screen.dart';
import 'package:flutter_hospital_management/screens/bed_list_screen.dart';
import 'package:flutter_hospital_management/screens/admission_list_screen.dart';
import 'package:flutter_hospital_management/screens/medicine_list_screen.dart';
import 'package:flutter_hospital_management/screens/pharmacy_sale_list_screen.dart';
import 'package:flutter_hospital_management/screens/test_order_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_report_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/doctor_list_screen.dart';
import 'package:flutter_hospital_management/screens/ambulance_list_screen.dart';
import 'package:flutter_hospital_management/screens/ambulance_trip_list_screen.dart';
import 'package:flutter_hospital_management/screens/diet_plan_list_screen.dart';
import 'package:flutter_hospital_management/screens/surgery_list_screen.dart';
import 'package:flutter_hospital_management/screens/surgery_master_list_screen.dart';
import 'package:flutter_hospital_management/screens/insurance_list_screen.dart';
import 'package:flutter_hospital_management/screens/insurance_claim_list_screen.dart';
import 'package:flutter_hospital_management/screens/role_access_screen.dart';
import 'package:flutter_hospital_management/screens/doctor_dashboard_screen.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/providers/infrastructure_provider.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';
import 'package:flutter_hospital_management/providers/lab_provider.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/providers/insurance_provider.dart';
import 'package:flutter_hospital_management/theme.dart';

class _Item {
  final String key;
  final IconData icon;
  final String label;
  final Widget screen;
  const _Item(this.key, this.icon, this.label, this.screen);
}

class _Section {
  final String title;
  final List<_Item> items;
  const _Section(this.title, this.items);
}

class AppDrawer extends ConsumerWidget {
  const AppDrawer({super.key});

  void _go(BuildContext context, Widget screen) {
    Navigator.pop(context);
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => screen),
    );
  }

  String _today() {
    final d = DateTime.now();
    final m = '${d.month}'.padLeft(2, '0');
    final day = '${d.day}'.padLeft(2, '0');
    return '${d.year}-$m-$day';
  }

  int? _badge(String key, WidgetRef ref) {
    switch (key) {
      case ModuleKeys.patients:
        final c = ref.watch(patientNotifierProvider).patients.length;
        return c > 0 ? c : null;
      case ModuleKeys.appointments: {
        final t = _today();
        final c = ref
            .watch(appointmentNotifierProvider)
            .appointments
            .where((a) => (a.appointmentDate ?? '').startsWith(t))
            .length;
        return c > 0 ? c : null;
      }
      case ModuleKeys.admissions:
        final c = ref.watch(admissionNotifierProvider).admissions.length;
        return c > 0 ? c : null;
      case ModuleKeys.beds: {
        final c = ref
            .watch(bedNotifierProvider)
            .beds
            .where((b) => (b.status ?? '').toLowerCase().contains('occup'))
            .length;
        return c > 0 ? c : null;
      }
      case ModuleKeys.billing:
        final c = ref.watch(billingNotifierProvider).invoices.length;
        return c > 0 ? c : null;
      case ModuleKeys.pharmacy:
        final c = ref.watch(medicineNotifierProvider).medicines.length;
        return c > 0 ? c : null;
      case ModuleKeys.pharmacySales:
        final c = ref.watch(saleNotifierProvider).sales.length;
        return c > 0 ? c : null;
      case ModuleKeys.labTests:
        final c = ref.watch(testOrderNotifierProvider).orders.length;
        return c > 0 ? c : null;
      case ModuleKeys.labReports:
        final c = ref.watch(labReportNotifierProvider).reports.length;
        return c > 0 ? c : null;
      case ModuleKeys.doctors:
        final c = ref.watch(doctorNotifierProvider).doctors.length;
        return c > 0 ? c : null;
      case ModuleKeys.dietPlans:
        final c = ref.watch(dietPlanNotifierProvider).plans.length;
        return c > 0 ? c : null;
      case ModuleKeys.insuranceClaims:
        final c = ref.watch(insuranceNotifierProvider).claims.length;
        return c > 0 ? c : null;
      case ModuleKeys.prescriptions:
        final c =
            ref.watch(prescriptionNotifierProvider).prescriptions.length;
        return c > 0 ? c : null;
      default:
        return null;
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(authNotifierProvider).user;
    final access = ref.watch(roleAccessProvider);
    final allowed = allowedModulesFor(user?.role, access);
    final isAdmin = normalizeRole(user?.role) == 'admin';

    const sections = [
      _Section('OVERVIEW', [
        _Item(ModuleKeys.dashboard, Icons.dashboard, 'Dashboard',
            DashboardScreen()),
      ]),
      _Section('CLINICAL', [
        _Item(ModuleKeys.patients, Icons.people, 'Patients', PatientListScreen()),
        _Item(ModuleKeys.appointments, Icons.calendar_today, 'Appointments',
            AppointmentListScreen()),
        _Item(ModuleKeys.admissions, Icons.assignment_ind, 'Admissions',
            AdmissionListScreen()),
        _Item(ModuleKeys.beds, Icons.bed, 'Beds', BedListScreen()),
      ]),
      _Section('BILLING & PHARMACY', [
        _Item(ModuleKeys.billing, Icons.receipt_long, 'Billing',
            BillingInvoiceListScreen()),
        _Item(ModuleKeys.pharmacy, Icons.medication, 'Pharmacy',
            MedicineListScreen()),
        _Item(ModuleKeys.pharmacySales, Icons.shopping_cart, 'Pharmacy Sales',
            PharmacySaleListScreen()),
      ]),
      _Section('LABORATORY', [
        _Item(ModuleKeys.labTests, Icons.science, 'Lab Tests',
            TestOrderListScreen()),
        _Item(ModuleKeys.labReports, Icons.biotech, 'Lab Reports',
            LabReportListScreen()),
        _Item(ModuleKeys.labDashboard, Icons.analytics, 'Lab Dashboard',
            LabDashboardScreen()),
      ]),
      _Section('STAFF & EMERGENCY', [
        _Item(ModuleKeys.doctors, Icons.medical_services, 'Doctors',
            DoctorListScreen()),
        _Item(ModuleKeys.ambulances, Icons.emergency, 'Ambulances',
            AmbulanceListScreen()),
        _Item(ModuleKeys.ambulanceTrips, Icons.route, 'Ambulance Trips',
            AmbulanceTripListScreen()),
      ]),
      _Section('DIET & SURGERY', [
        _Item(ModuleKeys.dietPlans, Icons.restaurant_menu, 'Diet Plans',
            DietPlanListScreen()),
        _Item(ModuleKeys.surgeryCatalog, Icons.category, 'Surgery Catalog',
            SurgeryMasterListScreen()),
        _Item(ModuleKeys.surgeries, Icons.medical_information, 'Surgeries',
            SurgeryListScreen()),
      ]),
      _Section('INSURANCE', [
        _Item(ModuleKeys.insurance, Icons.health_and_safety, 'Insurance',
            InsuranceListScreen()),
        _Item(ModuleKeys.insuranceClaims, Icons.receipt_long,
            'Insurance Claims', InsuranceClaimListScreen()),
      ]),
    ];

    final initials = (user?.name ?? '?')
        .trim()
        .split(RegExp(r'\s+'))
        .map((e) => e.isNotEmpty ? e[0] : '')
        .take(2)
        .join()
        .toUpperCase();

    final children = <Widget>[
      UserAccountsDrawerHeader(
        accountName: Text(user?.name ?? 'User',
            style: const TextStyle(fontWeight: FontWeight.bold)),
        accountEmail: Text(
            '${user?.role ?? ''}${user?.email != null ? ' • ${user!.email}' : ''}'),
        currentAccountPicture: CircleAvatar(
          backgroundColor: Colors.white,
          child: Text(initials,
              style: const TextStyle(
                  color: AppTheme.primary,
                  fontWeight: FontWeight.bold,
                  fontSize: 20)),
        ),
        decoration: const BoxDecoration(gradient: AppTheme.primaryGradient),
      ),
      _tile(context, Icons.home, 'Home', () => _go(context, const HomeScreen())),
      if (isAdmin || normalizeRole(user?.role) == 'doctor')
        _tile(
          context,
          Icons.medical_services,
          'Doctor Dashboard',
          () => _go(context, const DoctorDashboardScreen()),
        ),
      if (isAdmin)
        _tile(
          context,
          Icons.admin_panel_settings,
          'Role Permissions',
          () => _go(context, const RoleAccessScreen()),
        ),
    ];

    for (final sec in sections) {
      final items = sec.items.where((it) => allowed.contains(it.key)).toList();
      if (items.isEmpty) continue;
      children.add(_group(sec.title));
      for (final it in items) {
        final badge = _badge(it.key, ref);
        children.add(_tile(context, it.icon, it.label,
            () => _go(context, it.screen), badge: badge));
      }
    }

    children.add(const Divider());
    children.add(_tile(context, Icons.logout, 'Logout', () async {
      Navigator.pop(context);
      await ref.read(authNotifierProvider.notifier).logout();
    }, color: AppTheme.danger));

    return Drawer(child: ListView(padding: EdgeInsets.zero, children: children));
  }

  Widget _group(String label) => Padding(
        padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
        child: Text(label,
            style: const TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.w700,
                letterSpacing: 1,
                color: Colors.grey)),
      );

  Widget _tile(BuildContext context, IconData icon, String label,
      VoidCallback onTap, {Color? color, int? badge}) {
    return ListTile(
      leading: Icon(icon, color: color ?? AppTheme.primary),
      title: Text(label, style: TextStyle(color: color)),
      trailing: badge != null
          ? Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
              decoration: BoxDecoration(
                color: AppTheme.accent,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Text('$badge',
                  style: const TextStyle(color: Colors.white, fontSize: 12)),
            )
          : null,
      onTap: onTap,
      dense: true,
      horizontalTitleGap: 8,
    );
  }
}
