import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-inventory-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileCardComponent],
  templateUrl: './inventory-manager-dashboard.component.html',
  styleUrl: './inventory-manager-dashboard.component.css',
})
export class InventoryManagerDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  medicines: any[] = [];
  generics: any[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getInventoryStats().subscribe({
      next: (stats) => { this.stats = stats; this.cdr.detectChanges(); },
      error: () => { this.cdr.detectChanges(); }
    });
    this.dashboardService.getAllMedicines().subscribe({
      next: (medicines) => { this.medicines = medicines.slice(0, 4); this.cdr.detectChanges(); },
      error: () => { this.cdr.detectChanges(); }
    });
    this.dashboardService.getAllGenerics().subscribe({
      next: (generics) => { this.generics = generics; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.loading = false; this.cdr.detectChanges(); }
    });
  }

  getStockStatus(medicine: any): string {
    if (medicine.currentStock <= 10) return 'Critical';
    if (medicine.currentStock <= 30) return 'Low';
    return 'Adequate';
  }

  getStockBadgeClass(medicine: any): string {
    if (medicine.currentStock <= 10) return 'critical';
    if (medicine.currentStock <= 30) return 'observation';
    return 'stable';
  }
}
