import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { SurgeryService } from '../../../../services/surgery/surgery.service';
import { SurgeryResponse } from '../../../../models/surgery/surgery-response.model';

@Component({
  selector: 'app-surgery-details',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './surgery-details.component.html',
  styleUrl: './surgery-details.component.css'
})
export class SurgeryDetailsComponent implements OnInit {
  surgery: SurgeryResponse | null = null;
  loading = false;
  msg = '';
  msgType = 'success';

  constructor(
    private surgeryService: SurgeryService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) this.loadSurgery(Number(id));
    });
  }

  loadSurgery(id: number): void {
    this.loading = true;
    this.surgeryService.getById(id).subscribe({
      next: (res) => {
        this.surgery = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showMsg(err?.error?.message || 'Failed to load surgery', 'error');
        this.loading = false;
      }
    });
  }

  chargeRows(): Array<{ label: string; value: number }> {
    if (!this.surgery) return [];
    const rows = [
      { label: 'Surgery Charge', value: this.surgery.surgeryCharge || 0 },
      { label: 'OT Charge', value: this.surgery.otCharge || 0 },
      { label: 'Surgeon Fee', value: this.surgery.surgeonFee || 0 },
      { label: 'Assistant Surgeon Fee', value: this.surgery.assistantSurgeonFee || 0 },
      { label: 'Anesthesia Fee', value: this.surgery.anesthesiaFee || 0 },
      { label: 'Nursing Charge', value: this.surgery.nursingCharge || 0 },
      { label: 'Equipment Charge', value: this.surgery.equipmentCharge || 0 },
      { label: 'Consumable Charge', value: this.surgery.consumableCharge || 0 },
      { label: 'ICU Charge', value: this.surgery.icuCharge || 0 },
      { label: 'Ward / Cabin Charge', value: this.surgery.wardCabinCharge || 0 },
      { label: 'Medicine Charge', value: this.surgery.medicineCharge || 0 },
      { label: 'Laboratory Charge', value: this.surgery.laboratoryCharge || 0 },
      { label: 'Radiology Charge', value: this.surgery.radiologyCharge || 0 }
    ];
    return rows.filter(r => r.value > 0);
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

  toNumber(v: any): number {
    return Number(v) || 0;
  }

  editSurgery(): void {
    if (this.surgery) this.router.navigate(['/surgery/edit', this.surgery.id]);
  }

  viewInvoice(): void {
    if (this.surgery?.billingInvoiceId) {
      this.router.navigate(['/surgery/invoice', this.surgery.billingInvoiceId]);
    }
  }

  showMsg(text: string, type: string): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; this.cdr.detectChanges(); }, 4000);
  }
}
