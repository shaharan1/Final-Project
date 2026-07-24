import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';
import { DashboardService, DashboardStats } from '../../../../services/dashboard.service';

@Component({
  selector: 'app-lab-technician-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './lab-technician-dashboard.component.html',
  styleUrl: './lab-technician-dashboard.component.css',
})
export class LabTechnicianDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  tests: any[] = [];
  categories: { name: string; count: number }[] = [];
  loading = true;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.dashboardService.getLabStats().subscribe(stats => {
      this.stats = stats;
      this.cdr.detectChanges();
    });
    this.dashboardService.getAllTests().subscribe(tests => {
      this.tests = tests;
      this.buildCategories(tests);
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  private buildCategories(tests: any[]): void {
    const map = new Map<string, number>();
    tests.forEach(t => {
      const cat = t.category || 'Uncategorized';
      map.set(cat, (map.get(cat) || 0) + 1);
    });
    this.categories = Array.from(map.entries())
      .map(([name, count]) => ({ name, count }))
      .sort((a, b) => b.count - a.count);
  }
}
