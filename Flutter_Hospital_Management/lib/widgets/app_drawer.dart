import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
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
import 'package:flutter_hospital_management/screens/login_screen.dart';
import 'package:flutter_hospital_management/theme.dart';

class AppDrawer extends ConsumerWidget {
  const AppDrawer({super.key});

  void _go(BuildContext context, Widget screen) {
    Navigator.pop(context);
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (_) => screen),
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(authNotifierProvider).user;
    final initials = (user?.name ?? '?')
        .trim()
        .split(RegExp(r'\s+'))
        .map((e) => e.isNotEmpty ? e[0] : '')
        .take(2)
        .join()
        .toUpperCase();

    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          UserAccountsDrawerHeader(
            accountName: Text(user?.name ?? 'User',
                style: const TextStyle(fontWeight: FontWeight.bold)),
            accountEmail:
                Text('${user?.role ?? ''}${user?.email != null ? ' • ${user!.email}' : ''}'),
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
          _group('OVERVIEW'),
          _tile(context, Icons.dashboard, 'Dashboard',
              () => _go(context, const DashboardScreen())),
          _tile(context, Icons.home, 'Home',
              () => _go(context, const HomeScreen())),
          _group('CLINICAL'),
          _tile(context, Icons.people, 'Patients',
              () => _go(context, const PatientListScreen())),
          _tile(context, Icons.calendar_today, 'Appointments',
              () => _go(context, const AppointmentListScreen())),
          _tile(context, Icons.assignment_ind, 'Admissions',
              () => _go(context, const AdmissionListScreen())),
          _tile(context, Icons.bed, 'Beds',
              () => _go(context, const BedListScreen())),
          _group('BILLING & PHARMACY'),
          _tile(context, Icons.receipt_long, 'Billing',
              () => _go(context, const BillingInvoiceListScreen())),
          _tile(context, Icons.medication, 'Pharmacy',
              () => _go(context, const MedicineListScreen())),
          _tile(context, Icons.shopping_cart, 'Pharmacy Sales',
              () => _go(context, const PharmacySaleListScreen())),
          _group('LABORATORY'),
          _tile(context, Icons.science, 'Lab Tests',
              () => _go(context, const TestOrderListScreen())),
          _tile(context, Icons.biotech, 'Lab Reports',
              () => _go(context, const LabReportListScreen())),
          _tile(context, Icons.analytics, 'Lab Dashboard',
              () => _go(context, const LabDashboardScreen())),
          const Divider(),
          _tile(context, Icons.logout, 'Logout', () async {
            Navigator.pop(context);
            await ref.read(authNotifierProvider.notifier).logout();
          }, color: AppTheme.danger),
        ],
      ),
    );
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
      VoidCallback onTap, {Color? color}) {
    return ListTile(
      leading: Icon(icon, color: color ?? AppTheme.primary),
      title: Text(label, style: TextStyle(color: color)),
      onTap: onTap,
      dense: true,
      horizontalTitleGap: 8,
    );
  }
}
