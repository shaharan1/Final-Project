import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';

@Component({
  selector: 'app-billing-clerk-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './billing-clerk-dashboard.component.html',
  styleUrl: './billing-clerk-dashboard.component.css',
})
export class BillingClerkDashboardComponent {}
