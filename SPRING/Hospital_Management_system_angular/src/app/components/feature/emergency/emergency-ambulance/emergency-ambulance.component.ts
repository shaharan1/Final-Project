import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AmbulanceService } from '../../../../services/emergency/ambulance.service';
import { AmbulanceTripService } from '../../../../services/emergency/ambulance-trip.service';
import { EmergencyPatientService } from '../../../../services/emergency/emergency-patient.service';
import { Ambulance, AmbulanceTrip } from '../../../../models/emergency/ambulance.model';
import { EmergencyPatient } from '../../../../models/emergency/emergency-patient.model';

@Component({
  selector: 'app-emergency-ambulance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './emergency-ambulance.component.html',
  styleUrls: ['./emergency-ambulance.component.css']
})
export class EmergencyAmbulanceComponent implements OnInit {
  private ambulanceService = inject(AmbulanceService);
  private tripService = inject(AmbulanceTripService);
  private patientService = inject(EmergencyPatientService);

  ambulances: Ambulance[] = [];
  trips: AmbulanceTrip[] = [];
  patients: EmergencyPatient[] = [];
  selectedAmbulance: Ambulance | null = null;
  loading = true;
  msg = '';
  msgType: 'success' | 'error' = 'success';
  statusFilter = 'ALL';
  showModal = false;
  showCreateForm = false;
  showRequestPanel = false;
  activeTab: 'fleet' | 'trips' | 'history' = 'fleet';

  ambulanceForm: Partial<Ambulance> = this.getEmptyAmbulanceForm();
  tripForm: Partial<AmbulanceTrip> = this.getEmptyTripForm();

  ambulanceStatuses = ['AVAILABLE', 'ON_DUTY', 'RETURNING', 'MAINTENANCE'];
  vehicleTypes = ['BASIC', 'ADVANCED', 'ICU', 'AIR'];
  tripStatuses = ['DISPATCHED', 'EN_ROUTE', 'ARRIVED', 'COMPLETED', 'CANCELLED'];

  constructor(


    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAmbulances();
    this.loadTrips();
    this.loadPatients();
  }

  loadAmbulances(): void {
    this.loading = true;
    this.ambulanceService.getAll().subscribe({
      next: (data) => {
        this.ambulances = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.showMessage('Failed to load ambulances', 'error');
        this.loading = false;
      }
    });
  }

  loadTrips(): void {
    this.tripService.getAll().subscribe({
      next: (data) => this.trips = data,
      error: () => { }
    });
  }

  loadPatients(): void {
    this.patientService.getAll().subscribe({
      next: (data) => this.patients = data,
      error: () => { }
    });
  }

  createAmbulance(): void {
    this.ambulanceService.create(this.ambulanceForm).subscribe({
      next: () => {
        this.showMessage('Ambulance added successfully', 'success');
        this.showCreateForm = false;
        this.ambulanceForm = this.getEmptyAmbulanceForm();
        this.loadAmbulances();
      },
      error: () => this.showMessage('Failed to add ambulance', 'error')
    });
  }

  updateStatus(id: number, status: string): void {
    this.ambulanceService.updateStatus(id, status).subscribe({
      next: () => {
        this.showMessage(`Ambulance status updated to ${status}`, 'success');
        this.loadAmbulances();
      },
      error: () => this.showMessage('Failed to update status', 'error')
    });
  }

  dispatchTrip(): void {
    if (!this.tripForm.ambulanceId || !this.tripForm.emergencyPatientId) {
      this.showMessage('Please select an ambulance and patient', 'error');
      return;
    }
    this.tripService.create(this.tripForm).subscribe({
      next: (trip) => {
        this.tripService.dispatchTrip(trip.id!).subscribe({
          next: () => {
            this.showMessage('Trip dispatched successfully', 'success');
            this.showRequestPanel = false;
            this.tripForm = this.getEmptyTripForm();
            this.loadTrips();
            this.loadAmbulances();
          },
          error: () => this.showMessage('Failed to dispatch trip', 'error')
        });
      },
      error: () => this.showMessage('Failed to create trip', 'error')
    });
  }

  completeTrip(tripId: number): void {
    this.tripService.completeTrip(tripId).subscribe({
      next: () => {
        this.showMessage('Trip completed successfully', 'success');
        this.loadTrips();
        this.loadAmbulances();
      },
      error: () => this.showMessage('Failed to complete trip', 'error')
    });
  }

  cancelTrip(tripId: number): void {
    this.tripService.cancelTrip(tripId).subscribe({
      next: () => {
        this.showMessage('Trip cancelled', 'success');
        this.loadTrips();
        this.loadAmbulances();
      },
      error: () => this.showMessage('Failed to cancel trip', 'error')
    });
  }

