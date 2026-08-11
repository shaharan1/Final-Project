import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GenericModel } from '../../../models/genericModel';
import { GenericService } from '../../../services/generic.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-generic',
  imports: [CommonModule, FormsModule],
  templateUrl: './generic.html',
  styleUrl: './generic.css',
})
export class Generic {



  generic: GenericModel = {
    genericName: ''
  };

  constructor(
    private genericService: GenericService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  save() {

    this.genericService.save(this.generic).subscribe({

      next: () => {

        alert('Generic Saved Successfully');

        this.router.navigate(['/generic-list']);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }
}
