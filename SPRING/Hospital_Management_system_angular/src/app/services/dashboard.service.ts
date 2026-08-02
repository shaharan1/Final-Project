import { Injectable } from '@angular/core';
import { forkJoin, Observable, map } from 'rxjs';
import { PatientService } from './patient.service';
import { AppointmentService } from './appointment.service';
import { AdmissionService } from './admission.service';
import { DoctorModelService } from './doctor.service';
import { NurseService } from './nurse.service';
import { MedicineService } from './medicine.service';
import { GenericService } from './generic.service';
import { InfrastructureService } from './infrastructure.service';
import { TestMasterService } from './test-master.service';
import { OfficeStaffService } from './office-staff.service';
import { StorageService } from './storage.service';
import { PharmacySaleService } from './pharmacy-sale.service';
import { PurchasePharmacyService } from './purchase-pharmacy.service';
import { PaymentService } from './billing/payment.service';
import { InvoiceService } from './billing/invoice.service';

export interface DashboardStats {
  totalPatients: number;
  todayAppointments: number;
  availableBeds: number;
  totalBeds: number;
  monthlyRevenue: number;
  admittedPatients: number;
  totalDoctors: number;
  totalNurses: number;
  totalMedicines: number;
  totalGenerics: number;
  totalTests: number;
  totalOfficeStaff: number;
  lowStockMedicines: number;
  pendingAdmissions: number;
  activeAdmissions: number;
  totalAppointmentFees: number;
  todayAppointmentFees: number;
  totalAppointments: number;
  confirmedAppointments: number;
}

export interface WardOccupancy {
  wardName: string;
  totalBeds: number;
  occupiedBeds: number;
  percentage: number;
}

export interface RecentAdmission {
  patientName: string;
  patientCode: string;
  wardName: string;
  bedNumber: string;
  status: string;
  admissionDate: string;
}

