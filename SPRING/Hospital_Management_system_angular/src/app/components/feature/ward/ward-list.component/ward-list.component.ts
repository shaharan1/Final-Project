import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { WardModel } from '../../../../models/ward.model';
import { BedModel } from '../../../../models/bed.model';
import { InfrastructureService } from '../../../../services/infrastructure.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-ward-list.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ward-list.component.html',
  styleUrl: './ward-list.component.css',
})
export class WardListComponent implements OnInit {

  wards: WardModel[] = [];
  beds: BedModel[] = [];
  searchKeyword = '';

  constructor(
    private infrastructureService: InfrastructureService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadWards();
    this.loadBeds();
  }

  loadWards() {
    this.infrastructureService.getAllWards().subscribe({
      next: (res) => {
        this.wards = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.log(err)
    });
  }

  loadBeds() {
    this.infrastructureService.getAllBeds().subscribe({
      next: (res) => {
        this.beds = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.log(err)
    });
  }

  getWardAvailable(ward: WardModel): number {
    return this.beds.filter(b => b.wardId === ward.id && b.status === 'AVAILABLE').length;
  }

  getWardOccupied(ward: WardModel): number {
    return this.beds.filter(b => b.wardId === ward.id && b.status === 'OCCUPIED').length;
  }

  getWardTotal(ward: WardModel): number {
    return this.beds.filter(b => b.wardId === ward.id).length;
  }

  get filteredWards(): WardModel[] {
    if (!this.searchKeyword) return this.wards;
    const kw = this.searchKeyword.toLowerCase();
    return this.wards.filter(w =>
      w.name.toLowerCase().includes(kw) ||
      (w.departmentName && w.departmentName.toLowerCase().includes(kw))
    );
  }

  viewBeds(ward: WardModel): void {
    this.router.navigate(['/ward-management']);
  }
}
