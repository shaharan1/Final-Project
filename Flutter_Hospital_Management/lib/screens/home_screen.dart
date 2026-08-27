import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/screens/login_screen.dart';
import 'package:flutter_hospital_management/screens/patient_list_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_list_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_list_screen.dart';
import 'package:flutter_hospital_management/screens/bed_list_screen.dart';
import 'package:flutter_hospital_management/screens/admission_list_screen.dart';
import 'package:flutter_hospital_management/screens/dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/medicine_list_screen.dart';
import 'package:flutter_hospital_management/screens/pharmacy_sale_list_screen.dart';
import 'package:flutter_hospital_management/screens/test_order_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_report_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_dashboard_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final auth = ref.watch(authNotifierProvider);
    final user = auth.user;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Elite Care Hospital'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            tooltip: 'Logout',
            onPressed: () async {
              await ref.read(authNotifierProvider.notifier).logout();
              if (context.mounted) {
                Navigator.of(context).pushReplacement(
                  MaterialPageRoute(builder: (_) => const LoginScreen()),
                );
              }
            },
          ),
        ],
      ),
      drawer: const AppDrawer(),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            GradientHeader(
              'Welcome, ${user?.name ?? 'User'}',
              subtitle: '${user?.role ?? ''}${user?.email != null ? ' • ${user!.email}' : ''}',
            ),
            const SizedBox(height: 20),
            GridView.count(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              crossAxisCount: 2,
              mainAxisSpacing: 12,
              crossAxisSpacing: 12,
              childAspectRatio: 1.6,
              children: [
                _ModuleCard(
                  icon: Icons.people,
                  label: 'Patients',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const PatientListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.calendar_today,
                  label: 'Appointments',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const AppointmentListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.receipt_long,
                  label: 'Billing',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const BillingInvoiceListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.bed,
                  label: 'Beds',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const BedListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.assignment_ind,
                  label: 'Admissions',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const AdmissionListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.dashboard,
                  label: 'Dashboard',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DashboardScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.medication,
                  label: 'Pharmacy',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (_) => const MedicineListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.receipt_long,
                  label: 'Pharmacy Sales',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (_) => const PharmacySaleListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.science,
                  label: 'Lab Tests',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (_) => const TestOrderListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.biotech,
                  label: 'Lab Reports',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (_) => const LabReportListScreen()),
                  ),
                ),
                _ModuleCard(
                  icon: Icons.analytics,
                  label: 'Lab Dashboard',
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (_) => const LabDashboardScreen()),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class _ModuleCard extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;

  const _ModuleCard({
    required this.icon,
    required this.label,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 36, color: Colors.teal),
            const SizedBox(height: 8),
            Text(label, style: const TextStyle(fontSize: 16)),
          ],
        ),
      ),
    );
  }
}
