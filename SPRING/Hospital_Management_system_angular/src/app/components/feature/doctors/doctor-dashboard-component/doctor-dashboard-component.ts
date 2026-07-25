import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DoctorModel, DoctorResponseModel } from '../../../../models/doctorModel';
import { DoctorModelService } from '../../../../services/doctor.service';
import { KEYS, StorageService } from '../../../../services/storage.service';
import { LoginResponse } from '../../../../models/login.model';
import { AuthService } from '../../../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { AppointmentResponseModel } from '../../../../models/AppointmentResponseModel';
import { AppointmentService } from '../../../../services/appointment.service';
import { PrescriptionModel } from '../../../../models/prescriptionModel';
import { PrescriptionService } from '../../../../services/prescription.service';

@Component({
  selector: 'app-doctor-dashboard-component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './doctor-dashboard-component.html',
  styleUrl: './doctor-dashboard-component.css',
})
export class DoctorDashboardComponent {

  user: LoginResponse | null = null;
  doctor: DoctorResponseModel | null= null;
  appointments: AppointmentResponseModel[] = [];
  prescriptions: PrescriptionModel[] = [];

  totalPatients = 0;
  todayAppointments = 0;
  pendingReports = 0;

  loading = true;

  constructor(
    private doctorService: DoctorModelService,
    private appointmentService: AppointmentService,
    private prescriptionService: PrescriptionService,
    private cdr: ChangeDetectorRef,
    private storage: StorageService,
    private auth: AuthService,
      private router: Router
    
  ) { }

  ngOnInit(): void {
     this.user = this.storage.getUser();

    this.loadDoctor();
    this.loadAppointments();

  }


  loadDoctor(): void {
    this.loading = true;
  
    const cached = this.storage.getData<DoctorResponseModel>(KEYS.Doctor);
    if (cached) {     
      this.loading = false;
    }
  
    // Always refresh from server too, in case it changed elsewhere
    if (this.user?.userId) {
      this.doctorService.findByUserId(this.user.userId).subscribe({
        next: (res) => {
          this.doctor= res;
           this.loadAppointments();
           this.loadPrescriptions(res.id!);
           this.storage.saveData(KEYS.Doctor, res);
           this.loading = false;
           this.cdr.markForCheck();

          console.log("Doctor "+ this.doctor);
        },
        error: () => { this.loading = false; }
      });
    }
  }

  loadAppointments() {

  if(!this.doctor?.id){
    return;
  }

  this.appointmentService
      .getDoctorAppointments(this.doctor.id)
      .subscribe({

        next:(res)=>{

          this.appointments=res;
          this.todayAppointments = res.length;
          this.totalPatients = res.length;
          this.cdr.markForCheck();

        }

      });

}

  loadPrescriptions(doctorId: number): void {
    this.prescriptionService.getByDoctorId(doctorId).subscribe({
      next: (res) => {
        this.prescriptions = res;
        this.cdr.markForCheck();
      },
      error: () => {}
    });
  }

  getPrescriptionByAppointment(appointmentId: number): PrescriptionModel | undefined {
    return this.prescriptions.find(p => p.appointmentId === appointmentId);
  }

  hasPrescription(appointmentId: number): boolean {
    return this.prescriptions.some(p => p.appointmentId === appointmentId);
  }


   logout(): void {
    this.auth.logout();
    this.storage.removeData(KEYS.RIDER);
  }


  writePrescription(appointmentId: number): void {
  this.router.navigate(['/prescriptions/create', appointmentId]);
}

  editPrescription(appointmentId: number): void {
  const prescription = this.getPrescriptionByAppointment(appointmentId);
  if (prescription?.id) {
    this.router.navigate(['/prescriptions/edit', prescription.id]);
  } else {
    this.writePrescription(appointmentId);
  }
}

  viewPrescriptions(): void {
  this.router.navigate(['/prescriptions']);
}






}
