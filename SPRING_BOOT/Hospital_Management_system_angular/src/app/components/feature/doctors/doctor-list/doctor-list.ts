import { ChangeDetectorRef, Component } from '@angular/core';
import { DoctorModelService } from '../../../../services/doctor.service';
import { DoctorModel } from '../../../../models/doctorModel';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-doctor-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-list.html',
  styleUrl: './doctor-list.css',
})
export class DoctorList {

  doctors: DoctorModel[] = [];

  constructor(private doctorService: DoctorModelService, private cdr: ChangeDetectorRef, private router: Router) { }

  ngOnInit(): void {
    this.loadDoctors();
  }

  loadDoctors() {

    this.doctorService.getAll().subscribe({

      next: (res) => {
        this.doctors = res;
        this.cdr.markForCheck();
      },

      error: (err) => {
        console.log(err);
      }

    });

  }


  editDoctor(id: number) {
    this.router.navigate(['/doctor/edit', id]);
  }

  deleteDoctor(id: number) {

    if (!confirm('Are you sure you want to delete this doctor?')) {
      return;
    }

    this.doctorService.delete(id).subscribe({

      next: () => {
        alert('Doctor deleted successfully');
        this.loadDoctors();
      },

      error: err => {
        console.log(err);
      }

    });

  }


}
