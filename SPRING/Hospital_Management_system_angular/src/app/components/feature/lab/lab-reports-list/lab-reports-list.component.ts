import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { LabReport } from '../../../../models/lab-report.model';
import { LabReportService } from '../../../../services/lab-report.service';

@Component({
  selector: 'app-lab-reports-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lab-reports-list.component.html',
  styleUrl: './lab-reports-list.component.css',
})
export class LabReportsListComponent implements OnInit {

  reports: LabReport[] = [];
  loading = true;
  filter = 'all';
  searchKeyword = '';

  constructor(
    private labReportService: LabReportService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.filter = params['filter'] || 'all';
      this.loadReports();
    });
  }

  loadReports(): void {
    this.loading = true;
    this.labReportService.getAll().subscribe({
      next: (list) => {
        this.reports = [...list].sort((a, b) => (b.reportedDate || b.createdDate || '').localeCompare(a.reportedDate || a.createdDate || ''));
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  setFilter(f: string): void {
    this.router.navigate(['/lab/reports'], { queryParams: { filter: f } });
  }

  get filtered(): LabReport[] {
    let list = this.reports;
    switch (this.filter) {
      case 'normal': list = list.filter(r => r.reportStatus === 'NORMAL'); break;
      case 'abnormal': list = list.filter(r => r.reportStatus === 'ABNORMAL'); break;
      case 'critical': list = list.filter(r => r.reportStatus === 'CRITICAL'); break;
      case 'dengue': list = list.filter(r => r.reportStatus === 'DENGUE_POSITIVE'); break;
      case 'pending': list = list.filter(r => r.reportStatus !== 'READY'); break;
      case 'ready': list = list.filter(r => r.reportStatus === 'READY'); break;
      default: break;
    }
    if (this.searchKeyword) {
      const kw = this.searchKeyword.toLowerCase();
      list = list.filter(r =>
        r.patientName?.toLowerCase().includes(kw) ||
        r.testName?.toLowerCase().includes(kw) ||
        r.reportNumber?.toLowerCase().includes(kw)
      );
    }
    return list;
  }

  counts(): { [k: string]: number } {
    const c: { [k: string]: number } = { all: this.reports.length, normal: 0, abnormal: 0, critical: 0, dengue: 0, pending: 0, ready: 0 };
    this.reports.forEach(r => {
      if (c[r.reportStatus] != null) c[r.reportStatus]++;
      if (r.reportStatus === 'READY') c['ready']++;
      else c['pending']++;
    });
    return c;
  }

  statusChipClass(status?: string): string {
    const s = (status || '').toLowerCase();
    if (s.includes('critical') || s === 'dengue_positive') return 'chip-danger';
    if (s === 'normal') return 'chip-good';
    if (s === 'abnormal') return 'chip-warn';
    return 'chip-neutral';
  }

  formatStatus(status?: string): string {
    return (status || '').replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  formatDate(iso?: string): string {
    if (!iso) return '—';
    return new Date(iso).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
  }

  openReport(id: number): void {
    this.router.navigate(['/lab/report', id]);
  }

  getInitials(name?: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }
}
