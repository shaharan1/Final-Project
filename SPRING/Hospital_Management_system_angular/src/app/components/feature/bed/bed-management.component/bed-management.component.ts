import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-bed-management.component',
  imports: [],
  template: '<p>Redirecting...</p>',
})
export class BedManagementComponent implements OnInit {
  constructor(private router: Router) {}
  ngOnInit(): void {
    this.router.navigate(['/ward-management']);
  }
}
