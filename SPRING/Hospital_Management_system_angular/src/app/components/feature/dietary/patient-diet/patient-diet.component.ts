import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PatientService } from '../../../../services/patient.service';
import { DietAssignmentService } from '../../../../services/dietary/diet-assignment.service';
import { DietPlanService } from '../../../../services/dietary/diet-plan.service';
import { DietHistoryService } from '../../../../services/dietary/diet-history.service';
import { PatientDietAlertService } from '../../../../services/dietary/patient-diet-alert.service';
import { forkJoin } from 'rxjs';

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
  loading = true;
  searchTerm = '';
  filterWard = '';
  filterStatus = '';

  showModal = false;
  showDetailModal = false;
  showAlertModal = false;
  selectedPatient: any = null;
  patientAssignments: any[] = [];
  patientAlerts: any[] = [];
  patientHistory: any[] = [];

  assignmentForm: any = {
    patientId: null,
    dietPlanId: null,
    startDate: '',
    endDate: '',
    status: 'ACTIVE',
    reason: '',
    specialInstructions: '',
    targetCalories: null,
    targetWeight: null
  };

  alertForm: any = {
    patientId: null,
    alertType: 'FOOD_ALLERGY',
    description: '',
    severity: 'MEDIUM',
    allergenName: '',
    specialInstructions: ''
  };

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
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.msg = '';
    forkJoin({
      patients: this.patientService.getAll(),
      plans: this.dietPlanService.getActive(),
      assignments: this.assignmentService.getAll()
    }).subscribe({
      next: (result) => {
        this.patients = result.patients || [];
        this.filteredPatients = [...this.patients];
        this.dietPlans = result.plans || [];
        this.assignments = result.assignments || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load diet data:', err);
        this.msg = 'Failed to load patient diet data. Please try again.';
        this.msgType = 'error';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  filterPatients(): void {
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
      result = result.filter(p => p.ward?.name === this.filterWard);
    }
    this.filteredPatients = result;
  }

  getAssignmentForPatient(patientId: number): any {
    return this.assignments.find(a => a.patient?.id === patientId && a.status === 'ACTIVE');
  }

  getPatientInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
  }

  openAssignDiet(patient: any): void {
    this.selectedPatient = patient;
    this.assignmentForm = {
      patientId: patient.id,
      dietPlanId: null,
      startDate: new Date().toISOString().split('T')[0],
      endDate: '',
      status: 'ACTIVE',
      reason: '',
      specialInstructions: '',
      targetCalories: null,
      targetWeight: null
    };
    this.showModal = true;
    this.msg = '';
  }

  saveAssignment(): void {
    if (!this.assignmentForm.dietPlanId) {
      this.msg = 'Please select a diet plan';
      this.msgType = 'error';
      this.cdr.detectChanges();
      return;
    }
    this.assignmentService.create(this.assignmentForm).subscribe({
      next: () => {
        this.showModal = false;
        this.msg = 'Diet assigned successfully';
        this.msgType = 'success';
        this.loadData();
      },
      error: (err) => {
        console.error('Failed to assign diet:', err);
        this.msg = 'Failed to assign diet. Please try again.';
        this.msgType = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  viewPatientDiet(patient: any): void {
    this.selectedPatient = patient;
    this.patientAssignments = this.assignments.filter(a => a.patient?.id === patient.id);
    this.historyService.getByPatientId(patient.id).subscribe({
      next: (history) => { this.patientHistory = history || []; },
      error: () => { this.patientHistory = []; }
    });
    this.showDetailModal = true;
    this.msg = '';
  }

  openAddAlert(patient: any): void {
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
  }

  saveAlert(): void {
    if (!this.alertForm.description) {
      this.msg = 'Please enter a description';
      this.msgType = 'error';
      this.cdr.detectChanges();
      return;
    }
    this.alertService.create(this.alertForm).subscribe({
      next: () => {
        this.showAlertModal = false;
        this.msg = 'Alert created successfully';
        this.msgType = 'success';
        this.loadData();
      },
      error: (err) => {
        console.error('Failed to create alert:', err);
        this.msg = 'Failed to create alert. Please try again.';
        this.msgType = 'error';
        this.cdr.detectChanges();
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
      'Pregnancy': 'badge-info', 'PostSurgery': 'badge-warning', 'Special': 'badge-primary'
    };
    return map[dietType] || 'badge-secondary';
  }

  closeModals(): void {
    this.showModal = false;
    this.showDetailModal = false;
    this.showAlertModal = false;
    this.msg = '';
  }

  getUniqueWards(): string[] {
    const wards = this.patients.map(p => p.ward?.name).filter((w: string) => w);
    return [...new Set(wards)];
  }

  calculateAge(dateOfBirth: string): number {
    if (!dateOfBirth) return 0;
    const birth = new Date(dateOfBirth);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
      age--;
    }
    return age;
  }

  getMeals(plan: any): { time: string; name: string; content: string }[] {
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
}
