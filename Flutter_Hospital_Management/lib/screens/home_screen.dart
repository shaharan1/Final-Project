import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/screens/login_screen.dart';
import 'package:flutter_hospital_management/screens/dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/patient_list_screen.dart';
import 'package:flutter_hospital_management/screens/patient_form_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_list_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_form_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_list_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_form_screen.dart';
import 'package:flutter_hospital_management/screens/admission_list_screen.dart';
import 'package:flutter_hospital_management/screens/bed_list_screen.dart';
import 'package:flutter_hospital_management/screens/medicine_list_screen.dart';
import 'package:flutter_hospital_management/screens/pharmacy_sale_list_screen.dart';
import 'package:flutter_hospital_management/screens/test_order_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_report_list_screen.dart';
import 'package:flutter_hospital_management/screens/lab_dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/doctor_list_screen.dart';
import 'package:flutter_hospital_management/screens/doctor_dashboard_screen.dart';
import 'package:flutter_hospital_management/screens/ambulance_list_screen.dart';
import 'package:flutter_hospital_management/screens/ambulance_trip_list_screen.dart';
import 'package:flutter_hospital_management/screens/diet_plan_list_screen.dart';
import 'package:flutter_hospital_management/screens/surgery_list_screen.dart';
import 'package:flutter_hospital_management/screens/surgery_master_list_screen.dart';
import 'package:flutter_hospital_management/screens/insurance_list_screen.dart';
import 'package:flutter_hospital_management/screens/insurance_claim_list_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/theme.dart';

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(authNotifierProvider).user;
    final role = normalizeRole(user?.role);
    final access = ref.watch(roleAccessProvider);
    final allowed = allowedModulesFor(role, access);

    final activities = _activitiesFor(role);

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
              subtitle: '${role.toUpperCase()}${user?.email != null ? ' • ${user!.email}' : ''}',
            ),
            const SizedBox(height: 20),
            if (activities.isNotEmpty) ...[
              const SectionTitle('Your Activities', icon: Icons.flash_on),
              const SizedBox(height: 12),
              GridView.count(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                crossAxisCount: 2,
                mainAxisSpacing: 12,
                crossAxisSpacing: 12,
                childAspectRatio: 2.4,
                children: activities
                    .map((a) => _ActivityCard(activity: a))
                    .toList(),
              ),
              const SizedBox(height: 24),
            ],
            const SectionTitle('Modules', icon: Icons.apps),
            const SizedBox(height: 12),
            Builder(
              builder: (context) {
                final tiles = <Widget>[];
                for (final key in allModuleOrder) {
                  if (!allowed.contains(key)) continue;
                  final screen = _screenForKey(key);
                  if (screen == null) continue;
                  tiles.add(_ModuleCard(
                    icon: moduleIcons[key] ?? Icons.circle,
                    label: moduleLabels[key] ?? key,
                    onTap: () => Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => screen),
                    ),
                  ));
                }
                if (tiles.isEmpty) {
                  return const Text('No modules available for your role.',
                      style: TextStyle(color: Colors.grey));
                }
                return GridView.count(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  crossAxisCount: 2,
                  mainAxisSpacing: 12,
                  crossAxisSpacing: 12,
                  childAspectRatio: 1.8,
                  children: tiles,
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget? _screenForKey(String key) {
    switch (key) {
      case ModuleKeys.dashboard:
        return const DashboardScreen();
      case ModuleKeys.patients:
        return const PatientListScreen();
      case ModuleKeys.appointments:
        return const AppointmentListScreen();
      case ModuleKeys.admissions:
        return const AdmissionListScreen();
      case ModuleKeys.beds:
        return const BedListScreen();
      case ModuleKeys.billing:
        return const BillingInvoiceListScreen();
      case ModuleKeys.pharmacy:
        return const MedicineListScreen();
      case ModuleKeys.pharmacySales:
        return const PharmacySaleListScreen();
      case ModuleKeys.labTests:
        return const TestOrderListScreen();
      case ModuleKeys.labReports:
        return const LabReportListScreen();
      case ModuleKeys.labDashboard:
        return const LabDashboardScreen();
      case ModuleKeys.doctors:
        return const DoctorListScreen();
      case ModuleKeys.ambulances:
        return const AmbulanceListScreen();
      case ModuleKeys.ambulanceTrips:
        return const AmbulanceTripListScreen();
      case ModuleKeys.dietPlans:
        return const DietPlanListScreen();
      case ModuleKeys.surgeryCatalog:
        return const SurgeryMasterListScreen();
      case ModuleKeys.surgeries:
        return const SurgeryListScreen();
      case ModuleKeys.insurance:
        return const InsuranceListScreen();
      case ModuleKeys.insuranceClaims:
        return const InsuranceClaimListScreen();
      default:
        return null;
    }
  }
}

