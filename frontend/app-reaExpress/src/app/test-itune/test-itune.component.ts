import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ItuneService } from './itune-service';

@Component({
  selector: 'app-test-itune',
  templateUrl: './test-itune.component.html',
  styleUrls: ['./test-itune.component.css']
})
export class TestItuneComponent implements OnInit {

  constructor(public ituneservice: ItuneService){

  }

  ngOnInit(): void {

  }


  onsubmit(form: NgForm){
    this.ituneservice.searchMusic(form.value.search);
    console.log(form.value.search);
  }

}
