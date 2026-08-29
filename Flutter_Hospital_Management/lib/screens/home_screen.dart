import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/providers/infrastructure_provider.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';
import 'package:flutter_hospital_management/providers/lab_provider.dart';
import 'package:flutter_hospital_management/providers/insurance_provider.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/providers/dashboard_provider.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
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
import 'package:flutter_hospital_management/screens/prescription_form_screen.dart';
import 'package:flutter_hospital_management/screens/prescription_list_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/theme.dart';

class HomeScreen extends ConsumerStatefulWidget {
  const HomeScreen({super.key});

  @override
  ConsumerState<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends ConsumerState<HomeScreen> {
  bool _loaded = false;
  Timer? _timer;

  @override
  void initState() {
    super.initState();
    _timer = Timer.periodic(
        const Duration(seconds: 45), (_) => _loadRoleData());
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (!_loaded) {
      _loaded = true;
      _loadRoleData();
    }
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  void _loadRoleData() {
    final user = ref.read(authNotifierProvider).user;
    final role = normalizeRole(user?.role);
    final userId = user?.userId;
    switch (role) {
      case 'doctor':
        if (userId != null) {
          ref
              .read(doctorNotifierProvider.notifier)
              .loadMyProfile(userId)
              .then((_) {
            final id = ref.read(doctorNotifierProvider).myDoctor?.id;
            if (id != null) {
              ref.read(prescriptionNotifierProvider.notifier).loadByDoctor(id);
            }
          });
        }
        ref.read(appointmentNotifierProvider.notifier).load();
        ref.read(patientNotifierProvider.notifier).load();
        break;
      case 'receptionist':
        ref.read(appointmentNotifierProvider.notifier).load();
        ref.read(patientNotifierProvider.notifier).load();
        ref.read(bedNotifierProvider.notifier).load();
        ref.read(admissionNotifierProvider.notifier).load();
        break;
      case 'nurse':
        ref.read(bedNotifierProvider.notifier).load();
        ref.read(admissionNotifierProvider.notifier).load();
        ref.read(patientNotifierProvider.notifier).load();
        break;
      case 'pharmacist':
      case 'inventorymanager':
        ref.read(medicineNotifierProvider.notifier).load();
        ref.read(saleNotifierProvider.notifier).load();
        break;
      case 'labtechnician':
        ref.read(testOrderNotifierProvider.notifier).load();
        ref.read(labReportNotifierProvider.notifier).load();
        break;
      case 'billingclerk':
        ref.read(billingNotifierProvider.notifier).load();
        ref.read(dashboardNotifierProvider.notifier).load();
        ref.read(insuranceNotifierProvider.notifier).load();
        break;
      case 'dietician':
        ref.read(dietPlanNotifierProvider.notifier).load();
        ref.read(patientNotifierProvider.notifier).load();
        break;
      case 'wardmanager':
        ref.read(bedNotifierProvider.notifier).load();
        ref.read(admissionNotifierProvider.notifier).load();
        ref.read(patientNotifierProvider.notifier).load();
        break;
      default:
        ref.read(dashboardNotifierProvider.notifier).load();
        ref.read(patientNotifierProvider.notifier).load();
        ref.read(appointmentNotifierProvider.notifier).load();
        ref.read(bedNotifierProvider.notifier).load();
        ref.read(testOrderNotifierProvider.notifier).load();
    }
  }

  @override
  Widget build(BuildContext context) {
    final user = ref.watch(authNotifierProvider).user;
    final role = normalizeRole(user?.role);
    final access = ref.watch(roleAccessProvider);
    final allowed = allowedModulesFor(role, access);
    final activities = _activitiesFor(role);
    final stats = _buildStats(role);

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
      floatingActionButton: _fab(role),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            GradientHeader(
              'Welcome, ${user?.name ?? 'User'}',
              subtitle:
                  '${role.toUpperCase()}${user?.email != null ? ' • ${user!.email}' : ''}',
            ),
            const SizedBox(height: 20),
            Row(
              children: const [
                SectionTitle("Today's Overview", icon: Icons.insights),
                SizedBox(width: 8),
                _LiveDot(),
                Text('LIVE',
                    style: TextStyle(
                        fontSize: 11,
                        color: AppTheme.success,
                        fontWeight: FontWeight.w700,
                        letterSpacing: 1)),
              ],
            ),
            const SizedBox(height: 12),
            _statGrid(stats),
            const SizedBox(height: 24),
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

  Widget _statGrid(List<_Stat> stats) {
    final palette = [
      AppTheme.primary,
      AppTheme.info,
      AppTheme.accent,
      AppTheme.warning,
      AppTheme.success,
    ];
    return LayoutBuilder(
      builder: (_, c) {
        final w = c.maxWidth;
        final cols = w > 820 ? 4 : (w > 520 ? 3 : 2);
        final gap = 12.0;
        final cw = (w - gap * (cols - 1)) / cols;
        return Wrap(
          spacing: gap,
          runSpacing: gap,
          children: stats
              .asMap()
              .entries
              .map((e) => SizedBox(
                    width: cw,
                    child: _AnimatedStatCard(
                        e.value, palette[e.key % palette.length], e.key),
                  ))
              .toList(),
        );
      },
    );
  }

  List<_Stat> _buildStats(String role) {
    final patients = ref.watch(patientNotifierProvider).patients;
    final appointments = ref.watch(appointmentNotifierProvider).appointments;
    final beds = ref.watch(bedNotifierProvider).beds;
    final today = _today();
    final docId = ref.watch(doctorNotifierProvider).myDoctor?.id;

    switch (role) {
      case 'doctor':
        final mineToday = appointments
            .where((a) =>
                a.doctorId == docId &&
                (a.appointmentDate ?? '').startsWith(today))
            .length;
        final rx = ref.watch(prescriptionNotifierProvider).prescriptions.length;
        return [
          _Stat('My Appts Today', '$mineToday', Icons.calendar_today,
              AppTheme.primary),
          _Stat('Patients', '${patients.length}', Icons.people, AppTheme.info),
          _Stat('Prescriptions', '$rx', Icons.receipt_long, AppTheme.accent),
        ];
      case 'receptionist':
        final todayAppts = appointments
            .where((a) => (a.appointmentDate ?? '').startsWith(today))
            .length;
        final occupied = _occupied(beds);
        return [
          _Stat("Today's Appts", '$todayAppts', Icons.calendar_today,
              AppTheme.primary),
          _Stat('Patients', '${patients.length}', Icons.people, AppTheme.info),
          _Stat('Beds Occupied', '$occupied/${beds.length}', Icons.bed,
              AppTheme.warning),
        ];
      case 'nurse':
      case 'wardmanager':
        final occupied = _occupied(beds);
        final admissions =
            ref.watch(admissionNotifierProvider).admissions.length;
        return [
          _Stat('Beds Occupied', '$occupied/${beds.length}', Icons.bed,
              AppTheme.warning),
          _Stat('Admissions', '$admissions', Icons.assignment_ind,
              AppTheme.info),
          _Stat('Patients', '${patients.length}', Icons.people,
              AppTheme.primary),
        ];
      case 'pharmacist':
      case 'inventorymanager':
        final meds = ref.watch(medicineNotifierProvider).medicines.length;
        final sales = ref.watch(saleNotifierProvider).sales.length;
        return [
          _Stat('Medicines', '$meds', Icons.medication, AppTheme.primary),
          _Stat('Sales', '$sales', Icons.shopping_cart, AppTheme.accent),
        ];
      case 'labtechnician':
        final orders = ref.watch(testOrderNotifierProvider).orders.length;
        final reports = ref.watch(labReportNotifierProvider).reports.length;
        return [
          _Stat('Test Orders', '$orders', Icons.science, AppTheme.primary),
          _Stat('Reports', '$reports', Icons.biotech, AppTheme.info),
        ];
      case 'billingclerk':
        final invoices = ref.watch(billingNotifierProvider).invoices.length;
        final claims = ref.watch(insuranceNotifierProvider).claims.length;
        final rev =
            ref.watch(dashboardNotifierProvider).billing?.todayRevenue ?? 0;
        return [
          _Stat("Today's Revenue", _money(rev), Icons.savings,
              AppTheme.success),
          _Stat('Invoices', '$invoices', Icons.receipt_long, AppTheme.primary),
          _Stat('Claims', '$claims', Icons.health_and_safety,
              AppTheme.warning),
        ];
      case 'dietician':
        final plans = ref.watch(dietPlanNotifierProvider).plans.length;
        return [
          _Stat('Diet Plans', '$plans', Icons.restaurant_menu,
              AppTheme.primary),
          _Stat('Patients', '${patients.length}', Icons.people, AppTheme.info),
        ];
      default:
        final todayAppts = appointments
            .where((a) => (a.appointmentDate ?? '').startsWith(today))
            .length;
        final occupied = _occupied(beds);
        final orders = ref.watch(testOrderNotifierProvider).orders.length;
        return [
          _Stat('Patients', '${patients.length}', Icons.people,
              AppTheme.primary),
          _Stat("Today's Appts", '$todayAppts', Icons.calendar_today,
              AppTheme.info),
          _Stat('Beds Occupied', '$occupied/${beds.length}', Icons.bed,
              AppTheme.warning),
          _Stat('Lab Orders', '$orders', Icons.science, AppTheme.accent),
        ];
    }
  }

  int _occupied(List<dynamic> beds) => beds
      .where((b) => (b.status ?? '').toLowerCase().contains('occup'))
      .length;

  String _today() {
    final d = DateTime.now();
    final m = '${d.month}'.padLeft(2, '0');
    final day = '${d.day}'.padLeft(2, '0');
    return '${d.year}-$m-$day';
  }

  String _money(num v) =>
      '৳ ${v % 1 == 0 ? v.toInt() : v.toStringAsFixed(2)}';

  Widget? _fab(String role) {
    switch (role) {
      case 'doctor':
        return FloatingActionButton.extended(
          onPressed: () {
            final id = ref.read(doctorNotifierProvider).myDoctor?.id;
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => id != null
                    ? PrescriptionFormScreen(doctorId: id)
                    : const DoctorDashboardScreen(),
              ),
            );
          },
          icon: const Icon(Icons.add),
          label: const Text('Prescription'),
        );
      case 'receptionist':
        return FloatingActionButton.extended(
          onPressed: () => Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const AppointmentFormScreen()),
          ),
          icon: const Icon(Icons.add),
          label: const Text('Appointment'),
        );
      case 'billingclerk':
        return FloatingActionButton.extended(
          onPressed: () => Navigator.push(
            context,
            MaterialPageRoute(
                builder: (_) => const BillingInvoiceFormScreen()),
          ),
          icon: const Icon(Icons.add),
          label: const Text('Invoice'),
        );
      default:
        return null;
    }
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
      case ModuleKeys.prescriptions:
        return const PrescriptionListScreen();
      default:
        return null;
    }
  }
}

