import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { SurgeryService } from '../../../../services/surgery/surgery.service';
import { SurgeryReferenceService } from '../../../../services/surgery/surgery-reference.service';
import { PatientService } from '../../../../services/patient.service';
import { DoctorModelService } from '../../../../services/doctor.service';
import { DoctorsDepartmentService } from '../../../../services/doctors-department';
import { AdmissionService } from '../../../../services/admission.service';
import { SurgeryRequest } from '../../../../models/surgery/surgery.model';
import { SurgeryResponse } from '../../../../models/surgery/surgery-response.model';
import { SurgeryCategory } from '../../../../models/surgery/surgery-category.model';
import { SurgeryMaster } from '../../../../models/surgery/surgery-master.model';
import { OperationTheatre } from '../../../../models/surgery/operation-theatre.model';
import { DoctorDiscount } from '../../../../models/surgery/doctor-discount.model';
import { PatientModel } from '../../../../models/patientModel';
import { DoctorModel } from '../../../../models/doctorModel';
import { DoctorDepartmentModel } from '../../../../models/doctorDepartmentModel';
import { AdmissionResponse } from '../../../../models/admission-response.model';

@Component({
  selector: 'app-surgery-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './surgery-form.component.html',
  styleUrl: './surgery-form.component.css'
})
export class SurgeryFormComponent implements OnInit {
  isEdit = false;
  editId: number | null = null;
  loading = false;
  submitting = false;
  msg = '';
  msgType = 'success';

  categories: SurgeryCategory[] = [];
  categoryMasters: SurgeryMaster[] = [];
  masters: SurgeryMaster[] = [];
  theatres: OperationTheatre[] = [];
  discounts: DoctorDiscount[] = [];
  doctors: DoctorModel[] = [];
  departments: DoctorDepartmentModel[] = [];
  admissions: AdmissionResponse[] = [];

  patientResults: PatientModel[] = [];
  showPatientResults = false;
  patientSearchTerm = '';

  selectedMaster: SurgeryMaster | null = null;
  effectiveDiscountPercent: number | null = null;

  surgery: SurgeryRequest = {
    patientId: 0,
    admittedPatientId: null,
    surgeonId: 0,
    assistantSurgeonId: null,
    anesthesiologistId: null,
    departmentId: null,
    categoryId: null,
    surgeryMasterId: null,
    operationTheatreId: null,
    surgeryDate: new Date().toISOString().slice(0, 10),
    startTime: '09:00',
    endTime: '',
    estimatedDurationMin: 60,
    priority: 'ELECTIVE',
    anesthesiaType: '',
    clinicalNotes: '',
    preOperativeDiagnosis: '',
    postOperativeDiagnosis: '',
    status: 'SCHEDULED',
    surgeryCharge: 0,
    otCharge: 0,
    surgeonFee: 0,
    assistantSurgeonFee: 0,
    anesthesiaFee: 0,
    nursingCharge: 0,
    equipmentCharge: 0,
    consumableCharge: 0,
    icuCharge: 0,
    wardCabinCharge: 0,
    medicineCharge: 0,
    laboratoryCharge: 0,
    radiologyCharge: 0,
    discountPercent: null,
    vatRate: 5,
    insuranceCoverage: 0,
    advancePaid: 0,
    cancellationReason: ''
  };

  priorities = ['ELECTIVE', 'URGENT', 'EMERGENCY'];
  anesthesiaTypes = ['GENERAL', 'SPINAL', 'EPIDURAL', 'LOCAL', 'MAC', 'REGIONAL', 'IV_SEDATION', 'OTHER'];

  chargeFields = [
    { key: 'surgeryCharge', label: 'Surgery Charge' },
    { key: 'otCharge', label: 'OT Charge' },
    { key: 'surgeonFee', label: 'Surgeon Fee' },
    { key: 'assistantSurgeonFee', label: 'Assistant Surgeon Fee' },
    { key: 'anesthesiaFee', label: 'Anesthesia Fee' },
    { key: 'nursingCharge', label: 'Nursing Charge' },
    { key: 'equipmentCharge', label: 'Equipment Charge' },
    { key: 'consumableCharge', label: 'Consumable Charge' },
    { key: 'icuCharge', label: 'ICU Charge' },
    { key: 'wardCabinCharge', label: 'Ward / Cabin Charge' },
    { key: 'medicineCharge', label: 'Medicine Charge' },
    { key: 'laboratoryCharge', label: 'Laboratory Charge' },
    { key: 'radiologyCharge', label: 'Radiology Charge' }
  ];

  get selectedPatient(): PatientModel | null {
    if (!this.surgery.patientId) return null;
    const found = this.patientResults.find(p => p.id === this.surgery.patientId);
    if (found) return found;
    return null;
  }

