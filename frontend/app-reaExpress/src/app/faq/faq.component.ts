import { Component } from '@angular/core';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.css']
})
export class FaqComponent {
  readonly phoneDisplay = '+226 25 33 39 91';
  readonly phoneHref = 'tel:+22625333991';
}
