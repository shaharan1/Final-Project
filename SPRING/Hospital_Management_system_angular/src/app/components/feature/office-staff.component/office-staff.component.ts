import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { OfficeStaffService } from '../../../services/office-staff.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-office-staff.component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './office-staff.component.html',
  styleUrl: './office-staff.component.css',
})
export class OfficeStaffComponent {
  officeStaffForm!: FormGroup;

  isEdit = false;

  officeStaffId!: number;

  photoFile?: File;

  previewUrl = '';

  photoSizeError = '';
  roles = [
    { value: 'OfficeStaff', label: 'Office Staff' },
    { value: 'Receptionist', label: 'Receptionist' },
    { value: 'Pharmacist', label: 'Pharmacist' },
    { value: 'LabTechnician', label: 'Lab Technician' },
    { value: 'BillingClerk', label: 'Billing Clerk' },
    { value: 'InventoryManager', label: 'Inventory Manager' },
    { value: 'WardManager', label: 'Ward Manager' },
  ];

  constructor(
    private fb: FormBuilder,
    private officeStaffService: OfficeStaffService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.createForm();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.officeStaffId = +id;
      this.loadOfficeStaff(this.officeStaffId);
    }
  }

  createForm() {
    this.officeStaffForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      password: ['', this.isEdit ? [] : Validators.required],
      gender: ['', Validators.required],
      role: ['OfficeStaff', Validators.required],
      position: ['', Validators.required],
      age: ['', Validators.required],
      department: ['', Validators.required],
      workingHours: ['', Validators.required],
      joinDate: ['', Validators.required],
      photo: ['']
    });
  }

  loadOfficeStaff(id: number) {
    this.officeStaffService.getById(id).subscribe({
      next: (res) => {
        this.officeStaffForm.patchValue(res);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  save() {
    if (this.officeStaffForm.invalid) {
      this.officeStaffForm.markAllAsTouched();
      return;
    }
    const staffData = { ...this.officeStaffForm.value };
    if (this.photoFile) {
      delete staffData.photo;
    }
    if (this.isEdit) {
      this.officeStaffService.update(this.officeStaffId, staffData).subscribe({
        next: () => {
          if (this.photoFile) {
            this.officeStaffService.uploadPhoto(this.officeStaffId, this.photoFile).subscribe(() => {
              alert('Office Staff Updated Successfully');
              this.router.navigate(['/office-staff']);
            });
          } else {
            alert('Office Staff Updated Successfully');
            this.router.navigate(['/office-staff']);
          }
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else {
      this.officeStaffService.create(staffData).subscribe({
        next: (response) => {
          if (this.photoFile) {
            this.officeStaffService.uploadPhoto(response.id, this.photoFile).subscribe(() => {
              alert('Office Staff Saved Successfully');
              this.router.navigate(['/office-staff']);
            });
          } else {
            alert('Office Staff Saved Successfully');
            this.router.navigate(['/office-staff']);
          }
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      if (!file.type.match(/^image\/(jpeg|jpg|png|webp)$/)) {
        this.photoSizeError = 'Please select a valid image file (JPG, PNG, WebP).';
        this.photoFile = undefined;
        this.previewUrl = '';
        this.cdr.markForCheck();
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        this.photoSizeError = 'Image size must be under 2 MB.';
        this.photoFile = undefined;
        this.previewUrl = '';
        this.cdr.markForCheck();
        return;
      }
      this.photoSizeError = '';
      this.photoFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result as string;
        this.cdr.markForCheck();
      };
      reader.readAsDataURL(file);
    }
  }

  removePhoto(): void {
    this.photoFile = undefined;
    this.previewUrl = '';
    this.photoSizeError = '';
    this.officeStaffForm.patchValue({ photo: '' });
    this.cdr.markForCheck();
  }

  getPhotoPreview(): string {
    if (this.previewUrl) return this.previewUrl;
    if (this.isEdit && this.officeStaffForm.get('photo')?.value) {
      return this.officeStaffForm.get('photo')!.value as string;
    }
    return 'assets/images/doctor.png';
  }

  hasPhoto(): boolean {
    return !!this.previewUrl || (this.isEdit && !!this.officeStaffForm.get('photo')?.value);
  }

}
