import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-mail-send',
  templateUrl: './mail-send.component.html',
  styleUrls: ['./mail-send.component.css']
})
export class MailSendComponent implements OnInit {
  sent = false;

  productName = '';
  productReference = '';
  productImage = '';
  subCategory = '';

  subject = '';
  requestType = 'Question avant achat';
  name = '';
  email = '';
  message = '';

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit(): void {
    const params = this.route.snapshot.queryParamMap;
    this.productName = params.get('product') || '';
    this.productReference = params.get('reference') || '';
    this.productImage = params.get('image') || '';
    this.subCategory = params.get('subcategory') || '';

    if (this.productName) {
      this.requestType = 'Demande de devis';
      this.subject = this.productReference
        ? `Devis — ${this.productName} (${this.productReference})`
        : `Devis — ${this.productName}`;
    } else if (this.subCategory) {
      this.requestType = 'Demande de devis';
      this.subject = `Devis — ${this.subCategory}`;
    }
  }

  get hasProductContext(): boolean {
    return !!(this.productName || this.subCategory);
  }

  onSubmit(form: NgForm): void {
    if (form.invalid) {
      Object.values(form.controls).forEach((control) => control.markAsTouched());
      return;
    }
    this.sent = true;
  }
}
