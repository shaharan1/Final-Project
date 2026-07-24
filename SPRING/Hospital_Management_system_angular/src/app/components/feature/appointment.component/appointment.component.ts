import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AppointmentService } from '../../../services/appointment.service';
import { DoctorModelService } from '../../../services/doctor.service';
import { Router } from '@angular/router';
import { ScheduleSlotService } from '../../../services/schedule-slot.service';
import { ScheduleSlotModel } from '../../../models/ScheduleSlotModel';
import { DoctorsDepartmentService } from '../../../services/doctors-department';
import { DoctorDepartmentModel } from '../../../models/doctorDepartmentModel';

@Component({
  selector: 'app-appointment.component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './appointment.component.html',
  styleUrl: './appointment.component.css',
})
export class AppointmentComponent {



  appointmentForm!: FormGroup;

  doctors: any[] = [];
  availableSlots: ScheduleSlotModel[] = [];
  dosctorsDepartments: DoctorDepartmentModel[] = [];



  fee = 0;

  returningPatient = false;

  constructor(
    private fb: FormBuilder,
    private appointmentService: AppointmentService,
    private doctorService: DoctorModelService,
    private scheduleSlotService: ScheduleSlotService,
    private doctorsDepartmentService: DoctorsDepartmentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

  this.createForm();
  this.listenForDepartmentChanges();
  this.listenForSlotChanges();
  this.loadDoctorDepratments();

  }

  listenForDepartmentChanges() {
  this.appointmentForm.get('doctorDepartmentId')?.valueChanges.subscribe((depId) => {
    // reset dependent fields whenever department changes
    this.doctors = [];
    this.availableSlots = [];
    this.appointmentForm.patchValue(
      { doctorId: '', appointmentTime: '' },
      { emitEvent: false }
    );

    if (depId) {
      this.loadDoctorsByDepartment(depId);
    }
  });
}

loadDoctorsByDepartment(depId: number) {
  this.doctorService.getByDoctorDepartmentId(depId).subscribe({
    next: (res) => {
      this.doctors = res;
      this.cdr.markForCheck();
      console.log('Doctors for department:', res);
    },
    error: (err) => {
      console.error(err);
      this.doctors = [];
    }
  });
}


  loadDoctorDepratments() {

     this.doctorsDepartmentService.getAllDepartments()
      .subscribe({

        next: (res) => {

          this.dosctorsDepartments = res;
          this.cdr.markForCheck();

          console.log('Dosctors Departments:', res);

        },

        error: (err) => {

          console.error(err);

          this.availableSlots = [];

        }

      });

  }

  listenForSlotChanges() {

    this.appointmentForm.get('doctorId')?.valueChanges.subscribe(() => {
      this.loadAvailableSlots();
    });

    this.appointmentForm.get('appointmentDate')?.valueChanges.subscribe(() => {
      this.loadAvailableSlots();
    });

  }


  loadAvailableSlots() {

    const doctorId = this.appointmentForm.get('doctorId')?.value;
    const date = this.appointmentForm.get('appointmentDate')?.value;

    if (!doctorId || !date) {
      this.availableSlots = [];
      return;
    }

    this.scheduleSlotService.findAvailableSlots(doctorId, date)
      .subscribe({

        next: (res) => {

          this.availableSlots = res;
           this.cdr.markForCheck();

          console.log('Available Slots:', res);

        },

        error: (err) => {

          console.error(err);

          this.availableSlots = [];

        }

      });

  }




  createForm() {

  this.appointmentForm = this.fb.group({

    patientId: [],

    patientName: ['', Validators.required],

    mobileNumber: ['', Validators.required],

    doctorDepartmentId: ['', Validators.required],   // <-- fixed

    doctorId: ['', Validators.required],

    appointmentDate: ['', Validators.required],

    appointmentTime: ['', Validators.required],

    problemDescription: ['', Validators.required],

    paymentMethod: ['', Validators.required],

    transactionId: ['']

  });

}

  loadDoctors() {

    this.doctorService.getAll().subscribe({

      next: (res) => {

        this.doctors = res;

        this.cdr.markForCheck();

      },

      error: (err) => console.log(err)

    });

  }

  checkReturningPatient() {

    const phone = this.appointmentForm.value.mobileNumber;

    if (!phone) return;

    this.appointmentService.checkReturning(phone).subscribe({

      next: (res) => {

        this.returningPatient = res;

      }

    });

  }

  calculateFee() {

    const phone = this.appointmentForm.value.mobileNumber;

    const doctorId = this.appointmentForm.value.doctorId;

    if (!phone || !doctorId) return;

    this.appointmentService.calculateFee(phone, doctorId).subscribe({

      next: (res) => {

        this.fee = res;

      }

    });

  }

  save() {

    if (this.appointmentForm.invalid) {

      this.appointmentForm.markAllAsTouched();

      return;

    }

    this.appointmentService.bookAppointment(this.appointmentForm.value)
      .subscribe({

        next: (res) => {

          alert("Appointment Booked Successfully");

          console.log(res);

          this.router.navigate(['/appointment-list']);

        },

        error: (err) => {

          console.log(err);

        }

      });

  }

}
