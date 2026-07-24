import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DoctorModelService } from '../../../../services/doctor.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DoctorsDepartmentService } from '../../../../services/doctors-department';

@Component({
  selector: 'app-doctor',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './doctor.html',
  styleUrl: './doctor.css',
})
export class Doctor {




  doctorForm!: FormGroup;

  isEdit = false;

  doctorId!: number;

  departments: any[] = [];

  constructor(
    private fb: FormBuilder,
    private doctorService: DoctorModelService,
    private departmentService: DoctorsDepartmentService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.createForm();

    this.loadDepartments();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEdit = true;
      this.doctorId = +id;
      this.loadDoctor(this.doctorId);
    }

  }

  createForm() {

    this.doctorForm = this.fb.group({

      name: ['', Validators.required],
      email: ['', Validators.required],
      phone: ['', Validators.required],
      password: ['', Validators.required],

      gender: ['', Validators.required],

      status: ['Active'],

      study: ['', Validators.required],

      specialization: ['', Validators.required],

      designation: [''],

      registrationNumber: ['', Validators.required],

      experienceYears: ['', Validators.required],

      consultationFee: ['', Validators.required],

      followUpFee: ['', Validators.required],

      availableDays: ['', Validators.required],

      dutyHours: ['', Validators.required],

      chamber: ['', Validators.required],

      joinDate: ['', Validators.required],

      photo: [''],

      doctorDepatrmentId: ['', Validators.required]

    });

  }

  loadDepartments() {

    this.departmentService.getAllDepartments().subscribe(res => {

      this.departments = res;
      this.cdr.markForCheck();

    });

  }

  loadDoctor(id: number) {

    this.doctorService.getById(id).subscribe(res => {

      this.doctorForm.patchValue(res);
      this.cdr.markForCheck();

    });

  }

  save() {

    if (this.doctorForm.invalid) return;

    if (this.isEdit) {

      this.doctorService.update(this.doctorId, this.doctorForm.value)
        .subscribe(() => {

          alert("Doctor Updated");

          this.router.navigate(['/doctor']);

        });

    } else {

      this.doctorService.create(this.doctorForm.value)
        .subscribe(() => {

          alert("Doctor Saved");

          this.router.navigate(['/doctor']);

        });

    }

  }

}
