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
import { authGuard } from './guard/auth-guard-guard';
import { DoctorDashboardComponent } from './components/feature/doctors/doctor-dashboard-component/doctor-dashboard-component';
import { TestMasterComponent } from './components/feature/test-master.component/test-master.component';
import { TestListComponent } from './components/feature/test-list.component/test-list.component';
import { AdmissionComponent } from './components/feature/admission/admission-component/admission-component';
import { AdmissionListComponent } from './components/feature/admission/admission-list.component/admission-list.component';
import { WardManagementComponent } from './components/feature/ward/ward-management.component/ward-management.component';
import { WardComponent } from './components/feature/ward/ward.component/ward.component';
import { WardListComponent } from './components/feature/ward/ward-list.component/ward-list.component';
import { BedManagementComponent } from './components/feature/bed/bed-management.component/bed-management.component';

export const routes: Routes = [

  {
    path: 'doctor-departments',
    component: DoctorDepartment
  },
  {
    path: 'doctor/create',
    component: Doctor
  },
  {
    path: 'doctor/edit/:id',
    component: Doctor
  },
  {
    path: 'doctor',
    component: DoctorList
  },

  {
    path: 'nurses/create',
    component: Nurse
  },

  {
    path: 'nurses',
    component: NurseList
  },

  {
    path: 'office-staff/create',
    component: OfficeStaffComponent
  },
  {
    path: 'office-staff/edit/:id',
    component: OfficeStaffComponent
  },

  {
    path: 'office-staff',
    component: OfficeStaffList
  },

  // Schedule Slot
  {
    path: 'schedule-slot',
    component: ScheduleSlotComponent
  },


  // ===========================
  // Appointment
  // ===========================
  {
    path: 'appointments',
    component: AppointmentComponent
  },
  {
    path: 'appointment-list',
    component: AppointmentList
  },

  {
    path: 'appointments/create',
    component: AppointmentComponent
  },
  {
    path: 'appointments/edit/:id',
    component: AppointmentComponent
  },


  {
    path: 'appointment-slip',
    component: AppointmentSlip
  },

  // Medicine
  {
    path: 'medicines/create',
    component: MedicineComponent
  },

  {
    path: 'medicine-list',
    component: MedicineListComponent
  },

  {
    path: 'medicine/:id',
    component: MedicineComponent
  },

  {
    path: 'generics',
    component: Generic
  },
  {
    path: 'generic-list',
    component: GenericListComponent
  },

  {
    path: 'prescriptions',
    component: PrescriptionComponent
  },
  {
    path: 'prescriptions/edit/:id',
    component: PrescriptionComponent
  },

  {
    path: 'patient',
    component: PatientComponent
  },

  {
    path: 'patient/edit/:id',
    component: PatientComponent
  },

  {
    path: 'patient-list',
    component: PatientListComponent
  },


  //Login

  {
    path: 'login',
    component: LoginComponent
  },


  { path: 'dashboard', component: RoleRedirect },
  { path: 'doctor-dashboard', component: DoctorDashboardComponent },

  {
    path: 'doctor/prescription/:appointmentId',
    component: PrescriptionComponent
  },

  {
    path: 'prescriptions/create/:appointmentId',
    component: PrescriptionComponent
  },
  {
    path: 'prescriptions/edit/:id',
    component: PrescriptionComponent
  },

  // ------TESTS-------

  {
    path: 'tests/create',
    component: TestMasterComponent
  },
  {
    path: 'tests/edit/:id',
    component: TestMasterComponent
  },
  {
    path: 'test-list',
    component: TestListComponent
  },

  // ==========Admission Patient=======
  {
    path: 'admission',
    component: AdmissionComponent
  },
  {
    path: 'admission-list',
    component: AdmissionListComponent
  },


  {
    path: 'ward-management',
    component: WardManagementComponent
  },
  {
    path: 'ward',
    component: WardComponent
  },
  {
    path: 'ward-list',
    component: WardListComponent
  },
  {
    path: 'bed-management',
    component: BedManagementComponent
  },
  {
    path: 'admission-list',
    component: AdmissionListComponent
  }



];
