import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { AppointmentService } from '../../../../services/appointment.service';

@Component({
  selector: 'app-appointment-slip',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './appointment-slip.html',
  styleUrl: './appointment-slip.css',
})
export class AppointmentSlip implements AfterViewInit {

  appointment: any = null;
  loading = false;
  errorMsg = '';

  private sourceNumber: string | null = null;
  private pendingAutoDownload = false;

  @ViewChild('slip')
  slip!: ElementRef;

  constructor(
    private route: ActivatedRoute,
    private appointmentService: AppointmentService
  ) {}

  ngOnInit(): void {
    const fromState = history.state?.appointment;
    const fromQuery = this.route.snapshot.queryParamMap.get('number');
    this.sourceNumber = fromQuery || fromState?.appointmentNumber || null;
    this.pendingAutoDownload = !!history.state?.autoDownload;

    if (this.sourceNumber) {
      this.loadFromBackend(this.sourceNumber);
    } else if (fromState) {
      this.appointment = fromState;
      this.maybeAutoDownload();
    } else {
      this.errorMsg = 'No appointment selected. Please open a slip from the appointment list.';
    }
  }

  ngAfterViewInit(): void {
    // Placeholder for post-render hooks if needed.
  }

  private loadFromBackend(number: string): void {
    this.loading = true;
    this.errorMsg = '';
    this.appointmentService.getByAppointmentNumber(number).subscribe({
      next: (res: any) => {
        this.appointment = res;
        this.loading = false;
        this.maybeAutoDownload();
      },
      error: () => {
        this.loading = false;
        this.errorMsg = 'Could not load appointment details from the server.';
      }
    });
  }

  private maybeAutoDownload(): void {
    if (this.pendingAutoDownload) {
      this.pendingAutoDownload = false;
      setTimeout(() => this.downloadPDF(), 400);
    }
  }

  printSlip() {

    if (!this.slip?.nativeElement) return;
    const printContents = this.slip.nativeElement.innerHTML;

    const popup = window.open('', '_blank', 'width=900,height=700');

    popup!.document.open();

    popup!.document.write(`
      <html>
      <head>
      <title>Appointment Slip</title>

      <style>
      * { box-sizing: border-box; }
      body { font-family: 'Segoe UI', Arial, sans-serif; padding: 0; margin: 0; color: #1a1a2e; }

      .slip { max-width: 760px; margin: 20px auto; border: 1px solid #e4e9ec; border-radius: 16px; overflow: hidden; }

      .slip-header { display: flex; align-items: center; justify-content: space-between; gap: 16px;
        background: linear-gradient(135deg, #059669 0%, #0891b2 100%); color: #fff; padding: 24px 30px; }
      .slip-brand { display: flex; align-items: center; gap: 16px; }
      .slip-logo { width: 58px; height: 58px; border-radius: 12px; background: rgba(255,255,255,0.18); padding: 6px; }
      .slip-brand h2 { margin: 0; font-size: 19px; font-weight: 800; }
      .slip-brand p { margin: 4px 0 0; font-size: 12px; opacity: 0.85; }
      .slip-title { font-size: 14px; font-weight: 700; text-transform: uppercase; letter-spacing: 1.5px;
        background: rgba(255,255,255,0.16); padding: 10px 16px; border-radius: 12px; white-space: nowrap; }

      .slip-meta { display: flex; align-items: center; justify-content: space-between; padding: 12px 30px;
        background: #f0fdf4; border-bottom: 1px solid #e4e9ec; font-size: 14px; }
      .slip-meta-no strong { color: #0f5132; }
      .slip-status { background: #d1fae5; color: #065f46; font-weight: 700; font-size: 12px;
        padding: 5px 14px; border-radius: 20px; text-transform: uppercase; }

      .slip-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 1px; background: #e4e9ec; border-bottom: 1px solid #e4e9ec; }
      .slip-field { background: #fff; padding: 13px 30px; display: flex; flex-direction: column; gap: 3px; }
      .slip-field--full { grid-column: 1 / -1; }
      .slip-label { font-size: 11px; text-transform: uppercase; letter-spacing: 0.6px; color: #6c757d; font-weight: 600; }
      .slip-value { font-size: 15px; color: #1a1a2e; font-weight: 600; }

      .slip-qr { text-align: center; padding: 22px 16px 8px; }
      .slip-qr img { border: 1px solid #e4e9ec; border-radius: 10px; padding: 8px; }
      .slip-qr p { margin: 8px 0 0; font-size: 12px; color: #6c757d; }

      .slip-note { margin: 12px 30px 0; background: #fff8e6; border: 1px dashed #f0c14b; color: #7a5b00;
        border-radius: 10px; padding: 11px 16px; font-size: 13px; text-align: center; }
      .slip-footer { text-align: center; padding: 16px 30px 28px; color: #6c757d; font-size: 13px; }
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

    if (!this.slip?.nativeElement) return;

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
