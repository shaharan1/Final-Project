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
import { LabDashboardComponent } from './components/feature/lab/lab-dashboard/lab-dashboard.component';
import { LabTestEntryComponent } from './components/feature/lab/lab-test-entry/lab-test-entry.component';
import { LabReportPreviewComponent } from './components/feature/lab/lab-report-preview/lab-report-preview.component';
import { LabReportsListComponent } from './components/feature/lab/lab-reports-list/lab-reports-list.component';
import { ReferenceRangeManagementComponent } from './components/feature/lab/reference-range-management/reference-range-management.component';

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

import { SurgeryDashboardComponent } from './components/feature/surgery/surgery-dashboard/surgery-dashboard.component';
import { SurgeryListComponent } from './components/feature/surgery/surgery-list/surgery-list.component';
import { SurgeryFormComponent } from './components/feature/surgery/surgery-form/surgery-form.component';
import { SurgeryDetailsComponent } from './components/feature/surgery/surgery-details/surgery-details.component';
import { SurgeryRateManagementComponent } from './components/feature/surgery/surgery-rate-management/surgery-rate-management.component';
import { OtScheduleComponent } from './components/feature/surgery/ot-schedule/ot-schedule.component';
import { SurgeryInvoicePreviewComponent } from './components/feature/surgery/surgery-invoice-preview/surgery-invoice-preview.component';

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
      { path: 'admin-dashboard', component: AdminDashboardComponent, canActivate: [roleGuard(['Admin'])], data: { title: 'Admin Dashboard' } },
      { path: 'doctor-dashboard', component: DoctorDashboardHomeComponent, canActivate: [roleGuard(['Doctor'])], data: { title: 'Doctor Dashboard' } },
      { path: 'nurse-dashboard', component: NurseDashboardComponent, canActivate: [roleGuard(['Nurse'])], data: { title: 'Nurse Dashboard' } },
      { path: 'office-staff-dashboard', component: OfficeStaffDashboardComponent, canActivate: [roleGuard(['OfficeStaff'])], data: { title: 'Office Staff Dashboard' } },
      { path: 'receptionist-dashboard', component: ReceptionistDashboardComponent, canActivate: [roleGuard(['Receptionist'])], data: { title: 'Receptionist Dashboard' } },
      { path: 'pharmacist-dashboard', component: PharmacistDashboardComponent, canActivate: [roleGuard(['Pharmacist'])], data: { title: 'Pharmacist Dashboard' } },
      { path: 'lab-technician-dashboard', component: LabTechnicianDashboardComponent, canActivate: [roleGuard(['LabTechnician'])], data: { title: 'Lab Technician Dashboard' } },
      { path: 'billing-clerk-dashboard', component: BillingClerkDashboardComponent, canActivate: [roleGuard(['BillingClerk'])], data: { title: 'Billing Clerk Dashboard' } },
      { path: 'inventory-manager-dashboard', component: InventoryManagerDashboardComponent, canActivate: [roleGuard(['InventoryManager'])], data: { title: 'Inventory Manager Dashboard' } },
      { path: 'ward-manager-dashboard', component: WardManagerDashboardComponent, canActivate: [roleGuard(['WardManager'])], data: { title: 'Ward Manager Dashboard' } },

      // ============ Doctors ============
      { path: 'doctor-departments', component: DoctorDepartment, canActivate: [roleGuard(['Admin', 'Doctor', 'Receptionist'])], data: { title: 'Doctor Departments' } },
      { path: 'doctor/create', component: Doctor, canActivate: [roleGuard(['Admin'])], data: { title: 'Add Doctor' } },
      { path: 'doctor/edit/:id', component: Doctor, canActivate: [roleGuard(['Admin'])], data: { title: 'Edit Doctor' } },
      { path: 'doctor', component: DoctorList, canActivate: [roleGuard(['Admin', 'Doctor', 'Receptionist'])], data: { title: 'Doctors' } },
      { path: 'doctor/prescription/:appointmentId', component: PrescriptionComponent, canActivate: [roleGuard(['Doctor'])], data: { title: 'Write Prescription' } },

      // ============ Nurses ============
      { path: 'nurses/create', component: Nurse, canActivate: [roleGuard(['Admin'])], data: { title: 'Add Nurse' } },
      { path: 'nurses', component: NurseList, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse'])], data: { title: 'Nurses' } },

      // ============ Office Staff ============
      { path: 'office-staff/create', component: OfficeStaffComponent, canActivate: [roleGuard(['Admin'])], data: { title: 'Add Office Staff' } },
      { path: 'office-staff/edit/:id', component: OfficeStaffComponent, canActivate: [roleGuard(['Admin'])], data: { title: 'Edit Office Staff' } },
      { path: 'office-staff', component: OfficeStaffList, canActivate: [roleGuard(['Admin', 'OfficeStaff'])], data: { title: 'Office Staff' } },

      // ============ Schedule Slot ============
      { path: 'schedule-slot', component: ScheduleSlotComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Receptionist'])], data: { title: 'Schedule Slots' } },

      // ============ Appointments ============
      { path: 'appointments', component: AppointmentComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor'])], data: { title: 'Appointments' } },
      { path: 'appointment-list', component: AppointmentList, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor', 'Nurse'])], data: { title: 'Appointment List' } },
      { path: 'appointments/create', component: AppointmentComponent, canActivate: [roleGuard(['Admin', 'Receptionist'])], data: { title: 'Book Appointment' } },
      { path: 'appointments/edit/:id', component: AppointmentComponent, canActivate: [roleGuard(['Admin', 'Receptionist'])], data: { title: 'Edit Appointment' } },
      { path: 'appointment-slip', component: AppointmentSlip, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor'])], data: { title: 'Appointment Slip' } },

      // ============ Medicine ============
      { path: 'medicines/create', component: MedicineComponent, canActivate: [roleGuard(['Admin', 'Pharmacist'])], data: { title: 'Add Medicine' } },
      { path: 'medicine-list', component: MedicineListComponent, canActivate: [roleGuard(['Admin', 'Pharmacist', 'Doctor'])], data: { title: 'Medicine List' } },
      { path: 'medicine/:id', component: MedicineComponent, canActivate: [roleGuard(['Admin', 'Pharmacist'])], data: { title: 'Edit Medicine' } },

      // ============ Generics ============
      { path: 'generics', component: Generic, canActivate: [roleGuard(['Admin', 'Pharmacist'])], data: { title: 'Add Generic' } },
      { path: 'generic-list', component: GenericListComponent, canActivate: [roleGuard(['Admin', 'Pharmacist'])], data: { title: 'Generic List' } },

      // ============ Prescriptions ============
      { path: 'prescriptions', component: PrescriptionListComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse', 'Pharmacist'])], data: { title: 'Prescriptions' } },
      { path: 'prescriptions/edit/:id', component: PrescriptionComponent, canActivate: [roleGuard(['Doctor'])], data: { title: 'Edit Prescription' } },
      { path: 'prescriptions/create/:appointmentId', component: PrescriptionComponent, canActivate: [roleGuard(['Doctor'])], data: { title: 'Create Prescription' } },

      // ============ Patients ============
      { path: 'patient', component: PatientComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor'])], data: { title: 'Add Patient' } },
      { path: 'patient/edit/:id', component: PatientComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor'])], data: { title: 'Edit Patient' } },
      { path: 'patient-list', component: PatientListComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor', 'Nurse'])], data: { title: 'Patient List' } },

      // ============ Tests ============
      { path: 'tests/create', component: TestMasterComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Add Test' } },
      { path: 'tests/edit/:id', component: TestMasterComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Edit Test' } },
      { path: 'test-list', component: TestListComponent, canActivate: [roleGuard(['Admin', 'LabTechnician', 'Doctor'])], data: { title: 'Test List' } },

      // ============ Admission ============
      { path: 'admission', component: AdmissionComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor'])], data: { title: 'Patient Admission' } },
      { path: 'admission-list', component: AdmissionListComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Doctor', 'Nurse'])], data: { title: 'Admission List' } },

      // ============ Wards & Beds ============
      { path: 'ward-management', component: WardManagementComponent, canActivate: [roleGuard(['Admin', 'WardManager'])], data: { title: 'Ward Management' } },
      { path: 'ward', component: WardComponent, canActivate: [roleGuard(['Admin', 'WardManager'])], data: { title: 'Add Ward' } },
      { path: 'ward-list', component: WardListComponent, canActivate: [roleGuard(['Admin', 'WardManager', 'Doctor', 'Nurse'])], data: { title: 'Ward List' } },
      { path: 'bed-management', component: BedManagementComponent, canActivate: [roleGuard(['Admin', 'WardManager'])], data: { title: 'Bed Management' } },

      // ============ Laboratory ============
      { path: 'lab-reception', component: LabReceptionComponent, canActivate: [roleGuard(['Admin', 'LabTechnician', 'Receptionist'])], data: { title: 'Lab Reception' } },
      { path: 'sample-collection', component: SampleCollectionComponent, canActivate: [roleGuard(['Admin', 'LabTechnician', 'Nurse'])], data: { title: 'Sample Collection' } },
      { path: 'result-entry', component: ResultEntryComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Result Entry' } },
      { path: 'lab-verification', component: LabVerificationComponent, canActivate: [roleGuard(['Admin', 'LabTechnician', 'Doctor'])], data: { title: 'Lab Verification' } },
      { path: 'doctor-lab-reports', component: DoctorLabReportComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'LabTechnician'])], data: { title: 'Lab Reports' } },
      { path: 'lab/dashboard', component: LabDashboardComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Lab Report Dashboard' } },
      { path: 'lab/test-entry', component: LabTestEntryComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Test Result Entry' } },
      { path: 'lab/reports', component: LabReportsListComponent, canActivate: [roleGuard(['Admin', 'LabTechnician', 'Doctor'])], data: { title: 'Lab Reports' } },
      { path: 'lab/report/:id', component: LabReportPreviewComponent, canActivate: [roleGuard(['Admin', 'LabTechnician', 'Doctor'])], data: { title: 'Lab Report Preview' } },
      { path: 'lab/reference-ranges', component: ReferenceRangeManagementComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Reference Range Management' } },

      // ============ Pharmacy Module ============
      { path: 'pharmacy-dashboard', component: PharmacyDashboardComponent, canActivate: [roleGuard(['Admin', 'Pharmacist', 'InventoryManager'])], data: { title: 'Pharmacy Dashboard' } },
      { path: 'suppliers', component: SupplierComponent, canActivate: [roleGuard(['Admin', 'Pharmacist', 'InventoryManager'])], data: { title: 'Suppliers' } },
      { path: 'medicine-stock', component: MedicineStockComponent, canActivate: [roleGuard(['Admin', 'Pharmacist', 'InventoryManager'])], data: { title: 'Medicine Stock' } },
      { path: 'purchase-orders', component: PurchaseOrderComponent, canActivate: [roleGuard(['Admin', 'Pharmacist', 'InventoryManager'])], data: { title: 'Purchase Orders' } },
      { path: 'pharmacy-sale', component: PharmacySaleComponent, canActivate: [roleGuard(['Admin', 'Pharmacist'])], data: { title: 'Pharmacy Sale' } },
      { path: 'pharmacy-reports', component: PharmacyReportComponent, canActivate: [roleGuard(['Admin', 'Pharmacist', 'InventoryManager'])], data: { title: 'Pharmacy Reports' } },

      // ============ Billing Module ============
      { path: 'billing-dashboard', component: BillingDashboardComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Billing Dashboard' } },
      { path: 'patient-billing', component: PatientBillingComponent, canActivate: [roleGuard(['Admin', 'BillingClerk', 'Receptionist'])], data: { title: 'Patient Billing' } },
      { path: 'payments', component: PaymentModuleComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Payments' } },
      { path: 'invoices', component: InvoiceComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Invoices' } },
      { path: 'refunds', component: RefundManagementComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Refunds' } },
      { path: 'insurance', component: InsuranceComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Insurance' } },
      { path: 'billing-reports', component: BillingReportsComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Billing Reports' } },

      // ============ Emergency Module ============
      { path: 'emergency/dashboard', component: EmergencyDashboardComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse', 'Receptionist'])], data: { title: 'Emergency Dashboard' } },
      { path: 'emergency/registration', component: EmergencyRegistrationComponent, canActivate: [roleGuard(['Admin', 'Receptionist', 'Nurse'])], data: { title: 'Emergency Registration' } },
      { path: 'emergency/triage', component: EmergencyTriageComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse'])], data: { title: 'Triage' } },
      { path: 'emergency/assignments', component: EmergencyDoctorAssignmentComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse'])], data: { title: 'Doctor Assignments' } },
      { path: 'emergency/beds', component: EmergencyBedComponent, canActivate: [roleGuard(['Admin', 'WardManager', 'Nurse'])], data: { title: 'Emergency Beds' } },
      { path: 'emergency/ambulances', component: EmergencyAmbulanceComponent, canActivate: [roleGuard(['Admin', 'WardManager', 'Nurse'])], data: { title: 'Ambulances' } },
      { path: 'emergency/medicine', component: EmergencyMedicineComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse', 'Pharmacist'])], data: { title: 'Emergency Medicine' } },
      { path: 'emergency/lab-orders', component: EmergencyLabComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse', 'LabTechnician'])], data: { title: 'Emergency Lab Orders' } },
      { path: 'emergency/billing', component: EmergencyBillingComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Emergency Billing' } },
      { path: 'emergency/timeline', component: EmergencyTimelineComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse'])], data: { title: 'Emergency Timeline' } },
      { path: 'emergency/status-board', component: EmergencyStatusBoardComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse', 'Receptionist'])], data: { title: 'Emergency Status Board' } },
      { path: 'emergency', redirectTo: 'emergency/dashboard', pathMatch: 'full' },

      // ============ Reports & Analytics ============
      { path: 'reports', component: ReportsDashboardComponent, canActivate: [roleGuard(['Admin'])], data: { title: 'Reports Dashboard' } },
      { path: 'reports/patient', component: PatientReportsComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse'])], data: { title: 'Patient Reports' } },
      { path: 'reports/appointment', component: AppointmentReportsComponent, canActivate: [roleGuard(['Admin', 'Receptionist'])], data: { title: 'Appointment Reports' } },
      { path: 'reports/doctor', component: DoctorReportsComponent, canActivate: [roleGuard(['Admin'])], data: { title: 'Doctor Reports' } },
      { path: 'reports/revenue', component: RevenueReportsComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Revenue Reports' } },
      { path: 'reports/lab', component: LabReportsComponent, canActivate: [roleGuard(['Admin', 'LabTechnician'])], data: { title: 'Lab Reports' } },
      { path: 'reports/pharmacy', component: PharmacyReportsComponent, canActivate: [roleGuard(['Admin', 'Pharmacist'])], data: { title: 'Pharmacy Reports' } },
      { path: 'reports/bed', component: BedReportsComponent, canActivate: [roleGuard(['Admin', 'WardManager'])], data: { title: 'Bed Reports' } },
      { path: 'reports/emergency', component: EmergencyReportsComponent, canActivate: [roleGuard(['Admin', 'Doctor'])], data: { title: 'Emergency Reports' } },

      // ============ Dietary & Nutrition Module ============
      { path: 'dietary/dashboard', component: DietaryDashboardComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Dietician', 'Nurse'])], data: { title: 'Dietary Dashboard' } },
      { path: 'dietary/patient-diet', component: PatientDietComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Nurse', 'Dietician'])], data: { title: 'Patient Diet' } },
      { path: 'dietary/diet-plans', component: DietPlanComponent, canActivate: [roleGuard(['Admin', 'Dietician'])], data: { title: 'Diet Plans' } },
      { path: 'dietary/dieticians', component: DieticianManagementComponent, canActivate: [roleGuard(['Admin'])], data: { title: 'Dietician Management' } },
      { path: 'dietary/meal-schedule', component: MealScheduleComponent, canActivate: [roleGuard(['Admin', 'Dietician', 'Nurse', 'WardManager'])], data: { title: 'Meal Schedule' } },
      { path: 'dietary/kitchen-dashboard', component: KitchenDashboardComponent, canActivate: [roleGuard(['Admin', 'Nurse', 'WardManager'])], data: { title: 'Kitchen Dashboard' } },
      { path: 'dietary/kitchen-orders', component: KitchenOrdersComponent, canActivate: [roleGuard(['Admin', 'Nurse', 'WardManager'])], data: { title: 'Kitchen Orders' } },
      { path: 'dietary/nutrition-analytics', component: NutritionAnalyticsComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Nutrition Analytics' } },
      { path: 'dietary/diet-reports', component: DietReportsComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Diet Reports' } },
      { path: 'dietary/diet-alerts', component: DietAlertsComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Dietician', 'Nurse'])], data: { title: 'Diet Alerts' } },
      { path: 'dietary/diet-history', component: DietHistoryComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'Dietician'])], data: { title: 'Diet History' } },

      // ============ Surgery Module ============
      { path: 'surgery/dashboard', component: SurgeryDashboardComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'BillingClerk', 'Nurse'])], data: { title: 'Surgery Dashboard' } },
      { path: 'surgery/list', component: SurgeryListComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'BillingClerk', 'Nurse'])], data: { title: 'All Surgeries' } },
      { path: 'surgery/register', component: SurgeryFormComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'BillingClerk'])], data: { title: 'Register Surgery' } },
      { path: 'surgery/edit/:id', component: SurgeryFormComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'BillingClerk'])], data: { title: 'Edit Surgery' } },
      { path: 'surgery/details/:id', component: SurgeryDetailsComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'BillingClerk', 'Nurse'])], data: { title: 'Surgery Details' } },
      { path: 'surgery/invoice/:id', component: SurgeryInvoicePreviewComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Surgery Invoice' } },
      { path: 'surgery/schedule', component: OtScheduleComponent, canActivate: [roleGuard(['Admin', 'Doctor', 'BillingClerk', 'Nurse'])], data: { title: 'OT Schedule' } },
      { path: 'surgery/rate-management', component: SurgeryRateManagementComponent, canActivate: [roleGuard(['Admin', 'BillingClerk'])], data: { title: 'Surgery Rate Management' } },
      { path: 'surgery', redirectTo: 'surgery/dashboard', pathMatch: 'full' },
    ]
  },

  // ============ Default ============
  { path: '**', redirectTo: '' },
];
