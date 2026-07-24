import { CommonModule } from '@angular/common';
import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-appointment-slip',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './appointment-slip.html',
  styleUrl: './appointment-slip.css',
})
export class AppointmentSlip {

  ngOnInit(): void {
    this.appointment = history.state.appointment;
  }
  @Input() appointment: any;

  @ViewChild('slip')
  slip!: ElementRef;

  printSlip() {

    const printContents = this.slip.nativeElement.innerHTML;

    const popup = window.open('', '_blank', 'width=900,height=700');

    popup!.document.open();

    popup!.document.write(`
      <html>
      <head>
      <title>Appointment Slip</title>

      <style>

      body{
      font-family:Arial;
      padding:20px;
      }

      table{
      width:100%;
      border-collapse:collapse;
      }

      td,th{
      border:1px solid #000;
      padding:8px;
      }

      h2,h3,h4{
      text-align:center;
      }

      </style>

      </head>

      <body>

      ${printContents}

      </body>

      </html>
    `);

    popup!.document.close();

    popup!.focus();

    popup!.print();

    popup!.close();

  }

  downloadPDF() {

    html2canvas(this.slip.nativeElement).then(canvas => {

      const img = canvas.toDataURL('image/png');

      const pdf = new jsPDF('p', 'mm', 'a4');

      const width = 190;

      const height = (canvas.height * width) / canvas.width;

      pdf.addImage(img, 'PNG', 10, 10, width, height);

      pdf.save('AppointmentSlip.pdf');

    });

  }


}
