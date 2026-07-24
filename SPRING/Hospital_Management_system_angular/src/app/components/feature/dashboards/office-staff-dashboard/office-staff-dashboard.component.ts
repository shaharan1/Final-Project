import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';

@Component({
  selector: 'app-office-staff-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './office-staff-dashboard.component.html',
  styleUrl: './office-staff-dashboard.component.css',
})
export class OfficeStaffDashboardComponent {}
