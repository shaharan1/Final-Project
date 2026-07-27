import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { WardModel } from '../../../../models/ward.model';
import { BedModel } from '../../../../models/bed.model';
import { PatientModel } from '../../../../models/patientModel';
import { DoctorModel } from '../../../../models/doctorModel';
import { AdmissionResponse } from '../../../../models/admission-response.model';
import { AdmissionRequest } from '../../../../models/admission.model';
import { FacilityModel } from '../../../../models/bed.model';
import { InfrastructureService } from '../../../../services/infrastructure.service';
import { FacilityService } from '../../../../services/facility.service';
import { AdmissionService } from '../../../../services/admission.service';
import { PatientService } from '../../../../services/patient.service';
import { DoctorModelService } from '../../../../services/doctor.service';
import { PrescriptionService } from '../../../../services/prescription.service';
import { AppointmentService } from '../../../../services/appointment.service';

@Component({
  selector: 'app-ward-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ward-management.component.html',
  styleUrl: './ward-management.component.css',
})
export class WardManagementComponent implements OnInit {

  wards: WardModel[] = [];
  allBeds: BedModel[] = [];
  selectedWardBeds: BedModel[] = [];
  activeAdmissions: AdmissionResponse[] = [];
  patients: PatientModel[] = [];
  doctors: DoctorModel[] = [];

  selectedWard: WardModel | null = null;
  searchKeyword = '';
  statusFilter = 'ALL';

  showAdmitModal = false;
  admitBed: BedModel | null = null;

  patientSearchKeyword = '';
  filteredPatients: PatientModel[] = [];
  showPatientDropdown = false;
  selectedPatientDisplay = '';
  doctorAutoFilled = false;

  admission: AdmissionRequest = {
    patientId: 0,
    doctorId: 0,
    bedId: 0,
    initialDiagnosis: ''
  };

  totalBeds = 0;
  availableBeds = 0;
  occupiedBeds = 0;
  maintenanceBeds = 0;

  allFacilities: FacilityModel[] = [];

  showFacilityModal = false;
  facilityBed: BedModel | null = null;
  selectedFacilityIds: Set<number> = new Set();
  facilitySaving = false;

  constructor(
    private infraService: InfrastructureService,
    private facilityService: FacilityService,
    private admissionService: AdmissionService,
    private patientService: PatientService,
    private doctorService: DoctorModelService,
    private prescriptionService: PrescriptionService,
    private appointmentService: AppointmentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.infraService.getAllWards().subscribe({
      next: (res) => {
        this.wards = res;
        this.cdr.markForCheck();
      }
    });

    this.infraService.getAllBeds().subscribe({
      next: (res) => {
        this.allBeds = res;
        this.computeStats();
        this.cdr.markForCheck();
      }
    });

    this.admissionService.getActive().subscribe({
      next: (res) => {
        this.activeAdmissions = res;
        this.cdr.markForCheck();
      }
    });

    this.patientService.getAll().subscribe({
      next: (res) => {
        this.patients = res;
        this.cdr.markForCheck();
      }
    });

    this.doctorService.getAll().subscribe({
      next: (res) => {
        this.doctors = res;
        this.cdr.markForCheck();
      }
    });

    this.facilityService.getAll().subscribe({
      next: (res) => {
        this.allFacilities = res;
        this.cdr.markForCheck();
      }
    });
  }

  // ── Create Ward ──
  showCreateWardModal = false;
  newWard = { name: '', departmentId: 0, roomType: 'GENERAL_WARD', totalBeds: 10, basePricePerDay: 0 };
  roomTypes = ['GENERAL_WARD', 'SEMI_PRIVATE', 'PRIVATE_CABIN', 'ICU', 'CCU', 'NICU', 'EMERGENCY'];
  departments: { id: number; name: string }[] = [];

  openCreateWardModal(): void {
    this.newWard = { name: '', departmentId: 0, roomType: 'GENERAL_WARD', totalBeds: 10, basePricePerDay: 0 };
    this.showCreateWardModal = true;
  }

