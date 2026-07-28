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

  photoFile?: File;

  previewUrl = '';

  photoSizeError = '';

  constructor(private nurseService: NurseService) { }

  ngOnInit(): void {
    this.loadNurses();
  }

  loadNurses() {
    this.nurseService.getAllNurses().subscribe(res => {
      this.nurses = res;
      console.log(this.nurses);
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      if (!file.type.match(/^image\/(jpeg|jpg|png|webp)$/)) {
        this.photoSizeError = 'Please select a valid image file (JPG, PNG, WebP).';
        this.photoFile = undefined;
        this.previewUrl = '';
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        this.photoSizeError = 'Image size must be under 2 MB.';
        this.photoFile = undefined;
        this.previewUrl = '';
        return;
      }
      this.photoSizeError = '';
      this.photoFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  removePhoto(): void {
    this.photoFile = undefined;
    this.previewUrl = '';
    this.photoSizeError = '';
    this.nurse.photo = '';
  }

  getPhotoPreview(): string {
    if (this.previewUrl) return this.previewUrl;
    return this.nurse.photo || 'assets/images/doctor.png';
  }

  hasPhoto(): boolean {
    return !!this.previewUrl || !!this.nurse.photo;
  }

  saveNurse() {

    if (this.photoFile) {
      this.nurseService.createNurse(this.nurse).subscribe({
        next: (data) => {
          this.nurseService.uploadPhoto(data.id!, this.photoFile!).subscribe(() => {
            alert('Nurse Saved Successfully');
            this.loadNurses();
            this.resetForm();
          });
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else {
      this.nurseService.createNurse(this.nurse).subscribe({
        next: (data) => {
          alert('Nurse Saved Successfully');
          this.loadNurses();
          this.resetForm();
          console.log(data);
        },
        error: (err) => {
          console.error(err);
        }
      });
    }

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
