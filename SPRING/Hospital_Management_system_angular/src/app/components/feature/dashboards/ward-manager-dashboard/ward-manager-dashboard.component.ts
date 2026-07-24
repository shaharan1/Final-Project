import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';

@Component({
  selector: 'app-ward-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './ward-manager-dashboard.component.html',
  styleUrl: './ward-manager-dashboard.component.css',
})
export class WardManagerDashboardComponent {}