  closeCreateWardModal(): void {
    this.showCreateWardModal = false;
  }

  createWard(): void {
    if (!this.newWard.name || !this.newWard.departmentId) {
      alert('Please fill in ward name and department');
      return;
    }
    this.infraService.createWard(this.newWard).subscribe({
      next: () => {
        alert('Ward created successfully');
        this.closeCreateWardModal();
        this.loadData();
      },
      error: () => alert('Failed to create ward')
    });
  }

  // ── Delete Ward ──
  deleteWard(ward: WardModel, event: Event): void {
    event.stopPropagation();
    if (!confirm(`Delete ward "${ward.name}" and all its beds?`)) return;
    this.infraService.deleteWard(ward.id!).subscribe({
      next: () => {
        alert('Ward deleted');
        if (this.selectedWard?.id === ward.id) {
          this.backToWards();
        }
        this.loadData();
      },
      error: () => alert('Failed to delete ward')
    });
  }

  // ── Create Bed ──
  showCreateBedModal = false;
  newBed = { bedNumber: '', wardId: 0, status: 'AVAILABLE' };

  openCreateBedModal(): void {
    this.newBed = { bedNumber: '', wardId: this.selectedWard?.id || 0, status: 'AVAILABLE' };
    this.showCreateBedModal = true;
  }

  closeCreateBedModal(): void {
    this.showCreateBedModal = false;
  }

  createBed(): void {
    if (!this.newBed.bedNumber || !this.newBed.wardId) {
      alert('Please fill in bed number');
      return;
    }
    this.infraService.createBed(this.newBed).subscribe({
      next: () => {
        alert('Bed created successfully');
        this.closeCreateBedModal();
        this.loadData();
      },
      error: () => alert('Failed to create bed')
    });
  }

  // ── Delete Bed ──
  deleteBed(bed: BedModel): void {
    if (bed.status === 'OCCUPIED') {
      alert('Cannot delete an occupied bed. Discharge the patient first.');
      return;
    }
    if (!confirm(`Delete bed "${bed.bedNumber}"?`)) return;
    this.infraService.deleteBed(bed.id).subscribe({
      next: () => {
        alert('Bed deleted');
        this.loadData();
      },
      error: () => alert('Failed to delete bed')
    });
  }

  computeStats(): void {
    this.totalBeds = this.allBeds.length;
    this.availableBeds = this.allBeds.filter(b => b.status === 'AVAILABLE').length;
    this.occupiedBeds = this.allBeds.filter(b => b.status === 'OCCUPIED').length;
    this.maintenanceBeds = this.allBeds.filter(b => b.status === 'MAINTENANCE').length;
  }

  getWardAvailable(ward: WardModel): number {
    return this.allBeds.filter(b => b.wardId === ward.id && b.status === 'AVAILABLE').length;
  }

  getWardOccupied(ward: WardModel): number {
    return this.allBeds.filter(b => b.wardId === ward.id && b.status === 'OCCUPIED').length;
  }

  getWardTotal(ward: WardModel): number {
    return this.allBeds.filter(b => b.wardId === ward.id).length;
  }

  getWardMaintenance(ward: WardModel): number {
    return this.allBeds.filter(b => b.wardId === ward.id && b.status === 'MAINTENANCE').length;
  }

  getPatientForBed(bed: BedModel): string {
    if (bed.status !== 'OCCUPIED') return '';
    const admission = this.activeAdmissions.find(a => a.bedId === bed.id);
    return admission ? admission.patientName : '';
  }

  getDoctorForBed(bed: BedModel): string {
    if (bed.status !== 'OCCUPIED') return '';
    const admission = this.activeAdmissions.find(a => a.bedId === bed.id);
    return admission ? admission.doctorName : '';
  }

  selectWard(ward: WardModel): void {
    this.selectedWard = ward;
    this.selectedWardBeds = this.allBeds.filter(b => b.wardId === ward.id);
    this.searchKeyword = '';
    this.statusFilter = 'ALL';
    this.cdr.markForCheck();
  }

