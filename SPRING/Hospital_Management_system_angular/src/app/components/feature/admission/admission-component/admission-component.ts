import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PatientModel } from '../../../../models/patientModel';
import { DoctorModel } from '../../../../models/doctorModel';
import { WardModel } from '../../../../models/ward.model';
import { BedModel } from '../../../../models/bed.model';
import { AdmissionRequest } from '../../../../models/admission.model';
import { PatientService } from '../../../../services/patient.service';
import { DoctorModelService } from '../../../../services/doctor.service';
import { InfrastructureService } from '../../../../services/infrastructure.service';
import { AdmissionService } from '../../../../services/admission.service';

@Component({
  selector: 'app-admission-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './admission-component.html',
  styleUrl: './admission-component.css',
})
export class AdmissionComponent {



  patients: PatientModel[] = [];

  doctors: DoctorModel[] = [];

  wards: WardModel[] = [];

  beds: BedModel[] = [];

  admission: AdmissionRequest = {

    patientId: 0,

    doctorId: 0,

    bedId: 0,

    initialDiagnosis: ''

  };



  constructor(

    private patientService: PatientService,

    private doctorService: DoctorModelService,

    private infrastructureService: InfrastructureService,

    private admissionService: AdmissionService,

    private cdr: ChangeDetectorRef



  ) { }



  ngOnInit(): void {

    this.loadPatients();

    this.loadDoctors();

    this.loadWards();

  }


  loadPatients() {

    this.patientService.getAll().subscribe({

      next: (res) => {

        this.patients = res;

        this.cdr.markForCheck();

        console.log(this.patients);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }



  loadDoctors() {

    this.doctorService.getAll().subscribe({

      next: (res) => {

        this.doctors = res;

        this.cdr.markForCheck();

        console.log(this.doctors);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }



  loadWards() {

    this.infrastructureService.getAllWards().subscribe({

      next: (res) => {

        this.wards = res;

        this.cdr.markForCheck();

        console.log(this.wards);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }


  onWardChange(wardId: number) {

    if (!wardId) {

      this.beds = [];

      return;

    }

    this.infrastructureService.getBedsByWard(wardId).subscribe({

      next: (res) => {

        // sudu AVAILABLE Bed dekabe
        this.beds = res.filter(b => b.status === 'AVAILABLE');

        console.log(this.beds);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }



  admit() {

    this.admissionService.admit(this.admission).subscribe({

      next: (res) => {

        alert("Patient Admitted Successfully");

        console.log(res);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }


}
