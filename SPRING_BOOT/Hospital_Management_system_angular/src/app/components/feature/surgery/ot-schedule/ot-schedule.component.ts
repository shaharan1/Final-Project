import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { SurgeryService } from '../../../../services/surgery/surgery.service';
import { SurgerySchedule } from '../../../../models/surgery/surgery-schedule.model';

@Component({
  selector: 'app-ot-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ot-schedule.component.html',
  styleUrl: './ot-schedule.component.css'
})
export class OtScheduleComponent implements OnInit {
  selectedDate = new Date().toISOString().slice(0, 10);
  schedule: SurgerySchedule[] = [];
  upcoming: SurgerySchedule[] = [];
  loading = false;
  msg = '';
  msgType = 'success';

  constructor(
    private surgeryService: SurgeryService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadSchedule();
    this.loadUpcoming();
  }

  loadSchedule(): void {
    this.loading = true;
    this.surgeryService.getSchedule(this.selectedDate).subscribe({
      next: (res) => {
        this.schedule = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showMsg(err?.error?.message || 'Failed to load schedule', 'error');
        this.loading = false;
      }
    });
  }

  loadUpcoming(): void {
    this.surgeryService.getUpcomingSchedule().subscribe({
      next: (res) => {
        this.upcoming = res;
        this.cdr.detectChanges();
      },
      error: () => { this.cdr.detectChanges(); }
    });
  }

  onDateChange(): void {
    this.loadSchedule();
  }

  dayText(): string {
    const d = new Date(this.selectedDate + 'T00:00:00');
    return d.toLocaleDateString('en-GB', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });
  }

  // Group upcoming by date
  get upcomingByDate(): Array<{ date: string; items: SurgerySchedule[] }> {
    const map = new Map<string, SurgerySchedule[]>();
    for (const s of [...this.upcoming].sort((a, b) => (a.surgeryDate || '').localeCompare(b.surgeryDate || ''))) {
      if (!map.has(s.surgeryDate)) map.set(s.surgeryDate, []);
      map.get(s.surgeryDate)!.push(s);
    }
    return Array.from(map.entries()).map(([date, items]) => ({ date, items }));
  }

  get todayCount(): number {
    return this.schedule.length;
  }

  get completedCount(): number {
    return this.schedule.filter(s => s.status === 'COMPLETED').length;
  }

  get inProgressCount(): number {
    return this.schedule.filter(s => s.status === 'IN_PROGRESS').length;
  }

  get upcomingCount(): number {
    return this.upcoming.length;
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

  viewDetails(id: number): void {
    this.router.navigate(['/surgery/details', id]);
  }

  showMsg(text: string, type: string): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; this.cdr.detectChanges(); }, 4000);
  }
}
