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

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final auth = ref.watch(authNotifierProvider);
    final user = auth.user;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Elite Care Hospital'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
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
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Welcome, ${user?.name ?? 'User'}',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: 8),
            Text(
              'Role: ${user?.role ?? 'N/A'}',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 8),
            Text('Email: ${user?.email ?? 'N/A'}'),
            const SizedBox(height: 24),
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