export interface RecentAppointment {
  id: number;
  appointmentNumber?: string;
  patientName: string;
  doctorName: string;
  appointmentDate: string;
  appointmentTime: string;
  status: string;
  problemDescription: string;
  feeCharged: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {

  constructor(
    private patientService: PatientService,
    private appointmentService: AppointmentService,
    private admissionService: AdmissionService,
    private doctorService: DoctorModelService,
    private nurseService: NurseService,
    private medicineService: MedicineService,
    private genericService: GenericService,
    private infrastructureService: InfrastructureService,
    private testService: TestMasterService,
    private officeStaffService: OfficeStaffService,
    private storage: StorageService,
    private pharmacySaleService: PharmacySaleService,
    private purchaseService: PurchasePharmacyService,
    private paymentService: PaymentService,
    private invoiceService: InvoiceService,
  ) {}

  getAdminStats(): Observable<DashboardStats> {
    return forkJoin({
      patients: this.patientService.getAll(),
      appointments: this.appointmentService.getAllAppointments(),
      admissions: this.admissionService.getAll(),
      doctors: this.doctorService.getAll(),
      nurses: this.nurseService.getAllNurses(),
      medicines: this.medicineService.getAll(),
      generics: this.genericService.getAll(),
      tests: this.testService.getAll(),
      officeStaff: this.officeStaffService.getAll(),
      wards: this.infrastructureService.getAllWards(),
    }).pipe(
      map(data => {
        const today = new Date().toISOString().split('T')[0];
        const todayAppts = data.appointments.filter(a => a.appointmentDate === today);
        const activeAdmissions = data.admissions.filter(a => a.status === 'ADMITTED');
        const totalBeds = data.wards.reduce((sum, w) => sum + (w.totalBeds || 0), 0);

        return {
          totalPatients: data.patients.length,
          todayAppointments: todayAppts.length,
          availableBeds: Math.max(0, totalBeds - activeAdmissions.length),
          totalBeds: totalBeds,
          monthlyRevenue: todayAppts.reduce((sum, a) => sum + (a.feeCharged || 0), 0) * 30,
          admittedPatients: activeAdmissions.length,
          totalDoctors: data.doctors.length,
          totalNurses: data.nurses.length,
          totalMedicines: data.medicines.length,
          totalGenerics: data.generics.length,
          totalTests: data.tests.length,
          totalOfficeStaff: data.officeStaff.length,
          lowStockMedicines: 0,
          pendingAdmissions: data.admissions.filter(a => a.status === 'PENDING').length,
          activeAdmissions: activeAdmissions.length,
          totalAppointmentFees: data.appointments.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          todayAppointmentFees: todayAppts.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          totalAppointments: data.appointments.length,
          confirmedAppointments: data.appointments.filter((a: any) => a.status === 'CONFIRMED').length,
        };
      })
    );
  }

  getWardOccupancy(): Observable<WardOccupancy[]> {
    return forkJoin({
      wards: this.infrastructureService.getAllWards(),
      admissions: this.admissionService.getAll(),
    }).pipe(
      map(data => {
        const activeAdmissions = data.admissions.filter(a => a.status === 'ADMITTED');
        return data.wards.map(ward => {
          const occupied = activeAdmissions.filter(a => a.wardName === ward.name).length;
          const total = ward.totalBeds || 0;
          return {
            wardName: ward.name,
            totalBeds: total,
            occupiedBeds: occupied,
            percentage: total > 0 ? Math.round((occupied / total) * 100) : 0,
          };
        });
      })
    );
  }

  getRecentAdmissions(): Observable<RecentAdmission[]> {
    return this.admissionService.getAll().pipe(
      map(admissions =>
        admissions
          .sort((a, b) => new Date(b.admissionDate).getTime() - new Date(a.admissionDate).getTime())
          .slice(0, 5)
          .map(a => ({
            patientName: a.patientName,
            patientCode: a.patientCode,
            wardName: a.wardName,
            bedNumber: a.assignedBedNumber,
            status: a.status,
            admissionDate: a.admissionDate,
          }))
      )
    );
  }

  getTodayAppointments(): Observable<RecentAppointment[]> {
    return this.appointmentService.getAllAppointments().pipe(
      map(appts => {
        const today = new Date().toISOString().split('T')[0];
        return appts
          .filter(a => a.appointmentDate === today)
          .slice(0, 10)
          .map(a => ({
            id: a.id ?? 0,
            patientName: a.patientName ?? 'N/A',
            doctorName: a.doctorName ?? 'N/A',
            appointmentDate: a.appointmentDate,
            appointmentTime: a.appointmentTime,
            status: a.status ?? 'PENDING',
            problemDescription: a.problemDescription ?? '',
            feeCharged: a.feeCharged ?? 0,
          }));
      })
    );
  }

  getDoctorStats(doctorId: number): Observable<{
    totalPatients: number;
    todayAppointments: number;
    pendingReports: number;
    appointments: RecentAppointment[];
    allAppointments: RecentAppointment[];
  }> {
    return this.appointmentService.getDoctorAppointments(doctorId).pipe(
      map(appts => {
        const today = new Date().toISOString().split('T')[0];
        const todayAppts = appts.filter(a => a.appointmentDate === today);
        const uniquePatients = new Set(appts.map(a => a.patientName));
        const toRecent = (a: any): RecentAppointment => ({
          id: a.id,
          appointmentNumber: a.appointmentNumber,
          patientName: a.patientName,
          doctorName: a.doctorName,
          appointmentDate: a.appointmentDate,
          appointmentTime: a.appointmentTime,
          status: a.status,
          problemDescription: a.problemDescription,
          feeCharged: a.feeCharged ?? 0,
        });
        return {
          totalPatients: uniquePatients.size,
          todayAppointments: todayAppts.length,
          pendingReports: appts.filter(a => a.status === 'PENDING').length,
          totalAppointmentFees: appts.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          todayAppointmentFees: todayAppts.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          totalAppointments: appts.length,
          confirmedAppointments: appts.filter((a: any) => a.status === 'CONFIRMED').length,
          appointments: todayAppts.slice(0, 10).map(toRecent),
          allAppointments: appts.map(toRecent),
        };
      })
    );
  }

  getNurseStats(): Observable<DashboardStats> {
    return forkJoin({
      admissions: this.admissionService.getAll(),
      wards: this.infrastructureService.getAllWards(),
    }).pipe(
      map(data => {
        const active = data.admissions.filter(a => a.status === 'ADMITTED');
        const totalBeds = data.wards.reduce((sum, w) => sum + (w.totalBeds || 0), 0);
        return {
          totalPatients: active.length,
          todayAppointments: 0,
          availableBeds: Math.max(0, totalBeds - active.length),
          totalBeds,
          monthlyRevenue: 0,
          admittedPatients: active.length,
          totalDoctors: 0,
          totalNurses: 0,
          totalMedicines: 0,
          totalGenerics: 0,
          totalTests: 0,
          totalOfficeStaff: 0,
          lowStockMedicines: 0,
          pendingAdmissions: data.admissions.filter(a => a.status === 'PENDING').length,
          activeAdmissions: active.length,
          totalAppointmentFees: 0,
          todayAppointmentFees: 0,
          totalAppointments: 0,
          confirmedAppointments: 0,
        };
      })
    );
  }

  getReceptionistStats(): Observable<DashboardStats> {
    return forkJoin({
      patients: this.patientService.getAll(),
      appointments: this.appointmentService.getAllAppointments(),
      admissions: this.admissionService.getAll(),
    }).pipe(
      map(data => {
        const today = new Date().toISOString().split('T')[0];
        const todayAppts = data.appointments.filter(a => a.appointmentDate === today);
        return {
          totalPatients: data.patients.length,
          todayAppointments: todayAppts.length,
          availableBeds: 0,
          totalBeds: 0,
          monthlyRevenue: 0,
          admittedPatients: data.admissions.filter(a => a.status === 'ADMITTED').length,
          totalDoctors: 0,
          totalNurses: 0,
          totalMedicines: 0,
          totalGenerics: 0,
          totalTests: 0,
          totalOfficeStaff: 0,
          lowStockMedicines: 0,
          pendingAdmissions: 0,
          activeAdmissions: 0,
          totalAppointmentFees: data.appointments.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          todayAppointmentFees: todayAppts.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          totalAppointments: data.appointments.length,
          confirmedAppointments: data.appointments.filter((a: any) => a.status === 'CONFIRMED').length,
        };
      })
    );
  }

  getPharmacistStats(): Observable<DashboardStats> {
    return forkJoin({
      medicines: this.medicineService.getAll(),
      generics: this.genericService.getAll(),
    }).pipe(
      map(data => ({
        totalPatients: 0,
        todayAppointments: 0,
        availableBeds: 0,
        totalBeds: 0,
        monthlyRevenue: 0,
        admittedPatients: 0,
        totalDoctors: 0,
        totalNurses: 0,
        totalMedicines: data.medicines.length,
        totalGenerics: data.generics.length,
        totalTests: 0,
        totalOfficeStaff: 0,
        lowStockMedicines: Math.min(data.medicines.length, 5),
        pendingAdmissions: 0,
        activeAdmissions: 0,
        totalAppointmentFees: 0,
        todayAppointmentFees: 0,
        totalAppointments: 0,
        confirmedAppointments: 0,
      }))
    );
  }

  getLabStats(): Observable<DashboardStats> {
    return forkJoin({
      tests: this.testService.getAll(),
    }).pipe(
      map(data => ({
        totalPatients: 0,
        todayAppointments: 0,
        availableBeds: 0,
        totalBeds: 0,
        monthlyRevenue: 0,
        admittedPatients: 0,
        totalDoctors: 0,
        totalNurses: 0,
        totalMedicines: 0,
        totalGenerics: 0,
        totalTests: data.tests.length,
        totalOfficeStaff: 0,
        lowStockMedicines: 0,
        pendingAdmissions: 0,
        activeAdmissions: 0,
        totalAppointmentFees: 0,
        todayAppointmentFees: 0,
        totalAppointments: 0,
        confirmedAppointments: 0,
      }))
    );
  }

  getOfficeStats(): Observable<DashboardStats> {
    return forkJoin({
      patients: this.patientService.getAll(),
      appointments: this.appointmentService.getAllAppointments(),
      admissions: this.admissionService.getAll(),
      doctors: this.doctorService.getAll(),
    }).pipe(
      map(data => {
        const today = new Date().toISOString().split('T')[0];
        return {
          totalPatients: data.patients.length,
          todayAppointments: data.appointments.filter(a => a.appointmentDate === today).length,
          availableBeds: 0,
          totalBeds: 0,
          monthlyRevenue: 0,
          admittedPatients: data.admissions.filter(a => a.status === 'ADMITTED').length,
          totalDoctors: data.doctors.length,
          totalNurses: 0,
          totalMedicines: 0,
          totalGenerics: 0,
          totalTests: 0,
          totalOfficeStaff: 0,
          lowStockMedicines: 0,
          pendingAdmissions: data.admissions.filter(a => a.status === 'PENDING').length,
          activeAdmissions: data.admissions.filter(a => a.status === 'ADMITTED').length,
          totalAppointmentFees: data.appointments.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          todayAppointmentFees: data.appointments.filter((a: any) => a.appointmentDate === today).reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0),
          totalAppointments: data.appointments.length,
          confirmedAppointments: data.appointments.filter((a: any) => a.status === 'CONFIRMED').length,
        };
      })
    );
  }

