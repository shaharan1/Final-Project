import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { GenericService } from '../../../services/generic.service';
import { MedicineService } from '../../../services/medicine.service';
import { GenericModel } from '../../../models/genericModel';
import { MedicineModel } from '../../../models/medicineModel';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-medicine',
  imports: [CommonModule, FormsModule],
  templateUrl: './medicine.component.html',
  styleUrls: ['./medicine.component.css']
})
export class MedicineComponent implements OnInit {

  generics: GenericModel[] = [];
  medicineList: MedicineModel[] = [];
  selectedGenericId: number | null = null;
  
  isEditMode: boolean = false; 
  medicineId: number | null = null; 

  medicine: MedicineModel = {
    medicineName: '',
    dosage: '',
    genericId: 0
   
  };

  constructor(
    private genericService: GenericService,
    private medicineService: MedicineService,
    private route: ActivatedRoute, 
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadGenerics();
    this.checkForEditMode(); 
  }

  loadGenerics() {
    this.genericService.getAll().subscribe(data => {
      this.generics = data;
      this.cdr.markForCheck();
    });
  }

  
  checkForEditMode() {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.medicineId = +idParam; 
        this.isEditMode = true;
        this.loadMedicineForEdit(this.medicineId);
      }
    });
  }

  
  loadMedicineForEdit(id: number) {
   
    this.medicineService.getById(id).subscribe({
      next: (data) => {
        this.medicine = data;
        this.selectedGenericId = data.genericId ?? null; 
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error("Error loading medicine for edit", err);
      }
    });
  }

  onGenericChange() {
    if (!this.selectedGenericId) {
      this.medicineList = [];
      return;
    }
    this.medicine.genericId = this.selectedGenericId;
    this.medicineService
      .getMedicineByGeneric(this.selectedGenericId)
      .subscribe(data => {
        this.medicineList = data;
      });
  }

 
  save() {
    this.medicine.genericId = this.selectedGenericId!;

    if (this.isEditMode && this.medicineId) {
     
      this.medicineService.update(this.medicineId, this.medicine).subscribe({
        next: (res) => {
          alert("Medicine Updated Successfully");
          this.router.navigate(['/medicine-list']);
        },
        error: (err) => {
          console.error("Update failed", err);
        }
      });
    } else {
      
      this.medicineService.save(this.medicine).subscribe({
        next: (res) => {
          alert("Medicine Saved Successfully");
          console.log(res);
          this.router.navigate(['/medicine-list']);
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  }
}