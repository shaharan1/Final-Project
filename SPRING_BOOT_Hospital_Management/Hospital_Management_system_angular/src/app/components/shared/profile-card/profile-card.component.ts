import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StorageService } from '../../../services/storage.service';
import { ProfileService } from '../../../services/profile.service';
import { LoginResponse } from '../../../models/login.model';
import { environment } from '../../../../environments/environment';
import { ImageUrlPipe } from '../../../shared/pipes/image-url.pipe';

@Component({
  selector: 'app-profile-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ImageUrlPipe],
  templateUrl: './profile-card.component.html',
  styleUrl: './profile-card.component.css',
})
export class ProfileCardComponent implements OnInit {

  user: LoginResponse | null = null;
  showEditModal = false;
  editName = '';
  editPhone = '';
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  saving = false;
  imageUploading = false;
  msg = '';
  msgType = '';

  constructor(
    private storage: StorageService,
    private profileService: ProfileService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.user = this.storage.getUser();
    this.loadProfile();
  }

  loadProfile(): void {
    this.profileService.getProfile().subscribe({
      next: (res: any) => {
        if (res && !res.error) {
          this.user = res;
          const existing = this.storage.getUser();
          this.user = { ...existing, ...res };
          if (this.user) this.storage.saveSession(this.user);
          this.cdr.markForCheck();
        }
      }
    });
  }

  getInitials(): string {
    if (!this.user?.name) return '?';
    return this.user.name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  getRoleColor(): string {
    switch (this.user?.role) {
      case 'Admin': return '#6610f2';
      case 'Doctor': return '#0d6efd';
      case 'Nurse': return '#198754';
      case 'LabTechnician': return '#0dcaf0';
      case 'Pharmacist': return '#fd7e14';
      case 'Receptionist': return '#6f42c1';
      case 'OfficeStaff': return '#20c997';
      case 'BillingClerk': return '#d63384';
      case 'InventoryManager': return '#ffc107';
      case 'WardManager': return '#dc3545';
      default: return '#6c757d';
    }
  }

  getImageUrl(): string {
    if (this.previewUrl) return this.previewUrl;
    return this.user?.image || '';
  }

  openEditModal(): void {
    this.editName = this.user?.name || '';
    this.editPhone = this.user?.phone || '';
    this.showEditModal = true;
    this.msg = '';
    this.previewUrl = null;
    this.selectedFile = null;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.msg = '';
    this.selectedFile = null;
    this.previewUrl = null;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      const reader = new FileReader();
      reader.onload = () => { this.previewUrl = reader.result as string; this.cdr.markForCheck(); };
      reader.readAsDataURL(this.selectedFile);
      this.uploadImage();
    }
  }

  uploadImage(): void {
    if (!this.selectedFile) return;
    this.imageUploading = true;
    this.profileService.uploadImage(this.selectedFile).subscribe({
      next: (res: any) => {
        if (res.error) {
          this.imageUploading = false;
          this.msg = res.error;
          this.msgType = 'error';
          this.cdr.markForCheck();
          return;
        }
        const existing = this.storage.getUser();
        this.user = { ...existing, ...res };
        if (this.user) this.storage.saveSession(this.user);
        this.imageUploading = false;
        this.selectedFile = null;
        this.previewUrl = null;
        this.msg = 'Photo updated successfully!';
        this.msgType = 'success';
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        this.imageUploading = false;
        this.msg = err.error?.error || 'Image upload failed. Please try again.';
        this.msgType = 'error';
        this.cdr.markForCheck();
      }
    });
  }

  saveProfile(): void {
    if (!this.editName.trim()) return;
    this.saving = true;
    this.profileService.updateProfile({ name: this.editName, phone: this.editPhone }).subscribe({
      next: (res: any) => {
        if (res.error) {
          this.saving = false;
          this.msg = res.error;
          this.msgType = 'error';
          this.cdr.markForCheck();
          return;
        }
        const existing = this.storage.getUser();
        this.user = { ...existing, ...res };
        if (this.user) this.storage.saveSession(this.user);
        this.saving = false;
        this.msg = 'Profile updated successfully!';
        this.msgType = 'success';
        this.cdr.markForCheck();
        setTimeout(() => this.closeEditModal(), 1200);
      },
      error: (err: any) => {
        this.saving = false;
        this.msg = err.error?.error || 'Update failed. Please try again.';
        this.msgType = 'error';
        this.cdr.markForCheck();
      }
    });
  }
}
