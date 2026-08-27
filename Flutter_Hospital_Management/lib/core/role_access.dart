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
}

const Set<String> _allModules = {
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
};

Set<String> allowedModulesFor(String? role) {
  final r = (role ?? '').trim().toLowerCase();
  switch (r) {
    case 'admin':
    case 'officestaff':
      return _allModules;
    case 'receptionist':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.patients,
        ModuleKeys.appointments,
        ModuleKeys.admissions,
        ModuleKeys.beds,
        ModuleKeys.doctors,
      };
    case 'doctor':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.patients,
        ModuleKeys.appointments,
        ModuleKeys.doctors,
        ModuleKeys.labTests,
        ModuleKeys.labReports,
        ModuleKeys.dietPlans,
        ModuleKeys.surgeries,
        ModuleKeys.surgeryCatalog,
      };
    case 'nurse':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.patients,
        ModuleKeys.appointments,
        ModuleKeys.beds,
        ModuleKeys.admissions,
        ModuleKeys.dietPlans,
      };
    case 'pharmacist':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.pharmacy,
        ModuleKeys.pharmacySales,
      };
    case 'labtechnician':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.labTests,
        ModuleKeys.labReports,
        ModuleKeys.labDashboard,
      };
    case 'billingclerk':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.billing,
        ModuleKeys.pharmacySales,
        ModuleKeys.insurance,
        ModuleKeys.insuranceClaims,
      };
    case 'inventorymanager':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.pharmacy,
      };
    case 'wardmanager':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.beds,
        ModuleKeys.admissions,
        ModuleKeys.patients,
      };
    case 'dietician':
      return {
        ModuleKeys.dashboard,
        ModuleKeys.dietPlans,
        ModuleKeys.patients,
      };
    default:
      return {};
  }
}

bool canAccess(String? role, String key) =>
    allowedModulesFor(role).contains(key);
