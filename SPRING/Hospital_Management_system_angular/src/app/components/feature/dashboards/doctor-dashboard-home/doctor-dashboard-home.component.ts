import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';

@Component({
  selector: 'app-doctor-dashboard-home',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './doctor-dashboard-home.component.html',
  styleUrl: './doctor-dashboard-home.component.css',
})
export class DoctorDashboardHomeComponent {}
