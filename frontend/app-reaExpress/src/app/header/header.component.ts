import { Component, OnInit } from '@angular/core';



declare function Mytest(): void;
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})


export class HeaderComponent implements OnInit{

  myscriptElement: HTMLScriptElement | undefined;

  ngOnInit(): void {
    // let menubar = document.querySelector("#menubar");
    // let mynav = document.querySelector(".navbar");

    // menubar.
  }

  constructor(){
    this.myscriptElement = document.createElement("script");
    this.myscriptElement.src = "src/assets/js/custom.js";
  }

  CallExternalJsFunction(){
    Mytest();
  }

  toHome(){
    document.getElementById("home")?.scrollIntoView({behavior:"smooth"});
  }

  toAbout(){
    document.getElementById("about")?.scrollIntoView({behavior:"smooth"});
  }

  toProducts(){
    document.getElementById("products")?.scrollIntoView({behavior:"smooth"});
  }

  toGallery(){
    document.getElementById("gallery")?.scrollIntoView({behavior:"smooth"});
  }


  toContact(){
    document.getElementById("contact")?.scrollIntoView({behavior:"smooth"});
  }

  toBgrd(){
    document.getElementById("bgrd")?.scrollIntoView({behavior:"smooth"});
  }

}


