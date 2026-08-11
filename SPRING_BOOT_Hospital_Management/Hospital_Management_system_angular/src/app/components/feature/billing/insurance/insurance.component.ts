import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { InsuranceService } from '../../../../services/billing/insurance.service';
import { InsuranceClaimService } from '../../../../services/billing/insurance-claim.service';
import { PaymentService } from '../../../../services/billing/payment.service';
import { InsuranceCompany, InsuranceClaim } from '../../../../models/billing/insurance.model';

@Component({
  selector: 'app-insurance',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './insurance.component.html',
  styleUrls: ['./insurance.component.css']
})
export class InsuranceComponent implements OnInit {
  activeTab: 'companies' | 'claims' | 'newClaim' = 'companies';
  loading = false;
  loadingClaims = false;
  error = '';
  successMessage = '';

  companies: InsuranceCompany[] = [];
  filteredCompanies: InsuranceCompany[] = [];
  companySearch = '';

  claims: InsuranceClaim[] = [];
  filteredClaims: InsuranceClaim[] = [];
  claimSearch = '';
  claimStatusFilter = '';

  showCompanyModal = false;
  editingCompany: InsuranceCompany | null = null;
  companyForm: Partial<InsuranceCompany> = this.getEmptyCompanyForm();

  showClaimDetailModal = false;
  showApproveClaimModal = false;
  showRejectClaimModal = false;
  selectedClaim: InsuranceClaim | null = null;
  approveAmount = 0;
  rejectReason = '';
  processingAction = false;

  invoiceSearch = '';
  invoiceData: any = null;
  invoiceNotFound = false;

  newClaim: Partial<InsuranceClaim> = this.getEmptyClaimForm();

  totalCompanies = 0;
  activeCompanies = 0;
  totalClaims = 0;
  pendingClaims = 0;
  approvedClaims = 0;

  constructor(
    private insuranceService: InsuranceService,
    private claimService: InsuranceClaimService,
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCompanies();
    this.loadClaims();
  }

