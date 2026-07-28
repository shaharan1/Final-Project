import { Routes } from '@angular/router';

import { LandingPageComponent } from './components/public/landing-page/landing-page.component';
import { Doctor } from './components/feature/doctors/doctor/doctor';
import { DoctorList } from './components/feature/doctors/doctor-list/doctor-list';
import { DoctorDepartment } from './components/feature/doctors/doctor-department/doctor-department';
import { Nurse } from './components/feature/nurse/nurse';
import { NurseList } from './components/feature/nurse-list/nurse-list';
import { OfficeStaffComponent } from './components/feature/office-staff.component/office-staff.component';
import { OfficeStaffList } from './components/feature/office-staff-list.component/office-staff-list.component';
import { ScheduleSlotComponent } from './components/feature/schedule-slot.component/schedule-slot.component';
import { AppointmentList } from './components/feature/appointment/appointment-list.component/appointment-list.component';
import { AppointmentComponent } from './components/feature/appointment/appointment.component/appointment.component';
import { AppointmentSlip } from './components/feature/appointment/appointment-slip/appointment-slip';
import { MedicineComponent } from './components/feature/medicine.component/medicine.component';
import { MedicineListComponent } from './components/feature/medicine-list.component/medicine-list.component';
import { Generic } from './components/feature/generic/generic';
import { GenericListComponent } from './components/feature/generic-list/generic-list';
import { PrescriptionComponent } from './components/feature/prescription/prescription';
import { PrescriptionListComponent } from './components/feature/prescription/prescription-list.component';
import { PatientComponent } from './components/feature/patient.component/patient.component';
import { PatientListComponent } from './components/feature/patient-list.component/patient-list.component';
import { LoginComponent } from './components/auth/login-component/login-component';
import { RoleRedirect } from './components/auth/role-redirect/role-redirect';
import { authGuard, roleGuard } from './guard/auth-guard-guard';
import { TestMasterComponent } from './components/feature/test-master.component/test-master.component';
import { TestListComponent } from './components/feature/test-list.component/test-list.component';
import { AdmissionComponent } from './components/feature/admission/admission-component/admission-component';
import { AdmissionListComponent } from './components/feature/admission/admission-list.component/admission-list.component';
import { WardManagementComponent } from './components/feature/ward/ward-management.component/ward-management.component';
import { WardComponent } from './components/feature/ward/ward.component/ward.component';
import { WardListComponent } from './components/feature/ward/ward-list.component/ward-list.component';
import { BedManagementComponent } from './components/feature/bed/bed-management.component/bed-management.component';

import { AdminDashboardComponent } from './components/feature/dashboards/admin-dashboard/admin-dashboard.component';
import { DoctorDashboardHomeComponent } from './components/feature/dashboards/doctor-dashboard-home/doctor-dashboard-home.component';
import { NurseDashboardComponent } from './components/feature/dashboards/nurse-dashboard/nurse-dashboard.component';
import { OfficeStaffDashboardComponent } from './components/feature/dashboards/office-staff-dashboard/office-staff-dashboard.component';
import { ReceptionistDashboardComponent } from './components/feature/dashboards/receptionist-dashboard/receptionist-dashboard.component';
import { PharmacistDashboardComponent } from './components/feature/dashboards/pharmacist-dashboard/pharmacist-dashboard.component';
import { LabTechnicianDashboardComponent } from './components/feature/dashboards/lab-technician-dashboard/lab-technician-dashboard.component';
import { BillingClerkDashboardComponent } from './components/feature/dashboards/billing-clerk-dashboard/billing-clerk-dashboard.component';
import { InventoryManagerDashboardComponent } from './components/feature/dashboards/inventory-manager-dashboard/inventory-manager-dashboard.component';
import { WardManagerDashboardComponent } from './components/feature/dashboards/ward-manager-dashboard/ward-manager-dashboard.component';
import { LabReceptionComponent } from './components/feature/lab/lab-reception/lab-reception.component';
import { SampleCollectionComponent } from './components/feature/lab/sample-collection/sample-collection.component';
import { ResultEntryComponent } from './components/feature/lab/result-entry/result-entry.component';
import { LabVerificationComponent } from './components/feature/lab/lab-verification/lab-verification.component';
import { DoctorLabReportComponent } from './components/feature/lab/doctor-lab-report/doctor-lab-report.component';

