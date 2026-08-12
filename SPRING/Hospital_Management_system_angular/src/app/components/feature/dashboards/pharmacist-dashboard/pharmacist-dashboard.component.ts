import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProfileCardComponent } from '../../../shared/profile-card/profile-card.component';
import { DashboardService, DashboardStats } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-pharmacist-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ProfileCardComponent],
  templateUrl: './pharmacist-dashboard.component.html',
  styleUrl: './pharmacist-dashboard.component.css',
})
export class PharmacistDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  medicines: any[] = [];
  generics: any[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getPharmacistStats().subscribe(stats => {
      this.stats = stats;
      this.cdr.detectChanges();
    });
    this.dashboardService.getAllMedicines().subscribe(medicines => {
      this.medicines = medicines.slice(0, 5);
      this.cdr.detectChanges();
    });
    this.dashboardService.getAllGenerics().subscribe(generics => {
      this.generics = generics;
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  getMedicineStatus(medicine: any): string {
    if (medicine.currentStock <= 10) return 'Critical';
    if (medicine.currentStock <= 30) return 'Low';
    return 'Adequate';
  }

  getMedicineBadgeClass(medicine: any): string {
    if (medicine.currentStock <= 10) return 'critical';
    if (medicine.currentStock <= 30) return 'observation';
    return 'stable';
  }
}
