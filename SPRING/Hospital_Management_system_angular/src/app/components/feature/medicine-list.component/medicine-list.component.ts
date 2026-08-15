import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MedicineModel } from '../../../models/medicineModel';
import { MedicineService } from '../../../services/medicine.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-medicine-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './medicine-list.component.html',
  styleUrl: './medicine-list.component.css',
})
export class MedicineListComponent implements OnInit {
  medicines: MedicineModel[] = [];
  filteredMedicines: MedicineModel[] = [];
  searchTerm = '';

  constructor(
    private medicineService: MedicineService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.medicineService.getAll().subscribe({
      next: (res) => {
        this.medicines = res;
        this.filteredMedicines = [...res];
        this.cdr.markForCheck();
      }
    });
  }

  onSearch(event: Event): void {
    const term = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredMedicines = this.medicines.filter(m =>
      (m.medicineName || '').toLowerCase().includes(term) ||
      (m.genericName || '').toLowerCase().includes(term) ||
      (m.dosage || '').toLowerCase().includes(term)
    );
  }

  delete(id: number) {
    this.deleteMed(id);
  }

  deleteMed(id: number) {
    if (confirm('Delete this medicine?')) {
      this.medicineService.delete(id).subscribe(() => this.load());
    }
  }

  edit(id: number) {
    this.router.navigate(['/medicine', id]);
  }
}
