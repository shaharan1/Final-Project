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
  keyword = '';

  constructor(
    private genericService: GenericService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  get filtered(): GenericModel[] {
    const k = this.keyword.trim().toLowerCase();
    if (!k) return this.generics;
    return this.generics.filter(g => (g.genericName || '').toLowerCase().includes(k));
  }

  onSearch(event: Event): void {
    this.keyword = (event.target as HTMLInputElement).value;
    this.cdr.markForCheck();
  }

  loadData() {
    this.genericService.getAll().subscribe(data => {
      this.generics = data;
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

  initial(name: string | undefined): string {
    return (name && name.trim().charAt(0)) ? name.trim().charAt(0).toUpperCase() : '?';
  }

}
