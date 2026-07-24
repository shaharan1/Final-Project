import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TestMasterModel } from '../../../models/testMasterModel';
import { TestMasterService } from '../../../services/test-master.service';


@Component({
  selector: 'app-test-master.component',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './test-master.component.html',
  styleUrl: './test-master.component.css',
})
export class TestMasterComponent {



 test:TestMasterModel={

    testCode:'',
    testName:'',
    standardPrice:0,
    normalRange:''

  };

  isEdit=false;

  id=0;

  constructor(
    private service:TestMasterService,
    private route:ActivatedRoute,
    private router:Router,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){

    const id=this.route.snapshot.paramMap.get('id');

    if(id){

      this.id=+id;

      this.isEdit=true;

      this.service.getById(this.id).subscribe(res=>{

        this.test=res;

      });

    }

  }

  save(){

    if(this.isEdit){

      this.service.update(this.id,this.test).subscribe(()=>{

        alert("Updated Successfully");

        this.router.navigate(['/test-list']);

      });

    }else{

      this.service.save(this.test).subscribe(()=>{

        alert("Saved Successfully");

        this.router.navigate(['/test-list']);

      });

    }

  }

}
