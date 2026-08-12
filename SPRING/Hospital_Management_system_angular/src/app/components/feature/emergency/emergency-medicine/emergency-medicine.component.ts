import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmergencyMedicineService } from '../../../../services/emergency/emergency-medicine.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { EmergencyPatient } from '../../../../models/emergency/emergency-patient.model';
import { EmergencyMedicine } from '../../../../models/emergency/emergency-medicine.model';

@Component({
  selector: 'app-emergency-medicine',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-medicine.component.html',
  styleUrls: ['./emergency-medicine.component.css']
})
export class EmergencyMedicineComponent implements OnInit {
  patients: EmergencyPatient[] = [];
  medicines: EmergencyMedicine[] = [];
  selectedPatient: EmergencyPatient | null = null;
  searchTerm = '';
  loading = false;
  msg = '';
  msgType = '';

  medForm = {
    medicineName: '',
    dose: '',
    route: '',
    frequency: '',
    quantity: 0,
    administeredBy: '',
    stockAvailable: false,
    pharmacyRequestSent: false,
    notes: ''
  };

  routes = ['ORAL', 'IV', 'IM', 'SC', 'TOPICAL', 'INHALATION'];
  frequencies = ['STAT', 'QID', 'TID', 'BID', 'Q4H', 'Q6H', 'Q8H', 'PRN'];

  emergencyDrugs = [
    { name: 'Epinephrine', dose: '1mg', route: 'IV', icon: '💉' },
    { name: 'Atropine', dose: '0.5mg', route: 'IV', icon: '💊' },
    { name: 'Morphine', dose: '10mg', route: 'IM', icon: '🧴' },
    { name: 'Diazepam', dose: '10mg', route: 'IV', icon: '💊' },
    { name: 'Nitroglycerin', dose: '0.4mg', route: 'TOPICAL', icon: '💓' },
    { name: 'Aspirin', dose: '325mg', route: 'ORAL', icon: '💊' },
    { name: 'Normal Saline', dose: '1000ml', route: 'IV', icon: '🧪' },
    { name: 'Lidocaine', dose: '100mg', route: 'IV', icon: '💉' },
    { name: 'Amiodarone', dose: '150mg', route: 'IV', icon: '💊' },
    { name: 'Naloxone', dose: '0.4mg', route: 'IV', icon: '💉' },
    { name: 'Dopamine', dose: '5mcg/kg/min', route: 'IV', icon: '❤️' },
    { name: 'Salbutamol', dose: '2.5mg', route: 'INHALATION', icon: '🫁' }
  ];

  constructor(
    private medicineService: EmergencyMedicineService,
    private patientService: EmergencyPatientService
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    this.loadMedicines();
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data: EmergencyPatient[]) => this.patients = data,
      error: () => this.showMessage('Failed to load patients', 'error')
    });
  }

  loadMedicines(): void {
    this.medicineService.getAll().subscribe({
      next: (data: EmergencyMedicine[]) => this.medicines = data,
      error: () => this.showMessage('Failed to load medicines', 'error')
    });
  }

  selectPatient(patient: EmergencyPatient): void {
    this.selectedPatient = patient;
    this.loadMedicines();
  }

  orderMedicine(): void {
    if (!this.selectedPatient) {
      this.showMessage('Please select a patient first', 'error');
      return;
    }
    this.loading = true;
    this.medicineService.create({ ...this.medForm, emergencyPatientId: this.selectedPatient.id } as EmergencyMedicine).subscribe({
      next: (res) => {
        this.showMessage('Medicine order placed successfully', 'success');
        this.resetForm();
        this.loadMedicines();
        this.loading = false;
      },
      error: () => {
        this.showMessage('Failed to place order', 'error');
        this.loading = false;
      }
    });
  }

  requestPharmacy(id: number): void {
    this.medicineService.requestPharmacy(id).subscribe({
      next: () => {
        this.showMessage('Pharmacy request sent', 'success');
        this.loadMedicines();
      },
      error: () => this.showMessage('Pharmacy request failed', 'error')
    });
  }

  updateStatus(id: number, status: string): void {
    this.medicineService.updateStatus(id, status).subscribe({
      next: () => {
        this.showMessage('Status updated', 'success');
        this.loadMedicines();
      },
      error: () => this.showMessage('Failed to update status', 'error')
    });
  }

  prefillDrug(drug: { name: string; dose: string; route: string; icon: string }): void {
    this.medForm.medicineName = drug.name;
    this.medForm.dose = drug.dose;
    this.medForm.route = drug.route;
  }

  resetForm(): void {
    this.medForm = {
      medicineName: '',
      dose: '',
      route: '',
      frequency: '',
      quantity: 0,
      administeredBy: '',
      stockAvailable: false,
      pharmacyRequestSent: false,
      notes: ''
    };
  }

  showMessage(msg: string, type: string): void {
    this.msg = msg;
    this.msgType = type;
    setTimeout(() => { this.msg = ''; }, 4000);
  }

  getPatientMedicines(): EmergencyMedicine[] {
    if (!this.selectedPatient) return [];
    return this.medicines.filter(m => m.emergencyPatientId === this.selectedPatient?.id);
  }
}
