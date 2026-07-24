import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { NavGroup } from '../models/nav-item.model';

@Injectable({ providedIn: 'root' })
export class NavigationService {

  constructor(private storage: StorageService) {}

  getNavGroups(): NavGroup[] {
    const role = this.storage.getRole();
    switch (role) {
      case 'Admin':
        return this.adminNav();
      case 'Doctor':
        return this.doctorNav();
      case 'Nurse':
        return this.nurseNav();
      case 'OfficeStaff':
        return this.officeStaffNav();
      case 'Receptionist':
        return this.receptionistNav();
      case 'Pharmacist':
        return this.pharmacistNav();
      case 'LabTechnician':
        return this.labTechnicianNav();
      case 'BillingClerk':
        return this.billingClerkNav();
      case 'InventoryManager':
        return this.inventoryManagerNav();
      case 'WardManager':
        return this.wardManagerNav();
      default:
        return [];
    }
  }

  getDashboardRoute(): string {
    const role = this.storage.getRole();
    const map: Record<string, string> = {
      Admin: '/admin-dashboard',
      Doctor: '/doctor-dashboard',
      Nurse: '/nurse-dashboard',
      OfficeStaff: '/office-staff-dashboard',
      Receptionist: '/receptionist-dashboard',
      Pharmacist: '/pharmacist-dashboard',
      LabTechnician: '/lab-technician-dashboard',
      BillingClerk: '/billing-clerk-dashboard',
      InventoryManager: '/inventory-manager-dashboard',
      WardManager: '/ward-manager-dashboard',
    };
    return map[role ?? ''] ?? '/login';
  }

