import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { OfficeStaffModel } from '../../../models/office-staff.model';
import { OfficeStaffService } from '../../../services/office-staff.service';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-office-staff-list.component',
  imports: [CommonModule,FormsModule, ReactiveFormsModule],
  templateUrl: './office-staff-list.component.html',
  styleUrl: './office-staff-list.component.css',
})
export class OfficeStaffList implements OnInit {

  officeStaffs: OfficeStaffModel[] = [];

  constructor(
    private officeStaffService: OfficeStaffService,
    private router: Router,
     private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadOfficeStaff();
  }

  loadOfficeStaff() {

    this.officeStaffService.getAll().subscribe({

      next: (res) => {

        this.officeStaffs = res;
        this.cdr.markForCheck();

      },

      error: (err) => {

        console.log(err);

      }

    });

  }

  edit(id: number) {

    this.router.navigate(['/office-staff/edit', id]);

  }

  delete(id: number) {

    if (confirm('Are you sure to delete this Office Staff?')) {

      this.officeStaffService.delete(id).subscribe({

        next: () => {

          alert("Deleted Successfully");

          this.loadOfficeStaff();

        },

        error: (err) => {

          console.log(err);

        }

      });

    }

  }

}
