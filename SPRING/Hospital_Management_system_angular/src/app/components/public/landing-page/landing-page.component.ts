import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, HostListener } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { DoctorDepartmentModel } from '../../../models/doctorDepartmentModel';
import { ScheduleSlotModel } from '../../../models/ScheduleSlotModel';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css',
})
export class LandingPageComponent {
  menuOpen = false;
  isScrolled = false;
  bookingSubmitted = false;
  bookingFailed = false;
  bookingResponse: any = null;

  bookingForm!: FormGroup;
  doctorsDepartments: DoctorDepartmentModel[] = [];
  departments = [
    { icon: '❤️', name: 'Cardiology', desc: 'Heart care & cardiovascular surgery.' },
    { icon: '🧠', name: 'Neurology', desc: 'Brain & nervous system treatment.' },
    { icon: '🦴', name: 'Orthopedics', desc: 'Bone, joint & spine care.' },
    { icon: '👶', name: 'Pediatrics', desc: 'Child healthcare & vaccination.' },
    { icon: '🤰', name: 'Gynecology', desc: 'Women health & maternity care.' },
    { icon: '🏥', name: 'ICU', desc: 'Critical care & monitoring.' },
    { icon: '🚑', name: 'Emergency', desc: '24/7 emergency medical services.' },
    { icon: '🩺', name: 'General Medicine', desc: 'Primary care & internal medicine.' },
  ];
  doctors: any[] = [];
  availableSlots: ScheduleSlotModel[] = [];
  fee = 0;
  returningPatient = false;

  private apiUrl = environment.apiUrl;

  stats = [
    { value: '25+', label: 'Years Experience' },
    { value: '150+', label: 'Expert Doctors' },
    { value: '500+', label: 'Beds Available' },
    { value: '50K+', label: 'Patients Treated' },
  ];

  services = [
    { icon: '🩺', title: 'Appointment Booking', desc: 'Schedule visits with top doctors instantly.', link: '#booking' },
    { icon: '🚑', title: 'Emergency Care', desc: '24/7 emergency medical support.', link: 'tel:+8801712345678' },
    { icon: '🚐', title: 'Ambulance Service', desc: 'Fast ambulance dispatch anytime.', link: 'tel:+8801712345678' },
    { icon: '💊', title: 'Pharmacy', desc: 'In-house pharmacy with all medicines.', link: '/login' },
    { icon: '🔬', title: 'Laboratory', desc: 'Advanced diagnostic lab facilities.', link: '/login' },
    { icon: '🩻', title: 'Health Checkup', desc: 'Comprehensive health packages.', link: '#booking' },
  ];