import { SupplierComponent } from './components/feature/pharmacy/supplier/supplier.component';
import { MedicineStockComponent } from './components/feature/pharmacy/medicine-stock/medicine-stock.component';
import { PurchaseOrderComponent } from './components/feature/pharmacy/purchase-order/purchase-order.component';
import { PharmacySaleComponent } from './components/feature/pharmacy/pharmacy-sale/pharmacy-sale.component';
import { PharmacyDashboardComponent } from './components/feature/pharmacy/pharmacy-dashboard/pharmacy-dashboard.component';
import { PharmacyReportComponent } from './components/feature/pharmacy/pharmacy-report/pharmacy-report.component';

import { BillingDashboardComponent } from './components/feature/billing/billing-dashboard/billing-dashboard.component';
import { PatientBillingComponent } from './components/feature/billing/patient-billing/patient-billing.component';
import { PaymentModuleComponent } from './components/feature/billing/payment-module/payment-module.component';
import { InvoiceComponent } from './components/feature/billing/invoice/invoice.component';
import { RefundManagementComponent } from './components/feature/billing/refund-management/refund-management.component';
import { InsuranceComponent } from './components/feature/billing/insurance/insurance.component';
import { BillingReportsComponent } from './components/feature/billing/billing-reports/billing-reports.component';

import { EmergencyDashboardComponent } from './components/feature/emergency/emergency-dashboard/emergency-dashboard.component';
import { EmergencyRegistrationComponent } from './components/feature/emergency/emergency-registration/emergency-registration.component';
import { EmergencyTriageComponent } from './components/feature/emergency/emergency-triage/emergency-triage.component';
import { EmergencyDoctorAssignmentComponent } from './components/feature/emergency/emergency-doctor-assignment/emergency-doctor-assignment.component';
import { EmergencyBedComponent } from './components/feature/emergency/emergency-bed/emergency-bed.component';
import { EmergencyAmbulanceComponent } from './components/feature/emergency/emergency-ambulance/emergency-ambulance.component';
import { EmergencyMedicineComponent } from './components/feature/emergency/emergency-medicine/emergency-medicine.component';
import { EmergencyLabComponent } from './components/feature/emergency/emergency-lab/emergency-lab.component';
import { EmergencyBillingComponent } from './components/feature/emergency/emergency-billing/emergency-billing.component';
import { EmergencyTimelineComponent } from './components/feature/emergency/emergency-timeline/emergency-timeline.component';
import { EmergencyStatusBoardComponent } from './components/feature/emergency/emergency-status-board/emergency-status-board.component';

import { ReportsDashboardComponent } from './components/feature/reports/reports-dashboard/reports-dashboard.component';
import { PatientReportsComponent } from './components/feature/reports/patient-reports/patient-reports.component';
import { AppointmentReportsComponent } from './components/feature/reports/appointment-reports/appointment-reports.component';
import { DoctorReportsComponent } from './components/feature/reports/doctor-reports/doctor-reports.component';
import { RevenueReportsComponent } from './components/feature/reports/revenue-reports/revenue-reports.component';
import { LabReportsComponent } from './components/feature/reports/lab-reports/lab-reports.component';
import { PharmacyReportsComponent } from './components/feature/reports/pharmacy-reports/pharmacy-reports.component';
import { BedReportsComponent } from './components/feature/reports/bed-reports/bed-reports.component';
import { EmergencyReportsComponent } from './components/feature/reports/emergency-reports/emergency-reports.component';
import { DietaryDashboardComponent } from './components/feature/dietary/dietary-dashboard/dietary-dashboard.component';
import { PatientDietComponent } from './components/feature/dietary/patient-diet/patient-diet.component';
import { DietPlanComponent } from './components/feature/dietary/diet-plan/diet-plan.component';
import { DieticianManagementComponent } from './components/feature/dietary/dietician-management/dietician-management.component';
import { MealScheduleComponent } from './components/feature/dietary/meal-schedule/meal-schedule.component';
import { KitchenDashboardComponent } from './components/feature/dietary/kitchen-dashboard/kitchen-dashboard.component';
import { KitchenOrdersComponent } from './components/feature/dietary/kitchen-orders/kitchen-orders.component';
import { NutritionAnalyticsComponent } from './components/feature/dietary/nutrition-analytics/nutrition-analytics.component';
import { DietReportsComponent } from './components/feature/dietary/diet-reports/diet-reports.component';
import { DietAlertsComponent } from './components/feature/dietary/diet-alerts/diet-alerts.component';
import { DietHistoryComponent } from './components/feature/dietary/diet-history/diet-history.component';

