import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BillingDashboardService } from '../../../../services/billing/billing-dashboard.service';
import { PaymentService } from '../../../../services/billing/payment.service';
import { RefundService } from '../../../../services/billing/refund.service';

interface ReportTab {
  id: string;
  label: string;
  icon: string;
}

interface DailyCollectionRow {
  invoiceNumber: string;
  patientName: string;
  amount: number;
  method: string;
  status: string;
  time: string;
}

interface DailyHourlyBar {
  hour: string;
  amount: number;
  percentage: number;
}

interface DepartmentRow {
  department: string;
  revenue: number;
  patientCount: number;
  averageBill: number;
  percentage: number;
}

interface PendingDueRow {
  patientName: string;
  invoiceNumber: string;
  total: number;
  paid: number;
  due: number;
  dueDate: string;
  daysOverdue: number;
}

interface ProfitLossMonth {
  month: string;
  revenue: number;
  expenses: number;
  profit: number;
}

interface RefundRow {
  id: number;
  invoiceNumber: string;
  patientName: string;
  amount: number;
  reason: string;
  status: string;
  refundDate: string;
}

interface InsuranceRow {
  invoiceNumber: string;
  patientName: string;
  provider: string;
  claimed: number;
  approved: number;
  status: string;
  claimDate: string;
}

interface DoctorRevenueRow {
  doctorName: string;
  department: string;
  revenue: number;
  patientCount: number;
  averageBill: number;
}

interface PharmacyRow {
  medicineName: string;
  quantitySold: number;
  revenue: number;
  cost: number;
  profit: number;
}

interface LabRow {
  testName: string;
  count: number;
  revenue: number;
  averagePrice: number;
}

@Component({
  selector: 'app-billing-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './billing-reports.component.html',
  styleUrls: ['./billing-reports.component.css']
})
export class BillingReportsComponent implements OnInit {

  activeTab = 'daily-collection';
  loading = false;
  generatingReport = false;
  error = '';

  startDate = '';
  endDate = '';
  selectedDate = '';

