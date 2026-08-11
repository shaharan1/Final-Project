import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ScheduleSlotService } from '../../../services/schedule-slot.service';
import { DoctorModelService } from '../../../services/doctor.service';

@Component({
  selector: 'app-schedule-slot.component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './schedule-slot.component.html',
  styleUrl: './schedule-slot.component.css',
})
export class ScheduleSlotComponent implements OnInit {

  slotForm!: FormGroup;

  doctors: any[] = [];

  slots: any[] = [];

  constructor(
    private fb: FormBuilder,
    private slotService: ScheduleSlotService,
    private doctorService: DoctorModelService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.slotForm = this.fb.group({

      doctorId: ['', Validators.required],

      date: ['', Validators.required],

      startTime: ['', Validators.required],

      endTime: ['', Validators.required]

    });

    this.loadDoctors();

    this.loadSlots();

  }

  loadDoctors() {

    this.doctorService.getAll().subscribe(res => {

      this.doctors = res;
      this.cdr.markForCheck;

    });

  }

  loadSlots() {

    this.slotService.getAll().subscribe(res => {

      this.slots = res;
      this.cdr.markForCheck();

    });

  }

  save() {

    if (this.slotForm.invalid) return;

    this.slotService.create(this.slotForm.value)

      .subscribe(() => {

        alert("Schedule Created");

        this.slotForm.reset();

        this.loadSlots();

      });

  }

  changeStatus(slot: any) {

    this.slotService.changeStatus(slot.id, !slot.isBooked)

      .subscribe(() => {

        this.loadSlots();

      });

  }

}