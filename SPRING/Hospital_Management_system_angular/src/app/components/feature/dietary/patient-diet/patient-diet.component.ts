import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PatientService } from '../../../../services/patient.service';
import { DietAssignmentService } from '../../../../services/dietary/diet-assignment.service';
import { DietPlanService } from '../../../../services/dietary/diet-plan.service';
import { DietHistoryService } from '../../../../services/dietary/diet-history.service';
import { PatientDietAlertService } from '../../../../services/dietary/patient-diet-alert.service';
import { AdmissionService } from '../../../../services/admission.service';
import { forkJoin, catchError, of } from 'rxjs';

@Component({
  selector: 'app-patient-diet',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient-diet.component.html',
  styleUrl: './patient-diet.component.css'
})
export class PatientDietComponent implements OnInit {
  patients: any[] = [];
  filteredPatients: any[] = [];
  assignments: any[] = [];
  dietPlans: any[] = [];
  admissions: any[] = [];
  admissionMap: Map<number, any> = new Map();
  loading = true;
  searchTerm = '';
  filterWard = '';
  filterStatus = '';

  showModal = false;
  showDetailModal = false;
  showAlertModal = false;
  isEditMode = false;
  editingAssignmentId: number | null = null;
  selectedPatient: any = null;
  patientAssignments: any[] = [];
  patientAlerts: any[] = [];
  patientHistory: any[] = [];

  assignmentForm: any = {};
  alertForm: any = {};

  alertTypes = ['DIABETIC', 'LOW_SODIUM', 'ALLERGY', 'NPO', 'FASTING', 'CRITICAL', 'FOOD_ALLERGY', 'KITCHEN_ALERT', 'LATE_DELIVERY'];
  severities = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
  msg = '';
  msgType = '';

  constructor(
    private patientService: PatientService,
    private assignmentService: DietAssignmentService,
    private dietPlanService: DietPlanService,
    private historyService: DietHistoryService,
    private alertService: PatientDietAlertService,
    private admissionService: AdmissionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.msg = '';
    forkJoin({
      patients: this.patientService.getAll().pipe(catchError(() => of([]))),
      plans: this.dietPlanService.getActive().pipe(catchError(() => of([]))),
      assignments: this.assignmentService.getAll().pipe(catchError(() => of([]))),
      admissions: this.admissionService.getAll().pipe(catchError(() => of([])))
    }).subscribe({
      next: (result) => {
        this.patients = Array.isArray(result.patients) ? result.patients : [];
        this.filteredPatients = [...this.patients];
        this.dietPlans = Array.isArray(result.plans) ? result.plans : [];
        this.assignments = Array.isArray(result.assignments) ? result.assignments : [];
        this.admissions = Array.isArray(result.admissions) ? result.admissions : [];

        this.admissionMap.clear();
        for (const adm of this.admissions) {
          if (adm.patientId && adm.status === 'ADMITTED') {
            this.admissionMap.set(adm.patientId, adm);
          }
        }

        this.loading = false;
        this.safeDetectChanges();
      },
      error: () => {
        this.msg = 'Failed to load data. Please refresh.';
        this.msgType = 'error';
        this.loading = false;
        this.safeDetectChanges();
      }
    });
  }

