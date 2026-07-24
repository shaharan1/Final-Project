import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from '../../../shared/layout/layout/layout.component';

@Component({
  selector: 'app-lab-technician-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, LayoutComponent],
  templateUrl: './lab-technician-dashboard.component.html',
  styleUrl: './lab-technician-dashboard.component.css',
})
export class LabTechnicianDashboardComponent {}
