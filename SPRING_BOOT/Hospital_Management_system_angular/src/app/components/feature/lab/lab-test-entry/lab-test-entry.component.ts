import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TestOrderModel } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';
import { LabConfigService } from '../../../../services/lab-config.service';
import { LabReportService } from '../../../../services/lab-report.service';
import { StorageService } from '../../../../services/storage.service';
import { InterpretPreview, TestMasterDetail, TestParameter } from '../../../../models/lab-config.model';

interface EntryRow {
  parameter: TestParameter;
  value: string;
  preview: InterpretPreview | null;
  loading: boolean;
}

@Component({
  selector: 'app-lab-test-entry',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lab-test-entry.component.html',
  styleUrl: './lab-test-entry.component.css',
})
export class LabTestEntryComponent implements OnInit {

  orders: TestOrderModel[] = [];
  reportedOrderIds = new Set<number>();
  reportIdByOrderId = new Map<number, number>();
  searchKeyword = '';
  loading = true;
  userName = '';

  activeTab = 'READY';
  showStartModal = false;
  selectedOrder: TestOrderModel | null = null;

  showEntryModal = false;
  detail: TestMasterDetail | null = null;
  rows: EntryRow[] = [];
  entering = false;
  saving = false;
  savedReportId: number | null = null;

  constructor(
    private testOrderService: TestOrderService,
    private labConfigService: LabConfigService,
    private labReportService: LabReportService,
    private storage: StorageService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.userName = this.storage.getUser()?.name || '';
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.testOrderService.getAll().subscribe({
      next: (res) => {
        this.labReportService.getAll().subscribe({
          next: (reports) => {
            this.reportedOrderIds = new Set(reports.map(r => r.testOrderId));
            this.reportIdByOrderId = new Map(reports.filter(r => r.testOrderId).map(r => [r.testOrderId, r.id]));
            this.orders = res.filter(o =>
              ['SAMPLE_RECEIVED', 'TESTING', 'RESULT_ENTERED'].includes(o.orderStatus)
            );
            this.loading = false;
            this.cdr.markForCheck();
          },
          error: () => { this.loading = false; }
        });
      },
      error: () => { this.loading = false; }
    });
  }

  get filteredOrders(): TestOrderModel[] {
    let list = this.orders.filter(o => {
      if (this.activeTab === 'READY') return o.orderStatus === 'SAMPLE_RECEIVED';
      if (this.activeTab === 'TESTING') return o.orderStatus === 'TESTING';
      if (this.activeTab === 'DONE') return o.orderStatus === 'RESULT_ENTERED';
      return true;
    });
    if (this.searchKeyword) {
      const kw = this.searchKeyword.toLowerCase();
      list = list.filter(o =>
        o.patientName?.toLowerCase().includes(kw) ||
        o.testName?.toLowerCase().includes(kw) ||
        o.patientCode?.toLowerCase().includes(kw)
      );
    }
    return list;
  }

  get readyCount(): number { return this.orders.filter(o => o.orderStatus === 'SAMPLE_RECEIVED').length; }
  get testingCount(): number { return this.orders.filter(o => o.orderStatus === 'TESTING').length; }
  get doneCount(): number { return this.orders.filter(o => o.orderStatus === 'RESULT_ENTERED').length; }

  hasReport(order: TestOrderModel): boolean {
    return !!order.id && this.reportedOrderIds.has(order.id);
  }

  getReportId(order: TestOrderModel): number {
    return order.id ? this.reportIdByOrderId.get(order.id) ?? 0 : 0;
  }

  openStartModal(order: TestOrderModel): void {
    this.selectedOrder = order;
    this.showStartModal = true;
  }

  closeStartModal(): void {
    this.showStartModal = false;
    this.selectedOrder = null;
  }

  startTesting(): void {
    if (!this.selectedOrder?.id) return;
    this.testOrderService.startTesting(this.selectedOrder.id).subscribe({
      next: () => {
        this.closeStartModal();
        this.loadOrders();
      },
      error: () => { alert('Failed to start testing'); }
    });
  }

