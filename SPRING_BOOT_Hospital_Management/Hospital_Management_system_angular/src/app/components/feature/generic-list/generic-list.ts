import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GenericModel } from '../../../models/genericModel';
import { GenericService } from '../../../services/generic.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-generic-list',
  imports: [CommonModule,FormsModule],
  templateUrl: './generic-list.html',
  styleUrl: './generic-list.css',
})
export class GenericListComponent implements OnInit {

  generics: GenericModel[] = [];
  loading = false;
  errorMsg = '';
  keyword = '';

  constructor(
    private genericService: GenericService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.errorMsg = '';
    this.genericService.getAll().subscribe({
      next: data => {
        this.generics = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: err => {
        this.loading = false;
        this.errorMsg = 'Failed to load generics.';
        this.cdr.markForCheck();
      }
    });
  }

  search() {
    const k = this.keyword.trim().toLowerCase();
    if (!k) {
      this.loadData();
      return;
    }
    this.genericService.getAll().subscribe(data => {
      this.generics = data.filter(g =>
        (g.genericName || '').toLowerCase().includes(k)
      );
      this.cdr.markForCheck();
    });
  }

  delete(id: number) {

    if (confirm('Delete this Generic?')) {

      this.genericService.delete(id).subscribe(() => {

        this.loadData();

      });

    }

  }

  edit(id: number) {

    this.router.navigate(['/generic', id]);

  }

}
