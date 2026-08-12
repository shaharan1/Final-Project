import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { TestOrderModel } from '../../../../models/test-order.model';
import { TestOrderService } from '../../../../services/test-order.service';
import { StorageService } from '../../../../services/storage.service';
import { DoctorModelService } from '../../../../services/doctor.service';

@Component({
  selector: 'app-doctor-lab-report',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-lab-report.component.html',
  styleUrl: './doctor-lab-report.component.css',
})
export class DoctorLabReportComponent implements OnInit {

  orders: TestOrderModel[] = [];
  searchKeyword = '';
  loading = true;
  doctorId: number = 0;

  showPdfModal = false;
  selectedOrder: TestOrderModel | null = null;
  pdfUrl: SafeResourceUrl | null = null;

  constructor(
    private testOrderService: TestOrderService,
    private doctorService: DoctorModelService,
    private storage: StorageService,
    private sanitizer: DomSanitizer,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const user = this.storage.getUser();
    if (user?.userId) {
      this.doctorService.findByUserId(user.userId).subscribe({
        next: (doctor) => {
          if (doctor?.id) {
            this.doctorId = doctor.id;
            this.loadOrders();
          }
        }
      });
    }
  }

  loadOrders(): void {
    this.loading = true;
    this.testOrderService.getByDoctor(this.doctorId).subscribe({
      next: (res) => {
        this.orders = res.filter(o => ['RESULT_ENTERED', 'VERIFIED', 'COMPLETED'].includes(o.orderStatus));
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.loading = false; }
    });
  }

  get filteredOrders(): TestOrderModel[] {
    if (!this.searchKeyword) return this.orders;
    const kw = this.searchKeyword.toLowerCase();
    return this.orders.filter(o =>
      o.patientName?.toLowerCase().includes(kw) ||
      o.testName?.toLowerCase().includes(kw)
    );
  }

  viewPdf(order: TestOrderModel): void {
    if (!order.id) return;
    this.testOrderService.downloadReportPdf(order.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        this.selectedOrder = order;
        this.showPdfModal = true;
      },
      error: () => alert('PDF not available yet.')
    });
  }

  closePdfModal(): void {
    this.showPdfModal = false;
    this.pdfUrl = null;
    this.selectedOrder = null;
  }

  formatStatus(status: string): string {
    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getAvatarColor(name: string): string {
    const colors = ['#0dcaf0', '#0d6efd', '#198754', '#dc3545', '#ffc107', '#6610f2', '#fd7e14', '#e83e8c'];
    let hash = 0;
    for (let i = 0; i < (name || '').length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash);
    return colors[Math.abs(hash) % colors.length];
  }
}