class _Stat {
  final String label;
  final String value;
  final IconData icon;
  final Color color;
  const _Stat(this.label, this.value, this.icon, this.color);
}

class _StatCard extends StatelessWidget {
  final _Stat stat;
  final Color color;
  const _StatCard(this.stat, this.color);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: color.withValues(alpha: 0.18)),
        boxShadow: const [
          BoxShadow(
              color: Color(0x14000000), blurRadius: 8, offset: Offset(0, 3))
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.all(8),
            decoration: BoxDecoration(
              color: color.withValues(alpha: 0.12),
              borderRadius: BorderRadius.circular(10),
            ),
            child: Icon(stat.icon, color: color, size: 20),
          ),
          const SizedBox(height: 12),
          Text(
            stat.value,
            style: const TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: Color(0xFF263238)),
          ),
          const SizedBox(height: 2),
          Text(stat.label,
              style: const TextStyle(fontSize: 12, color: Colors.grey)),
        ],
      ),
    );
  }
}

class _AnimatedStatCard extends StatelessWidget {
  final _Stat stat;
  final Color color;
  final int index;
  const _AnimatedStatCard(this.stat, this.color, this.index);

  @override
  Widget build(BuildContext context) {
    return TweenAnimationBuilder<double>(
      tween: Tween(begin: 0, end: 1),
      duration: Duration(milliseconds: 350 + index * 70),
      curve: Curves.easeOutCubic,
      builder: (_, v, child) => Opacity(
        opacity: v,
        child: Transform.translate(
          offset: Offset(0, (1 - v) * 14),
          child: _StatCard(stat, color),
        ),
      ),
    );
  }
}

class _LiveDot extends StatefulWidget {
  const _LiveDot();
  @override
  State<_LiveDot> createState() => _LiveDotState();
}

class _LiveDotState extends State<_LiveDot>
    with SingleTickerProviderStateMixin {
  late final AnimationController _c = AnimationController(
      vsync: this, duration: const Duration(seconds: 1))
    ..repeat(reverse: true);
  late final Animation<double> _a =
      Tween<double>(begin: 0.25, end: 1).animate(_c);

  @override
  void dispose() {
    _c.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => FadeTransition(
        opacity: _a,
        child: Container(
          width: 9,
          height: 9,
          decoration: BoxDecoration(
            color: AppTheme.success, shape: BoxShape.circle,
            boxShadow: [
              BoxShadow(
                  color: AppTheme.success.withValues(alpha: 0.5),
                  blurRadius: 6,
                  spreadRadius: 1)
            ],
          ),
        ),
      );
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
