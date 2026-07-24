import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NurseModel } from '../../../models/nurseModel';
import { NurseService } from '../../../services/nurse.service';

@Component({
  selector: 'app-nurse',
  imports: [CommonModule, FormsModule],
  templateUrl: './nurse.html',
  styleUrl: './nurse.css',
})
export class Nurse implements OnInit {

  nurses: NurseModel[] = [];

  nurse: NurseModel = {
    name: '',
    email: '',
    phone: '',
    password: '',
    address: '',
    gender: '',
    joinDate: '',
    photo: '',
    nurseType: '',
    qualification: '',
    registrationNumber: '',
    experienceYears: 0,
    shift: '',
    workingHours: '',
    onDuty: true,
    assignedWard: '',
    remarks: ''
  };

  constructor(private nurseService: NurseService) { }

  ngOnInit(): void {
    this.loadNurses();
  }

  loadNurses() {
    this.nurseService.getAllNurses().subscribe(res => {
      this.nurses = res;
    });
  }


  saveNurse() {

    this.nurseService.createNurse(this.nurse).subscribe({
      next: (data) => {
        alert("Nurse Saved Successfully");
        this.loadNurses();
        this.resetForm();
        console.log(data);
      },
      error: (err) => {
        console.error(err);
      }

    });

  }



  

  dutyStatus(nurse: NurseModel) {

    this.nurseService.updateDutyStatus(
      nurse.id!,
      !nurse.onDuty
    ).subscribe(() => {
      this.loadNurses();
    });

  }

  activeStatus(nurse: NurseModel) {

    this.nurseService.updateActiveStatus(
      nurse.id!,
      !nurse.active
    ).subscribe(() => {
      this.loadNurses();
    });

  }

  resetForm() {

    this.nurse = {
      name: '',
      email: '',
      phone: '',
      password: '',
      address: '',
      gender: '',
      joinDate: '',
      photo: '',
      nurseType: '',
      qualification: '',
      registrationNumber: '',
      experienceYears: 0,
      shift: '',
      workingHours: '',
      onDuty: true,
      assignedWard: '',
      remarks: ''
    };

  }

}
