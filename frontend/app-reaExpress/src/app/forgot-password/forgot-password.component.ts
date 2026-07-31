import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  /** Étape 1 : demander le code — Étape 2 : saisir code + nouveau mot de passe. */
  step: 1 | 2 = 1;

  channel: 'email' | 'sms' = 'email';
  loading = false;
  errorMessage = '';
  infoMessage = '';

  emailForm: FormGroup;
  resetForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private reaService: ReaExpressService,
    private router: Router
  ) {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
    this.resetForm = this.fb.group({
      code: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    });
  }

  get email(): string {
    return (this.emailForm.value.email || '').trim();
  }

  selectChannel(channel: 'email' | 'sms'): void {
    this.channel = channel;
    this.errorMessage = channel === 'sms'
      ? "L'envoi par SMS sera bientôt disponible. Utilisez votre adresse e-mail."
      : '';
  }

  requestCode(): void {
    if (this.emailForm.invalid || this.channel === 'sms') {
      this.emailForm.markAllAsTouched();
      if (this.channel === 'sms') {
        this.errorMessage = "L'envoi par SMS sera bientôt disponible. Utilisez votre adresse e-mail.";
      }
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.infoMessage = '';

    this.reaService.forgotPassword(this.email, this.channel).subscribe({
      next: (response) => {
        this.loading = false;
        this.infoMessage = response?.message
          || 'Si un compte existe avec cet e-mail, un code de vérification vient d\'être envoyé.';
        this.step = 2;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Impossible d\'envoyer le code pour le moment.';
      }
    });
  }

  submitNewPassword(): void {
    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }
    const { code, newPassword, confirmPassword } = this.resetForm.value;
    if (newPassword !== confirmPassword) {
      this.errorMessage = 'Les deux mots de passe ne correspondent pas.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.reaService.resetPassword(this.email, code, newPassword).subscribe({
      next: (response) => {
        this.loading = false;
        this.infoMessage = response?.message || 'Mot de passe réinitialisé.';
        setTimeout(() => this.router.navigate(['/login']), 1800);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Impossible de réinitialiser le mot de passe.';
      }
    });
  }

  backToStepOne(): void {
    this.step = 1;
    this.errorMessage = '';
    this.infoMessage = '';
    this.resetForm.reset();
  }

  fieldInvalid(form: FormGroup, field: string): boolean {
    const control = form.get(field);
    return !!control && control.invalid && (control.dirty || control.touched);
  }
}
