import { Component } from '@angular/core';
import {AngularFirestore} from "@angular/fire/compat/firestore";
import {Observable} from "rxjs";
import * as AOS from 'aos';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app-reaExpress';
  toMap:boolean = true;
  items: Observable<any[]>;

  constructor(firestore: AngularFirestore) {
    this.items = firestore.collection('items').valueChanges();
  }

  switchToMap(){
    this.toMap = !this.toMap;
  }


  ngOnInit(){
    AOS.init()
    window.addEventListener('load', AOS.refresh);
  }
}