class _Activity {
  final IconData icon;
  final String label;
  final Widget screen;
  const _Activity(this.icon, this.label, this.screen);
}

List<_Activity> _activitiesFor(String role) {
  switch (role) {
    case 'doctor':
      return [
        const _Activity(Icons.medical_services, 'My Dashboard', DoctorDashboardScreen()),
        const _Activity(Icons.calendar_today, 'My Appointments', AppointmentListScreen()),
        const _Activity(Icons.people, 'Patients', PatientListScreen()),
        const _Activity(Icons.science, 'Lab Tests', TestOrderListScreen()),
      ];
    case 'receptionist':
      return [
        const _Activity(Icons.calendar_today, 'New Appointment', AppointmentFormScreen()),
        const _Activity(Icons.person_add, 'Register Patient', PatientFormScreen()),
        const _Activity(Icons.assignment_ind, 'Admissions', AdmissionListScreen()),
        const _Activity(Icons.bed, 'Beds', BedListScreen()),
      ];
    case 'nurse':
      return [
        const _Activity(Icons.bed, 'Beds', BedListScreen()),
        const _Activity(Icons.assignment_ind, 'Admissions', AdmissionListScreen()),
        const _Activity(Icons.people, 'Patients', PatientListScreen()),
        const _Activity(Icons.restaurant_menu, 'Diet Plans', DietPlanListScreen()),
      ];
    case 'pharmacist':
      return [
        const _Activity(Icons.medication, 'Medicines', MedicineListScreen()),
        const _Activity(Icons.shopping_cart, 'Sales', PharmacySaleListScreen()),
      ];
    case 'labtechnician':
      return [
        const _Activity(Icons.science, 'Lab Tests', TestOrderListScreen()),
        const _Activity(Icons.biotech, 'Reports', LabReportListScreen()),
        const _Activity(Icons.analytics, 'Lab Dashboard', LabDashboardScreen()),
      ];
    case 'billingclerk':
      return [
        const _Activity(Icons.receipt_long, 'New Invoice', BillingInvoiceFormScreen()),
        const _Activity(Icons.receipt_long, 'Invoices', BillingInvoiceListScreen()),
        const _Activity(Icons.health_and_safety, 'Insurance', InsuranceListScreen()),
        const _Activity(Icons.receipt_long, 'Claims', InsuranceClaimListScreen()),
      ];
    case 'dietician':
      return [
        const _Activity(Icons.restaurant_menu, 'Diet Plans', DietPlanListScreen()),
        const _Activity(Icons.people, 'Patients', PatientListScreen()),
      ];
    case 'wardmanager':
      return [
        const _Activity(Icons.bed, 'Beds', BedListScreen()),
        const _Activity(Icons.assignment_ind, 'Admissions', AdmissionListScreen()),
        const _Activity(Icons.people, 'Patients', PatientListScreen()),
      ];
    case 'inventorymanager':
      return [
        const _Activity(Icons.medication, 'Medicines', MedicineListScreen()),
        const _Activity(Icons.shopping_cart, 'Sales', PharmacySaleListScreen()),
      ];
    default:
      return [];
  }
}

class _ActivityCard extends StatelessWidget {
  final _Activity activity;
  const _ActivityCard({required this.activity});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        gradient: AppTheme.primaryGradient,
        borderRadius: BorderRadius.circular(16),
        boxShadow: const [
          BoxShadow(
              color: Color(0x1A000000), blurRadius: 8, offset: Offset(0, 3))
        ],
      ),
      child: InkWell(
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => activity.screen),
        ),
        borderRadius: BorderRadius.circular(16),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
          child: Row(
            children: [
              Icon(activity.icon, color: Colors.white, size: 22),
              const SizedBox(width: 10),
              Expanded(
                child: Text(
                  activity.label,
                  style: const TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.w600,
                      fontSize: 13),
                ),
              ),
            ],
          ),
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
        borderRadius: BorderRadius.circular(16),
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, color: AppTheme.primary, size: 28),
              const SizedBox(height: 8),
              Text(label, style: const TextStyle(fontWeight: FontWeight.w600)),
            ],
          ),
        ),
      ),
    );
  }
}