  openEntry(order: TestOrderModel): void {
    if (!order.testMasterId) {
      alert('This test has no parameters configured. Configure parameters first.');
      return;
    }
    this.selectedOrder = order;
    this.savedReportId = null;
    this.labConfigService.getTestMasterDetail(order.testMasterId).subscribe({
      next: (detail) => {
        this.detail = detail;
        this.rows = detail.parameters
          .filter(p => p.active !== false)
          .map(p => ({ parameter: p, value: '', preview: null, loading: false }));
        this.showEntryModal = true;
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to load test parameters')
    });
  }

  closeEntry(): void {
    this.showEntryModal = false;
    this.selectedOrder = null;
    this.detail = null;
    this.rows = [];
  }

  isQualitative(p: TestParameter): boolean {
    return p.resultType === 'POSITIVE_NEGATIVE' || p.resultType === 'TEXT' || p.resultType === 'MULTI_OPTION';
  }

  allowedValueList(p: TestParameter): string[] {
    if (!p.allowedValues) return [];
    return p.allowedValues.split(',').map(v => v.trim());
  }

  onValueChange(row: EntryRow): void {
    if (!row.value || !this.selectedOrder?.id) {
      row.preview = null;
      return;
    }
    row.loading = true;
    const age = this.computeAge(this.selectedOrder.patientDateOfBirth);
    this.labReportService.preview(
      row.parameter.id!,
      row.value,
      this.selectedOrder.patientGender,
      age
    ).subscribe({
      next: (preview) => {
        row.preview = preview;
        row.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { row.loading = false; }
    });
  }

  computeAge(dob?: string): number | undefined {
    if (!dob) return undefined;
    const birth = new Date(dob);
    const now = new Date();
    let age = now.getFullYear() - birth.getFullYear();
    const m = now.getMonth() - birth.getMonth();
    if (m < 0 || (m === 0 && now.getDate() < birth.getDate())) age--;
    return age >= 0 ? age : undefined;
  }

  statusChipClass(status: string): string {
    const s = (status || '').toLowerCase();
    if (s.includes('critical')) return 'chip-critical';
    if (s === 'normal' || s === 'negative' || s === 'non_reactive') return 'chip-normal';
    if (s === 'positive' || s === 'reactive' || s === 'abnormal') return 'chip-positive';
    if (s === 'borderline' || s === 'pending') return 'chip-borderline';
    return 'chip-warning';
  }

  get criticalCount(): number {
    return this.rows.filter(r => r.preview?.critical).length;
  }

  get abnormalCount(): number {
    return this.rows.filter(r => r.preview?.abnormal && !r.preview?.critical).length;
  }

  get normalCount(): number {
    return this.rows.filter(r => r.preview && !r.preview.abnormal && !r.preview.critical).length;
  }

  get liveSummary(): { label: string; cls: string; text: string } {
    const done = this.rows.filter(r => r.preview);
    if (this.criticalCount > 0) {
      return { label: 'CRITICAL', cls: 'sum-critical', text: 'One or more critical results detected.' };
    }
    if (this.abnormalCount >= 2) {
      return { label: 'NEEDS DOCTOR REVIEW', cls: 'sum-review', text: 'Multiple abnormal parameters detected.' };
    }
    if (this.abnormalCount === 1) {
      return { label: 'ABNORMAL', cls: 'sum-abnormal', text: 'One or more parameters out of range.' };
    }
    if (done.length > 0 && done.length === this.rows.length) {
      return { label: 'NORMAL', cls: 'sum-normal', text: 'All parameters within reference range.' };
    }
    return { label: 'PENDING', cls: 'sum-pending', text: 'Enter all parameter results to compute report status.' };
  }

  canSave(): boolean {
    return this.rows.length > 0 && this.rows.every(r => r.value && r.value.trim() !== '');
  }

  saveReport(): void {
    if (!this.selectedOrder?.id || !this.canSave()) {
      alert('Please enter all parameter results');
      return;
    }
    this.saving = true;
    const results = this.rows.map(r => ({ parameterId: r.parameter.id!, resultValue: r.value.trim() }));
    this.labReportService.createReport(this.selectedOrder.id, this.userName, results).subscribe({
      next: (report) => {
        this.saving = false;
        this.savedReportId = report.id;
        this.cdr.markForCheck();
      },
      error: () => {
        this.saving = false;
        alert('Failed to save report. Please try again.');
      }
    });
  }

  goToReport(): void {
    if (this.savedReportId) {
      this.router.navigate(['/lab/report', this.savedReportId]);
    }
  }

  formatStatus(status: string): string {
    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getAvatarColor(name: string): string {
    const colors = ['#ffc107', '#fd7e14', '#dc3545', '#e83e8c', '#6610f2', '#0d6efd', '#198754', '#20c997'];
    let hash = 0;
    for (let i = 0; i < (name || '').length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash);
    return colors[Math.abs(hash) % colors.length];
  }
}
