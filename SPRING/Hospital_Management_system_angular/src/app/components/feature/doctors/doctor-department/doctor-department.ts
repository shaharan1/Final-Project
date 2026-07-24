import { ChangeDetectorRef, Component } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DoctorDepartmentModel } from '../../../../models/doctorDepartmentModel';
import { DoctorsDepartmentService } from '../../../../services/doctors-department';

@Component({
  selector: 'app-doctor-department',
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-department.html',
  styleUrl: './doctor-department.css',
})
export class DoctorDepartment {

  departments: DoctorDepartmentModel[] = [];

  department: DoctorDepartmentModel = {
    departmentName: '',
    description: ''
  };

  isEdit = false;

  constructor(
    private departmentService: DoctorsDepartmentService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments() {
    this.departmentService.getAllDepartments().subscribe({
      next: (res) => {
        this.departments = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  saveDepartment() {

    if (this.isEdit) {

      this.departmentService.updateDepartment(
        this.department.id!,
        this.department
      ).subscribe({
        next: () => {
          alert('Department Updated Successfully');
          this.resetForm();
          this.loadDepartments();
        }
      });

    } else {

      this.departmentService.createDepartment(this.department).subscribe({
        next: () => {
          alert('Department Added Successfully');
          this.resetForm();
          this.loadDepartments();
        }
      });

    }

  }

  editDepartment(dep: DoctorDepartmentModel) {
    this.department = {
      id: dep.id,
      departmentName: dep.departmentName,
      description: dep.description
    };

    this.isEdit = true;
  }

  deleteDepartment(id: number) {

    if (!confirm('Delete this department?')) return;

    this.departmentService.deleteDepartment(id).subscribe({
      next: () => {
        alert('Deleted Successfully');
        this.loadDepartments();
      }
    });

  }

  resetForm() {

    this.department = {
      departmentName: '',
      description: ''
    };

    this.isEdit = false;

  }
}
