import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DietAssignmentService } from '../../../../services/dietary/diet-assignment.service';
import { KitchenOrderService } from '../../../../services/dietary/kitchen-order.service';
import { DietHistoryService } from '../../../../services/dietary/diet-history.service';
import { PatientDietAlertService } from '../../../../services/dietary/patient-diet-alert.service';

@Component({
  selector: 'app-diet-reports',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './diet-reports.component.html',
  styleUrl: './diet-reports.component.css'
})
export class DietReportsComponent implements OnInit {
  loading = true;
  selectedReport = 'meal';
  reportData: any[] = [];
  dateFrom = '';
  dateTo = '';
  filterWard = '';
  filterDietType = '';
  msg = '';
  msgType = '';

  reportTypes = [
    { key: 'meal', label: 'Meal Report', emoji: '🍽️', desc: 'Daily meal consumption and delivery' },
    { key: 'patient-diet', label: 'Patient Diet Report', emoji: '🧑', desc: 'Patient-wise diet assignment summary' },
    { key: 'kitchen', label: 'Kitchen Report', emoji: '🍳', desc: 'Kitchen production and efficiency' },
    { key: 'dietician', label: 'Dietician Report', emoji: '👩‍⚕️', desc: 'Dietician performance metrics' },
    { key: 'nutrition', label: 'Nutrition Report', emoji: '📊', desc: 'Nutrition intake analysis' },
    { key: 'consumption', label: 'Consumption Report', emoji: '🍴', desc: 'Meal consumption patterns' },
    { key: 'calories', label: 'Calories Report', emoji: '🔥', desc: 'Daily calorie tracking' },
    { key: 'ward', label: 'Ward Report', emoji: '🏥', desc: 'Wise meal distribution' },
    { key: 'doctor', label: 'Doctor Wise Diet', emoji: '👨‍⚕️', desc: 'Doctor-wise diet assignments' }
  ];

  constructor(
    private assignmentService: DietAssignmentService,
    private orderService: KitchenOrderService,
    private historyService: DietHistoryService,
    private alertService: PatientDietAlertService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.loadReport(); }

  loadReport(): void {
    this.loading = true;
    this.reportData = [];
    this.assignmentService.getAll().subscribe({
      next: (assignments) => {
        this.reportData = this.generateReportData(assignments);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  generateReportData(assignments: any[]): any[] {
    return assignments.map(a => ({
      id: a.id,
      patientName: a.patient?.name || 'Unknown',
      ward: a.patient?.ward?.name || 'N/A',
      dietType: a.dietPlan?.dietType || 'N/A',
      planName: a.dietPlan?.name || 'N/A',
      startDate: a.startDate,
      endDate: a.endDate,
      status: a.status,
      calories: a.dietPlan?.totalCalories || 0,
      doctor: a.assignedByDoctor?.specialization || 'N/A',
      dietician: a.dietician?.specialization || 'N/A'
    }));
  }

  exportPDF(): void {
    const win = window.open('', '_blank', 'width=800,height=600');
    if (!win) return;
    win.document.write(`<html><head><title>Diet Report</title><style>body{font-family:sans-serif;padding:24px;color:#333;}h1{color:#0d6efd;}table{border-collapse:collapse;width:100%;}th,td{border:1px solid #ddd;padding:8px;text-align:left;}th{background:#f0f0f0;}</style></head><body><h1>${this.selectedReport} Report</h1><p>Generated: ${new Date().toLocaleString()}</p><table><tr><th>Patient</th><th>Ward</th><th>Diet Type</th><th>Plan</th><th>Status</th><th>Calories</th></tr>${this.reportData.map(r => `<tr><td>${r.patientName}</td><td>${r.ward}</td><td>${r.dietType}</td><td>${r.planName}</td><td>${r.status}</td><td>${r.calories}</td></tr>`).join('')}</table></body></html>`);
    win.document.close();
    win.print();
  }

  exportExcel(): void {
    const rows = [['Patient', 'Ward', 'Diet Type', 'Plan', 'Status', 'Calories']];
    this.reportData.forEach(r => rows.push([r.patientName, r.ward, r.dietType, r.planName, r.status, r.calories]));
    const csv = rows.map(row => row.join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `diet-report-${this.selectedReport}-${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  exportCSV(): void {
    const rows = [['Patient', 'Ward', 'Diet Type', 'Plan', 'Status', 'Calories']];
    this.reportData.forEach(r => rows.push([r.patientName, r.ward, r.dietType, r.planName, r.status, r.calories]));
    const csv = rows.map(row => row.join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `diet-report-${this.selectedReport}-${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  printReport(): void {
    window.print();
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = { 'ACTIVE': 'badge-success', 'COMPLETED': 'badge-info', 'CANCELLED': 'badge-danger', 'ON_HOLD': 'badge-warning' };
    return map[status] || 'badge-secondary';
  }
}