  getInventoryStats(): Observable<DashboardStats> {
    return forkJoin({
      medicines: this.medicineService.getAll(),
      generics: this.genericService.getAll(),
      wards: this.infrastructureService.getAllWards(),
    }).pipe(
      map(data => {
        const totalBeds = data.wards.reduce((sum, w) => sum + (w.totalBeds || 0), 0);
        return {
          totalPatients: 0,
          todayAppointments: 0,
          availableBeds: totalBeds,
          totalBeds,
          monthlyRevenue: 0,
          admittedPatients: 0,
          totalDoctors: 0,
          totalNurses: 0,
          totalMedicines: data.medicines.length,
          totalGenerics: data.generics.length,
          totalTests: 0,
          totalOfficeStaff: 0,
          lowStockMedicines: Math.min(data.medicines.length, 3),
          pendingAdmissions: 0,
          activeAdmissions: 0,
          totalAppointmentFees: 0,
          todayAppointmentFees: 0,
          totalAppointments: 0,
          confirmedAppointments: 0,
        };
      })
    );
  }

  getWardManagerStats(): Observable<DashboardStats> {
    return forkJoin({
      admissions: this.admissionService.getAll(),
      wards: this.infrastructureService.getAllWards(),
    }).pipe(
      map(data => {
        const active = data.admissions.filter(a => a.status === 'ADMITTED');
        const totalBeds = data.wards.reduce((sum, w) => sum + (w.totalBeds || 0), 0);
        return {
          totalPatients: active.length,
          todayAppointments: 0,
          availableBeds: Math.max(0, totalBeds - active.length),
          totalBeds,
          monthlyRevenue: 0,
          admittedPatients: active.length,
          totalDoctors: 0,
          totalNurses: 0,
          totalMedicines: 0,
          totalGenerics: 0,
          totalTests: 0,
          totalOfficeStaff: 0,
          lowStockMedicines: 0,
          pendingAdmissions: data.admissions.filter(a => a.status === 'PENDING').length,
          activeAdmissions: active.length,
          totalAppointmentFees: 0,
          todayAppointmentFees: 0,
          totalAppointments: 0,
          confirmedAppointments: 0,
        };
      })
    );
  }

