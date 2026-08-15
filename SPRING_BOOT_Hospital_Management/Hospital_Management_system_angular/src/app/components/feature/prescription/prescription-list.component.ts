import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { PrescriptionModel } from '../../../models/prescriptionModel';
import { PrescriptionService } from '../../../services/prescription.service';
import { DoctorModelService } from '../../../services/doctor.service';
import { StorageService } from '../../../services/storage.service';
import { LoginResponse } from '../../../models/login.model';

@Component({
  selector: 'app-prescription-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './prescription-list.component.html',
  styleUrl: './prescription-list.component.css',
})
export class PrescriptionListComponent implements OnInit {

  prescriptions: PrescriptionModel[] = [];
  loading = true;
  errorMsg: string | null = null;
  keyword = '';

  constructor(
    private service: PrescriptionService,
    private doctorService: DoctorModelService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
    public router: Router
  ) {}

  ngOnInit(): void {
    const user: LoginResponse | null = this.storage.getUser();
    if (!user?.userId) {
      this.errorMsg = 'Unable to determine logged-in user.';
      this.loading = false;
      return;
    }

    this.doctorService.findByUserId(user.userId).subscribe({
      next: (doctor) => {
        if (doctor?.id) {
          this.loadPrescriptions(doctor.id);
        } else {
          this.loadAllPrescriptions();
        }
      },
      error: (err) => {
        if (err?.status === 404) {
          this.loadAllPrescriptions();
        } else {
          this.errorMsg = 'Failed to load doctor profile.';
          this.loading = false;
        }
      }
    });
  }

  loadPrescriptions(doctorId: number): void {
    this.loading = true;
    this.service.getByDoctorId(doctorId).subscribe({
      next: (res) => {
        this.prescriptions = res;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMsg = 'Failed to load prescriptions.';
        this.loading = false;
      }
    });
  }

  loadAllPrescriptions(): void {
    this.loading = true;
    this.service.getAll().subscribe({
      next: (res) => {
        this.prescriptions = res;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMsg = 'Failed to load prescriptions.';
        this.loading = false;
      }
    });
  }

  editPrescription(id: number): void {
    this.router.navigate(['/prescriptions/edit', id]);
  }

  viewPdf(id: number): void {
    this.service.downloadPdf(id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const win = window.open(url);
        if (win) {
          win.onload = () => win.print();
        }
      },
      error: () => {
        this.errorMsg = 'Failed to generate PDF.';
      }
    });
  }

  formatDate(dateStr: string | undefined): string {
    if (!dateStr) return 'N/A';
    const d = new Date(dateStr);
    if (isNaN(d.getTime())) return 'N/A';
    return d.toLocaleDateString('en-GB');
  }

  get filteredPrescriptions(): PrescriptionModel[] {
    const k = this.keyword.trim().toLowerCase();
    if (!k) return this.prescriptions;
    return this.prescriptions.filter(p =>
      (p.prescriptionNumber || '').toLowerCase().includes(k) ||
      (p.patientName || '').toLowerCase().includes(k) ||
      (p.diagnosis || '').toLowerCase().includes(k) ||
      (p.doctorName || '').toLowerCase().includes(k)
    );
  }
}