  whyChooseUs = [
    { icon: '👨‍⚕️', title: 'Experienced Doctors', desc: 'Board-certified specialists with years of experience.' },
    { icon: '🚑', title: '24/7 Emergency', desc: 'Round-the-clock emergency medical services.' },
    { icon: '🔬', title: 'Modern Equipment', desc: 'State-of-the-art medical technology.' },
    { icon: '📱', title: 'Online Appointment', desc: 'Book appointments from anywhere.' },
    { icon: '📋', title: 'Digital Reports', desc: 'Access lab reports & records online.' },
    { icon: '💰', title: 'Affordable Treatment', desc: 'Quality healthcare at reasonable cost.' },
  ];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.loadDepartments();
    this.listenFormChanges();
  }

  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 40;
  }

  createForm() {
    this.bookingForm = this.fb.group({
      patientName: ['', Validators.required],
      mobileNumber: ['', Validators.required],
      doctorDepartmentId: ['', Validators.required],
      doctorId: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      appointmentTime: ['', Validators.required],
      problemDescription: ['', Validators.required],
      paymentMethod: ['', Validators.required],
      transactionId: ['']
    });
  }

  listenFormChanges() {
    this.bookingForm.get('doctorDepartmentId')?.valueChanges.subscribe((depId) => {
      this.doctors = [];
      this.availableSlots = [];
      this.fee = 0;
      this.bookingForm.patchValue({ doctorId: '', appointmentTime: '' }, { emitEvent: false });
      if (depId) {
        this.loadDoctorsByDepartment(depId);
      }
    });

    this.bookingForm.get('doctorId')?.valueChanges.subscribe(() => {
      this.availableSlots = [];
      this.bookingForm.patchValue({ appointmentTime: '' }, { emitEvent: false });
      this.loadAvailableSlots();
      this.calculateFee();
    });

    this.bookingForm.get('appointmentDate')?.valueChanges.subscribe(() => {
      this.availableSlots = [];
      this.bookingForm.patchValue({ appointmentTime: '' }, { emitEvent: false });
      this.loadAvailableSlots();
    });

    this.bookingForm.get('mobileNumber')?.valueChanges.subscribe(() => {
      this.checkReturningPatient();
    });
  }

  loadDepartments() {
    this.http.get<DoctorDepartmentModel[]>(this.apiUrl + 'doctor-departments').subscribe({
      next: (res) => {
        this.doctorsDepartments = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.error(err)
    });
  }

  loadDoctorsByDepartment(depId: number) {
    this.http.get<any[]>(this.apiUrl + 'doctors/doctordepartment/' + depId).subscribe({
      next: (res) => {
        this.doctors = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.doctors = [];
      }
    });
  }

  loadAvailableSlots() {
    const doctorId = this.bookingForm.get('doctorId')?.value;
    const date = this.bookingForm.get('appointmentDate')?.value;
    if (!doctorId || !date) {
      this.availableSlots = [];
      return;
    }
    this.http.get<ScheduleSlotModel[]>(
      this.apiUrl + 'schedule-slots/doctor/' + doctorId + '/free?date=' + date
    ).subscribe({
      next: (res) => {
        this.availableSlots = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.availableSlots = [];
      }
    });
  }

  checkReturningPatient() {
    const phone = this.bookingForm.get('mobileNumber')?.value;
    if (!phone || phone.length < 10) return;
    this.http.get<boolean>(this.apiUrl + 'appointments/check-returning?phone=' + phone).subscribe({
      next: (res) => {
        this.returningPatient = res;
        this.cdr.markForCheck();
      }
    });
  }

  calculateFee() {
    const phone = this.bookingForm.get('mobileNumber')?.value;
    const doctorId = this.bookingForm.get('doctorId')?.value;
    if (!phone || !doctorId) return;
    this.http.get<number>(this.apiUrl + 'appointments/calculate-fee?phone=' + phone + '&doctorId=' + doctorId).subscribe({
      next: (res) => {
        this.fee = res;
        this.cdr.markForCheck();
      }
    });
  }

  submitBooking() {
    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      return;
    }

    const formVal = this.bookingForm.value;

    const payload = {
      patientName: formVal.patientName,
      mobileNumber: formVal.mobileNumber,
      doctorId: Number(formVal.doctorId),
      appointmentDate: formVal.appointmentDate,
      appointmentTime: formVal.appointmentTime,
      problemDescription: formVal.problemDescription,
      paymentMethod: formVal.paymentMethod,
      transactionId: formVal.transactionId || ''
    };

    this.http.post(this.apiUrl + 'public/checkout/confirm-booking', payload).subscribe({
      next: (res: any) => {
        this.bookingResponse = res;
        this.bookingSubmitted = true;
        this.bookingFailed = false;
        this.bookingForm.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.bookingSubmitted = false;
          this.bookingResponse = null;
        }, 5000);
      },
      error: (err) => {
        console.error(err);
        this.bookingFailed = true;
        this.cdr.markForCheck();
        setTimeout(() => { this.bookingFailed = false; }, 3000);
      }
    });
  }

  scrollToBooking() {
    document.getElementById('booking')?.scrollIntoView({ behavior: 'smooth' });
  }
}
