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
          { label: 'Pharmacy Dashboard', icon: '📊', route: '/pharmacy-dashboard', section: 'pharmacydashboard' },
          { label: 'Suppliers', icon: 'S', route: '/suppliers', section: 'suppliers' },
          { label: 'Medicine Stock', icon: '📦', route: '/medicine-stock', section: 'medicinestock' },
          { label: 'Purchase Orders', icon: '🛒', route: '/purchase-orders', section: 'purchaseorders' },
          { label: 'Pharmacy Sale', icon: '💊', route: '/pharmacy-sale', section: 'pharmacysale' },
          { label: 'Pharmacy Reports', icon: '📈', route: '/pharmacy-reports', section: 'pharmacyreports' },
        ],
      },
      {
        label: 'Laboratory & Tests',
        items: [
          { label: 'Test Catalog', icon: 'Ⓣ', route: '/test-list', section: 'tests' },
          { label: 'Lab Reception', icon: 'Ⓡ', route: '/lab-reception', section: 'labreception' },
          { label: 'Sample Collection', icon: 'Ⓢ', route: '/sample-collection', section: 'samplecollection' },
          { label: 'Result Entry', icon: 'Ⓔ', route: '/result-entry', section: 'resultentry' },
          { label: 'Verification', icon: '✓', route: '/lab-verification', section: 'labverification' },
        ],
      },
      {
        label: 'Accounts & Billing',
        items: [
          { label: 'Admission', icon: 'Ⓐ', route: '/admission', section: 'admissionform' },
          { label: 'Billing Dashboard', icon: '💰', route: '/billing-dashboard', section: 'billingdashboard' },
          { label: 'Patient Billing', icon: '📋', route: '/patient-billing', section: 'patientbilling' },
          { label: 'Payments', icon: '💳', route: '/payments', section: 'payments' },
          { label: 'Invoices', icon: '🧾', route: '/invoices', section: 'invoices' },
          { label: 'Refunds', icon: '↩️', route: '/refunds', section: 'refunds' },
           { label: 'Insurance', icon: '🛡️', route: '/insurance', section: 'insurance' },
           { label: 'Billing Reports', icon: '📊', route: '/billing-reports', section: 'billingreports' },
         ],
       },
       {
         label: 'Dietary & Nutrition',
         items: [
           { label: 'Dashboard', icon: '🍽️', route: '/dietary/dashboard', section: 'dietarydashboard' },
           { label: 'Patient Diet', icon: '👤', route: '/dietary/patient-diet', section: 'patientdiet' },
           { label: 'Diet Plans', icon: '📋', route: '/dietary/diet-plans', section: 'dietplans' },
           { label: 'Dieticians', icon: '👨‍⚕️', route: '/dietary/dieticians', section: 'dieticians' },
           { label: 'Meal Schedule', icon: '⏰', route: '/dietary/meal-schedule', section: 'mealschedule' },
           { label: 'Kitchen Dashboard', icon: '🍳', route: '/dietary/kitchen-dashboard', section: 'kitchendashboard' },
           { label: 'Kitchen Orders', icon: '📦', route: '/dietary/kitchen-orders', section: 'kitchenorders' },
           { label: 'Nutrition Analytics', icon: '📊', route: '/dietary/nutrition-analytics', section: 'nutritionanalytics' },
           { label: 'Diet Reports', icon: '📈', route: '/dietary/diet-reports', section: 'dietreports' },
           { label: 'Diet Alerts', icon: '⚠️', route: '/dietary/diet-alerts', section: 'dietalerts' },
           { label: 'Diet History', icon: '📜', route: '/dietary/diet-history', section: 'diethistory' },
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
        label: 'Pharmacy',
        items: [
          { label: 'Prescriptions', icon: 'Ⓟ', route: '/prescriptions', section: 'prescription' },
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
        ],
      },
      {
        label: 'Laboratory',
        items: [
          { label: 'Lab Reports', icon: 'Ⓛ', route: '/doctor-lab-reports', section: 'labreports' },
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
          { label: 'Pharmacy Dashboard', icon: '📊', route: '/pharmacy-dashboard', section: 'pharmacydashboard' },

        { label: 'Reports & Analytics', icon: '📊', route: '/reports', section: 'reports' },

        ],
      },
      {
        label: 'Pharmacy Operations',
        items: [
          { label: 'Pharmacy Sale', icon: '💊', route: '/pharmacy-sale', section: 'pharmacysale' },
          { label: 'Medicine Stock', icon: '📦', route: '/medicine-stock', section: 'medicinestock' },
          { label: 'Suppliers', icon: 'S', route: '/suppliers', section: 'suppliers' },
          { label: 'Purchase Orders', icon: '🛒', route: '/purchase-orders', section: 'purchaseorders' },
        ],
      },
      {
        label: 'Catalog',
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
      {
        label: 'Reports',
        items: [
          { label: 'Pharmacy Reports', icon: '📈', route: '/pharmacy-reports', section: 'pharmacyreports' },
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
          { label: 'Test Catalog', icon: 'Ⓣ', route: '/test-list', section: 'tests' },
          { label: 'Add Test', icon: '+', route: '/tests/create', section: 'addtest' },
        ],
      },
      {
        label: 'Lab Workflow',
        items: [
          { label: 'Lab Reception', icon: 'Ⓡ', route: '/lab-reception', section: 'labreception' },
          { label: 'Sample Collection', icon: 'Ⓢ', route: '/sample-collection', section: 'samplecollection' },
          { label: 'Result Entry', icon: 'Ⓔ', route: '/result-entry', section: 'resultentry' },
          { label: 'Verification', icon: '✓', route: '/lab-verification', section: 'labverification' },
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
          { label: 'Billing Dashboard', icon: '💰', route: '/billing-dashboard', section: 'billingdashboard' },
          { label: 'Reports & Analytics', icon: '📊', route: '/reports', section: 'reports' },
        ],
      },
      {
        label: 'Billing Operations',
        items: [
          { label: 'Patient Billing', icon: '📋', route: '/patient-billing', section: 'patientbilling' },
          { label: 'Payments', icon: '💳', route: '/payments', section: 'payments' },
          { label: 'Invoices', icon: '🧾', route: '/invoices', section: 'invoices' },
          { label: 'Refunds', icon: '↩️', route: '/refunds', section: 'refunds' },
          { label: 'Insurance', icon: '🛡️', route: '/insurance', section: 'insurance' },
        ],
      },
      {
        label: 'Admissions',
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
        label: 'Reports',
        items: [
          { label: 'Billing Reports', icon: '📊', route: '/billing-reports', section: 'billingreports' },
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
          { label: 'Pharmacy Dashboard', icon: '📊', route: '/pharmacy-dashboard', section: 'pharmacydashboard' },
        ],
      },
      {
        label: 'Inventory',
        items: [
          { label: 'Medicine', icon: 'Ⓜ', route: '/medicine-list', section: 'medicine' },
          { label: 'Add Medicine', icon: '+', route: '/medicines/create', section: 'addmedicine' },
          { label: 'Medicine Stock', icon: '📦', route: '/medicine-stock', section: 'medicinestock' },
          { label: 'Generic', icon: 'Ⓖ', route: '/generic-list', section: 'generic' },
          { label: 'Add Generic', icon: '+', route: '/generics', section: 'addgeneric' },
        ],
      },
      {
        label: 'Suppliers & Purchase',
        items: [
          { label: 'Suppliers', icon: 'S', route: '/suppliers', section: 'suppliers' },
          { label: 'Purchase Orders', icon: '🛒', route: '/purchase-orders', section: 'purchaseorders' },
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
        label: 'Reports',
        items: [
          { label: 'Pharmacy Reports', icon: '📈', route: '/pharmacy-reports', section: 'pharmacyreports' },
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
