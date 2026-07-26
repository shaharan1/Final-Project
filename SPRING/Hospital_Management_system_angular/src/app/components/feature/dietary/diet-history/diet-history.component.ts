import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DietHistoryService } from '../../../../services/dietary/diet-history.service';

@Component({
  selector: 'app-diet-history',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './diet-history.component.html',
  styleUrl: './diet-history.component.css'
})
export class DietHistoryComponent implements OnInit {
  history: any[] = [];
  loading = true;
  selectedPatient: any = null;
  patientIdInput = '';
  filterAction = '';

  constructor(private historyService: DietHistoryService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.loadHistory(); }

  loadHistory(): void {
    this.loading = true;
    if (this.patientIdInput) {
      this.historyService.getByPatientId(+this.patientIdInput).subscribe({
        next: (data) => { this.history = data; this.loading = false; this.cdr.detectChanges(); },
        error: () => { this.loading = false; this.cdr.detectChanges(); }
      });
    } else {
      this.historyService.getAll().subscribe({
        next: (data) => { this.history = data; this.loading = false; this.cdr.detectChanges(); },
        error: () => { this.loading = false; this.cdr.detectChanges(); }
      });
    }
  }

  searchPatient(): void {
    if (!this.patientIdInput) { this.loadHistory(); return; }
    this.loading = true;
    this.historyService.getByPatientId(+this.patientIdInput).subscribe({
      next: (data) => { this.history = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  getActionClass(action: string): string {
    const map: Record<string, string> = {
      'ASSIGNED': 'badge-success', 'UPDATED': 'badge-info', 'CANCELLED': 'badge-danger',
      'DOCTOR_RECOMMENDATION': 'badge-primary', 'DIETICIAN_RECOMMENDATION': 'badge-warning',
      'MEAL_CHANGED': 'badge-secondary'
    };
    return map[action] || 'badge-secondary';
  }
}