  filterPatients(): void {
    try {
      let result = [...this.patients];
      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase();
        result = result.filter(p =>
          (p.name && p.name.toLowerCase().includes(term)) ||
          (p.patientCode && p.patientCode.toLowerCase().includes(term)) ||
          (p.phone && p.phone.includes(term))
        );
      }
      if (this.filterWard) {
        result = result.filter(p => {
          const adm = this.admissionMap.get(p.id);
          return adm?.wardName === this.filterWard;
        });
      }
      if (this.filterStatus === 'ACTIVE') {
        result = result.filter(p => this.hasActiveAssignment(p.id));
      } else if (this.filterStatus === 'NO_DIET') {
        result = result.filter(p => !this.hasActiveAssignment(p.id));
      }
      this.filteredPatients = result;
    } catch (e) {
      console.error('Filter error:', e);
    }
  }

  hasActiveAssignment(patientId: number): boolean {
    try {
      return this.assignments.some(a => a.patient?.id === patientId && a.status === 'ACTIVE');
    } catch { return false; }
  }

  getAssignmentForPatient(patientId: number): any {
    try {
      return this.assignments.find(a => a.patient?.id === patientId && a.status === 'ACTIVE');
    } catch { return null; }
  }

  getPatientInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
  }

  openAssignDiet(patient: any): void {
    try {
      this.selectedPatient = patient;
      this.isEditMode = false;
      this.editingAssignmentId = null;
      this.assignmentForm = {
        patientId: patient.id,
        dietPlanId: '',
        startDate: new Date().toISOString().split('T')[0],
        endDate: '',
        status: 'ACTIVE',
        reason: '',
        specialInstructions: '',
        targetCalories: null,
        targetWeight: null,
        pricePerDay: null
      };
      this.showModal = true;
      this.msg = '';
      this.safeDetectChanges();
    } catch (e) {
      console.error('Open assign error:', e);
    }
  }

  openUpdateDiet(patient: any): void {
    try {
      const existing = this.getAssignmentForPatient(patient.id);
      if (!existing) {
        this.openAssignDiet(patient);
        return;
      }
      this.selectedPatient = patient;
      this.isEditMode = true;
      this.editingAssignmentId = existing.id;
      this.assignmentForm = {
        patientId: patient.id,
        dietPlanId: existing.dietPlan?.id || '',
        startDate: existing.startDate || new Date().toISOString().split('T')[0],
        endDate: existing.endDate || '',
        status: existing.status || 'ACTIVE',
        reason: existing.reason || '',
        specialInstructions: existing.specialInstructions || '',
        targetCalories: existing.targetCalories || null,
        targetWeight: existing.targetWeight || null,
        pricePerDay: existing.dietPlan?.pricePerDay || null
      };
      this.showModal = true;
      this.msg = '';
      this.safeDetectChanges();
    } catch (e) {
      console.error('Open update error:', e);
    }
  }

  saveAssignment(): void {
    if (!this.assignmentForm.dietPlanId) {
      this.msg = 'Please select a diet plan';
      this.msgType = 'error';
      this.safeDetectChanges();
      return;
    }
    const payload: any = {
      patient: { id: Number(this.assignmentForm.patientId) },
      dietPlan: { id: Number(this.assignmentForm.dietPlanId) },
      startDate: this.assignmentForm.startDate,
      endDate: this.assignmentForm.endDate || null,
      status: this.assignmentForm.status || 'ACTIVE',
      reason: this.assignmentForm.reason || '',
      specialInstructions: this.assignmentForm.specialInstructions || '',
      targetCalories: this.assignmentForm.targetCalories || null,
      targetWeight: this.assignmentForm.targetWeight || null
    };

    const obs = this.isEditMode && this.editingAssignmentId
      ? this.assignmentService.update(this.editingAssignmentId, payload)
      : this.assignmentService.create(payload);

    obs.subscribe({
      next: () => {
        this.showModal = false;
        this.msg = this.isEditMode ? 'Diet updated successfully' : 'Diet assigned successfully';
        this.msgType = 'success';
        this.isEditMode = false;
        this.editingAssignmentId = null;
        this.loadData();
      },
      error: (err) => {
        console.error('Save assignment error:', err);
        this.msg = 'Failed to save. Please try again.';
        this.msgType = 'error';
        this.safeDetectChanges();
      }
    });
  }

  viewPatientDiet(patient: any): void {
    try {
      this.selectedPatient = patient;
      this.patientAssignments = this.assignments.filter(a => a.patient?.id === patient.id);
      this.patientHistory = [];
      this.showDetailModal = true;
      this.msg = '';
      this.safeDetectChanges();

      this.historyService.getByPatientId(patient.id).pipe(
        catchError(() => of([]))
      ).subscribe({
        next: (history) => {
          this.patientHistory = Array.isArray(history) ? history : [];
          this.safeDetectChanges();
        },
        error: () => {
          this.patientHistory = [];
          this.safeDetectChanges();
        }
      });
    } catch (e) {
      console.error('View diet error:', e);
      this.showDetailModal = true;
      this.patientHistory = [];
      this.safeDetectChanges();
    }
  }

  openAddAlert(patient: any): void {
    try {
      this.selectedPatient = patient;
      this.alertForm = {
        patientId: patient.id,
        alertType: 'FOOD_ALLERGY',
        description: '',
        severity: 'MEDIUM',
        allergenName: '',
        specialInstructions: ''
      };
      this.showAlertModal = true;
      this.msg = '';
      this.safeDetectChanges();
    } catch (e) {
      console.error('Open alert error:', e);
    }
  }

  saveAlert(): void {
    if (!this.alertForm.description) {
      this.msg = 'Please enter a description';
      this.msgType = 'error';
      this.safeDetectChanges();
      return;
    }
    const payload: any = {
      patient: { id: Number(this.alertForm.patientId) },
      alertType: this.alertForm.alertType,
      description: this.alertForm.description,
      severity: this.alertForm.severity,
      status: 'ACTIVE',
      allergenName: this.alertForm.allergenName || '',
      specialInstructions: this.alertForm.specialInstructions || '',
      createdBy: 'Admin'
    };
    this.alertService.create(payload).subscribe({
      next: () => {
        this.showAlertModal = false;
        this.msg = 'Alert created successfully';
        this.msgType = 'success';
        this.loadData();
      },
      error: (err) => {
        console.error('Save alert error:', err);
        this.msg = 'Failed to create alert.';
        this.msgType = 'error';
        this.safeDetectChanges();
      }
    });
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'ACTIVE': 'badge-success', 'COMPLETED': 'badge-info',
      'CANCELLED': 'badge-danger', 'ON_HOLD': 'badge-warning'
    };
    return map[status] || 'badge-secondary';
  }

  getDietTypeBadgeClass(dietType: string): string {
    const map: Record<string, string> = {
      'Diabetic': 'badge-danger', 'LowSalt': 'badge-info', 'LowFat': 'badge-warning',
      'Cardiac': 'badge-danger', 'HighProtein': 'badge-primary', 'Liquid': 'badge-info',
      'Soft': 'badge-secondary', 'Renal': 'badge-warning', 'Pediatric': 'badge-success',
      'Pregnancy': 'badge-info', 'PostSurgery': 'badge-warning', 'Special': 'badge-primary',
      'Regular': 'badge-secondary'
    };
    return map[dietType] || 'badge-secondary';
  }

  closeModals(): void {
    this.showModal = false;
    this.showDetailModal = false;
    this.showAlertModal = false;
    this.isEditMode = false;
    this.editingAssignmentId = null;
    this.msg = '';
  }

  getUniqueWards(): string[] {
    const wards = this.patients.map(p => {
      const adm = this.admissionMap.get(p.id);
      return adm?.wardName;
    }).filter((w: any) => w);
    return [...new Set(wards)] as string[];
  }

  getPatientWard(patientId: number): string {
    const adm = this.admissionMap.get(patientId);
    return adm?.wardName || '-';
  }

  getPatientBed(patientId: number): string {
    const adm = this.admissionMap.get(patientId);
    return adm?.assignedBedNumber || '-';
  }

  calculateAge(dateOfBirth: string): number {
    if (!dateOfBirth) return 0;
    try {
      const birth = new Date(dateOfBirth);
      const today = new Date();
      let age = today.getFullYear() - birth.getFullYear();
      const monthDiff = today.getMonth() - birth.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
      }
      return age;
    } catch { return 0; }
  }

  getMeals(plan: any): { time: string; name: string; content: string }[] {
    if (!plan) return [];
    return [
      { time: plan.breakfastTime || '07:30', name: 'Breakfast', content: plan.breakfast || '-' },
      { time: plan.morningSnacksTime || '10:00', name: 'Morning Snacks', content: plan.morningSnacks || '-' },
      { time: plan.lunchTime || '12:30', name: 'Lunch', content: plan.lunch || '-' },
      { time: plan.eveningSnacksTime || '16:00', name: 'Evening Snacks', content: plan.eveningSnacks || '-' },
      { time: plan.dinnerTime || '19:00', name: 'Dinner', content: plan.dinner || '-' },
      { time: plan.nightDietTime || '21:00', name: 'Night Diet', content: plan.nightDiet || '-' }
    ];
  }

  getActionClass(action: string): string {
    const map: Record<string, string> = {
      'ASSIGNED': 'badge-success', 'UPDATED': 'badge-info', 'CANCELLED': 'badge-danger',
      'DOCTOR_RECOMMENDATION': 'badge-primary', 'DIETICIAN_RECOMMENDATION': 'badge-warning',
      'MEAL_CHANGED': 'badge-secondary'
    };
    return map[action] || 'badge-secondary';
  }

  trackById(index: number, item: any): number {
    return item.id || index;
  }

  private safeDetectChanges(): void {
    try { this.cdr.detectChanges(); } catch (e) { console.error('Change detection error:', e); }
  }
}
