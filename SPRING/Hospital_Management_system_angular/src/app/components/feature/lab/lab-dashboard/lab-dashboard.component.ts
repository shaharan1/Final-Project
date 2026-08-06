import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { LabReportService } from '../../../../services/lab-report.service';
import { LabDashboard, LabReport } from '../../../../models/lab-report.model';

@Component({
  selector: 'app-lab-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './lab-dashboard.component.html',
  styleUrl: './lab-dashboard.component.css',
})
export class LabDashboardComponent implements OnInit {

  dashboard: LabDashboard | null = null;
  loading = true;
  error = '';

  constructor(
    private labReportService: LabReportService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.labReportService.getDashboard().subscribe({
      next: (d) => {
        this.dashboard = d;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to load lab dashboard.';
      }
    });
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

  openReportsTab(tab: string): void {
    this.router.navigate(['/lab/reports'], { queryParams: { filter: tab } });
  }

  getInitials(name?: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }
}
