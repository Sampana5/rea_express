import { Component, HostListener, OnInit } from '@angular/core';


declare function Smooth(): void;
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})



export class HomeComponent implements OnInit{

  myscriptElement: HTMLScriptElement | undefined;
  bg : any;
  ngOnInit(): void {
    //window.addEventListener('scroll', SmoothExternalJS());
   // this.SmoothExternalJS();

  //  this.bg = document.querySelector('#bgrd');
  //   window.addEventListener('scroll', function(){
  //   bg.style.backgroundSize = 50 - + window.pageXOffset + '%';
  // })


  }


  constructor(){
    this.myscriptElement = document.createElement("script");
    this.myscriptElement.src = "src/assets/js/custom.js";
  }

  @HostListener("document:scroll")
  Scrollfunction(){

  }

  SmoothExternalJS(){
    Smooth();
  }



}
