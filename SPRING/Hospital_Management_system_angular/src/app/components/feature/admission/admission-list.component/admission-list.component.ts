import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdmissionService } from '../../../../services/admission.service';
import { AdmissionResponse } from '../../../../models/admission-response.model';

@Component({
  selector: 'app-admission-list.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admission-list.component.html',
  styleUrl: './admission-list.component.css',
})
export class AdmissionListComponent implements OnInit {

  admissions: AdmissionResponse[] = [];
  searchKeyword = '';
  filterStatus = 'ALL';

  constructor(
    private service: AdmissionService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAdmissions();
  }

  loadAdmissions() {
    this.service.getAll().subscribe({
      next: (res) => {
        this.admissions = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.log(err)
    });
  }

  get filteredAdmissions(): AdmissionResponse[] {
    let list = this.admissions;
    if (this.filterStatus !== 'ALL') {
      list = list.filter(a => a.status === this.filterStatus);
    }
    if (this.searchKeyword) {
      const kw = this.searchKeyword.toLowerCase();
      list = list.filter(a =>
        a.patientName.toLowerCase().includes(kw) ||
        a.doctorName.toLowerCase().includes(kw) ||
        a.wardName.toLowerCase().includes(kw)
      );
    }
    return list;
  }

  get activeCount(): number {
    return this.admissions.filter(a => a.status === 'ADMITTED').length;
  }

  get dischargedCount(): number {
    return this.admissions.filter(a => a.status === 'DISCHARGED').length;
  }

  discharge(id: number) {
    if (confirm("Discharge this patient?")) {
      this.service.discharge(id).subscribe({
        next: () => {
          alert("Patient Discharged Successfully");
          this.loadAdmissions();
        },
        error: err => console.log(err)
      });
    }
  }

  goToAdmit() {
    this.router.navigate(['/admission']);
  }
}
