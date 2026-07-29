import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  form: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private reaService: ReaExpressService,
    private router: Router
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      contactNumber: ['', [Validators.required, Validators.minLength(8)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      role: ['user']
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.reaService.signup(this.form.value).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMessage = res?.message || 'Inscription réussie.';
        setTimeout(() => this.router.navigate(['/login']), 1200);
      },
      error: (err) => {
        this.loading = false;
        const body = err?.error;
        if (typeof body === 'string') {
          try {
            this.errorMessage = JSON.parse(body)?.message || body;
          } catch {
            this.errorMessage = body;
          }
        } else {
          this.errorMessage = body?.message || 'Inscription impossible. Vérifiez les données ou le backend.';
        }
      }
    });
  }
}
