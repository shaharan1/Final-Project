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
import { InfrastructureService } from '../../../../services/infrastructure.service';
import { AdmissionService } from '../../../../services/admission.service';
import { PatientService } from '../../../../services/patient.service';
import { DoctorModelService } from '../../../../services/doctor.service';

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

  constructor(
    private infraService: InfrastructureService,
    private admissionService: AdmissionService,
    private patientService: PatientService,
    private doctorService: DoctorModelService,
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
    this.showAdmitModal = true;
  }

  closeAdmitModal(): void {
    this.showAdmitModal = false;
    this.admitBed = null;
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

  backToWards(): void {
    this.selectedWard = null;
    this.selectedWardBeds = [];
  }
}
