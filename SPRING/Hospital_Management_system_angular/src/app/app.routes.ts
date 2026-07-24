import { Routes } from '@angular/router';

import { Doctor } from './components/feature/doctors/doctor/doctor';
import { DoctorList } from './components/feature/doctors/doctor-list/doctor-list';
import { DoctorDepartment } from './components/feature/doctors/doctor-department/doctor-department';
import { Nurse } from './components/feature/nurse/nurse';
import { NurseList } from './components/feature/nurse-list/nurse-list';
import { OfficeStaffComponent } from './components/feature/office-staff.component/office-staff.component';
import { OfficeStaffList } from './components/feature/office-staff-list.component/office-staff-list.component';
import { ScheduleSlotComponent } from './components/feature/schedule-slot.component/schedule-slot.component';
import { AppointmentList } from './components/feature/appointment-list.component/appointment-list.component';
import { AppointmentComponent } from './components/feature/appointment.component/appointment.component';
import { AppointmentSlip } from './components/feature/appointment-slip/appointment-slip';
import { MedicineComponent } from './components/feature/medicine.component/medicine.component';
import { MedicineListComponent } from './components/feature/medicine-list.component/medicine-list.component';
import { Generic } from './components/feature/generic/generic';
import { GenericListComponent } from './components/feature/generic-list/generic-list';
import { PrescriptionComponent } from './components/feature/prescription/prescription';
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
  { path: 'prescriptions', component: PrescriptionComponent },
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

  // ============ Default ============
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' },
];
