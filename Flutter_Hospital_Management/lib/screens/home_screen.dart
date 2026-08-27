import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/screens/login_screen.dart';
import 'package:flutter_hospital_management/screens/patient_list_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_list_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_list_screen.dart';
import 'package:flutter_hospital_management/screens/dashboard_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/theme.dart';

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
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            GradientHeader(
              'Welcome, ${user?.name ?? 'User'}',
              subtitle:
                  '${user?.role ?? ''}${user?.email != null ? ' • ${user!.email}' : ''}',
            ),
            const SizedBox(height: 20),
            const SectionTitle('Quick Access', icon: Icons.flash_on),
            const SizedBox(height: 12),
            Builder(
              builder: (context) {
                final allowed = allowedModulesFor(user?.role);
                final quick = <Widget>[];
                if (allowed.contains(ModuleKeys.dashboard)) {
                  quick.add(_quickCard(context, Icons.dashboard, 'Dashboard',
                      const DashboardScreen()));
                }
                if (allowed.contains(ModuleKeys.patients)) {
                  quick.add(_quickCard(context, Icons.people, 'Patients',
                      const PatientListScreen()));
                }
                if (allowed.contains(ModuleKeys.appointments)) {
                  quick.add(_quickCard(context, Icons.calendar_today,
                      'Appointments', const AppointmentListScreen()));
                }
                if (allowed.contains(ModuleKeys.billing)) {
                  quick.add(_quickCard(context, Icons.receipt_long, 'Billing',
                      const BillingInvoiceListScreen()));
                }
                return GridView.count(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  crossAxisCount: 2,
                  mainAxisSpacing: 12,
                  crossAxisSpacing: 12,
                  childAspectRatio: 1.8,
                  children: quick,
                );
              },
            ),
            const SizedBox(height: 16),
            AppCard(
              child: Row(
                children: [
                  const Icon(Icons.menu, color: AppTheme.primary),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Text(
                      'Open the menu (top-left) for all modules: Beds, Admissions, Pharmacy, Lab and more.',
                      style: const TextStyle(color: Colors.grey),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _quickCard(BuildContext context, IconData icon, String label,
          Widget screen) =>
      _ModuleCard(
        icon: icon,
        label: label,
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => screen),
        ),
      );
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
        borderRadius: BorderRadius.circular(16),
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, color: AppTheme.primary, size: 28),
              const SizedBox(height: 8),
              Text(label,
                  style: const TextStyle(fontWeight: FontWeight.w600)),
            ],
          ),
        ),
      ),
    );
  }
}