  private adminNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/admin-dashboard', section: 'dashboard' },
          { label: 'Reports', icon: 'Ⓡ', route: '/reports', section: 'reports' },
        ],
      },
      {
        label: 'Patients & Admissions',
        items: [
          { label: 'Patient', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
          { label: 'Admitted Patient', icon: 'Ⓐ', route: '/admission-list', section: 'admission' },
          { label: 'Appointment', icon: 'Ⓒ', route: '/appointment-list', section: 'appointment' },
          { label: 'Schedule Slot', icon: 'Ⓢ', route: '/schedule-slot', section: 'schedule' },
        ],
      },
      {
        label: 'Beds & Wards',
        items: [
          { label: 'Bed Management', icon: 'Ⓑ', route: '/bed-management', section: 'bed' },
          { label: 'Ward', icon: 'Ⓦ', route: '/ward-list', section: 'ward' },
          { label: 'Department', icon: 'Ⓓ', route: '/doctor-departments', section: 'department' },
        ],
      },
      {
        label: 'Staff',
        items: [
          { label: 'Doctor', icon: 'Ⓓ', route: '/doctor', section: 'doctor' },
          { label: 'Nurse', icon: 'Ⓝ', route: '/nurses', section: 'nurse' },
          { label: 'Office Staff', icon: 'Ⓞ', route: '/office-staff', section: 'officestaff' },
        ],
      },
      {
        label: 'Pharmacy & Medicine',
        items: [
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
          { label: 'Generic', icon: 'Ⓖ', route: '/generic-list', section: 'generic' },
          { label: 'Prescription', icon: 'Ⓟ', route: '/prescriptions', section: 'prescription' },
        ],
      },
      {
        label: 'Laboratory & Tests',
        items: [
          { label: 'Tests', icon: 'Ⓣ', route: '/test-list', section: 'tests' },
        ],
      },
      {
        label: 'Accounts & Billing',
        items: [
          { label: 'Admission', icon: 'Ⓐ', route: '/admission', section: 'admissionform' },
        ],
      },
    ];
  }

  private doctorNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/doctor-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Appointments',
        items: [
          { label: 'My Appointments', icon: 'Ⓒ', route: '/appointment-list', section: 'appointment' },
          { label: 'Schedule Slot', icon: 'Ⓢ', route: '/schedule-slot', section: 'schedule' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Patient List', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
        ],
      },
      {
        label: 'Pharmacy',
        items: [
          { label: 'Prescriptions', icon: 'Ⓟ', route: '/prescriptions', section: 'prescription' },
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
        ],
      },
    ];
  }

  private nurseNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/nurse-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Patient List', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
          { label: 'Admitted Patient', icon: 'Ⓐ', route: '/admission-list', section: 'admission' },
        ],
      },
      {
        label: 'Wards & Beds',
        items: [
          { label: 'Ward', icon: 'Ⓦ', route: '/ward-list', section: 'ward' },
          { label: 'Bed Management', icon: 'Ⓑ', route: '/bed-management', section: 'bed' },
        ],
      },
      {
        label: 'Pharmacy',
        items: [
          { label: 'Prescriptions', icon: 'Ⓟ', route: '/prescriptions', section: 'prescription' },
        ],
      },
    ];
  }

  private officeStaffNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/office-staff-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Patient', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
          { label: 'Admitted Patient', icon: 'Ⓐ', route: '/admission-list', section: 'admission' },
          { label: 'Admission Form', icon: 'Ⓐ', route: '/admission', section: 'admissionform' },
        ],
      },
      {
        label: 'Appointments',
        items: [
          { label: 'Appointment', icon: 'Ⓒ', route: '/appointment-list', section: 'appointment' },
          { label: 'Schedule Slot', icon: 'Ⓢ', route: '/schedule-slot', section: 'schedule' },
        ],
      },
      {
        label: 'Staff',
        items: [
          { label: 'Doctor', icon: 'Ⓓ', route: '/doctor', section: 'doctor' },
          { label: 'Nurse', icon: 'Ⓝ', route: '/nurses', section: 'nurse' },
        ],
      },
    ];
  }

  private receptionistNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/receptionist-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Patient', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
          { label: 'New Patient', icon: '+', route: '/patient', section: 'newpatient' },
        ],
      },
      {
        label: 'Appointments',
        items: [
          { label: 'Appointment', icon: 'Ⓒ', route: '/appointment-list', section: 'appointment' },
          { label: 'New Appointment', icon: '+', route: '/appointments/create', section: 'newappointment' },
          { label: 'Appointment Slip', icon: 'Ⓢ', route: '/appointment-slip', section: 'slip' },
          { label: 'Schedule Slot', icon: 'Ⓢ', route: '/schedule-slot', section: 'schedule' },
        ],
      },
    ];
  }

  private pharmacistNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/pharmacist-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Pharmacy',
        items: [
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
          { label: 'Add Medicine', icon: '+', route: '/medicines/create', section: 'addmedicine' },
          { label: 'Generic', icon: 'Ⓖ', route: '/generic-list', section: 'generic' },
          { label: 'Add Generic', icon: '+', route: '/generics', section: 'addgeneric' },
        ],
      },
      {
        label: 'Prescriptions',
        items: [
          { label: 'Prescriptions', icon: 'Ⓟ', route: '/prescriptions', section: 'prescription' },
        ],
      },
    ];
  }

  private labTechnicianNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/lab-technician-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Laboratory',
        items: [
          { label: 'Tests', icon: 'Ⓣ', route: '/test-list', section: 'tests' },
          { label: 'Add Test', icon: '+', route: '/tests/create', section: 'addtest' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Patient List', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
        ],
      },
    ];
  }

  private billingClerkNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/billing-clerk-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Billing',
        items: [
          { label: 'Admission', icon: 'Ⓐ', route: '/admission', section: 'admissionform' },
          { label: 'Admission List', icon: 'Ⓛ', route: '/admission-list', section: 'admission' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Patient List', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
        ],
      },
      {
        label: 'Pharmacy',
        items: [
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
        ],
      },
    ];
  }

  private inventoryManagerNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/inventory-manager-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Inventory',
        items: [
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
          { label: 'Add Medicine', icon: '+', route: '/medicines/create', section: 'addmedicine' },
          { label: 'Generic', icon: 'Ⓖ', route: '/generic-list', section: 'generic' },
          { label: 'Add Generic', icon: '+', route: '/generics', section: 'addgeneric' },
        ],
      },
      {
        label: 'Wards & Beds',
        items: [
          { label: 'Ward', icon: 'Ⓦ', route: '/ward-list', section: 'ward' },
          { label: 'Bed Management', icon: 'Ⓑ', route: '/bed-management', section: 'bed' },
        ],
      },
    ];
  }

  private wardManagerNav(): NavGroup[] {
    return [
      {
        label: 'Overview',
        items: [
          { label: 'Dashboard', icon: '◆', route: '/ward-manager-dashboard', section: 'dashboard' },
        ],
      },
      {
        label: 'Wards & Beds',
        items: [
          { label: 'Ward', icon: 'Ⓦ', route: '/ward-list', section: 'ward' },
          { label: 'Ward Management', icon: 'Ⓦ', route: '/ward-management', section: 'wardmanagement' },
          { label: 'Bed Management', icon: 'Ⓑ', route: '/bed-management', section: 'bed' },
        ],
      },
      {
        label: 'Patients',
        items: [
          { label: 'Admitted Patient', icon: 'Ⓐ', route: '/admission-list', section: 'admission' },
          { label: 'Patient List', icon: 'Ⓟ', route: '/patient-list', section: 'patient' },
        ],
      },
      {
        label: 'Staff',
        items: [
          { label: 'Nurse', icon: 'Ⓝ', route: '/nurses', section: 'nurse' },
        ],
      },
    ];
  }
}
