import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { OfficeStaffModel } from '../../../models/office-staff.model';
import { OfficeStaffService } from '../../../services/office-staff.service';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';

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

  // Resolve the image URL for a staff member. Uses the backend origin so the
  // photo loads even when the dev-server proxy has not been reloaded, and falls
  // back to a generated initials avatar when no photo is set or it fails to load.
  getAvatar(staff: OfficeStaffModel): string {
    if (staff && staff.photo) {
      return staff.photo.startsWith('http')
        ? staff.photo
        : environment.imageBaseUrl + staff.photo;
    }
    return this.initialsAvatar(staff?.name || '?');
  }

  onImgError(event: Event, staff: OfficeStaffModel): void {
    (event.target as HTMLImageElement).src = this.initialsAvatar(staff?.name || '?');
  }

  private initialsAvatar(name: string): string {
    const parts = (name || '?').trim().split(/\s+/);
    const initials = (parts.length > 1
      ? parts[0][0] + parts[parts.length - 1][0]
      : parts[0].substring(0, 2)
    ).toUpperCase();
    const palette = ['#059669', '#0891b2', '#10b981', '#0d9488', '#0284c7', '#ca8a04', '#dc2626', '#7c3aed'];
    let hash = 0;
    for (const ch of name) hash = (hash * 31 + ch.charCodeAt(0)) >>> 0;
    const bg = palette[hash % palette.length];
    const svg =
      `<svg xmlns='http://www.w3.org/2000/svg' width='88' height='88'>` +
      `<rect width='88' height='88' rx='44' fill='${bg}'/>` +
      `<text x='50%' y='50%' dy='.35em' font-family='Arial,Helvetica,sans-serif' ` +
      `font-size='34' font-weight='600' fill='#ffffff' text-anchor='middle'>${initials}</text>` +
      `</svg>`;
    return 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(svg);
  }

}