  get filteredBeds(): BedModel[] {
    let beds = this.selectedWardBeds;

    if (this.searchKeyword) {
      const kw = this.searchKeyword.toLowerCase();
      beds = beds.filter(b => b.bedNumber.toLowerCase().includes(kw));
    }

    if (this.statusFilter !== 'ALL') {
      beds = beds.filter(b => b.status === this.statusFilter);
    }

    return beds;
  }

  getBedStatusClass(status: string): string {
    switch (status) {
      case 'AVAILABLE': return 'bed-available';
      case 'OCCUPIED': return 'bed-occupied';
      case 'MAINTENANCE': return 'bed-maintenance';
      default: return 'bed-available';
    }
  }

  getStatusIcon(status: string): string {
    switch (status) {
      case 'AVAILABLE': return '+';
      case 'OCCUPIED': return '';
      case 'MAINTENANCE': return '!';
      default: return '';
    }
  }

  openAdmitModal(bed: BedModel): void {
    if (bed.status !== 'AVAILABLE') return;
    this.admitBed = bed;
    this.admission = {
      patientId: 0,
      doctorId: 0,
      bedId: bed.id,
      initialDiagnosis: ''
    };
    this.patientSearchKeyword = '';
    this.filteredPatients = [];
    this.showPatientDropdown = false;
    this.selectedPatientDisplay = '';
    this.doctorAutoFilled = false;
    this.showAdmitModal = true;
  }

  closeAdmitModal(): void {
    this.showAdmitModal = false;
    this.admitBed = null;
  }

  onPatientSearch(): void {
    const kw = this.patientSearchKeyword.trim();
    if (kw.length < 1) {
      this.filteredPatients = this.patients.slice(0, 20);
      this.showPatientDropdown = true;
      return;
    }
    this.patientService.search(kw).subscribe({
      next: (res) => {
        this.filteredPatients = res;
        this.showPatientDropdown = true;
        this.cdr.markForCheck();
      }
    });
  }

  selectPatient(patient: PatientModel): void {
    this.admission.patientId = patient.id!;
    this.selectedPatientDisplay = `${patient.patientCode} — ${patient.name} (${patient.phone})`;
    this.patientSearchKeyword = '';
    this.showPatientDropdown = false;
    this.filteredPatients = [];

    if (patient.appointmentId) {
      this.appointmentService.getById(patient.appointmentId).subscribe({
        next: (apt: any) => {
          if (apt && apt.doctorId) {
            this.admission.doctorId = apt.doctorId;
            this.doctorAutoFilled = true;
          }
          this.cdr.markForCheck();
        }
      });
    }

    this.prescriptionService.getByPatientId(patient.id!).subscribe({
      next: (rxList) => {
        if (rxList && rxList.length > 0) {
          const latest = rxList[0];
          if (latest.diagnosis) {
            this.admission.initialDiagnosis = latest.diagnosis;
          }
          if (latest.doctorName && !this.admission.doctorId) {
            const matchedDoc = this.doctors.find(d => d.name === latest.doctorName);
            if (matchedDoc) {
              this.admission.doctorId = matchedDoc.id!;
              this.doctorAutoFilled = true;
            }
          }
          this.cdr.markForCheck();
        }
      }
    });
  }

  clearPatient(): void {
    this.admission.patientId = 0;
    this.selectedPatientDisplay = '';
    this.admission.doctorId = 0;
    this.admission.initialDiagnosis = '';
    this.doctorAutoFilled = false;
  }

  onPatientInputFocus(): void {
    if (!this.selectedPatientDisplay) {
      this.filteredPatients = this.patients.slice(0, 20);
      this.showPatientDropdown = true;
    }
  }

  onPatientInputBlur(): void {
    setTimeout(() => { this.showPatientDropdown = false; }, 200);
  }

