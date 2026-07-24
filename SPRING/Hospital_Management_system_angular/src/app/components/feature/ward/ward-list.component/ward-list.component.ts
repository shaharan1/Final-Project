import { Component } from '@angular/core';
import { WardModel } from '../../../../models/ward.model';
import { InfrastructureService } from '../../../../services/infrastructure.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-ward-list.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ward-list.component.html',
  styleUrl: './ward-list.component.css',
})
export class WardListComponent {


  wards: WardModel[] = [];

  constructor(private infrastructureService: InfrastructureService) { }

  ngOnInit(): void {
    this.loadWards();
  }

  loadWards() {
    this.infrastructureService.getAllWards().subscribe({
      next: (res) => {
        this.wards = res;
      },
      error: (err) => console.log(err)
    });
  }

}
