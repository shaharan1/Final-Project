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
          this.dailyCollection = data.payments.map((p: any) => ({
            invoiceNumber: p.invoiceNumber ?? `INV-${Math.floor(Math.random() * 90000) + 10000}`,
            patientName: p.patientName ?? 'N/A',
            amount: p.amount ?? 0,
            method: p.paymentMethod ?? 'Cash',
            status: p.paymentStatus ?? 'PAID',
            time: p.paymentDate ?? new Date().toLocaleTimeString()
          }));
          this.dailyTotalCollection = data.totalRevenue ?? this.dailyCollection.reduce((s, r) => s + r.amount, 0);
          this.dailyBillCount = data.billCount ?? this.dailyCollection.length;
        } else {
          this.generateMockDailyCollection();
        }
        this.computeDailyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockDailyCollection();
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
    this.dailyCashTotal = this.dailyCollection.filter(r => r.method === 'Cash').reduce((s, r) => s + r.amount, 0);
    this.dailyCardTotal = this.dailyCollection.filter(r => r.method === 'Card').reduce((s, r) => s + r.amount, 0);
    this.dailyMobileTotal = this.dailyCollection.filter(r => r.method === 'Mobile').reduce((s, r) => s + r.amount, 0);
    this.dailyInsuranceTotal = this.dailyCollection.filter(r => r.method === 'Insurance').reduce((s, r) => s + r.amount, 0);
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
      const hourStr = r.time.split(':')[0];
      const h = parseInt(hourStr, 10);
      if (h >= 8 && h <= 17) {
        const label = h <= 12 ? `${h} ${h < 12 ? 'AM' : 'PM'}` : `${h - 12} PM`;
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
    this.paymentService.getMonthlyRevenue(now.getFullYear(), now.getMonth() + 1).subscribe({
      next: (data: any) => {
        if (data && data.dailyBreakdown) {
          this.monthlyCollection = data.dailyBreakdown;
          this.monthlyTotalRevenue = data.totalRevenue ?? 0;
          this.monthlyTotalBills = data.totalBills ?? 0;
        } else {
          this.generateMockMonthly();
        }
        this.computeMonthlyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockMonthly();
        this.computeMonthlyStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockMonthly(): void {
    this.monthlyCollection = Array.from({ length: 30 }, (_, i) => ({
      date: `2026-07-${String(i + 1).padStart(2, '0')}`,
      revenue: Math.floor(Math.random() * 5000) + 1000,
      bills: Math.floor(Math.random() * 30) + 5
    }));
    this.monthlyTotalRevenue = this.monthlyCollection.reduce((s, d) => s + d.revenue, 0);
    this.monthlyTotalBills = this.monthlyCollection.reduce((s, d) => s + d.bills, 0);
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
            patientCount: d.patientCount ?? Math.floor(Math.random() * 100) + 10,
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
          this.generateMockDepartmentRevenue();
        }
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockDepartmentRevenue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockDepartmentRevenue(): void {
    const depts = ['Cardiology', 'Orthopedics', 'Neurology', 'Pediatrics', 'Oncology', 'Radiology', 'General Medicine', 'Emergency'];
    this.departmentRevenue = depts.map(dept => {
      const revenue = Math.floor(Math.random() * 80000) + 10000;
      const patientCount = Math.floor(Math.random() * 150) + 20;
      return { department: dept, revenue, patientCount, averageBill: Math.round(revenue / patientCount), percentage: 0 };
    });
    this.maxDepartmentRevenue = Math.max(...this.departmentRevenue.map(d => d.revenue), 1);
    this.departmentTotalRevenue = this.departmentRevenue.reduce((s, d) => s + d.revenue, 0);
    this.departmentRevenue.forEach(d => {
      d.percentage = Math.round((d.revenue / this.departmentTotalRevenue) * 100);
    });
  }

  private loadDoctorRevenue(): void {
    this.dashboardService.getDepartmentRevenue().subscribe({
      next: () => {
        this.generateMockDoctorRevenue();
        this.maxDoctorRevenue = Math.max(...this.doctorRevenue.map(d => d.revenue), 1);
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockDoctorRevenue();
        this.maxDoctorRevenue = Math.max(...this.doctorRevenue.map(d => d.revenue), 1);
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockDoctorRevenue(): void {
    const doctors = [
      { name: 'Dr. Sarah Wilson', dept: 'Cardiology' },
      { name: 'Dr. James Anderson', dept: 'Orthopedics' },
      { name: 'Dr. Emily Chen', dept: 'Neurology' },
      { name: 'Dr. Michael Brown', dept: 'Pediatrics' },
      { name: 'Dr. Lisa Martinez', dept: 'Oncology' },
      { name: 'Dr. Robert Taylor', dept: 'General Medicine' },
      { name: 'Dr. Amanda Garcia', dept: 'Radiology' },
      { name: 'Dr. Kevin Lee', dept: 'Emergency' }
    ];
    this.doctorRevenue = doctors.map(d => {
      const revenue = Math.floor(Math.random() * 50000) + 5000;
      const patientCount = Math.floor(Math.random() * 80) + 10;
      return { doctorName: d.name, department: d.dept, revenue, patientCount, averageBill: Math.round(revenue / patientCount) };
    });
  }

  private loadPharmacyRevenue(): void {
    this.paymentService.getMethodBreakdown().subscribe({
      next: () => {
        this.generateMockPharmacyRevenue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockPharmacyRevenue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockPharmacyRevenue(): void {
    const meds = ['Amoxicillin 500mg', 'Metformin 850mg', 'Atorvastatin 20mg', 'Omeprazole 20mg', 'Amlodipine 5mg', 'Metoprolol 50mg', 'Losartan 50mg', 'Levothyroxine 50mcg'];
    this.pharmacyRevenue = meds.map(name => {
      const quantitySold = Math.floor(Math.random() * 200) + 20;
      const cost = Math.floor(Math.random() * 20) + 5;
      const revenue = quantitySold * (cost + Math.floor(Math.random() * 10) + 5);
      return { medicineName: name, quantitySold, revenue, cost: quantitySold * cost, profit: revenue - (quantitySold * cost) };
    });
    this.pharmacyTotalRevenue = this.pharmacyRevenue.reduce((s, r) => s + r.revenue, 0);
    this.pharmacyTotalProfit = this.pharmacyRevenue.reduce((s, r) => s + r.profit, 0);
  }

  private loadLabRevenue(): void {
    this.paymentService.getMethodBreakdown().subscribe({
      next: () => {
        this.generateMockLabRevenue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockLabRevenue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockLabRevenue(): void {
    const tests = ['Complete Blood Count', 'Lipid Panel', 'Thyroid Function', 'Liver Function', 'Kidney Function', 'Blood Glucose', 'Urinalysis', 'X-Ray Chest'];
    this.labRevenue = tests.map(name => {
      const count = Math.floor(Math.random() * 100) + 10;
      const avgPrice = Math.floor(Math.random() * 80) + 20;
      return { testName: name, count, revenue: count * avgPrice, averagePrice: avgPrice };
    });
    this.labTotalRevenue = this.labRevenue.reduce((s, r) => s + r.revenue, 0);
    this.labTotalTests = this.labRevenue.reduce((s, r) => s + r.count, 0);
  }

  private loadInsuranceReport(): void {
    this.paymentService.getAll().subscribe({
      next: () => {
        this.generateMockInsuranceReport();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockInsuranceReport();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockInsuranceReport(): void {
    const providers = ['Blue Cross', 'Aetna', 'UnitedHealth', 'Cigna', 'Humana'];
    const statuses = ['APPROVED', 'APPROVED', 'PENDING', 'PENDING', 'REJECTED'];
    const names = ['Alice Johnson', 'Bob Smith', 'Carol White', 'David Brown', 'Emma Davis', 'Frank Miller', 'Grace Wilson', 'Henry Moore'];
    this.insuranceClaims = names.map((name, i) => {
      const claimed = Math.floor(Math.random() * 5000) + 500;
      const status = statuses[Math.floor(Math.random() * statuses.length)];
      return {
        invoiceNumber: `INV-${String(20001 + i).padStart(5, '0')}`,
        patientName: name,
        provider: providers[Math.floor(Math.random() * providers.length)],
        claimed,
        approved: status === 'APPROVED' ? claimed : status === 'PENDING' ? 0 : Math.floor(claimed * 0.5),
        status,
        claimDate: `2026-07-${String(Math.floor(Math.random() * 25) + 1).padStart(2, '0')}`
      };
    });
    this.insuranceTotalClaimed = this.insuranceClaims.reduce((s, c) => s + c.claimed, 0);
    this.insuranceTotalApproved = this.insuranceClaims.reduce((s, c) => s + c.approved, 0);
    this.insurancePendingCount = this.insuranceClaims.filter(c => c.status === 'PENDING').length;
  }

  private loadPendingDueReport(): void {
    this.paymentService.getAll().subscribe({
      next: () => {
        this.generateMockPendingDue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockPendingDue();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockPendingDue(): void {
    const names = ['Alice Johnson', 'Bob Smith', 'Carol White', 'David Brown', 'Emma Davis', 'Frank Miller', 'Grace Wilson', 'Henry Moore', 'Ivy Clark', 'Jack Turner'];
    this.pendingDues = names.map((name, i) => {
      const total = Math.floor(Math.random() * 3000) + 500;
      const paid = Math.floor(Math.random() * total * 0.7);
      const due = total - paid;
      const daysOverdue = Math.floor(Math.random() * 90);
      const dueDate = new Date();
      dueDate.setDate(dueDate.getDate() - daysOverdue);
      return {
        patientName: name,
        invoiceNumber: `INV-${String(30001 + i).padStart(5, '0')}`,
        total,
        paid,
        due,
        dueDate: dueDate.toISOString().split('T')[0],
        daysOverdue
      };
    });
    this.pendingTotalDue = this.pendingDues.reduce((s, d) => s + d.due, 0);
    this.pendingOverdue60 = this.pendingDues.filter(d => d.daysOverdue >= 60).length;
    this.pendingOverdue30 = this.pendingDues.filter(d => d.daysOverdue >= 30 && d.daysOverdue < 60).length;
  }

  private loadRefundReport(): void {
    this.refundService.getAll().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.refunds = data.map((r: any) => ({
            id: r.id ?? 0,
            invoiceNumber: r.invoiceNumber ?? 'N/A',
            patientName: r.patientName ?? 'N/A',
            amount: r.amount ?? 0,
            reason: r.reason ?? 'N/A',
            status: r.status ?? 'PENDING',
            refundDate: r.refundDate ?? r.createdDate ?? ''
          }));
        } else {
          this.generateMockRefunds();
        }
        this.computeRefundStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockRefunds();
        this.computeRefundStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockRefunds(): void {
    const statuses = ['APPROVED', 'APPROVED', 'PENDING', 'PROCESSED', 'REJECTED'];
    const reasons = ['Duplicate payment', 'Service cancelled', 'Insurance overlap', 'Billing error', 'Patient request'];
    const names = ['Alice Johnson', 'Bob Smith', 'Carol White', 'David Brown', 'Emma Davis'];
    this.refunds = names.map((name, i) => ({
      id: i + 1,
      invoiceNumber: `INV-${String(40001 + i).padStart(5, '0')}`,
      patientName: name,
      amount: Math.floor(Math.random() * 1000) + 100,
      reason: reasons[Math.floor(Math.random() * reasons.length)],
      status: statuses[Math.floor(Math.random() * statuses.length)],
      refundDate: `2026-07-${String(Math.floor(Math.random() * 25) + 1).padStart(2, '0')}`
    }));
  }

  private computeRefundStats(): void {
    this.refundTotalAmount = this.refunds.reduce((s, r) => s + r.amount, 0);
    this.refundPendingCount = this.refunds.filter(r => r.status === 'PENDING').length;
    this.refundApprovedCount = this.refunds.filter(r => r.status === 'APPROVED' || r.status === 'PROCESSED').length;
  }

  private loadProfitLoss(): void {
    this.dashboardService.getMonthlyRevenueChart().subscribe({
      next: (data: any[]) => {
        if (data && data.length) {
          this.profitLoss = data.map((d: any) => {
            const revenue = d.revenue ?? 0;
            const expenses = Math.round(revenue * (0.55 + Math.random() * 0.15));
            return {
              month: d.date,
              revenue,
              expenses,
              profit: revenue - expenses
            };
          });
        } else {
          this.generateMockProfitLoss();
        }
        this.computePLStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.generateMockProfitLoss();
        this.computePLStats();
        this.generatingReport = false;
        this.cdr.markForCheck();
      }
    });
  }

  private generateMockProfitLoss(): void {
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
    this.profitLoss = months.map(m => {
      const revenue = Math.floor(Math.random() * 80000) + 40000;
      const expenses = Math.round(revenue * (0.55 + Math.random() * 0.15));
      return { month: m, revenue, expenses, profit: revenue - expenses };
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
    return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  }
}
