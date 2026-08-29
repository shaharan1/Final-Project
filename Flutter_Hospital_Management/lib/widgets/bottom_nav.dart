import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_hospital_management/screens/home_screen.dart';
import 'package:flutter_hospital_management/screens/doctor_dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_list_screen.dart';
import 'package:flutter_hospital_management/screens/prescription_list_screen.dart';
import 'package:flutter_hospital_management/screens/patient_list_screen.dart';
import 'package:flutter_hospital_management/screens/bed_list_screen.dart';
import 'package:flutter_hospital_management/screens/admission_list_screen.dart';
import 'package:flutter_hospital_management/screens/medicine_list_screen.dart';
import 'package:flutter_hospital_management/screens/pharmacy_sale_list_screen.dart';
import 'package:flutter_hospital_management/screens/test_order_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_report_list_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_list_screen.dart';
import 'package:flutter_hospital_management/screens/dashboard_screen.dart';

class _NavDef {
  final String key;
  final String label;
  final IconData icon;
  final Widget screen;
  const _NavDef(this.key, this.label, this.icon, this.screen);
}

List<_NavDef> _itemsFor(String role) {
  final r = normalizeRole(role);
  switch (r) {
    case 'doctor':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('doctor', 'My Desk', Icons.medical_services,
            DoctorDashboardScreen()),
        _NavDef('appointments', 'Appts', Icons.calendar_today,
            AppointmentListScreen()),
        _NavDef('prescriptions', 'Rx', Icons.receipt_long,
            PrescriptionListScreen()),
        _NavDef('patients', 'Patients', Icons.people, PatientListScreen()),
      ];
    case 'nurse':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('patients', 'Patients', Icons.people, PatientListScreen()),
        _NavDef('beds', 'Beds', Icons.bed, BedListScreen()),
        _NavDef('admissions', 'Admits', Icons.assignment_ind,
            AdmissionListScreen()),
      ];
    case 'receptionist':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('appointments', 'Appts', Icons.calendar_today,
            AppointmentListScreen()),
        _NavDef('patients', 'Patients', Icons.people, PatientListScreen()),
        _NavDef('admissions', 'Admits', Icons.assignment_ind,
            AdmissionListScreen()),
      ];
    case 'pharmacist':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('pharmacy', 'Meds', Icons.medication, MedicineListScreen()),
        _NavDef('pharmacySales', 'Sales', Icons.shopping_cart,
            PharmacySaleListScreen()),
      ];
    case 'labtechnician':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('testOrders', 'Orders', Icons.science, TestOrderListScreen()),
        _NavDef('labReports', 'Reports', Icons.analytics,
            LabReportListScreen()),
      ];
    case 'billingclerk':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('billing', 'Invoices', Icons.receipt_long,
            BillingInvoiceListScreen()),
        _NavDef('pharmacySales', 'Sales', Icons.shopping_cart,
            PharmacySaleListScreen()),
      ];
    case 'wardmanager':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('beds', 'Beds', Icons.bed, BedListScreen()),
        _NavDef('admissions', 'Admits', Icons.assignment_ind,
            AdmissionListScreen()),
      ];
    case 'inventorymanager':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('pharmacy', 'Meds', Icons.medication, MedicineListScreen()),
      ];
    case 'officestaff':
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('dashboard', 'Stats', Icons.dashboard, DashboardScreen()),
        _NavDef('patients', 'Patients', Icons.people, PatientListScreen()),
      ];
    default:
      return const [
        _NavDef('home', 'Home', Icons.home, HomeScreen()),
        _NavDef('dashboard', 'Stats', Icons.dashboard, DashboardScreen()),
        _NavDef('patients', 'Patients', Icons.people, PatientListScreen()),
        _NavDef('billing', 'Invoices', Icons.receipt_long,
            BillingInvoiceListScreen()),
        _NavDef('beds', 'Beds', Icons.bed, BedListScreen()),
      ];
  }
}

class RoleBottomNav extends ConsumerWidget {
  final String currentKey;
  const RoleBottomNav({super.key, required this.currentKey});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    if (MediaQuery.of(context).size.width >= 700) {
      return const SizedBox.shrink();
    }
    final role = ref.watch(authNotifierProvider).user?.role ?? '';
    final items = _itemsFor(role);
    final idx = items.indexWhere((e) => e.key == currentKey);
    return BottomNavigationBar(
      currentIndex: idx < 0 ? 0 : idx,
      type: BottomNavigationBarType.fixed,
      selectedItemColor: AppTheme.primary,
      unselectedItemColor: Colors.grey.shade600,
      onTap: (i) {
        final item = items[i];
        if (item.key == currentKey) return;
        Navigator.of(context).pushReplacement(
          MaterialPageRoute(builder: (_) => item.screen),
        );
      },
      items: items
          .map((e) =>
              BottomNavigationBarItem(icon: Icon(e.icon), label: e.label))
          .toList(),
    );
  }
}
