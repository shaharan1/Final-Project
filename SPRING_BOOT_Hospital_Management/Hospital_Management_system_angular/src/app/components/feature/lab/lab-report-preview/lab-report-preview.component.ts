import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { LabReport, LabReportResult } from '../../../../models/lab-report.model';
import { LabReportService } from '../../../../services/lab-report.service';
import { StorageService } from '../../../../services/storage.service';

@Component({
  selector: 'app-lab-report-preview',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lab-report-preview.component.html',
  styleUrl: './lab-report-preview.component.css',
})
export class LabReportPreviewComponent implements OnInit {

  report: LabReport | null = null;
  loading = true;
  error = '';

  showVerifyModal = false;
  specialistName = '';
  specialistDesignation = '';
  specialistSignature = '';
  verificationNotes = '';
  verifying = false;

  showPdfModal = false;
  pdfUrl: SafeResourceUrl | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private labReportService: LabReportService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) this.loadReport(+id);
    });
  }

  loadReport(id: number): void {
    this.loading = true;
    this.labReportService.getById(id).subscribe({
      next: (report) => {
        this.report = report;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.error = 'Report not found.';
      }
    });
  }

  isReady(): boolean {
    return this.report?.reportStatus === 'READY';
  }

  isCriticalStatus(): boolean {
    const s = this.report?.reportStatus;
    return s === 'CRITICAL' || s === 'DENGUE_POSITIVE';
  }

  abnormalResults(): LabReportResult[] {
    return this.report?.results?.filter(r => r.abnormal) || [];
  }

  resultRowClass(r: LabReportResult): string {
    if (r.critical) return 'row-critical';
    if (r.abnormal) return 'row-abnormal';
    return '';
  }

  statusChipClass(status: string): string {
    const s = (status || '').toLowerCase();
    if (s.includes('critical') || s === 'positive' || s === 'reactive' || s === 'abnormal') return 'chip-danger';
    if (s === 'normal' || s === 'negative' || s === 'non_reactive') return 'chip-good';
    if (s === 'borderline') return 'chip-warn';
    return 'chip-neutral';
  }

  formatStatus(status: string): string {
    return (status || '').replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  formatDate(iso?: string): string {
    if (!iso) return '—';
    const d = new Date(iso);
    return d.toLocaleString(undefined, { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
  }

  statusBanner(): { cls: string; icon: string; title: string; text: string } {
    const s = this.report?.reportStatus || 'PENDING';
    switch (s) {
      case 'READY':
        return { cls: 'banner-ready', icon: 'bi-patch-check', title: 'Report Ready', text: 'This report has been verified and released.' };
      case 'CRITICAL':
        return { cls: 'banner-critical', icon: 'bi-exclamation-octagon', title: 'CRITICAL', text: 'Critical values detected — immediate attention required.' };
      case 'DENGUE_POSITIVE':
        return { cls: 'banner-dengue', icon: 'bi-bug', title: 'DENGUE POSITIVE', text: this.report?.finalImpression || 'Dengue infection suspected.' };
      case 'ABNORMAL':
        return { cls: 'banner-abnormal', icon: 'bi-exclamation-triangle', title: 'ABNORMAL', text: 'One or more parameters are outside the reference range.' };
      case 'NEEDS_DOCTOR_REVIEW':
        return { cls: 'banner-review', icon: 'bi-person-workspace', title: 'NEEDS DOCTOR REVIEW', text: 'Multiple abnormal parameters — physician review recommended.' };
      default:
        return { cls: 'banner-pending', icon: 'bi-hourglass-split', title: 'PENDING', text: 'Report is being prepared.' };
    }
  }

  openVerifyModal(): void {
    this.specialistName = '';
    this.specialistDesignation = '';
    this.specialistSignature = '';
    this.verificationNotes = '';
    this.showVerifyModal = true;
  }

  closeVerifyModal(): void {
    if (this.verifying) return;
    this.showVerifyModal = false;
  }

  onSignatureFile(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    if (file.size > 500 * 1024) {
      alert('Signature image must be under 500 KB.');
      input.value = '';
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      this.specialistSignature = (reader.result as string) || '';
      this.cdr.markForCheck();
    };
    reader.readAsDataURL(file);
  }

  canVerify(): boolean {
    return !!this.specialistName.trim() && !!this.specialistDesignation.trim();
  }

  verifyReport(): void {
    if (!this.report?.id || !this.canVerify()) return;
    this.verifying = true;
    this.labReportService.verifyReport(
      this.report.id,
      this.specialistName.trim(),
      this.specialistDesignation.trim(),
      this.specialistSignature,
      this.verificationNotes.trim()
    ).subscribe({
      next: (updated) => {
        this.report = updated;
        this.verifying = false;
        this.showVerifyModal = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.verifying = false;
        alert('Failed to verify report.');
      }
    });
  }

  downloadPdf(): void {
    if (!this.report?.id) return;
    this.labReportService.downloadPdf(this.report.id).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${this.report?.reportNumber || 'lab-report'}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to download PDF.')
    });
  }

  openPdf(): void {
    if (!this.report?.id) return;
    this.labReportService.downloadPdf(this.report.id).subscribe({
      next: (blob) => {
        this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(URL.createObjectURL(blob));
        this.showPdfModal = true;
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to load PDF.')
    });
  }

  closePdf(): void {
    this.showPdfModal = false;
    this.pdfUrl = null;
  }

  goToList(): void {
    this.router.navigate(['/lab/reports']);
  }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getAvatarColor(name: string): string {
    const colors = ['#0f766e', '#fd7e14', '#dc3545', '#e83e8c', '#6610f2', '#0d6efd', '#198754', '#20c997'];
    let hash = 0;
    for (let i = 0; i < (name || '').length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash);
    return colors[Math.abs(hash) % colors.length];
  }

  isVerified(): boolean {
    return !!this.report?.specialistName;
  }
}
