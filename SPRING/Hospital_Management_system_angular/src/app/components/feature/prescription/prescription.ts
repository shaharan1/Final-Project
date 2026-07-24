import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PrescriptionModel } from '../../../models/prescriptionModel';
import { PrescriptionService } from '../../../services/prescription.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AppointmentService } from '../../../services/appointment.service';
import { DoctorModelService } from '../../../services/doctor.service';
import { MedicineModel } from '../../../models/medicineModel';
import { MedicineService } from '../../../services/medicine.service';
import { TestMasterService } from '../../../services/test-master.service';
import { TestMasterModel } from '../../../models/testMasterModel';

import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-prescription',
  standalone: true,
  imports: [CommonModule, FormsModule, MatAutocompleteModule,
    MatInputModule,
    MatFormFieldModule],
  templateUrl: './prescription.html',
  styleUrl: './prescription.css',
})
export class PrescriptionComponent implements OnInit {

  medicines: MedicineModel[] = [];
  filteredMedicines: MedicineModel[] = [];

  filteredTests: TestMasterModel[] = [];

  selectedTests: TestMasterModel[] = [];

  testKeyword = '';

  prescription: PrescriptionModel = {

    appointmentId: null,
    doctorId: null,
    patientId: null,

    diagnosis: '',
    chiefComplaints: '',
    symptoms: '',

    bloodPressure: '',
    pulseRate: '',
    bodyTemperature: '',
    weight: '',

    notes: '',
    nextFollowUpDate: '',

    prescriptionItems: [],

    testIds: [],


  };

  doseList: string[] = [
    '1+0+1',
    '1+1+1',
    '1+0+0',
    '0+1+0',
    '0+0+1',
    '1+1+0',
    '0+1+1',
    '½+0+½',
    '½+½+½',
    'SOS',
    'STAT',
    'OD',
    'BD',
    'TDS',
    'QID',
    'HS'
  ];

  durationList = [

    '1 Day',

    '3 Days',

    '5 Days',

    '7 Days',

    '10 Days',

    '14 Days',

    '15 Days',

    '21 Days',

    '1 Month',

    '2 Months',

    'Continue'

  ];

  instructions: string[] = [
    'Before Meal',
    'After Meal',
    'With Meal',
    'Empty Stomach',
    'Before Breakfast',
    'After Breakfast',
    'Before Lunch',
    'After Lunch',
    'Before Dinner',
    'After Dinner',
    'Morning',
    'Noon',
    'Night',
    'Morning & Night',
    'As Needed (SOS)',
    'At Bedtime'
  ];

  constructor(
    private appointmentService: AppointmentService,
    private doctorService: DoctorModelService,
    private service: PrescriptionService,
    private medicineService: MedicineService,
    private testService: TestMasterService,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadMedicines();

    this.addMedicine();

    this.route.params.subscribe(params => {

      // Edit Prescription
      if (params['id']) {

        this.loadPrescription(+params['id']);
        return;
      }

      // Create From Appointment
      if (params['appointmentId']) {

        this.loadAppointment(+params['appointmentId']);

        console.log(this.loadAppointment(+params['appointmentId']));
      }

    });

    const appointmentId =
      Number(this.route.snapshot.paramMap.get('appointmentId'));

    if (appointmentId) {

      this.loadAppointment(appointmentId);

    }

  }

  loadMedicines() {
    this.medicineService.getAll().subscribe({
      next: (res) => {
        this.medicines = res;
        this.cdr.markForCheck();
        console.log("Medicines: " + this.medicines);
      }
    });
  }

loadAppointment(appointmentId: number) {

  this.appointmentService.getById(appointmentId).subscribe({

    next: (res) => {

      console.log("Appointment Response =", res);

      this.prescription.appointmentId = res.id!;
      this.prescription.patientId = res.registeredPatientId!;
      this.prescription.doctorId = res.doctorId!;

      console.log("registeredPatientId =", res.registeredPatientId);
      console.log("patientId =", this.prescription.patientId);

    },

    error: err => console.log(err)

  });

}


  loadPrescription(id: number) {

    this.service.getById(id).subscribe({

      next: (res) => {

        this.prescription = res;
        this.cdr.markForCheck();

      },

      error: (err) => {

        console.log(err);

      }

    });

  }

  searchMedicine(item: any) {

    console.log("Typing:", item.medicineName);

    if (!item.medicineName || item.medicineName.length < 2) {
      item.suggestions = [];
      return;
    }

    this.medicineService.search(item.medicineName).subscribe({

      next: (res: any) => {

        console.log("Full Response =", res);
        console.log("Is Array =", Array.isArray(res));
        console.log("Length =", res?.length);

        item.suggestions = res;
        this.cdr.detectChanges();
      },

      error: err => {
        console.log(err);
      }

    });

  }


