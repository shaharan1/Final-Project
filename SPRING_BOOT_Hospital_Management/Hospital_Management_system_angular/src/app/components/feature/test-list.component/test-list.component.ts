import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TestMasterModel } from '../../../models/testMasterModel';
import { TestMasterService } from '../../../services/test-master.service';

@Component({
  selector: 'app-test-list.component',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './test-list.component.html',
  styleUrl: './test-list.component.css',
})
export class TestListComponent {


 tests:TestMasterModel[]=[];

  keyword='';
  loading = false;
  errorMsg: string | null = null;;

  constructor(
    private service:TestMasterService,
    private router:Router,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){

    this.load();

  }

  load(){

    this.loading = true;
    this.errorMsg = null;
    this.service.getAll().subscribe({
      next: (res) => {
        this.tests = res;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMsg = 'Failed to load tests.';
        this.loading = false;
      }
    });

  }

  search(){

    this.errorMsg = null;
    if(this.keyword==''){

      this.load();

      return;

    }

    this.loading = true;
    this.service.search(this.keyword).subscribe({
      next: (res) => {
        this.tests = res;
        this.loading = false;
      },
      error: () => {
        this.errorMsg = 'Failed to search tests.';
        this.loading = false;
      }
    });

  }

  edit(id:number){

    this.router.navigate(['/tests/edit',id]);

  }

  delete(id:number){

    if(confirm("Delete?")){

      this.service.delete(id).subscribe({
        next: () => {

          this.load();

        },
        error: () => {
          this.errorMsg = 'Failed to delete test.';
        }
      });

    }

  }


}