  tabs: ReportTab[] = [
    { id: 'daily-collection', label: 'Daily Collection', icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z' },
    { id: 'monthly-collection', label: 'Monthly Collection', icon: 'M3 3v18h18M9 17V9m4 8V5m4 12v-4' },
    { id: 'department-revenue', label: 'Department Revenue', icon: 'M3 3h18v18H3z' },
    { id: 'doctor-revenue', label: 'Doctor Revenue', icon: 'M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2M9 11a4 4 0 100-8 4 4 0 000 8z' },
    { id: 'pharmacy-revenue', label: 'Pharmacy Revenue', icon: 'M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h14a2 2 0 012 2v14a2 2 0 01-2 2z' },
    { id: 'laboratory-revenue', label: 'Laboratory Revenue', icon: 'M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2z' },
    { id: 'insurance-report', label: 'Insurance Report', icon: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z' },
    { id: 'pending-due', label: 'Pending Due Report', icon: 'M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z' },
    { id: 'refund-report', label: 'Refund Report', icon: 'M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6' },
    { id: 'profit-loss', label: 'Profit & Loss', icon: 'M13 7h8m0 0v8m0-8l-8 8-4-4-6 6' }
  ];

  dailyCollection: DailyCollectionRow[] = [];
  dailyHourlyBars: DailyHourlyBar[] = [];
  dailyTotalCollection = 0;
  dailyCashTotal = 0;
  dailyCardTotal = 0;
  dailyMobileTotal = 0;
  dailyInsuranceTotal = 0;
  dailyBillCount = 0;

  monthlyCollection: any[] = [];
  monthlyTotalRevenue = 0;
  monthlyAvgDaily = 0;
  monthlyPeakDay = '';
  monthlyPeakAmount = 0;
  monthlyTotalBills = 0;

  departmentRevenue: DepartmentRow[] = [];
  maxDepartmentRevenue = 0;
  departmentTotalRevenue = 0;

  doctorRevenue: DoctorRevenueRow[] = [];
  maxDoctorRevenue = 0;

  pharmacyRevenue: PharmacyRow[] = [];
  pharmacyTotalRevenue = 0;
  pharmacyTotalProfit = 0;

  labRevenue: LabRow[] = [];
  labTotalRevenue = 0;
  labTotalTests = 0;

  insuranceClaims: InsuranceRow[] = [];
  insuranceTotalClaimed = 0;
  insuranceTotalApproved = 0;
  insurancePendingCount = 0;

  pendingDues: PendingDueRow[] = [];
  pendingTotalDue = 0;
  pendingOverdue60 = 0;
  pendingOverdue30 = 0;

  refunds: RefundRow[] = [];
  refundTotalAmount = 0;
  refundPendingCount = 0;
  refundApprovedCount = 0;

  profitLoss: ProfitLossMonth[] = [];
  plTotalRevenue = 0;
  plTotalExpenses = 0;
  plTotalProfit = 0;
  maxPLValue = 0;

  constructor(
    private dashboardService: BillingDashboardService,
    private paymentService: PaymentService,
    private refundService: RefundService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.setDefaultDates();
    this.generateReport();
  }

  setDefaultDates(): void {
    const today = new Date();
    this.selectedDate = today.toISOString().split('T')[0];
    const firstOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
    this.startDate = firstOfMonth.toISOString().split('T')[0];
    this.endDate = today.toISOString().split('T')[0];
  }

  switchTab(tabId: string): void {
    this.activeTab = tabId;
    this.generateReport();
  }

  generateReport(): void {
    this.generatingReport = true;
    this.error = '';

    switch (this.activeTab) {
      case 'daily-collection': this.loadDailyCollection(); break;
      case 'monthly-collection': this.loadMonthlyCollection(); break;
      case 'department-revenue': this.loadDepartmentRevenue(); break;
      case 'doctor-revenue': this.loadDoctorRevenue(); break;
      case 'pharmacy-revenue': this.loadPharmacyRevenue(); break;
      case 'laboratory-revenue': this.loadLabRevenue(); break;
      case 'insurance-report': this.loadInsuranceReport(); break;
      case 'pending-due': this.loadPendingDueReport(); break;
      case 'refund-report': this.loadRefundReport(); break;
      case 'profit-loss': this.loadProfitLoss(); break;
    }
  }

  private loadDailyCollection(): void {
    this.paymentService.getDailyRevenue(this.selectedDate).subscribe({
      next: (data: any) => {
        if (data && data.payments) {
          this.dailyCollection = data.payments.map((p: any) => {
            let time = p.paymentDate ?? '';
            if (time.includes('T')) {
              const t = new Date(time);
              if (!isNaN(t.getTime())) {
                time = t.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
              }
            }
            return {
              invoiceNumber: p.invoiceNumber ?? `INV-${Math.floor(Math.random() * 90000) + 10000}`,
              patientName: p.patientName ?? 'N/A',
              amount: p.amount ?? 0,
              method: p.paymentMethod ?? 'CASH',
              status: p.paymentStatus ?? 'PAID',
              time
            };
          });
          this.dailyTotalCollection = data.totalRevenue ?? this.dailyCollection.reduce((s, r) => s + r.amount, 0);
          this.dailyBillCount = data.billCount ?? this.dailyCollection.length;
        } else {
          this.dailyCollection = [];
          this.dailyTotalCollection = 0;
          this.dailyBillCount = 0;
        }
        this.computeDailyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.dailyCollection = [];
        this.dailyTotalCollection = 0;
        this.dailyBillCount = 0;
        this.computeDailyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockDailyCollection(): void {
    const methods = ['Cash', 'Card', 'Mobile', 'Insurance'];
    const statuses = ['PAID', 'PAID', 'PAID', 'PENDING', 'PARTIAL'];
    const names = ['Alice Johnson', 'Bob Smith', 'Carol White', 'David Brown', 'Emma Davis', 'Frank Miller', 'Grace Wilson', 'Henry Moore'];
    this.dailyCollection = Array.from({ length: 8 }, (_, i) => ({
      invoiceNumber: `INV-${String(10001 + i).padStart(5, '0')}`,
      patientName: names[i],
      amount: Math.floor(Math.random() * 800) + 100,
      method: methods[Math.floor(Math.random() * methods.length)],
      status: statuses[Math.floor(Math.random() * statuses.length)],
      time: `${8 + i}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')} AM`
    }));
  }

  private computeDailyStats(): void {
    this.dailyTotalCollection = this.dailyCollection.reduce((s, r) => s + r.amount, 0);
    const byMethod = (m: string) => this.dailyCollection.filter(r => (r.method || '').toUpperCase() === m).reduce((s, r) => s + r.amount, 0);
    this.dailyCashTotal = byMethod('CASH');
    this.dailyCardTotal = byMethod('CARD');
    this.dailyMobileTotal = byMethod('MOBILE');
    this.dailyInsuranceTotal = byMethod('INSURANCE');
    this.dailyBillCount = this.dailyCollection.length;
    this.computeHourlyBars();
  }

  private computeHourlyBars(): void {
    const hourMap: Record<string, number> = {};
    for (let h = 8; h <= 17; h++) {
      const label = h <= 12 ? `${h} ${h < 12 ? 'AM' : 'PM'}` : `${h - 12} PM`;
      hourMap[label] = 0;
    }
    this.dailyCollection.forEach(r => {
      const timeStr = r.time || '';
      let h = NaN;
      const tMatch = timeStr.match(/T(\d{1,2}):/);
      const aMatch = timeStr.match(/(\d{1,2})(?::(\d{2}))?\s*(AM|PM)/i);
      if (tMatch) {
        h = parseInt(tMatch[1], 10);
      } else if (aMatch) {
        let hour = parseInt(aMatch[1], 10);
        const period = aMatch[3].toUpperCase();
        if (period === 'PM' && hour < 12) hour += 12;
        if (period === 'AM' && hour === 12) hour = 0;
        h = hour;
      }
      if (!isNaN(h) && h >= 8 && h <= 17) {
        const label = h === 12 ? '12 PM' : h < 12 ? `${h} AM` : `${h - 12} PM`;
        hourMap[label] += r.amount;
      }
    });
    const maxVal = Math.max(...Object.values(hourMap), 1);
    this.dailyHourlyBars = Object.entries(hourMap).map(([hour, amount]) => ({
      hour,
      amount,
      percentage: (amount / maxVal) * 100
    }));
  }

  private loadMonthlyCollection(): void {
    const now = new Date();
    this.dashboardService.getMonthlyCollection(now.getFullYear(), now.getMonth() + 1).subscribe({
      next: (data: any) => {
        if (data && data.dailyBreakdown) {
          this.monthlyCollection = data.dailyBreakdown;
          this.monthlyTotalRevenue = data.totalRevenue ?? 0;
          this.monthlyTotalBills = data.totalBills ?? 0;
        } else {
          this.monthlyCollection = [];
          this.monthlyTotalRevenue = 0;
          this.monthlyTotalBills = 0;
        }
        this.computeMonthlyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.monthlyCollection = [];
        this.monthlyTotalRevenue = 0;
        this.monthlyTotalBills = 0;
        this.computeMonthlyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private computeMonthlyStats(): void {
    if (this.monthlyCollection.length) {
      this.monthlyAvgDaily = Math.round(this.monthlyTotalRevenue / this.monthlyCollection.length);
      const peak = this.monthlyCollection.reduce((max, d) => d.revenue > max.revenue ? d : max, this.monthlyCollection[0]);
      this.monthlyPeakDay = peak.date;
      this.monthlyPeakAmount = peak.revenue;
    }
  }

  private loadDepartmentRevenue(): void {
    this.dashboardService.getDepartmentRevenue().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.departmentRevenue = data.map((d: any) => ({
            department: d.department ?? 'Unknown',
            revenue: d.revenue ?? 0,
            patientCount: d.patientCount ?? 0,
            averageBill: 0,
            percentage: d.percentage ?? 0
          }));
          this.departmentRevenue.forEach(d => {
            d.averageBill = d.patientCount > 0 ? Math.round(d.revenue / d.patientCount) : 0;
          });
          this.maxDepartmentRevenue = Math.max(...this.departmentRevenue.map(d => d.revenue), 1);
          this.departmentTotalRevenue = this.departmentRevenue.reduce((s, d) => s + d.revenue, 0);
          if (!this.departmentRevenue[0].percentage) {
            this.departmentRevenue.forEach(d => {
              d.percentage = this.departmentTotalRevenue > 0 ? Math.round((d.revenue / this.departmentTotalRevenue) * 100) : 0;
            });
          }
        } else {
          this.departmentRevenue = [];
          this.departmentTotalRevenue = 0;
          this.maxDepartmentRevenue = 1;
        }
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.departmentRevenue = [];
        this.departmentTotalRevenue = 0;
        this.maxDepartmentRevenue = 1;
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadDoctorRevenue(): void {
    this.dashboardService.getDoctorRevenue().subscribe({
      next: (data: any[]) => {
        this.doctorRevenue = (data && data.length)
          ? data.map((d: any) => ({
              doctorName: d.doctorName ?? 'N/A',
              department: d.department ?? 'General',
              revenue: d.revenue ?? 0,
              patientCount: d.patientCount ?? 0,
              averageBill: d.averageBill ?? 0
            }))
          : [];
        this.maxDoctorRevenue = Math.max(...this.doctorRevenue.map(d => d.revenue), 1);
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.doctorRevenue = [];
        this.maxDoctorRevenue = 1;
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadPharmacyRevenue(): void {
    this.dashboardService.getPharmacyRevenue().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.pharmacyRevenue = data.map((r: any) => ({
            medicineName: r.medicineName ?? 'N/A',
            quantitySold: r.quantitySold ?? 0,
            revenue: r.revenue ?? 0,
            cost: r.cost ?? 0,
            profit: r.profit ?? 0
          }));
          this.pharmacyTotalRevenue = this.pharmacyRevenue.reduce((s, r) => s + r.revenue, 0);
          this.pharmacyTotalProfit = this.pharmacyRevenue.reduce((s, r) => s + r.profit, 0);
        } else {
          this.pharmacyRevenue = [];
          this.pharmacyTotalRevenue = 0;
          this.pharmacyTotalProfit = 0;
        }
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.pharmacyRevenue = [];
        this.pharmacyTotalRevenue = 0;
        this.pharmacyTotalProfit = 0;
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadLabRevenue(): void {
    this.dashboardService.getLabRevenue().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.labRevenue = data.map((r: any) => ({
            testName: r.testName ?? 'N/A',
            count: r.count ?? 0,
            revenue: r.revenue ?? 0,
            averagePrice: r.averagePrice ?? 0
          }));
          this.labTotalRevenue = this.labRevenue.reduce((s, r) => s + r.revenue, 0);
          this.labTotalTests = this.labRevenue.reduce((s, r) => s + r.count, 0);
        } else {
          this.labRevenue = [];
          this.labTotalRevenue = 0;
          this.labTotalTests = 0;
        }
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.labRevenue = [];
        this.labTotalRevenue = 0;
        this.labTotalTests = 0;
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadInsuranceReport(): void {
    this.dashboardService.getInsuranceReport().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.insuranceClaims = data.map((c: any) => ({
            invoiceNumber: c.invoiceNumber ?? 'N/A',
            patientName: c.patientName ?? 'N/A',
            provider: c.provider ?? 'N/A',
            claimed: c.claimed ?? 0,
            approved: c.approved ?? 0,
            status: c.status ?? 'N/A',
            claimDate: c.claimDate ?? ''
          }));
        } else {
          this.insuranceClaims = [];
        }
        this.insuranceTotalClaimed = this.insuranceClaims.reduce((s, c) => s + c.claimed, 0);
        this.insuranceTotalApproved = this.insuranceClaims.reduce((s, c) => s + c.approved, 0);
        this.insurancePendingCount = this.insuranceClaims.filter(c => c.status === 'PENDING' || c.status === 'SUBMITTED' || c.status === 'UNDER_REVIEW').length;
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.insuranceClaims = [];
        this.insuranceTotalClaimed = 0;
        this.insuranceTotalApproved = 0;
        this.insurancePendingCount = 0;
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadPendingDueReport(): void {
    this.dashboardService.getPendingDue().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.pendingDues = data.map((d: any) => ({
            patientName: d.patientName ?? 'N/A',
            invoiceNumber: d.invoiceNumber ?? 'N/A',
            total: d.total ?? 0,
            paid: d.paid ?? 0,
            due: d.due ?? 0,
            dueDate: d.dueDate ?? '',
            daysOverdue: d.daysOverdue ?? 0
          }));
        } else {
          this.pendingDues = [];
        }
        this.pendingTotalDue = this.pendingDues.reduce((s, d) => s + d.due, 0);
        this.pendingOverdue60 = this.pendingDues.filter(d => d.daysOverdue >= 60).length;
        this.pendingOverdue30 = this.pendingDues.filter(d => d.daysOverdue >= 30 && d.daysOverdue < 60).length;
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.pendingDues = [];
        this.pendingTotalDue = 0;
        this.pendingOverdue60 = 0;
        this.pendingOverdue30 = 0;
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadRefundReport(): void {
    this.refundService.getAll().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.refunds = data.map((r: any) => ({
            id: r.id ?? 0,
            invoiceNumber: r.invoiceNumber ?? 'N/A',
            patientName: r.patientName ?? 'N/A',
            amount: r.refundAmount ?? r.amount ?? 0,
            reason: r.refundReason ?? r.reason ?? 'N/A',
            status: r.refundStatus ?? r.status ?? 'PENDING',
            refundDate: r.createdDate ? r.createdDate.substring(0, 10) : (r.refundDate ?? '')
          }));
        } else {
          this.refunds = [];
        }
        this.computeRefundStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.refunds = [];
        this.computeRefundStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private computeRefundStats(): void {
    this.refundTotalAmount = this.refunds.reduce((s, r) => s + r.amount, 0);
    this.refundPendingCount = this.refunds.filter(r => r.status === 'PENDING').length;
    this.refundApprovedCount = this.refunds.filter(r => r.status === 'APPROVED' || r.status === 'PROCESSED').length;
  }

  private loadProfitLoss(): void {
    this.dashboardService.getProfitLoss().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.profitLoss = data.map((d: any) => ({
            month: d.month,
            revenue: d.revenue ?? 0,
            expenses: d.expenses ?? 0,
            profit: d.profit ?? 0
          }));
        } else {
          this.profitLoss = [];
        }
        this.computePLStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.profitLoss = [];
        this.computePLStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private computePLStats(): void {
    this.plTotalRevenue = this.profitLoss.reduce((s, p) => s + p.revenue, 0);
    this.plTotalExpenses = this.profitLoss.reduce((s, p) => s + p.expenses, 0);
    this.plTotalProfit = this.profitLoss.reduce((s, p) => s + p.profit, 0);
    this.maxPLValue = Math.max(...this.profitLoss.map(p => Math.max(p.revenue, p.expenses, p.profit)), 1);
  }

  getDeptBarWidth(revenue: number): number {
    return this.maxDepartmentRevenue > 0 ? (revenue / this.maxDepartmentRevenue) * 100 : 0;
  }

  getDoctorBarWidth(revenue: number): number {
    return this.maxDoctorRevenue > 0 ? (revenue / this.maxDoctorRevenue) * 100 : 0;
  }

  getMonthlyBarHeight(revenue: number): number {
    const maxRev = Math.max(...this.monthlyCollection.map(d => d.revenue), 1);
    return (revenue / maxRev) * 100;
  }

  getPLBarHeight(value: number): number {
    return this.maxPLValue > 0 ? (value / this.maxPLValue) * 100 : 0;
  }

  getOverdueClass(days: number): string {
    if (days >= 60) return 'overdue--red';
    if (days >= 30) return 'overdue--yellow';
    return 'overdue--green';
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PAID':
      case 'APPROVED':
      case 'PROCESSED': return 'badge--success';
      case 'PENDING':
      case 'PROCESSING': return 'badge--warning';
      case 'REJECTED':
      case 'CANCELLED': return 'badge--danger';
      default: return 'badge--info';
    }
  }

  exportPdf(): void {
    window.print();
  }

  exportExcel(): void {
    let csvContent = '';
    switch (this.activeTab) {
      case 'daily-collection':
        csvContent = 'Invoice,Patient,Amount,Method,Status,Time\n';
        csvContent += this.dailyCollection.map(r =>
          `${r.invoiceNumber},${r.patientName},${r.amount},${r.method},${r.status},${r.time}`
        ).join('\n');
        break;
      case 'department-revenue':
        csvContent = 'Department,Revenue,Patient Count,Average Bill\n';
        csvContent += this.departmentRevenue.map(r =>
          `${r.department},${r.revenue},${r.patientCount},${r.averageBill}`
        ).join('\n');
        break;
      case 'pending-due':
        csvContent = 'Patient,Invoice,Total,Paid,Due,Days Overdue\n';
        csvContent += this.pendingDues.map(r =>
          `${r.patientName},${r.invoiceNumber},${r.total},${r.paid},${r.due},${r.daysOverdue}`
        ).join('\n');
        break;
      case 'refund-report':
        csvContent = 'Invoice,Patient,Amount,Reason,Status,Date\n';
        csvContent += this.refunds.map(r =>
          `${r.invoiceNumber},${r.patientName},${r.amount},${r.reason},${r.status},${r.refundDate}`
        ).join('\n');
        break;
      default:
        csvContent = 'No data available for this report type';
    }
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${this.activeTab}-report.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  }

  printReport(): void {
    window.print();
  }

  formatCurrency(value: number): string {
    return '৳' + value.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
  }

  formatMonthLabel(dateStr: string): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    if (isNaN(d.getTime())) {
      return dateStr;
    }
    return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  }

  formatProfitLossMonth(monthStr: string): string {
    if (!monthStr) return '';
    const d = new Date(monthStr + (monthStr.length === 7 ? '-01' : ''));
    if (isNaN(d.getTime())) {
      return monthStr;
    }
    return d.toLocaleDateString('en-US', { month: 'short', year: 'numeric' });
  }
}
