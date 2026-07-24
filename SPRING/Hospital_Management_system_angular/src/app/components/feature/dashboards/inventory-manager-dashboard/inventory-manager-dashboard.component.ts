import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';

@Component({
  selector: 'app-inventory-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './inventory-manager-dashboard.component.html',
  styleUrl: './inventory-manager-dashboard.component.css',
})
export class InventoryManagerDashboardComponent {}