  requestAmbulance(patientId: number): void {
    const availableAmbulance = this.ambulances.find(a => a.status === 'AVAILABLE');
    if (!availableAmbulance) {
      this.showMessage('No ambulances available', 'error');
      return;
    }
    this.tripForm.ambulanceId = availableAmbulance.id;
    this.tripForm.emergencyPatientId = patientId;
    this.showRequestPanel = true;
    this.activeTab = 'trips';
  }

  getActiveTrips(): AmbulanceTrip[] {
    return this.trips.filter(t => t.status === 'DISPATCHED' || t.status === 'EN_ROUTE' || t.status === 'ARRIVED');
  }

  getCompletedTrips(): AmbulanceTrip[] {
    return this.trips.filter(t => t.status === 'COMPLETED' || t.status === 'CANCELLED');
  }

  getFilteredAmbulances(): Ambulance[] {
    if (this.statusFilter === 'ALL') return this.ambulances;
    return this.ambulances.filter(a => a.status === this.statusFilter);
  }

  getStatusColor(status: string | undefined): string {
    const colors: Record<string, string> = {
      'AVAILABLE': '#198754',
      'ON_DUTY': '#fd7e14',
      'RETURNING': '#0d6efd',
      'MAINTENANCE': '#6c757d',
      'DISPATCHED': '#ffc107',
      'EN_ROUTE': '#fd7e14',
      'ARRIVED': '#0dcaf0',
      'COMPLETED': '#198754',
      'CANCELLED': '#dc3545'
    };
    return colors[status || ''] || '#6c757d';
  }

  getVehicleTypeColor(type: string | undefined): string {
    const colors: Record<string, string> = {
      'BASIC': '#0dcaf0',
      'ADVANCED': '#6610f2',
      'ICU': '#dc3545',
      'AIR': '#ffc107'
    };
    return colors[type || ''] || '#6c757d';
  }

  getVehicleTypeIcon(type: string | undefined): string {
    const icons: Record<string, string> = {
      'BASIC': 'bi-truck',
      'ADVANCED': 'bi-truck-front',
      'ICU': 'bi-heart-pulse',
      'AIR': 'bi-airplane'
    };
    return icons[type || ''] || 'bi-truck';
  }

  getFuelColor(fuel: number): string {
    if (fuel > 50) return '#198754';
    if (fuel > 20) return '#ffc107';
    return '#dc3545';
  }

  getPatientName(id?: number): string {
    if (!id) return 'Unknown';
    const patient = this.patients.find(p => p.id === id);
    return patient ? patient.patientName : 'Unknown';
  }

  getAmbulanceNumber(id?: number): string {
    if (!id) return 'Unknown';
    const amb = this.ambulances.find(a => a.id === id);
    return amb ? amb.ambulanceNumber || 'N/A' : 'Unknown';
  }

  getFleetStats(): { total: number; available: number; onDuty: number; maintenance: number } {
    return {
      total: this.ambulances.length,
      available: this.ambulances.filter(a => a.status === 'AVAILABLE').length,
      onDuty: this.ambulances.filter(a => a.status === 'ON_DUTY' || a.status === 'RETURNING').length,
      maintenance: this.ambulances.filter(a => a.status === 'MAINTENANCE').length
    };
  }

  openAmbulanceModal(ambulance: Ambulance): void {
    this.selectedAmbulance = ambulance;
    this.showModal = true;
  }

  closeModal(): void {
    this.selectedAmbulance = null;
    this.showModal = false;
  }

  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
    if (!this.showCreateForm) {
      this.ambulanceForm = this.getEmptyAmbulanceForm();
    }
  }

  toggleRequestPanel(): void {
    this.showRequestPanel = !this.showRequestPanel;
    if (!this.showRequestPanel) {
      this.tripForm = this.getEmptyTripForm();
    }
  }

  setActiveTab(tab: 'fleet' | 'trips' | 'history'): void {
    this.activeTab = tab;
  }

  private getEmptyAmbulanceForm(): Partial<Ambulance> {
    return {
      ambulanceNumber: '',
      vehicleType: 'BASIC',
      vehiclePlate: '',
      driverName: '',
      driverPhone: '',
      paramedicName: '',
      paramedicPhone: '',
      status: 'AVAILABLE',
      fuelStatus: 100
    };
  }

  private getEmptyTripForm(): Partial<AmbulanceTrip> {
    return {
      ambulanceId: undefined,
      emergencyPatientId: undefined,
      tripType: 'EMERGENCY',
      pickupLocation: '',
      dropoffLocation: 'Hospital Emergency Dept',
      status: 'DISPATCHED'
    };
  }

  private showMessage(text: string, type: 'success' | 'error'): void {
    this.msg = text;
    this.msgType = type;
    setTimeout(() => this.msg = '', 4000);
  }
}
