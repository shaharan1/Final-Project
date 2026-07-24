import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppointmentModel } from '../../../models/appointmentModel';
import { AppointmentService } from '../../../services/appointment.service';
import { DoctorModelService } from '../../../services/doctor.service';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-appointment-list.component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './appointment-list.component.html',
  styleUrl: './appointment-list.component.css',
})
export class AppointmentList implements OnInit {

  appointments: AppointmentModel[] = [];

  // Filter
  doctorId?: number;
  appointmentDate = '';

  // Doctor List
  doctors: any[] = [];

  constructor(
    private appointmentService: AppointmentService,
    private doctorService: DoctorModelService,
    private cdr: ChangeDetectorRef,
    private router: Router

  ) { }

  ngOnInit(): void {

    this.loadDoctors();

    this.loadAppointments();

  }

  loadDoctors() {

    this.doctorService.getAll().subscribe({

      next: (res) => {

        this.doctors = res;

      }

    });

  }

  loadAppointments() {

    this.appointmentService.getAllAppointments().subscribe({

      next: (res) => {

        this.appointments = res;
        this.cdr.markForCheck();

      },

      error: (err) => console.log(err)

    });

  }

  search() {

    this.appointmentService
      .filterAppointments(
        this.doctorId,
        this.appointmentDate
      )
      .subscribe({

        next: (res) => {

          this.appointments = res;

        },

        error: (err) => console.log(err)

      });

  }


  printSlip(app: AppointmentModel) {

    this.router.navigate(
      ['/appointment-slip'],
      {
        state: {
          appointment: app
        }
      }
    );

  }

  downloadPdf(app: AppointmentModel) {

    this.router.navigate(
      ['/appointment-slip'],
      {
        state: {
          appointment: app,
          autoDownload: true
        }
      }
    );

  }

  reset() {

    this.doctorId = undefined;

    this.appointmentDate = '';

    this.loadAppointments();

  }

  cancel(id: number) {

    if (confirm("Cancel this Appointment?")) {

      this.appointmentService
        .cancelAppointment(id)
        .subscribe(() => {

          alert("Appointment Cancelled");

          this.loadAppointments();

        });

    }

  }


  downloadDoctorWisePdf() {

    if (this.appointments.length === 0) {
      alert("No Appointment Found");
      return;
    }

    const doc = new jsPDF("p", "mm", "a4");

    // ================= Header =================

    doc.setFont("helvetica", "bold");
    doc.setFontSize(20);
    doc.text("MODERN HOSPITAL MANAGEMENT SYSTEM", 105, 15, {
      align: "center"
    });

    doc.setFontSize(14);
    doc.text("Doctor Wise Appointment Report", 105, 24, {
      align: "center"
    });

    doc.setLineWidth(0.5);
    doc.line(10, 28, 200, 28);

    // ================= Doctor Info =================

    const doctorName =
      this.doctors.find(d => d.id === this.doctorId)?.name ?? "All Doctors";

    doc.setFontSize(11);
    doc.setFont("helvetica", "normal");

    doc.text("Doctor : " + doctorName, 14, 38);
    doc.text(
      "Date : " +
      (this.appointmentDate === "" ? "All Dates" : this.appointmentDate),
      14,
      45
    );

    doc.text(
      "Generated : " + new Date().toLocaleString(),
      14,
      52
    );

    // ================= Table =================

    autoTable(doc, {
      startY: 60,

      head: [[
        "#",
        "Appointment No",
        "Patient",
        "Mobile",
        "Time",
        "Fee",
        "Status"
      ]],

      body: this.appointments.map(
        (a, index): (string | number)[] => [
          index + 1,
          a.appointmentNumber ?? "",
          a.patientName ?? "",
          a.mobileNumber ?? "",
          a.appointmentTime ?? "",
          a.feeCharged ?? 0,
          a.status ?? ""
        ]
      ),

      theme: "grid",

      headStyles: {
        fillColor: [41, 128, 185],
        textColor: 255,
        fontStyle: "bold",
        halign: "center"
      },

      bodyStyles: {
        halign: "center"
      },

      alternateRowStyles: {
        fillColor: [245, 245, 245]
      }
    });

    // ================= Footer =================

    const finalY = (doc as any).lastAutoTable.finalY;

    doc.setFont("helvetica", "bold");
    doc.setFontSize(12);

    doc.text(
      "Total Appointment : " + this.appointments.length,
      14,
      finalY + 10
    );

    doc.line(14, finalY + 28, 70, finalY + 28);
    doc.text("Doctor Signature", 18, finalY + 34);

    doc.line(140, finalY + 28, 196, finalY + 28);
    doc.text("Authorized Signature", 145, finalY + 34);

    // ================= Save =================

    const fileName =
      "DoctorWiseAppointmentReport_" +
      doctorName.replace(/\s+/g, "_") +
      "_" +
      (this.appointmentDate || "All") +
      ".pdf";

    doc.save(fileName);

  }
}