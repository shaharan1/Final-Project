import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MedicineModel } from '../../../models/medicineModel';
import { MedicineService } from '../../../services/medicine.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-medicine-list.component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './medicine-list.component.html',
  styleUrl: './medicine-list.component.css',
})
export class MedicineListComponent implements OnInit {


  medicines: MedicineModel[] = [];

  constructor(
    private medicineService: MedicineService,
    private cdr: ChangeDetectorRef,
     private router: Router

  ) { }

  ngOnInit() {

    this.load();

  }

  load() {

    this.medicineService.getAll().subscribe({

      next: (res) => {

        this.medicines = res;
        this.cdr.markForCheck();

      }

    });

  }

  delete(id: number) {

    if (confirm("Delete Medicine?")) {

      this.medicineService.delete(id).subscribe(() => {

        this.load();

      });

    }

  }

  edit(id: number) {

  this.router.navigate(['/medicine', id]);

}



}
