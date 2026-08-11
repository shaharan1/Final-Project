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

  constructor(
    private service:TestMasterService,
    private router:Router,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){

    this.load();

  }

  load(){

    this.service.getAll().subscribe(res=>{

      this.tests=res;
      this.cdr.markForCheck();

    });

  }

  search(){

    if(this.keyword==''){

      this.load();

      return;

    }

    this.service.search(this.keyword).subscribe(res=>{

      this.tests=res;

    });

  }

  edit(id:number){

    this.router.navigate(['/tests/edit',id]);

  }

  delete(id:number){

    if(confirm("Delete?")){

      this.service.delete(id).subscribe(()=>{

        this.load();

      });

    }

  }


}
