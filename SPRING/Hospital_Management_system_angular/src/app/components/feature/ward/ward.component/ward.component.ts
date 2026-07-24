import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-ward.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ward.component.html',
  styleUrl: './ward.component.css',
})
export class WardComponent {

  constructor(private router: Router) { }

  openWardList() {
    this.router.navigate(['/ward-list']);
  }

  openBedManagement() {
    this.router.navigate(['/bed-management']);
  }

  openAdmissions() {
    this.router.navigate(['/admission-list']);
  }

}
