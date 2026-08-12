import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { SurgeryService } from '../../../../services/surgery/surgery.service';
import { SurgeryResponse } from '../../../../models/surgery/surgery-response.model';

@Component({
  selector: 'app-surgery-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './surgery-list.component.html',
  styleUrl: './surgery-list.component.css'
})
export class SurgeryListComponent implements OnInit {
  surgeries: SurgeryResponse[] = [];
  filtered: SurgeryResponse[] = [];
  loading = false;
  msg = '';
  msgType = 'success';

  searchTerm = '';
  filterStatus = '';
  filterFrom = '';
  filterTo = '';
  statuses = ['SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'POSTPONED'];
  page = 1;
  pageSize = 10;
  Math = Math;

  selected: SurgeryResponse | null = null;
  showStatusModal = false;
  newStatus = 'COMPLETED';
  cancellationReason = '';

  constructor(
    private surgeryService: SurgeryService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadSurgeries();
  }

  loadSurgeries(): void {
    this.loading = true;
    this.surgeryService.getAll().subscribe({
      next: (res) => {
        this.surgeries = res;
        this.applyFilters();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showMsg(err?.error?.message || 'Failed to load surgeries', 'error');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    const q = this.searchTerm.trim().toLowerCase();
    let list = this.surgeries;

    if (q) {
      list = list.filter(s =>
        s.surgeryNumber?.toLowerCase().includes(q) ||
        s.patientName?.toLowerCase().includes(q) ||
        s.patientCode?.toLowerCase().includes(q) ||
        s.surgeonName?.toLowerCase().includes(q) ||
        s.surgeryName?.toLowerCase().includes(q) ||
        s.operationTheatreName?.toLowerCase().includes(q)
      );
    }
    if (this.filterStatus) {
      list = list.filter(s => s.status === this.filterStatus);
    }
    if (this.filterFrom) {
      list = list.filter(s => s.surgeryDate >= this.filterFrom);
    }
    if (this.filterTo) {
      list = list.filter(s => s.surgeryDate <= this.filterTo);
    }

    list = [...list].sort((a, b) => (b.surgeryDate || '').localeCompare(a.surgeryDate || ''));
    this.filtered = list;
    this.page = 1;
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.filterStatus = '';
    this.filterFrom = '';
    this.filterTo = '';
    this.applyFilters();
  }

  get paged(): SurgeryResponse[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filtered.length / this.pageSize));
  }

  openStatusModal(s: SurgeryResponse): void {
    this.selected = s;
    this.newStatus = s.status === 'COMPLETED' ? 'CANCELLED' : s.status;
    this.cancellationReason = '';
    this.showStatusModal = true;
  }

  confirmStatusChange(): void {
    if (!this.selected) return;
    if ((this.newStatus === 'CANCELLED' || this.newStatus === 'POSTPONED') && !this.cancellationReason.trim()) {
      this.showMsg('Cancellation / postponement reason is required', 'error');
      return;
    }
    this.surgeryService.updateStatus(this.selected.id, this.newStatus, this.cancellationReason.trim() || undefined)
      .subscribe({
        next: (updated) => {
          const idx = this.surgeries.findIndex(s => s.id === updated.id);
          if (idx !== -1) this.surgeries[idx] = updated;
          this.applyFilters();
          this.showStatusModal = false;
          this.selected = null;
          this.showMsg('Surgery status updated', 'success');
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.showMsg(err?.error?.message || 'Failed to update status', 'error');
          this.cdr.detectChanges();
        }
      });
  }

  viewDetails(id: number): void {
    this.router.navigate(['/surgery/details', id]);
  }

  editSurgery(id: number): void {
    this.router.navigate(['/surgery/edit', id]);
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'SCHEDULED': 'badge-info',
      'IN_PROGRESS': 'badge-warning',
      'COMPLETED': 'badge-success',
      'CANCELLED': 'badge-danger',
      'POSTPONED': 'badge-secondary'
    };
    return map[status] || 'badge-secondary';
  }

  getPriorityClass(priority: string): string {
    const map: Record<string, string> = {
      'EMERGENCY': 'prio-emergency',
      'URGENT': 'prio-urgent',
      'ELECTIVE': 'prio-elective'
    };
    return map[priority] || 'prio-elective';
  }

  formatCurrency(amount: number | undefined): string {
    return '৳' + (amount || 0).toLocaleString('en-BD', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  showMsg(text: string, type: string): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; this.cdr.detectChanges(); }, 4000);
  }
}
