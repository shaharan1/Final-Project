import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PatientModel } from '../../../models/patientModel';
import { PatientService } from '../../../services/patient.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-patient-list.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient-list.component.html',
  styleUrl: './patient-list.component.css',
})
export class PatientListComponent implements OnInit {


  patients: PatientModel[] = [];
  filteredPatients: PatientModel[] = [];

  // Search & Filter
  searchText: string = '';
  patientCode: string = '';
  phone = '';


  constructor(
    private patientService: PatientService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadPatients();
  }


  // Load All Patients

  loadPatients(): void {

    this.patientService.getAll().subscribe({

      next: (data) => {
        this.patients = data;
        this.filteredPatients = data;
        this.cdr.markForCheck();
      },

      error: (err) => {
        console.log(err);
      }

    });

  }


  // Search & Filter

  filterPatients(): void {

    this.filteredPatients = this.patients.filter(patient => {

      const matchesName =
        !this.searchText ||
        patient.name.toLowerCase().includes(this.searchText.toLowerCase());

      const matchesCode =
        !this.patientCode ||
        (patient.patientCode ?? '').toLowerCase().includes(this.patientCode.toLowerCase());

      const matchesPhone =
        !this.phone ||
        (patient.phone ?? '').toLowerCase().includes(this.phone.toLowerCase());

      return (
        matchesName &&
        matchesCode &&
        matchesPhone
      );

    });

  }


  // Edit

  edit(id: number): void {

    this.router.navigate(['/patient/edit', id]);

  }


  // View Details

  view(id: number) {
    this.router.navigate(['/patient/edit', id]);
  }


  // Delete

  delete(id: number): void {

    if (!confirm('Are you sure you want to delete this patient?')) {
      return;
    }

    this.patientService.delete(id).subscribe({

      next: () => {

        alert('Patient deleted successfully.');

        this.loadPatients();

      },

      error: (err) => {

        console.log(err);

      }

    });

  }


  // Clear Filters

  clearFilters() {

    this.searchText = '';
    this.patientCode = '';
    this.phone = '';

    this.filteredPatients = [...this.patients];

  }

  

}