  loadCompanies(): void {
    this.loading = true;
    this.error = '';
    this.insuranceService.getAll().subscribe({
      next: (data: InsuranceCompany[]) => {
        this.companies = data;
        this.filteredCompanies = [...this.companies];
        this.computeCompanyStats();
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to load insurance companies';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  loadClaims(): void {
    this.loadingClaims = true;
    this.claimService.getAll().subscribe({
      next: (data: InsuranceClaim[]) => {
        this.claims = data;
        this.filteredClaims = [...this.claims];
        this.computeClaimStats();
        this.loadingClaims = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingClaims = false;
        this.cdr.markForCheck();
      }
    });
  }

  computeCompanyStats(): void {
    this.totalCompanies = this.companies.length;
    this.activeCompanies = this.companies.filter(c => c.active).length;
  }

  computeClaimStats(): void {
    this.totalClaims = this.claims.length;
    this.pendingClaims = this.claims.filter(c => c.claimStatus === 'PENDING').length;
    this.approvedClaims = this.claims.filter(c => c.claimStatus === 'APPROVED').length;
  }

  filterCompanies(): void {
    let result = [...this.companies];
    if (this.companySearch.trim()) {
      const q = this.companySearch.toLowerCase();
      result = result.filter(c =>
        c.companyName.toLowerCase().includes(q) ||
        c.contactPerson.toLowerCase().includes(q)
      );
    }
    this.filteredCompanies = result;
  }

  filterClaims(): void {
    let result = [...this.claims];
    if (this.claimSearch.trim()) {
      const q = this.claimSearch.toLowerCase();
      result = result.filter(c =>
        (c.claimNumber || '').toLowerCase().includes(q) ||
        c.patientName.toLowerCase().includes(q) ||
        c.insuranceCompanyName.toLowerCase().includes(q)
      );
    }
    if (this.claimStatusFilter) {
      result = result.filter(c => c.claimStatus === this.claimStatusFilter);
    }
    this.filteredClaims = result;
  }

  // ── Company CRUD ──────────────────────────────
  openAddCompanyModal(): void {
    this.editingCompany = null;
    this.companyForm = this.getEmptyCompanyForm();
    this.showCompanyModal = true;
  }

  openEditCompanyModal(company: InsuranceCompany): void {
    this.editingCompany = company;
    this.companyForm = { ...company };
    this.showCompanyModal = true;
  }

  saveCompany(): void {
    if (!this.companyForm.companyName) return;
    this.loading = true;
    if (this.editingCompany && this.editingCompany.id) {
      this.insuranceService.update(this.editingCompany.id, this.companyForm).subscribe({
        next: (updated: InsuranceCompany) => {
          const idx = this.companies.findIndex(c => c.id === updated.id);
          if (idx >= 0) this.companies[idx] = updated;
          this.filteredCompanies = [...this.companies];
          this.computeCompanyStats();
          this.showCompanyModal = false;
          this.loading = false;
          this.successMessage = 'Company updated successfully!';
          setTimeout(() => this.successMessage = '', 4000);
          this.cdr.markForCheck();
        },
        error: () => {
          this.error = 'Failed to update company';
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
    } else {
      this.insuranceService.create(this.companyForm).subscribe({
        next: (saved: InsuranceCompany) => {
          this.companies.unshift(saved);
          this.filteredCompanies = [...this.companies];
          this.computeCompanyStats();
          this.showCompanyModal = false;
          this.loading = false;
          this.successMessage = 'Company added successfully!';
          setTimeout(() => this.successMessage = '', 4000);
          this.cdr.markForCheck();
        },
        error: () => {
          this.error = 'Failed to add company';
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
    }
  }

  deleteCompany(company: InsuranceCompany): void {
    if (!company.id || !confirm(`Delete ${company.companyName}?`)) return;
    this.insuranceService.delete(company.id).subscribe({
      next: () => {
        this.companies = this.companies.filter(c => c.id !== company.id);
        this.filteredCompanies = [...this.companies];
        this.computeCompanyStats();
        this.successMessage = 'Company deleted.';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to delete company';
        this.cdr.markForCheck();
      }
    });
  }

  closeCompanyModal(): void {
    this.showCompanyModal = false;
    this.editingCompany = null;
  }

  // ── Claim Operations ──────────────────────────
  lookupInvoice(): void {
    if (!this.invoiceSearch.trim()) return;
    this.loading = true;
    this.invoiceNotFound = false;
    this.invoiceData = null;
    this.paymentService.getByInvoice(this.invoiceSearch).subscribe({
      next: (data: any) => {
        if (data && data.length > 0) {
          this.invoiceData = data[0];
          this.newClaim.invoiceNumber = this.invoiceData.invoiceNumber || this.invoiceSearch;
          this.newClaim.patientId = this.invoiceData.patientId || 0;
          this.newClaim.patientName = this.invoiceData.patientName || '';
        } else {
          this.invoiceNotFound = true;
        }
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.invoiceNotFound = true;
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  submitClaim(): void {
    if (!this.newClaim.invoiceNumber || !this.newClaim.insuranceId || !this.newClaim.claimAmount) return;
    this.loading = true;
    this.claimService.create(this.newClaim).subscribe({
      next: (saved: InsuranceClaim) => {
        this.claims.unshift(saved);
        this.filteredClaims = [...this.claims];
        this.computeClaimStats();
        this.resetClaimForm();
        this.loading = false;
        this.activeTab = 'claims';
        this.successMessage = 'Insurance claim submitted successfully!';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to submit claim';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  resetClaimForm(): void {
    this.newClaim = this.getEmptyClaimForm();
    this.invoiceSearch = '';
    this.invoiceData = null;
    this.invoiceNotFound = false;
  }

  onCompanySelect(): void {
    const company = this.companies.find(c => c.id === this.newClaim.insuranceId);
    if (company) {
      this.newClaim.insuranceCompanyName = company.companyName;
    }
  }

  viewClaimDetail(claim: InsuranceClaim): void {
    this.selectedClaim = claim;
    this.showClaimDetailModal = true;
  }

  openApproveClaimModal(claim: InsuranceClaim): void {
    this.selectedClaim = claim;
    this.approveAmount = claim.claimAmount;
    this.showApproveClaimModal = true;
  }

  approveClaim(): void {
    if (!this.selectedClaim || !this.selectedClaim.id) return;
    this.processingAction = true;
    this.claimService.approve(this.selectedClaim.id, this.approveAmount).subscribe({
      next: (updated: InsuranceClaim) => {
        this.updateClaimInList(updated);
        this.showApproveClaimModal = false;
        this.processingAction = false;
        this.successMessage = 'Claim approved successfully!';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to approve claim';
        this.processingAction = false;
        this.cdr.markForCheck();
      }
    });
  }

  openRejectClaimModal(claim: InsuranceClaim): void {
    this.selectedClaim = claim;
    this.rejectReason = '';
    this.showRejectClaimModal = true;
  }

  rejectClaim(): void {
    if (!this.selectedClaim || !this.selectedClaim.id || !this.rejectReason.trim()) return;
    this.processingAction = true;
    this.claimService.reject(this.selectedClaim.id, this.rejectReason).subscribe({
      next: (updated: InsuranceClaim) => {
        this.updateClaimInList(updated);
        this.showRejectClaimModal = false;
        this.processingAction = false;
        this.successMessage = 'Claim rejected.';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to reject claim';
        this.processingAction = false;
        this.cdr.markForCheck();
      }
    });
  }

  settleClaim(claim: InsuranceClaim): void {
    if (!claim.id || !confirm('Settle this claim?')) return;
    this.claimService.settle(claim.id).subscribe({
      next: (updated: InsuranceClaim) => {
        this.updateClaimInList(updated);
        this.successMessage = 'Claim settled successfully!';
        setTimeout(() => this.successMessage = '', 4000);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Failed to settle claim';
        this.cdr.markForCheck();
      }
    });
  }

  closeModals(): void {
    this.showClaimDetailModal = false;
    this.showApproveClaimModal = false;
    this.showRejectClaimModal = false;
    this.selectedClaim = null;
  }

  private updateClaimInList(updated: InsuranceClaim): void {
    const idx = this.claims.findIndex(c => c.id === updated.id);
    if (idx >= 0) {
      this.claims[idx] = updated;
      this.filteredClaims = [...this.claims];
      this.computeClaimStats();
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'PENDING': return 'badge-pending';
      case 'APPROVED': return 'badge-approved';
      case 'REJECTED': return 'badge-rejected';
      case 'SETTLED': return 'badge-settled';
      case 'SUBMITTED': return 'badge-submitted';
      default: return 'badge-pending';
    }
  }

  private getEmptyCompanyForm(): Partial<InsuranceCompany> {
    return {
      companyName: '',
      contactPerson: '',
      phone: '',
      email: '',
      address: '',
      policyPrefix: '',
      coveragePercentage: 0,
      maxCoverage: 0,
      active: true,
      notes: ''
    };
  }

  private getEmptyClaimForm(): Partial<InsuranceClaim> {
    return {
      insuranceId: 0,
      insuranceCompanyName: '',
      policyNumber: '',
      patientId: 0,
      patientName: '',
      invoiceNumber: '',
      claimAmount: 0,
      notes: '',
      claimStatus: 'PENDING'
    };
  }
}
