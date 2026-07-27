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
import { DoctorDashboardComponent } from './components/feature/doctors/doctor-dashboard-component/doctor-dashboard-component';
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


const ALL_ROLES = ['Admin', 'Doctor', 'Nurse', 'OfficeStaff', 'Receptionist', 'Pharmacist', 'LabTechnician', 'BillingClerk', 'InventoryManager', 'WardManager'];

export const routes: Routes = [

  // ============ Auth ============
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: RoleRedirect },

  // ============ Role Dashboards ============
  { path: 'admin-dashboard', component: AdminDashboardComponent, canActivate: [authGuard, roleGuard(['Admin'])] },
  { path: 'doctor-dashboard', component: DoctorDashboardHomeComponent, canActivate: [authGuard, roleGuard(['Doctor'])] },
  { path: 'nurse-dashboard', component: NurseDashboardComponent, canActivate: [authGuard, roleGuard(['Nurse'])] },
  { path: 'office-staff-dashboard', component: OfficeStaffDashboardComponent, canActivate: [authGuard, roleGuard(['OfficeStaff'])] },
  { path: 'receptionist-dashboard', component: ReceptionistDashboardComponent, canActivate: [authGuard, roleGuard(['Receptionist'])] },
  { path: 'pharmacist-dashboard', component: PharmacistDashboardComponent, canActivate: [authGuard, roleGuard(['Pharmacist'])] },
  { path: 'lab-technician-dashboard', component: LabTechnicianDashboardComponent, canActivate: [authGuard, roleGuard(['LabTechnician'])] },
  { path: 'billing-clerk-dashboard', component: BillingClerkDashboardComponent, canActivate: [authGuard, roleGuard(['BillingClerk'])] },
  { path: 'inventory-manager-dashboard', component: InventoryManagerDashboardComponent, canActivate: [authGuard, roleGuard(['InventoryManager'])] },
  { path: 'ward-manager-dashboard', component: WardManagerDashboardComponent, canActivate: [authGuard, roleGuard(['WardManager'])] },

  // ============ Doctors ============
  { path: 'doctor-departments', component: DoctorDepartment },
  { path: 'doctor/create', component: Doctor },
  { path: 'doctor/edit/:id', component: Doctor },
  { path: 'doctor', component: DoctorList },
  { path: 'doctor/prescription/:appointmentId', component: PrescriptionComponent },

  // ============ Nurses ============
  { path: 'nurses/create', component: Nurse },
  { path: 'nurses', component: NurseList },

  // ============ Office Staff ============
  { path: 'office-staff/create', component: OfficeStaffComponent },
  { path: 'office-staff/edit/:id', component: OfficeStaffComponent },
  { path: 'office-staff', component: OfficeStaffList },

  // ============ Schedule Slot ============
  { path: 'schedule-slot', component: ScheduleSlotComponent },

  // ============ Appointments ============
  { path: 'appointments', component: AppointmentComponent },
  { path: 'appointment-list', component: AppointmentList },
  { path: 'appointments/create', component: AppointmentComponent },
  { path: 'appointments/edit/:id', component: AppointmentComponent },
  { path: 'appointment-slip', component: AppointmentSlip },

  // ============ Medicine ============
  { path: 'medicines/create', component: MedicineComponent },
  { path: 'medicine-list', component: MedicineListComponent },
  { path: 'medicine/:id', component: MedicineComponent },

  // ============ Generics ============
  { path: 'generics', component: Generic },
  { path: 'generic-list', component: GenericListComponent },

  // ============ Prescriptions ============
  { path: 'prescriptions', component: PrescriptionListComponent },
  { path: 'prescriptions/edit/:id', component: PrescriptionComponent },
  { path: 'prescriptions/create/:appointmentId', component: PrescriptionComponent },

  // ============ Patients ============
  { path: 'patient', component: PatientComponent },
  { path: 'patient/edit/:id', component: PatientComponent },
  { path: 'patient-list', component: PatientListComponent },

  // ============ Tests ============
  { path: 'tests/create', component: TestMasterComponent },
  { path: 'tests/edit/:id', component: TestMasterComponent },
  { path: 'test-list', component: TestListComponent },

  // ============ Admission ============
  { path: 'admission', component: AdmissionComponent },
  { path: 'admission-list', component: AdmissionListComponent },

  // ============ Wards & Beds ============
  { path: 'ward-management', component: WardManagementComponent },
  { path: 'ward', component: WardComponent },
  { path: 'ward-list', component: WardListComponent },
  { path: 'bed-management', component: BedManagementComponent },

  // ============ Laboratory ============
  { path: 'lab-reception', component: LabReceptionComponent },
  { path: 'sample-collection', component: SampleCollectionComponent },
  { path: 'result-entry', component: ResultEntryComponent },
  { path: 'lab-verification', component: LabVerificationComponent },
  { path: 'doctor-lab-reports', component: DoctorLabReportComponent },

  // ============ Pharmacy Module ============
  { path: 'pharmacy-dashboard', component: PharmacyDashboardComponent },
  { path: 'suppliers', component: SupplierComponent },
  { path: 'medicine-stock', component: MedicineStockComponent },
  { path: 'purchase-orders', component: PurchaseOrderComponent },
  { path: 'pharmacy-sale', component: PharmacySaleComponent },
  { path: 'pharmacy-reports', component: PharmacyReportComponent },


  // ============ Billing Module ============
  { path: 'billing-dashboard', component: BillingDashboardComponent },
  { path: 'patient-billing', component: PatientBillingComponent },
  { path: 'payments', component: PaymentModuleComponent },
  { path: 'invoices', component: InvoiceComponent },
  { path: 'refunds', component: RefundManagementComponent },
  { path: 'insurance', component: InsuranceComponent },
  { path: 'billing-reports', component: BillingReportsComponent },

  // ============ Reports & Analytics ============
  { path: 'reports', component: ReportsDashboardComponent },
  { path: 'reports/patient', component: PatientReportsComponent },
  { path: 'reports/appointment', component: AppointmentReportsComponent },
  { path: 'reports/doctor', component: DoctorReportsComponent },
  { path: 'reports/revenue', component: RevenueReportsComponent },
  { path: 'reports/lab', component: LabReportsComponent },
  { path: 'reports/pharmacy', component: PharmacyReportsComponent },
  { path: 'reports/bed', component: BedReportsComponent },
  { path: 'reports/emergency', component: EmergencyReportsComponent },

  // ============ Dietary & Nutrition Module ============
  { path: 'dietary/dashboard', component: DietaryDashboardComponent, canActivate: [authGuard, roleGuard(['Admin'])], data: { title: 'Dietary Dashboard' } },
  { path: 'dietary/patient-diet', component: PatientDietComponent, canActivate: [authGuard, roleGuard(['Admin', 'Doctor', 'Nurse'])], data: { title: 'Patient Diet' } },
  { path: 'dietary/diet-plans', component: DietPlanComponent, canActivate: [authGuard, roleGuard(['Admin', 'Dietician'])], data: { title: 'Diet Plans' } },
  { path: 'dietary/dieticians', component: DieticianManagementComponent, canActivate: [authGuard, roleGuard(['Admin'])], data: { title: 'Dietician Management' } },
  { path: 'dietary/meal-schedule', component: MealScheduleComponent, canActivate: [authGuard, roleGuard(['Admin', 'Dietician', 'Nurse'])], data: { title: 'Meal Schedule' } },
  { path: 'dietary/kitchen-dashboard', component: KitchenDashboardComponent, canActivate: [authGuard, roleGuard(['Admin', 'Nurse', 'WardManager'])], data: { title: 'Kitchen Dashboard' } },
  { path: 'dietary/kitchen-orders', component: KitchenOrdersComponent, canActivate: [authGuard, roleGuard(['Admin', 'Nurse', 'WardManager'])], data: { title: 'Kitchen Orders' } },
  { path: 'dietary/nutrition-analytics', component: NutritionAnalyticsComponent, canActivate: [authGuard, roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Nutrition Analytics' } },
  { path: 'dietary/diet-reports', component: DietReportsComponent, canActivate: [authGuard, roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Diet Reports' } },
  { path: 'dietary/diet-alerts', component: DietAlertsComponent, canActivate: [authGuard, roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Diet Alerts' } },
  { path: 'dietary/diet-history', component: DietHistoryComponent, canActivate: [authGuard, roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Diet History' } },


  // ============ Public ============
  { path: '', component: LandingPageComponent },

  // ============ Default ============
  { path: '**', redirectTo: '' },
];