  getFinancialSummary(): Observable<any> {
    return forkJoin({
      appointments: this.appointmentService.getAllAppointments(),
      pharmacySales: this.pharmacySaleService.getAll(),
      purchases: this.purchaseService.getAll(),
      payments: this.paymentService.getAll(),
    }).pipe(
      map(data => {
        const appointmentFees = data.appointments.reduce((sum: number, a: any) => sum + (a.feeCharged || 0), 0);
        const pharmacyRevenue = data.pharmacySales.reduce((sum: number, s: any) => sum + (s.netPayable || s.totalAmount || 0), 0);
        const purchaseExpenses = data.purchases.reduce((sum: number, p: any) => sum + (p.netAmount || 0), 0);
        const paymentIncome = data.payments.reduce((sum: number, p: any) => sum + (p.amount || 0), 0);

        const totalCredit = pharmacyRevenue + paymentIncome + appointmentFees;
        const totalDebit = purchaseExpenses;
        const netBalance = totalCredit - totalDebit;

        return {
          totalCredit,
          totalDebit,
          netBalance,
          appointmentFees,
          pharmacyRevenue,
          purchaseExpenses,
          paymentIncome,
          totalSalesCount: data.pharmacySales.length,
          totalPurchasesCount: data.purchases.length,
          totalPaymentsCount: data.payments.length,
          totalAppointmentsCount: data.appointments.length,
        };
      })
    );
  }

  getAllMedicines() {
    return this.medicineService.getAll();
  }

  getAllGenerics() {
    return this.genericService.getAll();
  }

  getAllTests() {
    return this.testService.getAll();
  }

  getAllDoctors() {
    return this.doctorService.getAll();
  }

  getAllNurses() {
    return this.nurseService.getAllNurses();
  }

  getAllPatients() {
    return this.patientService.getAll();
  }

  getAllAppointments() {
    return this.appointmentService.getAllAppointments();
  }

  getAllAdmissions() {
    return this.admissionService.getAll();
  }

  getAllWards() {
    return this.infrastructureService.getAllWards();
  }

  getAllOfficeStaff() {
    return this.officeStaffService.getAll();
  }
}
