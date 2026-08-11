import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DoctorModelService } from '../../../../services/doctor.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DoctorsDepartmentService } from '../../../../services/doctors-department';

@Component({
  selector: 'app-doctor',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './doctor.html',
  styleUrl: './doctor.css',
})
export class Doctor {

  doctorForm!: FormGroup;

  isEdit = false;

  doctorId!: number;

  departments: any[] = [];

  photoFile?: File;

  previewUrl = '';

  photoSizeError = '';

  constructor(
    private fb: FormBuilder,
    private doctorService: DoctorModelService,
    private departmentService: DoctorsDepartmentService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.createForm();

    this.loadDepartments();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEdit = true;
      this.doctorId = +id;
      this.loadDoctor(this.doctorId);
    }

  }

  createForm() {

    this.doctorForm = this.fb.group({

      name: ['', Validators.required],
      email: ['', Validators.required],
      phone: ['', Validators.required],
      password: ['', Validators.required],

      gender: ['', Validators.required],

      status: ['Active'],

      study: ['', Validators.required],

      specialization: ['', Validators.required],

      designation: [''],

      registrationNumber: ['', Validators.required],

      experienceYears: ['', Validators.required],

      consultationFee: ['', Validators.required],

      followUpFee: ['', Validators.required],

      availableDays: ['', Validators.required],

      dutyHours: ['', Validators.required],

      chamber: ['', Validators.required],

      joinDate: ['', Validators.required],

      photo: [''],

      doctorDepatrmentId: ['', Validators.required]

    });

  }

  loadDepartments() {

    this.departmentService.getAllDepartments().subscribe(res => {

      this.departments = res;
      this.cdr.markForCheck();

    });

  }

  loadDoctor(id: number) {

    this.doctorService.getById(id).subscribe(res => {

      this.doctorForm.patchValue(res);
      this.cdr.markForCheck();

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
        this.cdr.detectChanges();
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        this.photoSizeError = 'Image size must be under 2 MB.';
        this.photoFile = undefined;
        this.previewUrl = '';
        this.cdr.detectChanges();
        return;
      }
      this.photoSizeError = '';
      this.photoFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result as string;
        this.cdr.detectChanges();
      };
      reader.readAsDataURL(file);
    }
  }

  removePhoto(): void {
    this.photoFile = undefined;
    this.previewUrl = '';
    this.photoSizeError = '';
    this.doctorForm.patchValue({ photo: '' });
    this.cdr.detectChanges();
  }

  getPhotoPreview(): string {
    if (this.previewUrl) return this.previewUrl;
    if (this.isEdit && this.doctorForm.get('photo')?.value) return this.doctorForm.get('photo')!.value as string;
    return 'assets/images/doctor.png';
  }

  hasPhoto(): boolean {
    return !!(this.previewUrl || (this.isEdit && this.doctorForm.get('photo')?.value));
  }

  save() {

    if (this.doctorForm.invalid) return;

    const doctorData = { ...this.doctorForm.value };
    if (this.photoFile) {
      delete doctorData.photo;
    }

    if (this.isEdit) {
      this.doctorService.update(this.doctorId, doctorData).subscribe(() => {
        if (this.photoFile) {
          this.doctorService.uploadPhoto(this.doctorId, this.photoFile).subscribe(() => {
            alert('Doctor Updated');
            this.router.navigate(['/doctor']);
          });
        } else {
          alert('Doctor Updated');
          this.router.navigate(['/doctor']);
        }
      });
    } else {
      this.doctorService.create(doctorData).subscribe((response) => {
        if (this.photoFile) {
          this.doctorService.uploadPhoto(response.id!, this.photoFile).subscribe(() => {
            alert('Doctor Saved');
            this.router.navigate(['/doctor']);
          });
        } else {
          alert('Doctor Saved');
          this.router.navigate(['/doctor']);
        }
      });
    }
  }

}