import { LayoutComponent } from './components/shared/layout/layout/layout.component';

export const routes: Routes = [

  // ============ Public ============
  { path: 'login', component: LoginComponent },
  { path: '', component: LandingPageComponent },

  // ============ Dashboard redirect ============
  { path: 'dashboard', component: RoleRedirect, canActivate: [authGuard] },

  // ============ All authenticated routes under persistent layout ============
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [

      // ============ Role Dashboards ============
      { path: 'admin-dashboard', component: AdminDashboardComponent, data: { title: 'Admin Dashboard' } },
      { path: 'doctor-dashboard', component: DoctorDashboardHomeComponent, data: { title: 'Doctor Dashboard' } },
      { path: 'nurse-dashboard', component: NurseDashboardComponent, data: { title: 'Nurse Dashboard' } },
      { path: 'office-staff-dashboard', component: OfficeStaffDashboardComponent, data: { title: 'Office Staff Dashboard' } },
      { path: 'receptionist-dashboard', component: ReceptionistDashboardComponent, data: { title: 'Receptionist Dashboard' } },
      { path: 'pharmacist-dashboard', component: PharmacistDashboardComponent, data: { title: 'Pharmacist Dashboard' } },
      { path: 'lab-technician-dashboard', component: LabTechnicianDashboardComponent, data: { title: 'Lab Technician Dashboard' } },
      { path: 'billing-clerk-dashboard', component: BillingClerkDashboardComponent, data: { title: 'Billing Clerk Dashboard' } },
      { path: 'inventory-manager-dashboard', component: InventoryManagerDashboardComponent, data: { title: 'Inventory Manager Dashboard' } },
      { path: 'ward-manager-dashboard', component: WardManagerDashboardComponent, data: { title: 'Ward Manager Dashboard' } },

      // ============ Doctors ============
      { path: 'doctor-departments', component: DoctorDepartment, data: { title: 'Doctor Departments' } },
      { path: 'doctor/create', component: Doctor, data: { title: 'Add Doctor' } },
      { path: 'doctor/edit/:id', component: Doctor, data: { title: 'Edit Doctor' } },
      { path: 'doctor', component: DoctorList, data: { title: 'Doctors' } },
      { path: 'doctor/prescription/:appointmentId', component: PrescriptionComponent, data: { title: 'Write Prescription' } },

      // ============ Nurses ============
      { path: 'nurses/create', component: Nurse, data: { title: 'Add Nurse' } },
      { path: 'nurses', component: NurseList, data: { title: 'Nurses' } },

      // ============ Office Staff ============
      { path: 'office-staff/create', component: OfficeStaffComponent, data: { title: 'Add Office Staff' } },
      { path: 'office-staff/edit/:id', component: OfficeStaffComponent, data: { title: 'Edit Office Staff' } },
      { path: 'office-staff', component: OfficeStaffList, data: { title: 'Office Staff' } },

      // ============ Schedule Slot ============
      { path: 'schedule-slot', component: ScheduleSlotComponent, data: { title: 'Schedule Slots' } },

      // ============ Appointments ============
      { path: 'appointments', component: AppointmentComponent, data: { title: 'Appointments' } },
      { path: 'appointment-list', component: AppointmentList, data: { title: 'Appointment List' } },
      { path: 'appointments/create', component: AppointmentComponent, data: { title: 'Book Appointment' } },
      { path: 'appointments/edit/:id', component: AppointmentComponent, data: { title: 'Edit Appointment' } },
      { path: 'appointment-slip', component: AppointmentSlip, data: { title: 'Appointment Slip' } },

      // ============ Medicine ============
      { path: 'medicines/create', component: MedicineComponent, data: { title: 'Add Medicine' } },
      { path: 'medicine-list', component: MedicineListComponent, data: { title: 'Medicine List' } },
      { path: 'medicine/:id', component: MedicineComponent, data: { title: 'Edit Medicine' } },

      // ============ Generics ============
      { path: 'generics', component: Generic, data: { title: 'Add Generic' } },
      { path: 'generic-list', component: GenericListComponent, data: { title: 'Generic List' } },

      // ============ Prescriptions ============
      { path: 'prescriptions', component: PrescriptionListComponent, data: { title: 'Prescriptions' } },
      { path: 'prescriptions/edit/:id', component: PrescriptionComponent, data: { title: 'Edit Prescription' } },
      { path: 'prescriptions/create/:appointmentId', component: PrescriptionComponent, data: { title: 'Create Prescription' } },

      // ============ Patients ============
      { path: 'patient', component: PatientComponent, data: { title: 'Add Patient' } },
      { path: 'patient/edit/:id', component: PatientComponent, data: { title: 'Edit Patient' } },
      { path: 'patient-list', component: PatientListComponent, data: { title: 'Patient List' } },

      // ============ Tests ============
      { path: 'tests/create', component: TestMasterComponent, data: { title: 'Add Test' } },
      { path: 'tests/edit/:id', component: TestMasterComponent, data: { title: 'Edit Test' } },
      { path: 'test-list', component: TestListComponent, data: { title: 'Test List' } },

      // ============ Admission ============
      { path: 'admission', component: AdmissionComponent, data: { title: 'Patient Admission' } },
      { path: 'admission-list', component: AdmissionListComponent, data: { title: 'Admission List' } },

      // ============ Wards & Beds ============
      { path: 'ward-management', component: WardManagementComponent, data: { title: 'Ward Management' } },
      { path: 'ward', component: WardComponent, data: { title: 'Add Ward' } },
      { path: 'ward-list', component: WardListComponent, data: { title: 'Ward List' } },
      { path: 'bed-management', component: BedManagementComponent, data: { title: 'Bed Management' } },

      // ============ Laboratory ============
      { path: 'lab-reception', component: LabReceptionComponent, data: { title: 'Lab Reception' } },
      { path: 'sample-collection', component: SampleCollectionComponent, data: { title: 'Sample Collection' } },
      { path: 'result-entry', component: ResultEntryComponent, data: { title: 'Result Entry' } },
      { path: 'lab-verification', component: LabVerificationComponent, data: { title: 'Lab Verification' } },
      { path: 'doctor-lab-reports', component: DoctorLabReportComponent, data: { title: 'Lab Reports' } },

      // ============ Pharmacy Module ============
      { path: 'pharmacy-dashboard', component: PharmacyDashboardComponent, data: { title: 'Pharmacy Dashboard' } },
      { path: 'suppliers', component: SupplierComponent, data: { title: 'Suppliers' } },
      { path: 'medicine-stock', component: MedicineStockComponent, data: { title: 'Medicine Stock' } },
      { path: 'purchase-orders', component: PurchaseOrderComponent, data: { title: 'Purchase Orders' } },
      { path: 'pharmacy-sale', component: PharmacySaleComponent, data: { title: 'Pharmacy Sale' } },
      { path: 'pharmacy-reports', component: PharmacyReportComponent, data: { title: 'Pharmacy Reports' } },

      // ============ Billing Module ============
      { path: 'billing-dashboard', component: BillingDashboardComponent, data: { title: 'Billing Dashboard' } },
      { path: 'patient-billing', component: PatientBillingComponent, data: { title: 'Patient Billing' } },
      { path: 'payments', component: PaymentModuleComponent, data: { title: 'Payments' } },
      { path: 'invoices', component: InvoiceComponent, data: { title: 'Invoices' } },
      { path: 'refunds', component: RefundManagementComponent, data: { title: 'Refunds' } },
      { path: 'insurance', component: InsuranceComponent, data: { title: 'Insurance' } },
      { path: 'billing-reports', component: BillingReportsComponent, data: { title: 'Billing Reports' } },

      // ============ Emergency Module ============
      { path: 'emergency/dashboard', component: EmergencyDashboardComponent, data: { title: 'Emergency Dashboard' } },
      { path: 'emergency/registration', component: EmergencyRegistrationComponent, data: { title: 'Emergency Registration' } },
      { path: 'emergency/triage', component: EmergencyTriageComponent, data: { title: 'Triage' } },
      { path: 'emergency/assignments', component: EmergencyDoctorAssignmentComponent, data: { title: 'Doctor Assignments' } },
      { path: 'emergency/beds', component: EmergencyBedComponent, data: { title: 'Emergency Beds' } },
      { path: 'emergency/ambulances', component: EmergencyAmbulanceComponent, data: { title: 'Ambulances' } },
      { path: 'emergency/medicine', component: EmergencyMedicineComponent, data: { title: 'Emergency Medicine' } },
      { path: 'emergency/lab-orders', component: EmergencyLabComponent, data: { title: 'Emergency Lab Orders' } },
      { path: 'emergency/billing', component: EmergencyBillingComponent, data: { title: 'Emergency Billing' } },
      { path: 'emergency/timeline', component: EmergencyTimelineComponent, data: { title: 'Emergency Timeline' } },
      { path: 'emergency/status-board', component: EmergencyStatusBoardComponent, data: { title: 'Emergency Status Board' } },
      { path: 'emergency', redirectTo: 'emergency/dashboard', pathMatch: 'full' },

      // ============ Reports & Analytics ============
      { path: 'reports', component: ReportsDashboardComponent, data: { title: 'Reports Dashboard' } },
      { path: 'reports/patient', component: PatientReportsComponent, data: { title: 'Patient Reports' } },
      { path: 'reports/appointment', component: AppointmentReportsComponent, data: { title: 'Appointment Reports' } },
      { path: 'reports/doctor', component: DoctorReportsComponent, data: { title: 'Doctor Reports' } },
      { path: 'reports/revenue', component: RevenueReportsComponent, data: { title: 'Revenue Reports' } },
      { path: 'reports/lab', component: LabReportsComponent, data: { title: 'Lab Reports' } },
      { path: 'reports/pharmacy', component: PharmacyReportsComponent, data: { title: 'Pharmacy Reports' } },
      { path: 'reports/bed', component: BedReportsComponent, data: { title: 'Bed Reports' } },
      { path: 'reports/emergency', component: EmergencyReportsComponent, data: { title: 'Emergency Reports' } },

      // ============ Dietary & Nutrition Module ============
      { path: 'dietary/dashboard', component: DietaryDashboardComponent, data: { title: 'Dietary Dashboard' } },
      { path: 'dietary/patient-diet', component: PatientDietComponent, data: { title: 'Patient Diet' } },
      { path: 'dietary/diet-plans', component: DietPlanComponent, data: { title: 'Diet Plans' } },
      { path: 'dietary/dieticians', component: DieticianManagementComponent, data: { title: 'Dietician Management' } },
      { path: 'dietary/meal-schedule', component: MealScheduleComponent, data: { title: 'Meal Schedule' } },
      { path: 'dietary/kitchen-dashboard', component: KitchenDashboardComponent, data: { title: 'Kitchen Dashboard' } },
      { path: 'dietary/kitchen-orders', component: KitchenOrdersComponent, data: { title: 'Kitchen Orders' } },
      { path: 'dietary/nutrition-analytics', component: NutritionAnalyticsComponent, data: { title: 'Nutrition Analytics' } },
      { path: 'dietary/diet-reports', component: DietReportsComponent, data: { title: 'Diet Reports' } },
      { path: 'dietary/diet-alerts', component: DietAlertsComponent, data: { title: 'Diet Alerts' } },
      { path: 'dietary/diet-history', component: DietHistoryComponent, data: { title: 'Diet History' } },
    ]
  },

  // ============ Default ============
  { path: '**', redirectTo: '' },
];