  admitPatient(): void {
    if (!this.admission.patientId || !this.admission.doctorId) {
      alert('Please select patient and doctor');
      return;
    }
    this.admissionService.admit(this.admission).subscribe({
      next: () => {
        alert('Patient Admitted Successfully');
        this.closeAdmitModal();
        this.loadData();
      },
      error: (err) => {
        console.log(err);
        alert('Admission Failed');
      }
    });
  }

  dischargePatient(bed: BedModel): void {
    if (bed.status !== 'OCCUPIED') return;
    const admission = this.activeAdmissions.find(a => a.bedId === bed.id);
    if (!admission) return;

    if (confirm(`Discharge ${admission.patientName} from bed ${bed.bedNumber}?`)) {
      this.admissionService.discharge(admission.admissionId).subscribe({
        next: () => {
          alert('Patient Discharged Successfully');
          this.loadData();
        },
        error: (err) => console.log(err)
      });
    }
  }

  getAdmissionForBed(bed: BedModel): AdmissionResponse | undefined {
    if (bed.status !== 'OCCUPIED') return undefined;
    return this.activeAdmissions.find(a => a.bedId === bed.id);
  }

  getDaysSinceAdmission(dateStr: string): string {
    if (!dateStr) return '';
    const admitted = new Date(dateStr);
    const now = new Date();
    const diffMs = now.getTime() - admitted.getTime();
    const days = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    if (days === 0) return 'Today';
    if (days === 1) return '1 day';
    return days + ' days';
  }

  getRoomTypeIcon(type: string): string {
    switch (type?.toUpperCase()) {
      case 'AC': return '❄';
      case 'NON-AC': return '♨';
      case 'DELUXE': return '★';
      case 'ICU': return '✚';
      case 'SEMI-ICU': return '✚';
      case 'GENERAL': return '♥';
      default: return '♥';
    }
  }

  getFacilityIcon(name: string): string {
    const n = name.toLowerCase();
    if (n.includes('oxygen') || n.includes('ventilator')) return '✚';
    if (n.includes('ac') || n.includes('air')) return '❄';
    if (n.includes('monitor') || n.includes('cardiac')) return '♥';
    if (n.includes('wifi') || n.includes('internet')) return '📡';
    if (n.includes('tv') || n.includes('television')) return '📺';
    if (n.includes('phone') || n.includes('call')) return '📞';
    if (n.includes('bathroom') || n.includes('toilet') || n.includes('shower')) return '🚿';
    if (n.includes('wheelchair') || n.includes('lift')) return '♿';
    if (n.includes('nurse') || n.includes('call bell')) return '🔔';
    if (n.includes('x-ray') || n.includes('xray')) return '📷';
    if (n.includes('bed')) return '🛏';
    if (n.includes('safe') || n.includes('locker')) return '🔒';
    return '✦';
  }

  backToWards(): void {
    this.selectedWard = null;
    this.selectedWardBeds = [];
  }

  openFacilityModal(bed: BedModel): void {
    this.facilityBed = bed;
    this.selectedFacilityIds = new Set<number>();
    if (bed.facilities) {
      bed.facilities.forEach(f => this.selectedFacilityIds.add(f.id));
    }
    this.showFacilityModal = true;
  }

  closeFacilityModal(): void {
    this.showFacilityModal = false;
    this.facilityBed = null;
  }

  toggleFacility(facilityId: number): void {
    if (this.selectedFacilityIds.has(facilityId)) {
      this.selectedFacilityIds.delete(facilityId);
    } else {
      this.selectedFacilityIds.add(facilityId);
    }
  }

  isFacilitySelected(facilityId: number): boolean {
    return this.selectedFacilityIds.has(facilityId);
  }

  saveFacilities(): void {
    if (!this.facilityBed) return;
    this.facilitySaving = true;
    this.facilityService.updateBedFacilities(this.facilityBed.id, Array.from(this.selectedFacilityIds)).subscribe({
      next: () => {
        this.facilitySaving = false;
        this.closeFacilityModal();
        this.loadData();
      },
      error: () => {
        this.facilitySaving = false;
        alert('Failed to save facilities');
      }
    });
  }
}
