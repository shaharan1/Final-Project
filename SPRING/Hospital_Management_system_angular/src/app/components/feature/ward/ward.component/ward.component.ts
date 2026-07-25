import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { WardModel } from '../../../../models/ward.model';
import { BedModel } from '../../../../models/bed.model';
import { InfrastructureService } from '../../../../services/infrastructure.service';

@Component({
  selector: 'app-ward.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ward.component.html',
  styleUrl: './ward.component.css',
})
export class WardComponent implements OnInit {

  wards: WardModel[] = [];
  beds: BedModel[] = [];

  totalWards = 0;
  totalBeds = 0;
  occupiedBeds = 0;
  availableBeds = 0;

  constructor(
    private router: Router,
    private infraService: InfrastructureService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.infraService.getAllWards().subscribe({
      next: (res) => {
        this.wards = res;
        this.totalWards = res.length;
        this.cdr.markForCheck();
      }
    });

    this.infraService.getAllBeds().subscribe({
      next: (res) => {
        this.beds = res;
        this.totalBeds = res.length;
        this.occupiedBeds = res.filter(b => b.status === 'OCCUPIED').length;
        this.availableBeds = res.filter(b => b.status === 'AVAILABLE').length;
        this.cdr.markForCheck();
      }
    });
  }

  openWardManagement() {
    this.router.navigate(['/ward-management']);
  }

  openWardList() {
    this.router.navigate(['/ward-list']);
  }

  openBedManagement() {
    this.router.navigate(['/ward-management']);
  }

  openAdmissions() {
    this.router.navigate(['/admission-list']);
  }
}