  constructor(
    private surgeryService: SurgeryService,
    private refService: SurgeryReferenceService,
    private patientService: PatientService,
    private doctorService: DoctorModelService,
    private deptService: DoctorsDepartmentService,
    private admissionService: AdmissionService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEdit = true;
        this.editId = Number(id);
        this.loadSurgery(this.editId);
      }
    });
    this.loadReferences();
  }

  loadReferences(): void {
    this.refService.getActiveCategories().subscribe(res => { this.categories = res; this.cdr.detectChanges(); });
    this.refService.getMasters().subscribe(res => { this.masters = res; this.cdr.detectChanges(); });
    this.refService.getActiveTheatres().subscribe(res => { this.theatres = res; this.cdr.detectChanges(); });
    this.refService.getActiveDiscounts().subscribe(res => { this.discounts = res; this.cdr.detectChanges(); });
    this.doctorService.getAll().subscribe(res => { this.doctors = res.filter(d => d.status !== 'INACTIVE'); this.cdr.detectChanges(); });
    this.deptService.getAllDepartments().subscribe(res => { this.departments = res; this.cdr.detectChanges(); });
    this.admissionService.getActive().subscribe(res => { this.admissions = res; this.cdr.detectChanges(); });
  }

  loadSurgery(id: number): void {
    this.loading = true;
    this.surgeryService.getById(id).subscribe({
      next: (s) => {
        this.surgery = {
          patientId: s.patientId,
          admittedPatientId: s.admittedPatientId ?? null,
          surgeonId: s.surgeonId ?? 0,
          assistantSurgeonId: s.assistantSurgeonId ?? null,
          anesthesiologistId: s.anesthesiologistId ?? null,
          departmentId: s.departmentId ?? null,
          categoryId: s.categoryId ?? null,
          surgeryMasterId: s.surgeryMasterId ?? null,
          operationTheatreId: s.operationTheatreId ?? null,
          surgeryDate: s.surgeryDate,
          startTime: s.startTime || '',
          endTime: s.endTime || '',
          estimatedDurationMin: s.estimatedDurationMin || 60,
          priority: s.priority || 'ELECTIVE',
          anesthesiaType: s.anesthesiaType || '',
          clinicalNotes: s.clinicalNotes || '',
          preOperativeDiagnosis: s.preOperativeDiagnosis || '',
          postOperativeDiagnosis: s.postOperativeDiagnosis || '',
          status: s.status || 'SCHEDULED',
          surgeryCharge: s.surgeryCharge || 0,
          otCharge: s.otCharge || 0,
          surgeonFee: s.surgeonFee || 0,
          assistantSurgeonFee: s.assistantSurgeonFee || 0,
          anesthesiaFee: s.anesthesiaFee || 0,
          nursingCharge: s.nursingCharge || 0,
          equipmentCharge: s.equipmentCharge || 0,
          consumableCharge: s.consumableCharge || 0,
          icuCharge: s.icuCharge || 0,
          wardCabinCharge: s.wardCabinCharge || 0,
          medicineCharge: s.medicineCharge || 0,
          laboratoryCharge: s.laboratoryCharge || 0,
          radiologyCharge: s.radiologyCharge || 0,
          discountPercent: s.discountPercent ?? null,
          vatRate: s.vatRate ?? 5,
          insuranceCoverage: s.insuranceCoverage || 0,
          advancePaid: s.advancePaid || 0,
          cancellationReason: s.cancellationReason || ''
        };
        if (s.patientId) {
          this.patientService.getById(s.patientId).subscribe(p => {
            this.patientResults = [p];
            this.patientSearchTerm = `${p.patientCode} — ${p.name}`;
            this.cdr.detectChanges();
          });
        }
        if (s.categoryId) this.onCategoryChange();
        if (s.surgeonId) this.onSurgeonChange();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showMsg(err?.error?.message || 'Failed to load surgery', 'error');
        this.loading = false;
      }
    });
  }

  searchPatients(): void {
    const q = this.patientSearchTerm.trim();
    if (!q) {
      this.patientResults = [];
      this.showPatientResults = false;
      return;
    }
    this.patientService.search(q).subscribe(res => {
      this.patientResults = res;
      this.showPatientResults = true;
      this.cdr.detectChanges();
    });
  }

  selectPatient(p: PatientModel): void {
    this.surgery.patientId = p.id!;
    this.patientSearchTerm = `${p.patientCode} — ${p.name}`;
    this.showPatientResults = false;
    const activeAdmission = this.admissions.find(a => a.patientId === p.id);
    if (activeAdmission && !this.isEdit) {
      this.surgery.admittedPatientId = activeAdmission.admissionId;
    }
  }

  clearPatient(): void {
    this.surgery.patientId = 0;
    this.surgery.admittedPatientId = null;
    this.patientSearchTerm = '';
    this.patientResults = [];
  }

  onCategoryChange(): void {
    this.categoryMasters = this.masters.filter(m => m.categoryId === this.surgery.categoryId && m.active !== false);
    this.surgery.surgeryMasterId = null;
    this.selectedMaster = null;
  }

  onMasterChange(): void {
    const m = this.masters.find(x => x.id === this.surgery.surgeryMasterId);
    this.selectedMaster = m || null;
    if (m) {
      this.surgery.surgeryCharge = m.standardRate;
      this.surgery.otCharge = m.otCharge ?? this.surgery.otCharge;
      this.surgery.anesthesiaFee = m.anesthesiaCharge ?? this.surgery.anesthesiaFee;
      this.surgery.nursingCharge = m.nursingCharge ?? this.surgery.nursingCharge;
      this.surgery.equipmentCharge = m.equipmentCharge ?? this.surgery.equipmentCharge;
      this.surgery.consumableCharge = m.consumableCharge ?? this.surgery.consumableCharge;
      this.surgery.icuCharge = m.icuCharge ?? this.surgery.icuCharge;
      if (m.estimatedDurationMin) this.surgery.estimatedDurationMin = m.estimatedDurationMin;
    }
  }

  onSurgeonChange(): void {
    const d = this.discounts.find(x => x.doctorId === this.surgery.surgeonId);
    if (d) {
      this.effectiveDiscountPercent = this.totalDiscountFor(d);
      this.surgery.discountPercent = this.surgery.discountPercent ?? this.effectiveDiscountPercent;
    } else {
      this.effectiveDiscountPercent = null;
    }
  }

  totalDiscountFor(d: DoctorDiscount): number {
    return d.effectiveDiscountPercent ??
      ((d.percentageDiscount || 0) + (d.departmentDiscount || 0) + (d.specialPromoDiscount || 0));
  }

  getActiveAdmissionsForSelectedPatient(): AdmissionResponse[] {
    if (!this.surgery.patientId) return [];
    return this.admissions.filter(a => a.patientId === this.surgery.patientId);
  }

  // ===== Live totals =====
  get subtotal(): number {
    return this.chargeFields.reduce((sum, f) => sum + (Number(this.surgery[f.key as keyof SurgeryRequest]) || 0), 0);
  }

  get discountAmount(): number {
    return this.subtotal * (Number(this.surgery.discountPercent) || 0) / 100;
  }

  get vatAmount(): number {
    return this.subtotal * (Number(this.surgery.vatRate) || 0) / 100;
  }

  get totalAmount(): number {
    return this.subtotal - this.discountAmount + this.vatAmount;
  }

  get finalPayable(): number {
    return Math.max(0, this.totalAmount - (Number(this.surgery.insuranceCoverage) || 0) - (Number(this.surgery.advancePaid) || 0));
  }

  formatCurrency(amount: number): string {
    return '৳' + amount.toLocaleString('en-BD', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  toNumber(v: any): number {
    return Number(v) || 0;
  }

  chargeValue(key: string): number {
    return Number((this.surgery as any)[key]) || 0;
  }

  setChargeValue(key: string, value: any): void {
    (this.surgery as any)[key] = Number(value) || 0;
  }

  submit(): void {
    if (!this.surgery.patientId) { this.showMsg('Please select a patient', 'error'); return; }
    if (!this.surgery.surgeonId) { this.showMsg('Please select a surgeon', 'error'); return; }
    if (!this.surgery.surgeryDate) { this.showMsg('Please select a surgery date', 'error'); return; }
    if (!this.surgery.operationTheatreId) { this.showMsg('Please select an operation theatre', 'error'); return; }

    this.submitting = true;
    const payload: SurgeryRequest = {
      ...this.surgery,
      discountPercent: this.surgery.discountPercent === null ? null : Number(this.surgery.discountPercent),
      vatRate: Number(this.surgery.vatRate) || 0,
      insuranceCoverage: Number(this.surgery.insuranceCoverage) || 0,
      advancePaid: Number(this.surgery.advancePaid) || 0,
      estimatedDurationMin: Number(this.surgery.estimatedDurationMin) || 60
    };

    const request = this.isEdit && this.editId
      ? this.surgeryService.update(this.editId, payload)
      : this.surgeryService.create(payload);

    request.subscribe({
      next: (res) => {
        this.submitting = false;
        this.showMsg(this.isEdit ? `Surgery ${res.surgeryNumber} updated` : `Surgery ${res.surgeryNumber} registered`, 'success');
        setTimeout(() => this.router.navigate(['/surgery/details', res.id]), 1200);
      },
      error: (err) => {
        this.submitting = false;
        this.showMsg(err?.error?.message || 'Failed to save surgery', 'error');
      }
    });
  }

  showMsg(text: string, type: string): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; this.cdr.detectChanges(); }, 4000);
  }
}
