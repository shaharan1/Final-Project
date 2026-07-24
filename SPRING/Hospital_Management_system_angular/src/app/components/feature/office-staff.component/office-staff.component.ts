import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { OfficeStaffService } from '../../../services/office-staff.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-office-staff.component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './office-staff.component.html',
  styleUrl: './office-staff.component.css',
})
export class OfficeStaffComponent {


 officeStaffForm!: FormGroup;

  isEdit = false;

  officeStaffId!: number;

  constructor(

    private fb: FormBuilder,

    private officeStaffService: OfficeStaffService,

    private route: ActivatedRoute,

    private router: Router,

     private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.createForm();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {

      this.isEdit = true;

      this.officeStaffId = +id;

      this.loadOfficeStaff(this.officeStaffId);

    }

  }

  createForm() {

    this.officeStaffForm = this.fb.group({

      name: ['', Validators.required],

      email: ['', [Validators.required, Validators.email]],

      phone: ['', Validators.required],

      password: ['', Validators.required],

      gender: ['', Validators.required],

      position: ['', Validators.required],

      age: ['', Validators.required],

      department: ['', Validators.required],

      workingHours: ['', Validators.required],

      joinDate: ['', Validators.required],

      photo: ['']

    });

  }

  loadOfficeStaff(id: number) {

    this.officeStaffService.getById(id).subscribe({

      next: (res) => {

        this.officeStaffForm.patchValue(res);
        this.cdr.markForCheck();

      },

      error: (err) => {

        console.error(err);

      }

    });

  }

  save() {

    if (this.officeStaffForm.invalid) {

      this.officeStaffForm.markAllAsTouched();

      return;

    }

    if (this.isEdit) {

      this.officeStaffService.update(
        this.officeStaffId,
        this.officeStaffForm.value
      ).subscribe({

        next: () => {

          alert("Office Staff Updated Successfully");

          this.router.navigate(['/office-staff']);

        },

        error: (err) => {

          console.error(err);

        }

      });

    } else {

      this.officeStaffService.create(
        this.officeStaffForm.value
      ).subscribe({

        next: () => {

          alert("Office Staff Saved Successfully");

          this.router.navigate(['/office-staff']);

        },

        error: (err) => {

          console.error(err);

        }

      });

    }

  }


}
