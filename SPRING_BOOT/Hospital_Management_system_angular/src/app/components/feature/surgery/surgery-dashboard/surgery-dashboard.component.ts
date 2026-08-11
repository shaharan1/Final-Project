import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SurgeryService } from '../../../../services/surgery/surgery.service';
import { SurgeryDashboard } from '../../../../models/surgery/surgery-dashboard.model';

@Component({
  selector: 'app-surgery-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './surgery-dashboard.component.html',
  styleUrl: './surgery-dashboard.component.css'
})
export class SurgeryDashboardComponent implements OnInit {
  summary: SurgeryDashboard | null = null;
  loading = false;
  selectedDate = new Date().toISOString().slice(0, 10);
  maxMonth = 0;
  msg = '';
  msgType = 'success';

  constructor(
    private surgeryService: SurgeryService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.surgeryService.getDashboard(this.selectedDate).subscribe({
      next: (res) => {
        this.summary = res;
        this.maxMonth = Math.max(1, ...res.monthlyStats.map(m => m[2]));
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onDateChange(): void {
    this.loadDashboard();
  }

  formatCurrency(amount: number | undefined): string {
    return '৳' + (amount || 0).toLocaleString('en-BD', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
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

  monthName(month: number): string {
    const names = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return names[month - 1] || month.toString();
  }
}