  selectMedicine(item: any, medicine: any) {

    console.log("item =", item);
    console.log("medicine =", medicine);
    console.log("typeof medicine =", typeof medicine);

    if (!medicine) {
      alert("Medicine is undefined");
      return;
    }

console.log("Selected Medicine:", medicine);

    item.medicineId = medicine.id;
    item.medicineName = medicine.medicineName;
    item.suggestions = [];

  }

  addMedicine() {

    this.prescription.prescriptionItems.push({

      medicineId: 0,

      medicineName: '',

      dosage: '',

      duration: '',

      instruction: '',

      suggestions: []

    });

  }

  removeMedicine(index: number) {

    this.prescription.prescriptionItems.splice(index, 1);

  }

  searchTest(keyword: string): void {

    this.testKeyword = keyword;

    if (!keyword || keyword.trim().length < 1) {

      this.filteredTests = [];
      return;

    }

    this.testService.search(keyword).subscribe({

      next: (res) => {

        this.filteredTests = res;
        this.cdr.markForCheck();

      }

    });

  }

  selectTest(test: TestMasterModel): void {

    const exists = this.prescription.testIds.includes(test.id!);

    if (!exists) {

      this.prescription.testIds.push(test.id!);

      this.selectedTests.push(test);

    }

    this.filteredTests = [];

    this.testKeyword = '';

  }

  removeTest(test: TestMasterModel): void {

    this.selectedTests =
      this.selectedTests.filter(t => t.id !== test.id);

    this.prescription.testIds =
      this.prescription.testIds.filter(id => id !== test.id);

  }

  get totalTestCost(): number {

    return this.selectedTests.reduce(
      (sum, t) => sum + t.standardPrice,
      0
    );

  }

  private cleanPrescriptionItems(items: any[]): any[] {
    return items.map(({ medicineName, ...rest }) => rest);
  }



//   save() {

//     console.log("AppointmentId:", this.prescription.appointmentId);
// console.log("DoctorId:", this.prescription.doctorId);
// console.log("PatientId:", this.prescription.patientId);
// console.log("Prescription Items:", this.prescription.prescriptionItems);

//     const payload = {
//       ...this.prescription,
//       prescriptionItems: this.cleanPrescriptionItems(this.prescription.prescriptionItems)
//     };

//     if (this.prescription.id) {

//       console.log(
//         JSON.stringify(this.prescription, null, 2)
//       );

//       this.service.update(this.prescription.id, payload).subscribe({

//         next: () => {

//           alert('Prescription Updated Successfully');

//           this.router.navigate(['/prescription-list']);



//         },

//         error: err => console.log(err)

//       });

//     } else {

//       this.service.save(this.prescription).subscribe({

//         next: (res) => {

//           console.log(res);

//           this.downloadPdf(res.id!);

//         },

//         error: err => console.log(err)

//       });

//     }

//   }


save() {

  const payload = {
    ...this.prescription,
    prescriptionItems: this.cleanPrescriptionItems(
      this.prescription.prescriptionItems
    )
  };

  console.log("========== Prescription Payload ==========");
  console.log("Payload =", payload);
  console.log("Payload JSON =", JSON.stringify(payload, null, 2));
  console.log("AppointmentId =", payload.appointmentId);
  console.log("DoctorId =", payload.doctorId);
  console.log("PatientId =", payload.patientId);
  console.log("PrescriptionItems =", payload.prescriptionItems);
  console.log("TestIds =", payload.testIds);

  if (this.prescription.id) {

    this.service.update(this.prescription.id, payload).subscribe({

      next: () => {

        alert('Prescription Updated Successfully');

        this.router.navigate(['/prescription-list']);

      },

      error: err => {

        console.log(err);

      }

    });

  } else {

    this.service.save(payload).subscribe({

      next: (res) => {

        console.log("Save Success =", res);

        this.downloadPdf(res.id!);

      },

      error: (err) => {

        console.log("Save Error =", err);

      }

    });

  }

}


  downloadPdf(id: number) {

    this.service.downloadPdf(id).subscribe({

      next: (blob: Blob) => {

        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');

        a.href = url;

        a.download = `Prescription-${id}.pdf`;

        a.click();

        window.URL.revokeObjectURL(url);

      },

      error: err => {

        console.log(err);

      }

    });

  }

  printPdf(id: number) {

    this.service.downloadPdf(id).subscribe({

      next: (blob: Blob) => {

        const fileURL = URL.createObjectURL(blob);

        const win = window.open(fileURL);

        if (win) {

          win.onload = () => {

            win.print();

          };

        }

      }

    });

  }
}
