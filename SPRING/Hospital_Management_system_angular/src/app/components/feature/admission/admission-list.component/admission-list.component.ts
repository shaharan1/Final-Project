import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdmissionRequest } from '../../../../models/admission.model';
import { AdmissionService } from '../../../../services/admission.service';
import { AdmissionResponse } from '../../../../models/admission-response.model';

@Component({
  selector: 'app-admission-list.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admission-list.component.html',
  styleUrl: './admission-list.component.css',
})
export class AdmissionListComponent {


  admissions: AdmissionResponse[] = [];
  constructor(
    private service: AdmissionService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAdmissions();
  }

  loadAdmissions() {
    this.service.getAll().subscribe({

      next: (res) => {
        this.admissions = res;
        this.cdr.markForCheck();
        console.log(res);
      },

      error: (err) => {
        console.log(err);
      }

    });
  }

  discharge(id: number) {

    if (confirm("Discharge this patient?")) {

      this.service.discharge(id).subscribe({

        next: () => {

          alert("Patient Discharged Successfully");

          this.loadAdmissions();

        },

        error: err => console.log(err)

      });

    }

  }


}
